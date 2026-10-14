package net.josh.wungus.entity.custom;

import net.josh.wungus.misc.ModDamageTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LadderBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.UUID;

/**
 * The wunging giant, a huge puppet on a wheeled base (blockbench exports/wunging_giant.bbmodel), based on the Rolling
 * Giant from Kane Parsons' The Oldest View. It hunts one player at a time, and the longer it hunts the same player
 * the more of its own rules it breaks (see {@link Phase}):
 * 1. Statue: it only moves while nobody looks at it. It can't go up anything (stairs, steps, ladders), and when its
 *    target gets above it, it rolls off somewhere it can't be seen from up there and waits for them to come down,
 *    like the Rolling Giant at the escalator.
 * 2. Restless: it also creeps towards its target while watched, a little faster the longer this lasts.
 * 3. Climber: faster, and it rolls up steps and stairs and climbs ladders (and other climbable blocks).
 * 4. Relentless: it never stops.
 * When it gets its hands on its target it lifts them up in front of its face, shakes them for 2 seconds and kills them.
 * Losing its target (they died, left, went far away or into creative) sends it back to the start.
 */
public class WungingGiantEntity extends Monster {
    // The entity id of the player it holds, -1 when it holds nobody. Synced so their screen can shake.
    private static final EntityDataAccessor<Integer> HELD_ID =
            SynchedEntityData.defineId(WungingGiantEntity.class, EntityDataSerializers.INT);

    // How long it has to hunt the same player before each next phase. Short for testing: 1, 2 and 3 minutes.
    private static final int RESTLESS_AFTER = 60 * 20;
    private static final int CLIMBER_AFTER = 2 * 60 * 20;
    private static final int RELENTLESS_AFTER = 3 * 60 * 20;

    // Players closer than this get hunted, the hunt is over when they get further away than LOSE_RANGE
    private static final double HUNT_RANGE = 48.0D;
    private static final double LOSE_RANGE = 80.0D;
    // When it is loaded while hunting someone who isn't there (logged out, or not back yet), it waits this long for them
    private static final int WAIT_FOR_TARGET_TICKS = 200;

    // Speeds (times the movement speed attribute) while nobody watches, and while it is watched.
    // Mob speed goes with the square of this: about 4, 4.8, 6.2 and 7.8 blocks per second while unwatched
    // (a player walks 4.3 and sprints 5.6), creeping at 0.5 to 1.4 while restless and 2.2 while climbing.
    private static final double STATUE_SPEED = 1.0D;
    private static final double RESTLESS_SPEED = 1.1D;
    private static final double RESTLESS_CREEP_START = 0.35D;
    private static final double RESTLESS_CREEP_END = 0.6D;
    private static final double CLIMBER_SPEED = 1.25D;
    private static final double CLIMBER_CREEP = 0.75D;
    private static final double RELENTLESS_SPEED = 1.4D;

    // Before it can climb it only rolls over tiny bumps (carpet, snow, path blocks), not over stairs or slabs.
    // It never jumps (see jumpFromGround), so it can't get up anything higher than this.
    private static final double LOW_STEP_HEIGHT = 0.4D;
    private static final double CLIMBING_STEP_HEIGHT = 1.1D;

    // Players further away than this don't count as watching
    private static final double WATCH_RANGE = 128.0D;
    // It counts as seen when it is within this angle of where a player looks. A bit wider than the screen at the
    // normal FOV, so it never moves at the edge of the screen.
    private static final double WATCH_ANGLE_COS = Math.cos(Math.toRadians(70.0D));
    // Where its hands are (blocks to each side and up), they count for being seen too
    private static final double HAND_REACH = 2.6D;
    private static final double HAND_HEIGHT = 3.4D;

    private static final int REPATH_TICKS = 10;
    // Ladders it looks for when it can't get up to its target any other way
    private static final int CLIMB_SEARCH_RANGE = 12;
    private static final double CLOSE_TO_LADDER = 2.5D;
    private static final int CLIMB_GIVE_UP_TICKS = 200;
    // Hiding from a target it can't get up to
    private static final int HIDE_SEARCH_RANGE = 16;
    private static final int HIDE_GIVE_UP_TICKS = 200;
    private static final int LURK_TICKS = 100;

    // Grabbing: how close it has to get, how long it shakes them before they die, how high it lifts them (their eyes
    // end up at about the height of its face) and how hard their position jolts while it shakes them
    private static final double GRAB_REACH = 0.6D;
    private static final int SHAKE_TICKS = 40;
    private static final int LIFT_TICKS = 8;
    private static final double MAX_LIFT = 2.2D;
    private static final double SHAKE_OFFSET = 0.08D;
    // When someone survives it (a totem of undying), it lets go and starts over after this long
    private static final int GRAB_COOLDOWN_TICKS = 100;
    // How far from the giant a player it holds can be, to find the giant holding a player
    private static final double HOLD_SEARCH_RANGE = 6.0D;

    // The wheels in the model are 2x2 pixel squares stood on a corner, so about 1.4 pixels from the axle to the
    // ground. One full turn rolls it this far (in blocks).
    private static final float WHEEL_TURN_DISTANCE = 2.0F * (float) Math.PI * (float) Math.sqrt(2.0D) / 16.0F;
    // One full turn of the wheels is one loop of the Rolling animation, which is 20 ticks long
    private static final float ROLL_ANIMATION_TICKS = 20.0F;

    /** How far it is in breaking its rules, by how long it has been hunting the same player. */
    public enum Phase {
        STATUE, RESTLESS, CLIMBER, RELENTLESS;

        public boolean canClimb() {
            return this.ordinal() >= CLIMBER.ordinal();
        }
    }

    // Hunting (server side only)
    private int huntTicks;
    // The player it was hunting when it was saved, until they are found again
    private @Nullable UUID huntedId;
    private int waitForTargetTicks;
    private boolean watched;
    private boolean frozen;
    private float frozenYaw;

    // Getting to its target
    private int repathCooldown;
    private int pathCooldown;
    private boolean targetReachable;
    private @Nullable Vec3 lastPathTarget;
    private @Nullable Vec3 hidingSpot;
    private int hideTicks;
    private int lurkTicks;
    private @Nullable BlockPos climbPos;
    private boolean climbing;
    private int climbTicks;

    // Holding its target
    private int grabCooldown;
    private int holdTicks;
    private Vec3 holdPos = Vec3.ZERO;
    private double liftHeight;

    // How far the wheels have turned, in ticks of the Rolling animation (client side only)
    private float wheelRoll;
    private float wheelRollO;

    public WungingGiantEntity(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 100.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
                .add(Attributes.FOLLOW_RANGE, LOSE_RANGE)
                .add(Attributes.STEP_HEIGHT, LOW_STEP_HEIGHT);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
        super.defineSynchedData(pBuilder);
        pBuilder.define(HELD_ID, -1);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide()) {
            this.turnWheels();
        }
    }

    // Client side only. The wheels turn by how far it rolled, so they never spin while it stands still.
    private void turnWheels() {
        this.wheelRollO = this.wheelRoll;
        // Only movement along the way it faces turns them (backwards turns them back), being pushed sideways doesn't
        float yaw = this.yBodyRot * ((float) Math.PI / 180.0F);
        double forward = -(this.getX() - this.xo) * Mth.sin(yaw) + (this.getZ() - this.zo) * Mth.cos(yaw);
        this.wheelRoll += (float) (forward / WHEEL_TURN_DISTANCE) * ROLL_ANIMATION_TICKS;
        // Both values are kept within about one turn (and never below 0, the animation can't play backwards) so the
        // float doesn't lose precision. They move together, so the turning stays smooth between ticks.
        float wrap = Mth.floor(Math.min(this.wheelRoll, this.wheelRollO) / ROLL_ANIMATION_TICKS) * ROLL_ANIMATION_TICKS;
        this.wheelRoll -= wrap;
        this.wheelRollO -= wrap;
    }

    /** How far the wheels have turned, in ticks of the Rolling animation. */
    public float getWheelRoll(float partialTick) {
        return Mth.lerp(partialTick, this.wheelRollO, this.wheelRoll);
    }

    @Override
    public void aiStep() {
        if (!this.level().isClientSide() && this.isAlive()) {
            this.updateHunt();
            this.watched = this.isWatched();
            int heldId = this.getHeldEntityId();
            if (heldId >= 0) {
                if (this.level().getEntity(heldId) instanceof Player player) {
                    this.holdOnTo(player);
                } else {
                    this.release();
                }
            } else {
                this.stalk();
            }
        }
        super.aiStep();
    }

    /* HUNTING */

    public Phase getPhase() {
        if (this.huntTicks >= RELENTLESS_AFTER) {
            return Phase.RELENTLESS;
        } else if (this.huntTicks >= CLIMBER_AFTER) {
            return Phase.CLIMBER;
        } else if (this.huntTicks >= RESTLESS_AFTER) {
            return Phase.RESTLESS;
        }
        return Phase.STATUE;
    }

    private @Nullable Player getHuntedPlayer() {
        return this.getTarget() instanceof Player player ? player : null;
    }

    private boolean isValidTarget(Player player, double range) {
        return player.isAlive() && !player.isCreative() && !player.isSpectator() && player.level() == this.level()
                && this.distanceToSqr(player) <= range * range;
    }

    /** Keeps track of who it hunts and for how long, and picks someone new when it hunts nobody. */
    private void updateHunt() {
        if (this.grabCooldown > 0) {
            this.grabCooldown--;
        }

        Player target = this.getHuntedPlayer();
        if (target != null && !this.isValidTarget(target, LOSE_RANGE)) {
            this.loseTarget();
            target = null;
        }

        if (target == null && this.huntedId != null) {
            // It was hunting someone when it was saved: it carries on when they are back, or gives up on them
            Player returning = this.level().getPlayerByUUID(this.huntedId);
            if (returning != null && this.isValidTarget(returning, LOSE_RANGE)) {
                int ticks = this.huntTicks;
                this.startHunting(returning);
                this.huntTicks = ticks;
                target = returning;
            } else if (++this.waitForTargetTicks > WAIT_FOR_TARGET_TICKS) {
                this.huntedId = null;
                this.huntTicks = 0;
            }
        }

        if (target == null && this.huntedId == null && this.tickCount % 20 == 0) {
            Player nearest = this.level().getNearestPlayer(this.getX(), this.getY(), this.getZ(), HUNT_RANGE,
                    entity -> entity instanceof Player candidate && this.isValidTarget(candidate, HUNT_RANGE));
            if (nearest != null) {
                this.startHunting(nearest);
                target = nearest;
            }
        }

        if (target != null) {
            this.huntTicks = Math.min(this.huntTicks + 1, RELENTLESS_AFTER);
        }

        double stepHeight = this.getPhase().canClimb() ? CLIMBING_STEP_HEIGHT : LOW_STEP_HEIGHT;
        if (this.getAttributeBaseValue(Attributes.STEP_HEIGHT) != stepHeight) {
            this.getAttribute(Attributes.STEP_HEIGHT).setBaseValue(stepHeight);
        }
    }

    private void startHunting(Player player) {
        this.setTarget(player);
        this.huntTicks = 0;
        this.huntedId = null;
        this.waitForTargetTicks = 0;
        this.resetPlans();
    }

    private void loseTarget() {
        this.setTarget(null);
        this.huntTicks = 0;
        this.resetPlans();
        this.getNavigation().stop();
    }

    private void resetPlans() {
        this.repathCooldown = 0;
        this.pathCooldown = 0;
        this.targetReachable = false;
        this.lastPathTarget = null;
        this.hidingSpot = null;
        this.stopClimbing();
    }

    // A player hitting it while it hunts nobody becomes its target
    @Override
    public boolean hurtServer(ServerLevel pLevel, DamageSource pSource, float pAmount) {
        boolean hurt = super.hurtServer(pLevel, pSource, pAmount);
        if (hurt && this.getHuntedPlayer() == null && this.huntedId == null && pSource.getEntity() instanceof Player player
                && this.isValidTarget(player, LOSE_RANGE)) {
            this.startHunting(player);
        }
        return hurt;
    }

    /* BEING WATCHED */

    /** True when any player is looking at it (spectators don't count, players in creative do). */
    private boolean isWatched() {
        for (Player player : this.level().players()) {
            if (this.isWatchedBy(player)) {
                return true;
            }
        }
        return false;
    }

    /** It is in front of the player, not too far away, and they can see some part of it. Blind players can't. */
    private boolean isWatchedBy(Player player) {
        if (player.isSpectator() || !player.isAlive() || this.distanceToSqr(player) > WATCH_RANGE * WATCH_RANGE
                || player.hasEffect(MobEffects.BLINDNESS) || player.hasEffect(MobEffects.DARKNESS)) {
            return false;
        }
        Vec3 eye = player.getEyePosition();
        Vec3 look = player.getViewVector(1.0F);
        for (Vec3 point : this.getWatchPoints()) {
            Vec3 toPoint = point.subtract(eye);
            double distance = toPoint.length();
            if (distance < 0.5D) {
                return true;
            }
            if (look.dot(toPoint) / distance >= WATCH_ANGLE_COS && this.isVisibleFrom(player, point)) {
                return true;
            }
        }
        return false;
    }

    /** Its feet, middle, face and both hands: seeing any of them counts as seeing it. */
    private Vec3[] getWatchPoints() {
        float yaw = this.yBodyRot * ((float) Math.PI / 180.0F);
        // (cos, sin) of the body rotation points to its left side
        double sideX = Mth.cos(yaw) * HAND_REACH;
        double sideZ = Mth.sin(yaw) * HAND_REACH;
        double x = this.getX();
        double y = this.getY();
        double z = this.getZ();
        return new Vec3[]{
                new Vec3(x, y + 0.5D, z),
                new Vec3(x, y + this.getBbHeight() / 2.0D, z),
                new Vec3(x, this.getEyeY(), z),
                new Vec3(x + sideX, y + HAND_HEIGHT, z + sideZ),
                new Vec3(x - sideX, y + HAND_HEIGHT, z - sideZ)
        };
    }

    /** Nothing blocks the view from the player's eyes to the point (glass and other see-through blocks don't). */
    private boolean isVisibleFrom(Player player, Vec3 point) {
        return this.level().clip(new ClipContext(player.getEyePosition(), point, ClipContext.Block.VISUAL,
                ClipContext.Fluid.NONE, player)).getType() == HitResult.Type.MISS;
    }

    /* MOVING */

    /** How fast it may move right now (times its movement speed), 0 when it has to stand still. */
    private double currentSpeed(Phase phase) {
        return switch (phase) {
            case STATUE -> this.watched ? 0.0D : STATUE_SPEED;
            case RESTLESS -> {
                if (!this.watched) {
                    yield RESTLESS_SPEED;
                }
                double progress = (double) (this.huntTicks - RESTLESS_AFTER) / (CLIMBER_AFTER - RESTLESS_AFTER);
                yield Mth.lerp(Mth.clamp(progress, 0.0D, 1.0D), RESTLESS_CREEP_START, RESTLESS_CREEP_END);
            }
            case CLIMBER -> this.watched ? CLIMBER_CREEP : CLIMBER_SPEED;
            case RELENTLESS -> RELENTLESS_SPEED;
        };
    }

    /** Goes after its target, as far as its phase allows. */
    private void stalk() {
        Player target = this.getHuntedPlayer();
        if (target == null) {
            // Nobody to hunt: it just stands there
            this.frozen = false;
            return;
        }
        Phase phase = this.getPhase();
        double speed = this.currentSpeed(phase);
        if (speed <= 0.0D) {
            this.freeze();
            return;
        }
        this.frozen = false;

        if (this.grabCooldown <= 0 && this.getBoundingBox().inflate(GRAB_REACH, 0.0D, GRAB_REACH).intersects(target.getBoundingBox())) {
            this.grab(target);
            return;
        }
        if (this.climbing) {
            this.keepClimbing(speed);
            return;
        }

        if (this.pathCooldown > 0) {
            this.pathCooldown--;
        }
        this.getNavigation().setSpeedModifier(speed);
        boolean targetMoved = this.lastPathTarget == null || target.position().distanceToSqr(this.lastPathTarget) > 2.25D;
        if (--this.repathCooldown <= 0 || targetMoved) {
            this.repathCooldown = REPATH_TICKS;
            this.lastPathTarget = target.position();
            Path path = this.getNavigation().createPath(target, 1);
            this.targetReachable = path != null && path.canReach();
            if (path != null && !phase.canClimb() && truncateAtClimb(path)) {
                this.targetReachable = false;
            }
            if (this.targetReachable || !this.isAbove(target)) {
                // Straight for them, or as close as it can get when there is no way to them on this level
                this.hidingSpot = null;
                this.climbPos = null;
                this.getNavigation().moveTo(path, speed);
                return;
            }
            if (phase.canClimb() && this.climbPos == null) {
                this.climbPos = this.findClimbable(target);
                if (this.climbPos == null) {
                    // Nothing to climb around here: as close as it can get, and wait
                    this.getNavigation().moveTo(path, speed);
                    return;
                }
            }
        }

        if (this.targetReachable || !this.isAbove(target)) {
            return;
        }
        if (phase.canClimb()) {
            if (this.climbPos != null) {
                this.goClimb(speed);
            }
        } else {
            this.hide(target, speed);
        }
    }

    /** Watched while it is a statue: it stops dead and doesn't even turn. */
    private void freeze() {
        if (!this.frozen) {
            this.frozen = true;
            this.frozenYaw = this.yBodyRot;
        }
        this.getNavigation().stop();
        this.setDeltaMovement(0.0D, this.getDeltaMovement().y, 0.0D);
        this.setYRot(this.frozenYaw);
        this.yBodyRot = this.frozenYaw;
        this.yHeadRot = this.frozenYaw;
        // The moment nobody looks any more it sets off again, without waiting for its next path
        this.repathCooldown = 0;
        this.pathCooldown = 0;
    }

    /** Its target is up on a higher floor (more than a jump above it). */
    private boolean isAbove(Player target) {
        return target.getY() - this.getY() > 1.5D;
    }

    /** Cuts the path off where it would have to go up (a step, stairs or a ladder). True when it was cut. */
    private static boolean truncateAtClimb(Path path) {
        for (int i = 1; i < path.getNodeCount(); i++) {
            if (path.getNode(i).y > path.getNode(i - 1).y) {
                path.truncateNodes(i);
                return true;
            }
        }
        return false;
    }

    /**
     * Before it can climb: it can't get up to its target, so it rolls off somewhere it can't be seen from up there
     * and waits for them to come back down (stalk goes back to the chase as soon as they can be reached).
     */
    private void hide(Player target, double speed) {
        if (this.hidingSpot == null || this.hideTicks > HIDE_GIVE_UP_TICKS) {
            if (this.pathCooldown > 0) {
                return;
            }
            this.pathCooldown = REPATH_TICKS;
            this.hidingSpot = this.findHidingSpot(target);
            this.hideTicks = 0;
            this.lurkTicks = LURK_TICKS;
            if (this.hidingSpot == null) {
                return;
            }
        }

        if (this.position().distanceToSqr(this.hidingSpot) > 4.0D) {
            this.hideTicks++;
            // (Again) on its way there, also after standing still because it was watched
            if (this.getNavigation().isDone() && this.pathCooldown <= 0) {
                this.pathCooldown = REPATH_TICKS;
                Path path = this.getNavigation().createPath(BlockPos.containing(this.hidingSpot), 0);
                if (path != null) {
                    truncateAtClimb(path);
                }
                this.getNavigation().moveTo(path, speed);
            }
        } else if (--this.lurkTicks <= 0) {
            // Waiting. Now and then it checks if it can still be seen from up there, then it finds a better spot.
            this.lurkTicks = LURK_TICKS;
            if (this.isVisibleFrom(target, this.position().add(0.0D, this.getBbHeight() / 2.0D, 0.0D))) {
                this.hidingSpot = null;
            }
        }
    }

    /** Somewhere away from the target, out of their sight if possible. */
    private @Nullable Vec3 findHidingSpot(Player target) {
        Vec3 fallback = null;
        for (int i = 0; i < 10; i++) {
            Vec3 pos = LandRandomPos.getPosAway(this, HIDE_SEARCH_RANGE, 4, target.position());
            if (pos == null) {
                continue;
            }
            if (!this.isVisibleFrom(target, pos.add(0.0D, this.getBbHeight() / 2.0D, 0.0D))) {
                return pos;
            }
            if (fallback == null) {
                fallback = pos;
            }
        }
        return fallback;
    }

    /* CLIMBING */

    /**
     * Once it is a climber it can get up ladders (and vines, scaffolding and so on). It is too big for a ladder
     * shaft, so it climbs the ladders on a wall it rolls up to: anything climbable touching its sides counts.
     * Wheels can't climb anything before that, also not ladders it happens to stand in.
     */
    @Override
    public boolean onClimbable() {
        return this.climbing && this.touchesClimbable();
    }

    private boolean touchesClimbable() {
        AABB box = this.getBoundingBox().inflate(0.15D, 0.0D, 0.15D);
        for (BlockPos pos : BlockPos.betweenClosed(BlockPos.containing(box.minX, box.minY, box.minZ),
                BlockPos.containing(box.maxX, box.maxY, box.maxZ))) {
            if (this.level().getBlockState(pos).is(BlockTags.CLIMBABLE)) {
                return true;
            }
        }
        return false;
    }

    /** The bottom of a ladder (at least 2 blocks high) on its level, the closer to it and its target the better. */
    private @Nullable BlockPos findClimbable(Player target) {
        BlockPos feet = this.blockPosition();
        BlockPos best = null;
        double bestScore = Double.MAX_VALUE;
        for (BlockPos pos : BlockPos.betweenClosed(feet.offset(-CLIMB_SEARCH_RANGE, 0, -CLIMB_SEARCH_RANGE),
                feet.offset(CLIMB_SEARCH_RANGE, 2, CLIMB_SEARCH_RANGE))) {
            if (!this.level().getBlockState(pos).is(BlockTags.CLIMBABLE)
                    || !this.level().getBlockState(pos.above()).is(BlockTags.CLIMBABLE)) {
                continue;
            }
            double score = Math.sqrt(pos.distToCenterSqr(this.position())) + Math.sqrt(pos.distToCenterSqr(target.position()));
            if (score < bestScore) {
                bestScore = score;
                best = pos.immutable();
            }
        }
        return best;
    }

    /** Rolls up to the ladder, and starts climbing once it touches it. */
    private void goClimb(double speed) {
        BlockPos ladder = this.climbPos;
        if (ladder == null || !this.level().getBlockState(ladder).is(BlockTags.CLIMBABLE)) {
            this.climbPos = null;
            return;
        }
        if (this.touchesClimbable()) {
            this.climbing = true;
            this.climbTicks = 0;
            this.getNavigation().stop();
            this.keepClimbing(speed);
            return;
        }
        Vec3 bottom = Vec3.atBottomCenterOf(ladder);
        double dx = bottom.x - this.getX();
        double dz = bottom.z - this.getZ();
        if (dx * dx + dz * dz < CLOSE_TO_LADDER * CLOSE_TO_LADDER) {
            // Right there: straight at the wall it is on
            this.getNavigation().stop();
            Vec3 wall = this.wallBehind(ladder);
            this.getMoveControl().setWantedPosition(wall.x, this.getY(), wall.z, speed);
        } else if (this.getNavigation().isDone() && this.pathCooldown <= 0) {
            this.pathCooldown = REPATH_TICKS;
            this.getNavigation().moveTo(bottom.x, bottom.y, bottom.z, speed);
        }
    }

    /** Pushes against the wall, which makes it go up (like any mob on a ladder), until it rolls off on top. */
    private void keepClimbing(double speed) {
        BlockPos ladder = this.climbPos;
        if (ladder == null || ++this.climbTicks > CLIMB_GIVE_UP_TICKS) {
            // Stuck (no room for it up there?): lets go, and looks for another way later
            this.stopClimbing();
            return;
        }
        if (this.onGround() && !this.touchesClimbable()) {
            // Made it to the top (or it was pushed off): back to rolling
            this.stopClimbing();
            this.repathCooldown = 0;
            return;
        }
        Vec3 wall = this.wallBehind(ladder);
        this.getMoveControl().setWantedPosition(wall.x, this.getY(), wall.z, speed);
    }

    private void stopClimbing() {
        this.climbing = false;
        this.climbPos = null;
        this.climbTicks = 0;
    }

    /** The middle of the block a ladder hangs on (a ladder faces away from its wall), or of the climbable block itself. */
    private Vec3 wallBehind(BlockPos pos) {
        BlockState state = this.level().getBlockState(pos);
        if (state.hasProperty(LadderBlock.FACING)) {
            return Vec3.atCenterOf(pos.relative(state.getValue(LadderBlock.FACING).getOpposite()));
        }
        return Vec3.atCenterOf(pos);
    }

    /* GRABBING */

    public int getHeldEntityId() {
        return this.entityData.get(HELD_ID);
    }

    /** True when a wunging giant holds this entity (used on the client to shake the screen). */
    public static boolean isHeldByGiant(Entity entity) {
        return !entity.level().getEntitiesOfClass(WungingGiantEntity.class, entity.getBoundingBox().inflate(HOLD_SEARCH_RANGE),
                giant -> giant.getHeldEntityId() == entity.getId()).isEmpty();
    }

    /** Gets its hands on its target: they are lifted up where they stand, in front of its face. */
    private void grab(Player target) {
        this.getNavigation().stop();
        this.stopClimbing();
        target.stopRiding();
        if (target.isSleeping()) {
            target.stopSleeping();
        }
        this.entityData.set(HELD_ID, target.getId());
        this.holdTicks = 0;
        this.holdPos = target.position();
        this.liftHeight = this.findLiftHeight(target);

        float yaw = (float) (Mth.atan2(target.getZ() - this.getZ(), target.getX() - this.getX()) * (180.0F / (float) Math.PI)) - 90.0F;
        this.setYRot(yaw);
        this.yBodyRot = yaw;
        this.yHeadRot = yaw;
    }

    /** As high as there is room for, up to its face. */
    private double findLiftHeight(Player target) {
        for (double lift = MAX_LIFT; lift > 0.0D; lift -= 0.5D) {
            if (this.level().noCollision(target, target.getBoundingBox().move(0.0D, lift, 0.0D))) {
                return lift;
            }
        }
        return 0.0D;
    }

    /** Holds them up and shakes them (their screen shakes too, see ModEventBusClientEvents), then kills them. */
    private void holdOnTo(Player player) {
        this.getNavigation().stop();
        this.setDeltaMovement(0.0D, this.getDeltaMovement().y, 0.0D);
        if (!player.isAlive() || player.isCreative() || player.isSpectator() || player.level() != this.level()) {
            this.release();
            return;
        }

        this.holdTicks++;
        double lift = this.liftHeight * Math.min(1.0D, (double) this.holdTicks / LIFT_TICKS);
        double shakeX = (this.random.nextDouble() - 0.5D) * 2.0D * SHAKE_OFFSET;
        double shakeZ = (this.random.nextDouble() - 0.5D) * 2.0D * SHAKE_OFFSET;
        player.teleportTo(this.holdPos.x + shakeX, this.holdPos.y + lift, this.holdPos.z + shakeZ);
        player.setDeltaMovement(Vec3.ZERO);
        player.resetFallDistance();

        if (this.holdTicks >= SHAKE_TICKS && this.level() instanceof ServerLevel serverLevel) {
            player.hurtServer(serverLevel, ModDamageTypes.causeWungingGiant(serverLevel.registryAccess(), this), Float.MAX_VALUE);
            this.release();
            if (player.isAlive()) {
                // Survived it (a totem of undying): it lets go, and the hunt starts over
                this.grabCooldown = GRAB_COOLDOWN_TICKS;
                this.huntTicks = 0;
                this.resetPlans();
            }
        }
    }

    private void release() {
        this.entityData.set(HELD_ID, -1);
        this.holdTicks = 0;
    }

    @Override
    public void die(DamageSource pDamageSource) {
        this.release();
        super.die(pDamageSource);
    }

    /* MISC */

    // It is on wheels, it can't jump (so it can't get up anything taller than its step height either)
    @Override
    public void jumpFromGround() {
    }

    // A huge puppet: players walking into it and flowing water don't shove it around (a statue that slides
    // around while you look at it wouldn't be much of a statue)
    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected void doPush(Entity pEntity) {
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    // It rolls off ledges without getting hurt, and it doesn't breathe
    @Override
    public boolean causeFallDamage(double pFallDistance, float pDamageMultiplier, DamageSource pDamageSource) {
        return false;
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    // It stays where it is, to keep hunting (it will only spawn in its own structure)
    @Override
    public boolean removeWhenFarAway(double pDistanceToClosestPlayer) {
        return false;
    }

    // No footsteps, it rolls (its own sounds will be added later)
    @Override
    protected void playStepSound(BlockPos pPos, BlockState pState) {
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        Player target = this.getHuntedPlayer();
        UUID hunted = target != null ? target.getUUID() : this.huntedId;
        if (hunted != null) {
            output.store("Hunted", UUIDUtil.CODEC, hunted);
        }
        output.putInt("HuntTicks", this.huntTicks);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.huntedId = input.read("Hunted", UUIDUtil.CODEC).orElse(null);
        this.huntTicks = this.huntedId != null ? input.getIntOr("HuntTicks", 0) : 0;
    }
}
