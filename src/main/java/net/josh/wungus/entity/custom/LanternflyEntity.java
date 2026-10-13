package net.josh.wungus.entity.custom;

import net.josh.wungus.block.ModBlocks;
import net.josh.wungus.block.custom.InfestedAilanthusLogBlock;
import net.josh.wungus.entity.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.Optional;

/**
 * The spotted lanternfly. Moves like a real one: it crawls slowly, climbs straight up trunks and sits on them,
 * and gets around by jumping and gliding (the wings only flap while it is in the air).
 * - When a player comes closer it jumps and glides away. Sneaking and creative players don't scare it.
 *   Every jump away tires it out: the next one is shorter and it needs longer before it can jump again,
 *   so after about 3 jumps it can hardly get away any more. It recovers when it is left alone for a while.
 * - Sometimes it lands on the shoulder of a player who stands still (or sneaks) and rides along for a while.
 * - It lives in infested ailanthus logs (see InfestedAilanthusLogBlock): it goes in at night and when it rains,
 *   and the log lets it out again in the morning. Without a log it looks for one, and now and then it moves
 *   into a plain ailanthus log, which makes that log infested.
 * - Landing on it after a fall can squish it, like trampling farmland (see ModEvents).
 */
public class LanternflyEntity extends PathfinderMob {
    private static final EntityDataAccessor<Boolean> FLYING =
            SynchedEntityData.defineId(LanternflyEntity.class, EntityDataSerializers.BOOLEAN);
    // The side of the trunk/wall it is clinging to (Direction 3D data value), -1 when it isn't on one
    private static final EntityDataAccessor<Byte> WALL_FACE =
            SynchedEntityData.defineId(LanternflyEntity.class, EntityDataSerializers.BYTE);
    // Which shoulder it sits on, needed on the client too to place it there
    private static final EntityDataAccessor<Boolean> LEFT_SHOULDER =
            SynchedEntityData.defineId(LanternflyEntity.class, EntityDataSerializers.BOOLEAN);

    // Gliding: how fast it may sink (blocks per tick) and the longest flight before it gives up and drops
    private static final double GLIDE_FALL_SPEED = 0.08D;
    private static final int MAX_FLIGHT_TICKS = 200;
    private static final double CLIMB_SPEED = 0.04D;

    // A player walking towards it within this range (sideways) makes it jump away, within CLOSE_RANGE any movement does.
    // Players up to NOTICE_HEIGHT above or below count too, so it still notices you from high up on a trunk.
    private static final double APPROACH_RANGE = 6.0D;
    private static final double NOTICE_HEIGHT = 6.0D;
    private static final double CLOSE_RANGE = 1.5D;
    // Every jump away from a player goes this much (times) as far as the one before: about 8, 4, 2, 1 blocks
    private static final float FLEE_TIRING = 0.65F;
    private static final float MIN_FLEE_POWER = 0.1F;
    private static final int MAX_FLEE_FATIGUE = 8;
    // It recovers one jump's worth of tiredness after being left alone for this long
    private static final int FATIGUE_RECOVERY_TICKS = 200;

    private static final double SHOULDER_SIDE_OFFSET = 0.32D;
    private static final double SHOULDER_HEIGHT = 1.4D;
    private static final double SHOULDER_HEIGHT_CROUCHING = 1.15D;

    // Homes further away than this are forgotten
    private static final double MAX_HOME_DISTANCE = 32.0D;
    // Chance to pick a plain ailanthus log when it has no infested log to go to
    private static final float INFEST_CHANCE = 0.25F;

    public final AnimationState flyingAnimationState = new AnimationState();

    private @Nullable BlockPos homePos;
    private Vec3 glideVelocity = Vec3.ZERO;
    private int flightTicks;
    private int climbTicks;
    private boolean climbingUp;

    private int hopCooldown;
    // Time it has to wait after landing before it can jump again, set by the jump
    private int cooldownAfterLanding;
    // How many times it jumped away from a player recently
    private int fleeFatigue;
    private int fatigueRecoveryTicks;
    // Time it stays outside after leaving a log (during the day)
    private int stayOutCooldown;
    private int findHomeCooldown;
    private int shoulderTicks;
    private int shoulderCooldown;

    // The nearest player, to tell if it is coming closer
    private int watchedPlayerId = -1;
    private Vec3 watchedPlayerPos = Vec3.ZERO;
    private double watchedPlayerDistance;
    private double watchedPlayerMoved;
    private @Nullable Player approachingPlayer;
    // It won't land on the shoulder of a player who scared it recently
    private int scaredOfPlayerId = -1;
    private int scaredTicks;

    public LanternflyEntity(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.xpReward = 1;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 2.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.15D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new HopAwayFromPlayerGoal(this));
        this.goalSelector.addGoal(2, new LandOnShoulderGoal(this));
        this.goalSelector.addGoal(3, new GoIntoLogGoal(this));
        this.goalSelector.addGoal(4, new HopToTreeGoal(this));
        this.goalSelector.addGoal(5, new CrawlAroundGoal(this, 1.0D));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(FLYING, false);
        this.entityData.define(WALL_FACE, (byte) -1);
        this.entityData.define(LEFT_SHOULDER, false);
    }

    public boolean isFlying() {
        return this.entityData.get(FLYING);
    }

    private void setFlying(boolean flying) {
        this.entityData.set(FLYING, flying);
    }

    /** The side of the block it is clinging to, null when it isn't on a trunk or wall. */
    public @Nullable Direction getWallFace() {
        byte face = this.entityData.get(WALL_FACE);
        return face < 0 ? null : Direction.from3DDataValue(face);
    }

    private void setWallFace(@Nullable Direction face) {
        this.entityData.set(WALL_FACE, face == null ? (byte) -1 : (byte) face.get3DDataValue());
    }

    private boolean isOnLeftShoulder() {
        return this.entityData.get(LEFT_SHOULDER);
    }

    private void setOnLeftShoulder(boolean left) {
        this.entityData.set(LEFT_SHOULDER, left);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide()) {
            // The wings only flap while it is in the air
            if (this.isFlying()) {
                this.flyingAnimationState.startIfStopped(this.tickCount);
            } else {
                this.flyingAnimationState.stop();
            }
        }
    }

    @Override
    public void aiStep() {
        if (!this.level().isClientSide()) {
            if (this.isDeadOrDying()) {
                // Dead: lets go of its trunk and stops flapping, the body just drops
                this.setWallFace(null);
                this.setFlying(false);
            } else if (!this.isPassenger()) {
                this.tickCooldowns();
                this.watchNearestPlayer();
                this.updateMovement();
            }
        }
        super.aiStep();
    }

    private void tickCooldowns() {
        if (this.hopCooldown > 0 && !this.isFlying()) this.hopCooldown--;
        if (this.stayOutCooldown > 0) this.stayOutCooldown--;
        if (this.findHomeCooldown > 0) this.findHomeCooldown--;
        if (this.shoulderCooldown > 0) this.shoulderCooldown--;
        if (this.scaredTicks > 0 && --this.scaredTicks == 0) this.scaredOfPlayerId = -1;
        if (this.fleeFatigue > 0 && !this.isFlying() && ++this.fatigueRecoveryTicks >= FATIGUE_RECOVERY_TICKS) {
            this.fleeFatigue--;
            this.fatigueRecoveryTicks = 0;
        }
    }

    /** Remembers where the nearest player was, to notice when it walks towards the lanternfly. */
    private void watchNearestPlayer() {
        this.approachingPlayer = null;
        Player player = this.level().getNearestPlayer(this.getX(), this.getY(), this.getZ(), APPROACH_RANGE + NOTICE_HEIGHT,
                EntitySelector.NO_SPECTATORS);
        double distance = player == null ? 0.0D : Math.sqrt(Mth.square(player.getX() - this.getX()) + Mth.square(player.getZ() - this.getZ()));
        if (player == null || distance > APPROACH_RANGE || Math.abs(player.getY() - this.getY()) > NOTICE_HEIGHT) {
            this.watchedPlayerId = -1;
            return;
        }

        Vec3 playerPos = player.position();
        if (player.getId() == this.watchedPlayerId) {
            this.watchedPlayerMoved = playerPos.distanceTo(this.watchedPlayerPos);
            boolean moving = this.watchedPlayerMoved > 0.03D;
            boolean comingCloser = distance < this.watchedPlayerDistance - 0.02D;
            boolean close = this.distanceTo(player) < CLOSE_RANGE;
            if (!player.isShiftKeyDown() && !player.isCreative() && moving && (comingCloser || close)) {
                this.approachingPlayer = player;
            }
        } else {
            this.watchedPlayerMoved = 0.0D;
        }
        this.watchedPlayerId = player.getId();
        this.watchedPlayerPos = playerPos;
        this.watchedPlayerDistance = distance;
    }

    /** Gliding, clinging to trunks and walls, climbing. Runs on the server before the normal movement. */
    private void updateMovement() {
        if (this.isFlying()) {
            this.flightTicks++;
            // The first ticks it is still taking off (and may still be on the ground or touching its wall)
            boolean takingOff = this.flightTicks <= 2;
            if (!takingOff && (this.onGround() || this.horizontalCollision || this.isInWater() || this.isInLava()
                    || this.flightTicks > MAX_FLIGHT_TICKS)) {
                // Landed on the ground, or bumped into a trunk or wall and holds on to it
                this.land();
            } else {
                Vec3 motion = this.getDeltaMovement();
                this.setDeltaMovement(this.glideVelocity.x, Math.max(motion.y, -GLIDE_FALL_SPEED), this.glideVelocity.z);
                return;
            }
        }

        Direction wall = null;
        if (!this.isInWater() && !this.isInLava() && (!this.onGround() || this.horizontalCollision)) {
            wall = this.findTouchedWall();
        }
        if (wall != null && this.onGround()) {
            // Crawled into something: climb it if it is a tree, otherwise stay on the ground
            if (this.climbTicks <= 0 && this.level().getBlockState(this.blockPosition().relative(wall)).is(BlockTags.LOGS)) {
                this.startClimbing(true, 20 + this.random.nextInt(40));
            }
            if (this.climbTicks <= 0) {
                wall = null;
            }
        }
        this.setWallFace(wall);

        if (wall != null) {
            this.clingTo(wall);
        } else if (!this.onGround() && !this.isInWater() && !this.isInLava() && this.getDeltaMovement().y < -0.1D) {
            // Falling (dropped off something, or its trunk was chopped): spread the wings and glide down
            this.hop(Vec3.directionFromRotation(0.0F, this.getYRot()), 0.15D, 0.0D);
        }
    }

    /** Holds on to the trunk or wall without sliding down, and slowly climbs it now and then. */
    private void clingTo(Direction wall) {
        float yaw = wall.toYRot();
        this.setYRot(yaw);
        this.yBodyRot = yaw;
        this.yHeadRot = yaw;
        this.getNavigation().stop();

        double climb = 0.0D;
        if (this.climbTicks > 0) {
            this.climbTicks--;
            boolean canGo = this.climbingUp ? this.hasWallAbove(wall) : !this.onGround();
            if (canGo) {
                climb = this.climbingUp ? CLIMB_SPEED : -CLIMB_SPEED;
            } else {
                this.climbTicks = 0;
            }
        } else if (this.random.nextInt(120) == 0) {
            // Mostly up, like real ones
            this.startClimbing(this.random.nextInt(4) != 0, 10 + this.random.nextInt(30));
        }
        this.setDeltaMovement(0.0D, climb, 0.0D);
    }

    private void startClimbing(boolean up, int ticks) {
        this.climbingUp = up;
        this.climbTicks = ticks;
        if (up && this.onGround()) {
            // Get off the ground so it counts as being on the trunk
            this.setDeltaMovement(0.0D, CLIMB_SPEED, 0.0D);
        }
    }

    private @Nullable Direction findTouchedWall() {
        AABB box = this.getBoundingBox();
        Direction current = this.getWallFace();
        if (current != null && this.touchesWall(box, current)) {
            return current;
        }
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            if (this.touchesWall(box, direction)) {
                return direction;
            }
        }
        return null;
    }

    private boolean touchesWall(AABB box, Direction direction) {
        return !this.level().noCollision(this, box.move(direction.getStepX() * 0.05D, 0.0D, direction.getStepZ() * 0.05D));
    }

    /** True when the trunk or wall continues above it. */
    private boolean hasWallAbove(Direction wall) {
        BlockPos above = BlockPos.containing(this.getX(), this.getY() + this.getBbHeight() + 0.1D, this.getZ()).relative(wall);
        return !this.level().getBlockState(above).getCollisionShape(this.level(), above).isEmpty();
    }

    private boolean isAtTopOfWall() {
        Direction wall = this.getWallFace();
        return wall != null && !this.hasWallAbove(wall);
    }

    /** It can jump when it sits somewhere (not while it is already in the air or on a shoulder). */
    public boolean canHop() {
        return !this.isFlying() && !this.isPassenger() && this.hopCooldown <= 0
                && (this.onGround() || this.getWallFace() != null || this.isInWater());
    }

    /** Jumps and glides in the given direction. Speed is per tick sideways, upward is the jump speed. */
    public void hop(Vec3 direction, double speed, double upward) {
        Vec3 flat = new Vec3(direction.x, 0.0D, direction.z);
        if (flat.lengthSqr() < 1.0E-4D) {
            flat = Vec3.directionFromRotation(0.0F, this.getYRot());
        }
        flat = flat.normalize();

        // Jumping off a trunk: push off away from it instead of into it
        Direction wall = this.getWallFace();
        if (wall != null) {
            Vec3 intoWall = new Vec3(wall.getStepX(), 0.0D, wall.getStepZ());
            double into = flat.dot(intoWall);
            if (into > -0.3D) {
                flat = flat.subtract(intoWall.scale(into + 0.6D)).normalize();
            }
        }

        this.glideVelocity = flat.scale(speed);
        this.setDeltaMovement(this.glideVelocity.x, upward, this.glideVelocity.z);
        // A short moment after landing before it can jump again (running away sets a longer one)
        this.cooldownAfterLanding = 10 + this.random.nextInt(30);
        float yaw = (float) (Mth.atan2(flat.z, flat.x) * (180.0F / (float) Math.PI)) - 90.0F;
        this.setYRot(yaw);
        this.yBodyRot = yaw;
        this.yHeadRot = yaw;

        this.setWallFace(null);
        this.climbTicks = 0;
        this.flightTicks = 0;
        this.setFlying(true);
        this.getNavigation().stop();
        this.hasImpulse = true;
    }

    /** Jumps so the glide ends at (or passes close to) the target, like jumping onto a trunk or a shoulder. */
    public void hopTowards(Vec3 target) {
        Vec3 offset = target.subtract(this.position());
        double horizontal = Math.sqrt(offset.x * offset.x + offset.z * offset.z);
        double speed = Mth.clamp(horizontal / 7.0D, 0.06D, 0.4D);
        // It goes up for about 6 ticks, after that it sinks while gliding: start high enough to still reach it
        double glideTicks = Math.max(0.0D, horizontal / speed - 6.0D);
        double peak = Math.max(0.3D, offset.y + GLIDE_FALL_SPEED * glideTicks) + 0.2D;
        double upward = Mth.clamp(Math.sqrt(0.17D * peak), 0.25D, 0.8D);
        this.hop(offset, speed, upward);
    }

    private void land() {
        this.setFlying(false);
        this.flightTicks = 0;
        this.hopCooldown = this.cooldownAfterLanding;
        Vec3 motion = this.getDeltaMovement();
        this.setDeltaMovement(motion.x * 0.2D, motion.y, motion.z * 0.2D);
    }

    /* SHOULDER */

    private Vec3 shoulderPosition(Player player) {
        float yaw = player.yBodyRot * ((float) Math.PI / 180.0F);
        // (cos, sin) of the body rotation points to the player's left side
        double side = this.isOnLeftShoulder() ? SHOULDER_SIDE_OFFSET : -SHOULDER_SIDE_OFFSET;
        double height = player.isCrouching() ? SHOULDER_HEIGHT_CROUCHING : SHOULDER_HEIGHT;
        return player.position().add(Mth.cos(yaw) * side, height, Mth.sin(yaw) * side);
    }

    private boolean landOnShoulder(Player player) {
        this.setFlying(false);
        this.flightTicks = 0;
        this.setWallFace(null);
        if (this.startRiding(player)) {
            this.shoulderTicks = 600 + this.random.nextInt(1800);
            return true;
        }
        return false;
    }

    // Rides the player like a parrot, sitting on its shoulder instead of on top of its head
    @Override
    public void rideTick() {
        super.rideTick();
        if (this.getVehicle() instanceof Player player) {
            Vec3 pos = this.shoulderPosition(player);
            this.setPos(pos.x, pos.y, pos.z);
            this.setYRot(player.yBodyRot);
            this.yBodyRot = player.yBodyRot;
            this.yHeadRot = player.yBodyRot;

            if (!this.level().isClientSide()) {
                this.tickOnShoulder(player);
            }
        }
    }

    private void tickOnShoulder(Player player) {
        if (player.level() != this.level() || !player.isAlive()) {
            this.stopRiding();
            return;
        }
        if (this.scaredTicks > 0) this.scaredTicks--;
        // Like parrots it doesn't hold on when the player jumps or falls, swims, sleeps, flies or gets hurt.
        // Otherwise it rides along for a while, and it wants to go home at night.
        boolean shaken = player.fallDistance > 0.5D || player.isInWater() || player.isSleeping() || player.isFallFlying()
                || player.getAbilities().flying || player.hurtTime > 0 || player.isSpectator();
        if (shaken || --this.shoulderTicks <= 0 || (this.isStayInsideTime() && this.hasValidHome())) {
            this.jumpOffShoulder(player);
        }
    }

    private void jumpOffShoulder(Player player) {
        Vec3 pos = this.shoulderPosition(player);
        this.stopRiding();
        this.setPos(pos.x, pos.y, pos.z);
        this.shoulderCooldown = 1200 + this.random.nextInt(1200);
        // Off to the side it was sitting on
        float yaw = player.yBodyRot * ((float) Math.PI / 180.0F);
        double side = this.isOnLeftShoulder() ? 1.0D : -1.0D;
        this.hop(new Vec3(Mth.cos(yaw) * side, 0.0D, Mth.sin(yaw) * side), 0.2D, 0.25D);
    }

    private void scaredBy(Player player) {
        this.scaredOfPlayerId = player.getId();
        this.scaredTicks = 600;
    }

    // It is tiny, players walking into it don't shove it around (that would make it impossible to step on)
    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected void doPush(Entity pEntity) {
    }

    // Can't be hit or clicked while it sits on a shoulder, so it doesn't get in the way of the player
    @Override
    public boolean isPickable() {
        return !this.isPassenger() && super.isPickable();
    }

    /* LOGS */

    /** Night and rain: the same rule bees use to stay in their hives. */
    public static boolean isStayInsideTime(Level level, BlockPos pos) {
        return level.isNight() || level.isRaining();
    }

    private boolean isStayInsideTime() {
        return isStayInsideTime(this.level(), this.blockPosition());
    }

    private boolean hasValidHome() {
        if (this.homePos == null) {
            return false;
        }
        if (!this.level().isLoaded(this.homePos)
                || this.homePos.distToCenterSqr(this.position()) > MAX_HOME_DISTANCE * MAX_HOME_DISTANCE) {
            this.homePos = null;
            return false;
        }
        BlockState state = this.level().getBlockState(this.homePos);
        return InfestedAilanthusLogBlock.hasRoom(state) || state.is(ModBlocks.AILANTHUS_LOG.get());
    }

    /** Checks the home log, or looks for a new one. Returns true when it has a log to go to. */
    private boolean pickHome() {
        if (this.hasValidHome()) {
            // Small chance to move into a plain ailanthus log nearby instead, which then gets infested
            if (this.random.nextInt(20) == 0) {
                this.findPlainLog(6).ifPresent(pos -> this.homePos = pos);
            }
            return true;
        }
        if (this.findHomeCooldown > 0) {
            return false;
        }
        this.findHomeCooldown = 400 + this.random.nextInt(400);

        Optional<BlockPos> infested = BlockPos.findClosestMatch(this.blockPosition(), 10, 6,
                pos -> InfestedAilanthusLogBlock.hasRoom(this.level().getBlockState(pos)) && this.hasOpenSide(pos));
        if (infested.isPresent()) {
            this.homePos = infested.get().immutable();
            return true;
        }
        if (this.random.nextFloat() < INFEST_CHANCE) {
            Optional<BlockPos> plain = this.findPlainLog(8);
            if (plain.isPresent()) {
                this.homePos = plain.get();
                return true;
            }
        }
        return false;
    }

    private Optional<BlockPos> findPlainLog(int range) {
        return BlockPos.findClosestMatch(this.blockPosition(), range, 6,
                pos -> this.level().getBlockState(pos).is(ModBlocks.AILANTHUS_LOG.get()) && this.hasOpenSide(pos))
                .map(BlockPos::immutable);
    }

    /** A log it can get into: at least one side isn't covered. */
    private boolean hasOpenSide(BlockPos pos) {
        return InfestedAilanthusLogBlock.findOpenSide(this.level(), pos, this.random) != null;
    }

    private boolean isCloseToHome() {
        return this.homePos != null && this.position().distanceToSqr(Vec3.atCenterOf(this.homePos)) < 2.6D;
    }

    /** Goes into its home log. Returns false when it can't (full or not a log any more). */
    private boolean tryEnterHome() {
        if (this.homePos == null || !InfestedAilanthusLogBlock.addLanternfly(this.level(), this.homePos)) {
            return false;
        }
        this.discard();
        return true;
    }

    /** Lets a lanternfly out of a log, on the given (open) side. It remembers the log as its home. */
    public static void spawnFromLog(ServerLevel level, BlockPos logPos, Direction side) {
        LanternflyEntity lanternfly = ModEntities.LANTERNFLY.get().create(level);
        if (lanternfly == null) {
            return;
        }
        Vec3 center = Vec3.atCenterOf(logPos);
        double reach = 0.5D + lanternfly.getBbWidth() / 2.0D + 0.01D;
        double x = center.x + side.getStepX() * reach;
        double z = center.z + side.getStepZ() * reach;
        double y;
        if (side == Direction.UP) {
            y = logPos.getY() + 1.0D;
        } else if (side == Direction.DOWN) {
            y = logPos.getY() - lanternfly.getBbHeight() - 0.01D;
        } else {
            y = logPos.getY() + 0.4D;
        }
        lanternfly.moveTo(x, y, z);
        // Out of the side it came from, facing the log
        float yaw = side.getAxis().isHorizontal() ? side.getOpposite().toYRot() : level.getRandom().nextFloat() * 360.0F;
        lanternfly.setYRot(yaw);
        lanternfly.yBodyRot = yaw;
        lanternfly.yHeadRot = yaw;
        lanternfly.homePos = logPos.immutable();
        lanternfly.stayOutCooldown = 1200 + level.getRandom().nextInt(2400);
        level.addFreshEntity(lanternfly);
    }

    /** A startled lanternfly jumping out of a broken log. It has no home any more. */
    public static void spawnStartled(ServerLevel level, BlockPos pos) {
        LanternflyEntity lanternfly = ModEntities.LANTERNFLY.get().create(level);
        if (lanternfly == null) {
            return;
        }
        lanternfly.moveTo(pos.getX() + 0.5D, pos.getY() + 0.3D, pos.getZ() + 0.5D);
        float yaw = level.getRandom().nextFloat() * 360.0F;
        lanternfly.setYRot(yaw);
        lanternfly.hop(Vec3.directionFromRotation(0.0F, yaw), 0.25D, 0.35D);
        level.addFreshEntity(lanternfly);
    }

    /* MISC */

    // Lanternflies live on in their logs, they don't despawn
    @Override
    public boolean removeWhenFarAway(double pDistanceToClosestPlayer) {
        return false;
    }

    // Insects glide down, they don't get hurt by falling
    @Override
    public boolean causeFallDamage(float pFallDistance, float pDamageMultiplier, DamageSource pDamageSource) {
        return false;
    }

    // Silent, like real lanternflies (no sound files yet)
    @Override
    protected void playStepSound(BlockPos pPos, BlockState pState) {
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(DamageSource pDamageSource) {
        return null;
    }

    @Override
    protected @Nullable SoundEvent getDeathSound() {
        return null;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("HasHome", this.homePos != null);
        if (this.homePos != null) {
            compound.putInt("HomeX", this.homePos.getX());
            compound.putInt("HomeY", this.homePos.getY());
            compound.putInt("HomeZ", this.homePos.getZ());
        }
        compound.putInt("StayOutCooldown", this.stayOutCooldown);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.homePos = compound.getBoolean("HasHome")
                ? new BlockPos(compound.getInt("HomeX"), compound.getInt("HomeY"), compound.getInt("HomeZ"))
                : null;
        this.stayOutCooldown = compound.getInt("StayOutCooldown");
    }

    /* GOALS */

    /** A player walks up to it: jump and glide away from them, a bit shorter every time (see fleeFatigue). */
    static class HopAwayFromPlayerGoal extends Goal {
        private final LanternflyEntity lanternfly;

        HopAwayFromPlayerGoal(LanternflyEntity lanternfly) {
            this.lanternfly = lanternfly;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP));
        }

        @Override
        public boolean canUse() {
            return this.lanternfly.approachingPlayer != null && this.lanternfly.canHop();
        }

        @Override
        public boolean canContinueToUse() {
            return false;
        }

        @Override
        public void start() {
            Player player = this.lanternfly.approachingPlayer;
            if (player == null) {
                return;
            }
            Vec3 away = this.lanternfly.position().subtract(player.position());
            Direction wall = this.lanternfly.getWallFace();
            if (wall != null) {
                away = this.wallEscapeDirection(wall, away);
            } else {
                // Away from the player, not always in a straight line
                away = away.yRot((this.lanternfly.getRandom().nextFloat() - 0.5F) * (float) Math.PI / 2.0F);
            }
            // The more tired it is, the shorter and lower the jump
            float power = Math.max(MIN_FLEE_POWER, (float) Math.pow(FLEE_TIRING, this.lanternfly.fleeFatigue));
            this.lanternfly.hop(away, (0.28D + this.lanternfly.getRandom().nextDouble() * 0.08D) * power,
                    (0.4D + this.lanternfly.getRandom().nextDouble() * 0.12D) * (0.55F + 0.45F * power));

            // ...and the longer it needs before the next one: about 1.5, 2, 3, 3.5 seconds, at most 4.5
            this.lanternfly.fleeFatigue = Math.min(MAX_FLEE_FATIGUE, this.lanternfly.fleeFatigue + 1);
            this.lanternfly.fatigueRecoveryTicks = 0;
            this.lanternfly.cooldownAfterLanding = Math.min(80, 10 + 15 * this.lanternfly.fleeFatigue)
                    + this.lanternfly.getRandom().nextInt(10);
            this.lanternfly.scaredBy(player);
            this.lanternfly.approachingPlayer = null;
        }

        /**
         * On a trunk or wall the player can only be on the open side, so straight away from them is into the wall
         * (and pushing off it would send it right back at them). It jumps off sideways instead, to the side away
         * from the player (a random side when the player is right in front of it), and a bit outwards to get clear.
         */
        private Vec3 wallEscapeDirection(Direction wall, Vec3 away) {
            Vec3 outward = new Vec3(-wall.getStepX(), 0.0D, -wall.getStepZ());
            Vec3 along = new Vec3(-wall.getStepZ(), 0.0D, wall.getStepX());
            double side = away.dot(along);
            if (Math.abs(side) < 0.3D) {
                side = this.lanternfly.getRandom().nextBoolean() ? 1.0D : -1.0D;
            }
            return along.scale(Math.signum(side)).add(outward.scale(0.4D + this.lanternfly.getRandom().nextDouble() * 0.3D));
        }
    }

    /** Now and then it jumps onto the shoulder of a player standing still (or sneaking) nearby. */
    static class LandOnShoulderGoal extends Goal {
        private static final double RANGE = 5.0D;
        private final LanternflyEntity lanternfly;
        private @Nullable Player player;
        private int ticks;

        LandOnShoulderGoal(LanternflyEntity lanternfly) {
            this.lanternfly = lanternfly;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP));
        }

        @Override
        public boolean canUse() {
            if (!this.lanternfly.canHop() || this.lanternfly.shoulderCooldown > 0 || this.lanternfly.isStayInsideTime()
                    || this.lanternfly.getRandom().nextInt(300) != 0) {
                return false;
            }
            Player nearest = this.lanternfly.level().getNearestPlayer(this.lanternfly.getX(), this.lanternfly.getY(),
                    this.lanternfly.getZ(), RANGE, EntitySelector.NO_SPECTATORS);
            if (nearest == null || nearest.getId() == this.lanternfly.scaredOfPlayerId || nearest.isVehicle()
                    || nearest.isPassenger() || !nearest.onGround() || nearest.isInWater() || nearest.isSleeping()) {
                return false;
            }
            boolean standingStill = nearest.getId() == this.lanternfly.watchedPlayerId && this.lanternfly.watchedPlayerMoved < 0.01D;
            if (!standingStill && !nearest.isShiftKeyDown()) {
                return false;
            }
            this.player = nearest;
            return true;
        }

        @Override
        public void start() {
            this.ticks = 0;
            if (this.player != null) {
                this.lanternfly.setOnLeftShoulder(this.lanternfly.getRandom().nextBoolean());
                this.lanternfly.hopTowards(this.lanternfly.shoulderPosition(this.player));
            }
        }

        @Override
        public boolean canContinueToUse() {
            return this.player != null && this.player.isAlive() && this.lanternfly.isFlying() && !this.lanternfly.isPassenger()
                    && this.ticks < 60;
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            this.ticks++;
            if (this.player != null && this.lanternfly.position().distanceToSqr(this.lanternfly.shoulderPosition(this.player)) < 1.0D) {
                this.lanternfly.landOnShoulder(this.player);
            }
        }

        @Override
        public void stop() {
            this.player = null;
        }
    }

    /** At night and in the rain (and now and then for a short rest) it goes into its log. */
    static class GoIntoLogGoal extends Goal {
        private static final int GIVE_UP_TICKS = 400;
        private final LanternflyEntity lanternfly;
        private int ticks;

        GoIntoLogGoal(LanternflyEntity lanternfly) {
            this.lanternfly = lanternfly;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP));
        }

        @Override
        public boolean canUse() {
            // Named lanternflies stay outside, going into a log would lose their name
            if (this.lanternfly.isFlying() || this.lanternfly.isPassenger() || this.lanternfly.hasCustomName()
                    || this.lanternfly.hopCooldown > 0) {
                return false;
            }
            boolean night = this.lanternfly.isStayInsideTime();
            boolean rest = !night && this.lanternfly.stayOutCooldown <= 0 && this.lanternfly.getRandom().nextInt(400) == 0;
            return (night || rest) && this.lanternfly.pickHome();
        }

        @Override
        public void start() {
            this.ticks = 0;
        }

        @Override
        public boolean canContinueToUse() {
            return this.ticks < GIVE_UP_TICKS && !this.lanternfly.isPassenger() && this.lanternfly.hasValidHome();
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            this.ticks++;
            BlockPos home = this.lanternfly.homePos;
            if (home == null) {
                return;
            }
            if (this.lanternfly.isCloseToHome()) {
                if (!this.lanternfly.tryEnterHome()) {
                    this.lanternfly.homePos = null;
                }
                return;
            }
            if (this.lanternfly.isFlying() || this.lanternfly.hopCooldown > 0) {
                return;
            }

            Direction wall = this.lanternfly.getWallFace();
            BlockPos wallPos = wall == null ? null : this.lanternfly.blockPosition().relative(wall);
            if (wallPos != null && wallPos.getX() == home.getX() && wallPos.getZ() == home.getZ()) {
                // Already on the right trunk: climb up or down to the log
                if (this.lanternfly.climbTicks <= 0) {
                    this.lanternfly.startClimbing(home.getY() > this.lanternfly.getY(), 20);
                }
            } else {
                this.lanternfly.hopTowards(Vec3.atCenterOf(home));
            }
        }

        @Override
        public void stop() {
            if (this.ticks >= GIVE_UP_TICKS) {
                // Couldn't get there, look for another log later
                this.lanternfly.homePos = null;
            }
        }
    }

    /** Jumps and glides to another tree now and then, or just somewhere else. */
    static class HopToTreeGoal extends Goal {
        private final LanternflyEntity lanternfly;

        HopToTreeGoal(LanternflyEntity lanternfly) {
            this.lanternfly = lanternfly;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP));
        }

        @Override
        public boolean canUse() {
            // Too tired from running away to go anywhere
            if (!this.lanternfly.canHop() || this.lanternfly.fleeFatigue > 0) {
                return false;
            }
            // Mostly sits on its trunk, but it leaves when it reached the top
            int chance;
            if (this.lanternfly.getWallFace() == null) {
                chance = 300;
            } else if (this.lanternfly.isAtTopOfWall()) {
                chance = 60;
            } else {
                chance = 600;
            }
            return this.lanternfly.getRandom().nextInt(chance) == 0;
        }

        @Override
        public boolean canContinueToUse() {
            return false;
        }

        @Override
        public void start() {
            BlockPos tree = this.findTree();
            if (tree != null) {
                this.lanternfly.hopTowards(Vec3.atCenterOf(tree));
            } else {
                float yaw = this.lanternfly.getRandom().nextFloat() * 360.0F;
                this.lanternfly.hop(Vec3.directionFromRotation(0.0F, yaw), 0.25D, 0.45D);
            }
        }

        /** A log nearby (but not the trunk it sits on), ailanthus trees preferred. Checks a few random spots. */
        private @Nullable BlockPos findTree() {
            Level level = this.lanternfly.level();
            Direction wall = this.lanternfly.getWallFace();
            BlockPos current = wall == null ? null : this.lanternfly.blockPosition().relative(wall);
            BlockPos anyLog = null;
            for (int i = 0; i < 16; i++) {
                BlockPos pos = this.lanternfly.blockPosition().offset(
                        this.lanternfly.getRandom().nextInt(21) - 10,
                        this.lanternfly.getRandom().nextInt(7) - 2,
                        this.lanternfly.getRandom().nextInt(21) - 10);
                if (current != null && pos.getX() == current.getX() && pos.getZ() == current.getZ()) {
                    continue;
                }
                BlockState state = level.getBlockState(pos);
                if (state.is(ModBlocks.AILANTHUS_LOG.get()) || state.is(ModBlocks.INFESTED_AILANTHUS_LOG.get())) {
                    return pos;
                }
                if (anyLog == null && state.is(BlockTags.LOGS)) {
                    anyLog = pos;
                }
            }
            return anyLog;
        }
    }

    /** Slowly crawls around on the ground. */
    static class CrawlAroundGoal extends WaterAvoidingRandomStrollGoal {
        private final LanternflyEntity lanternfly;

        CrawlAroundGoal(LanternflyEntity lanternfly, double speedModifier) {
            super(lanternfly, speedModifier);
            this.lanternfly = lanternfly;
        }

        @Override
        public boolean canUse() {
            return this.lanternfly.onGround() && !this.lanternfly.isFlying() && this.lanternfly.getWallFace() == null
                    && super.canUse();
        }

        @Override
        public boolean canContinueToUse() {
            return this.lanternfly.onGround() && !this.lanternfly.isFlying() && this.lanternfly.getWallFace() == null
                    && super.canContinueToUse();
        }
    }
}
