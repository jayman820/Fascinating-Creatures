package net.josh.wungus.entity.custom;

import net.josh.wungus.entity.ModEntities;
import net.josh.wungus.entity.variant.WungusVariant;
import net.josh.wungus.item.ModItems;
import net.josh.wungus.particle.ModParticles;
import net.josh.wungus.sound.ModSounds;
import net.josh.wungus.worldgen.ModBiomeModifiers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Util;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.DismountHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.event.EventHooks;
import net.neoforged.neoforge.event.entity.EntityTeleportEvent;
import org.jspecify.annotations.Nullable;

import java.util.Locale;

public class WungusEntity extends TamableAnimal implements PlayerRideableJumping {
    // Jump power used when the wungus jumps on its own. The JUMP_STRENGTH attribute is only used for jumps
    // made while being ridden, like it was before jump strength became a generic attribute.
    private static final float DEFAULT_JUMP_POWER = 0.42F;

    private boolean allowStandSliding;
    private float playerJumpPendingScale;
    private boolean isJumping = false;

    public WungusEntity(EntityType<? extends TamableAnimal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    private static final EntityDataAccessor<Boolean> SITTING =
            SynchedEntityData.defineId(WungusEntity.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> TRUSTING =
            SynchedEntityData.defineId(WungusEntity.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Integer> DATA_ID_TYPE_VARIANT =
            SynchedEntityData.defineId(WungusEntity.class, EntityDataSerializers.INT);

    public static final EntityDataAccessor<Integer> TOTAL_STEROID_USES =
            SynchedEntityData.defineId(WungusEntity.class, EntityDataSerializers.INT);

    public static final EntityDataAccessor<Integer> HEALTH_STEROID_USES =
            SynchedEntityData.defineId(WungusEntity.class, EntityDataSerializers.INT);

    public static final EntityDataAccessor<Integer> SPEED_STEROID_USES =
            SynchedEntityData.defineId(WungusEntity.class, EntityDataSerializers.INT);

    public static final EntityDataAccessor<Integer> JUMP_STEROID_USES =
            SynchedEntityData.defineId(WungusEntity.class, EntityDataSerializers.INT);


    public final AnimationState runningAnimationState = new AnimationState();
    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState sittingAnimation = new AnimationState();
    public final AnimationState standingAnimation = new AnimationState();
    private boolean isBeingChased = false;
    private int idleAnimationTimeout = 1;
    private boolean tamedGoalsAdded = false;
    private WungusAvoidEntityGoal<Player> avoidEntityGoal;
    private FollowParentGoal followParentGoal;


    @Override
    public void tick() {
        super.tick();
        if(this.level().isClientSide()) {
            setupAnimationStates();
        }
    }

    private void setupAnimationStates() {
        if(this.isBeingChased) {
            this.runningAnimationState.start(this.tickCount);
        }
        if(this.entityData.get(SITTING)) {
            this.sittingAnimation.startIfStopped(this.tickCount);
        } else {
            this.sittingAnimation.stop();
        }

        if(this.idleAnimationTimeout <= 0 && !this.isBeingChased) {
            this.idleAnimationTimeout = this.random.nextInt(40) + 80;
            this.idleAnimationState.start(this.tickCount);
        } else {
            --this.idleAnimationTimeout;
        }
    }

    @Override
    protected void updateWalkAnimation(float pDistance) {
        float f;
        if (this.getPose() == Pose.STANDING) {
            f = Math.min(pDistance * 6F, 1f);
        } else {
            f = 0f;
        }

        this.walkAnimation.update(f, 0.2f, 1f);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new WungusPanicGoal(this, 1.4D));
        this.avoidEntityGoal = new WungusAvoidEntityGoal<>(this, Player.class, 16.0F, 0.8D, 1.33D);
        this.goalSelector.addGoal(2, this.avoidEntityGoal);
        this.followParentGoal = new FollowParentGoal(this,1.2D);
        this.goalSelector.addGoal(6, this.followParentGoal);
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.1D));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8f));
        this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createAnimalAttributes()
                .add(Attributes.MAX_HEALTH, 100)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.JUMP_STRENGTH, 1.0F)
                .add(Attributes.FOLLOW_RANGE, 240)
                .add(Attributes.ARMOR_TOUGHNESS, .05f)
                .add(Attributes.STEP_HEIGHT, 1.0D);
    }

    /** Picks the variant matching the biome the wungus is in, or a random one if the biome has none. */
    public static WungusVariant variantForBiome(Holder<Biome> biome, RandomSource random) {
        if (biome.is(ModBiomeModifiers.SPAWN_WUNGUS_TAG)) {
            return WungusVariant.DEFAULT;
        } else if (biome.is(ModBiomeModifiers.SPAWN_WHITE_WUNGUS_TAG)) {
            return WungusVariant.WHITE;
        } else if (biome.is(ModBiomeModifiers.SPAWN_GREEN_WUNGUS_TAG)) {
            return WungusVariant.GREEN;
        } else if (biome.is(ModBiomeModifiers.SPAWN_BLUE_WUNGUS_TAG)) {
            return WungusVariant.BLUE;
        }
        return Util.getRandom(WungusVariant.values(), random);
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        WungusVariant baby = variantForBiome(serverLevel.getBiome(this.getOnPos()), this.random);
        WungusEntity wungus = ModEntities.WUNGUS.get().create(serverLevel, EntitySpawnReason.BREEDING);
        if (wungus == null) {
            return null;
        }
        Player closest = serverLevel.getNearestPlayer(ageableMob.getX() + 0.5F, ageableMob.getY() + 0.5F, ageableMob.getZ() + 0.5F, 20, EntitySelector.NO_SPECTATORS);
        if (closest != null) {
            wungus.tame(closest);
        }
        wungus.setVariant(baby);
        return wungus;
    }

    @Override
    public void aiStep() {
        if (this.level().isClientSide() && this.nameContains("sakura")) {
            for(int i = 0; i < 1; ++i) {
                int rand_int = (int) Math.floor((Math.random() * 3));
                SimpleParticleType sparkleParticle;
                if (rand_int == 0) {
                    sparkleParticle = ModParticles.BLUE_SPARKLE_PARTICLES.get();
                } else if (rand_int == 1) {
                    sparkleParticle = ModParticles.DARK_SPARKLE_PARTICLES.get();
                } else {
                    sparkleParticle = ModParticles.LIGHT_SPARKLE_PARTICLES.get();
                }
                this.level().addParticle(sparkleParticle, this.getRandomX(0.5D), this.getRandomY(), this.getRandomZ(0.5D), (this.random.nextDouble() - 0.5D) * 0.2D, -this.random.nextDouble() * 0.2D, (this.random.nextDouble() - 0.5D) * 0.2D);
            }
        }
        super.aiStep();
    }

    /** Easter eggs: some wungus names change the texture, sound and particles. */
    public boolean nameContains(String name) {
        return this.getName().getString().toLowerCase(Locale.ROOT).contains(name);
    }

    @Override
    public boolean isFood(ItemStack pStack) {
        return pStack.is(Tags.Items.SEEDS);
    }

    public boolean isTrusting() {
        return this.entityData.get(TRUSTING);
    }

    public void setTrusting(boolean pTrusting) {
        this.entityData.set(TRUSTING, pTrusting);
    }

    // Called when tamed and also when a tamed wungus is loaded (then without side effects)
    @Override
    public void setTame(boolean pTamed, boolean pApplySideEffects) {
        super.setTame(pTamed, pApplySideEffects);
        if (pTamed && !this.tamedGoalsAdded) {
            this.tamedGoalsAdded = true;
            this.setTrusting(true);
            this.goalSelector.addGoal(2, new SitWhenOrderedToGoal(this));
            this.goalSelector.addGoal(3, new BreedGoal(this, 1.15D));
            this.goalSelector.addGoal(4, new FollowOwnerGoal(this, 1.0D, 10.0F, 2.0F));
            this.goalSelector.removeGoal(this.followParentGoal);
            this.goalSelector.removeGoal(this.avoidEntityGoal);
        }
    }

    @Override
    protected void applyTamingSideEffects() {
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(50.0D);
        if (this.isTame()) {
            this.setHealth(50.0F);
        }
    }

    @Override
    public InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        if (itemstack.is(Items.BUCKET) && !this.isBaby()) {
            pPlayer.playSound(SoundEvents.COW_MILK, 1.0F, 1.0F);
            ItemStack itemstack1 = ItemUtils.createFilledResult(itemstack, pPlayer, new ItemStack(ModItems.WUNGUS_MILK.get()));
            pPlayer.setItemInHand(pHand, itemstack1);
            return InteractionResult.SUCCESS;
        } else {
            InteractionResult interactionresult = super.mobInteract(pPlayer, pHand);
            if (interactionresult.consumesAction()) {
                return interactionresult;
            }
            if (!this.isOwnedBy(pPlayer) && itemstack.is(ModItems.WUNGUS_AMBROSIA.get())) {
                itemstack.shrink(1);
                if (!this.level().isClientSide()) {
                    this.tame(pPlayer);
                }
                return interactionresult;
            }
            if (this.isOwnedBy(pPlayer) && (itemstack.is(ModItems.HEALTH_STEROID.get()) || itemstack.is(ModItems.SPEED_STEROID.get()) || itemstack.is(ModItems.JUMP_STEROID.get()))) {
                if (itemstack.is(ModItems.HEALTH_STEROID.get())) {
                    if (this.getHealthSteroidUses() < 5 && this.getTotalSteroidUses() < 10) {
                        this.setHealthSteroidUses(this.getHealthSteroidUses() + 1);
                        this.setTotalSteroidUses(this.getTotalSteroidUses() + 1);
                        float health = (float) this.getAttributes().getBaseValue(Attributes.MAX_HEALTH);
                        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(health + 1);
                        this.setHealth(health + 1.0f);
                        itemstack.shrink(1);
                    }
                    return interactionresult;

                } else if (itemstack.is(ModItems.SPEED_STEROID.get())) {
                    if (this.getSpeedSteroidUses() < 5 && this.getTotalSteroidUses() < 10) {
                        this.setSpeedSteroidUses(this.getSpeedSteroidUses() + 1);
                        this.setTotalSteroidUses(this.getTotalSteroidUses() + 1);
                        float speed = (float) this.getAttributes().getBaseValue(Attributes.MOVEMENT_SPEED);
                        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(speed + 0.05);
                        itemstack.shrink(1);
                    }
                    return interactionresult;

                } else if (itemstack.is(ModItems.JUMP_STEROID.get())) {
                    if (this.getJumpSteroidUses() < 5 && this.getTotalSteroidUses() < 10) {
                        this.setJumpSteroidUses(this.getJumpSteroidUses() + 1);
                        this.setTotalSteroidUses(this.getTotalSteroidUses() + 1);
                        float jump = (float) this.getAttributes().getBaseValue(Attributes.JUMP_STRENGTH);
                        this.getAttribute(Attributes.JUMP_STRENGTH).setBaseValue(jump + 0.075);
                        itemstack.shrink(1);
                    }
                    return interactionresult;

                }
            }
            if (!pPlayer.isCrouching() && !this.isBaby() && !this.isOrderedToSit() && this.isOwnedBy(pPlayer)) {
                setRiding(pPlayer);
            } else {
                boolean sit = this.isOrderedToSit();
                if (this.isOwnedBy(pPlayer)) {
                    this.setOrderedToSit(!sit);
                    this.jumping = false;
                    this.navigation.stop();
                    return InteractionResult.SUCCESS;
                }
            }
            return interactionresult;
        }
    }

    protected boolean teleport() {
        if (!this.level().isClientSide() && this.isAlive()) {
            double d0 = this.getX() + (this.random.nextDouble() - 0.5D) * 64.0D;
            double d1 = this.getY() + (double)(this.random.nextInt(64) - 32);
            double d2 = this.getZ() + (this.random.nextDouble() - 0.5D) * 64.0D;
            return this.teleport(d0, d1, d2);
        } else {
            return false;
        }
    }

    private boolean teleport(double pX, double pY, double pZ) {
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos(pX, pY, pZ);

        while(blockpos$mutableblockpos.getY() > this.level().getMinY() && !this.level().getBlockState(blockpos$mutableblockpos).blocksMotion()) {
            blockpos$mutableblockpos.move(Direction.DOWN);
        }

        BlockState blockstate = this.level().getBlockState(blockpos$mutableblockpos);
        boolean flag = blockstate.blocksMotion();
        boolean flag1 = blockstate.getFluidState().is(FluidTags.WATER);
        if (flag && !flag1) {
            EntityTeleportEvent.EnderEntity event = EventHooks.onEnderTeleport(this, pX, pY, pZ);
            if (event.isCanceled()) return false;
            Vec3 vec3 = this.position();
            boolean flag2 = this.randomTeleport(event.getTargetX(), event.getTargetY(), event.getTargetZ(), true);
            if (flag2) {
                this.level().gameEvent(GameEvent.TELEPORT, vec3, GameEvent.Context.of(this));
                if (!this.isSilent()) {
                    this.level().playSound(null, this.xo, this.yo, this.zo, SoundEvents.ENDERMAN_TELEPORT, this.getSoundSource(), 1.0F, 1.0F);
                    this.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
                }
            }

            return flag2;
        } else {
            return false;
        }
    }

    @Override
    protected int calculateFallDamage(double pFallDistance, float pDamageMultiplier) {
        if (pFallDistance <= 10) {
            return 0;
        }
        return super.calculateFallDamage(pFallDistance, pDamageMultiplier);
    }

    @Override
    protected float getJumpPower() {
        return DEFAULT_JUMP_POWER * this.getBlockJumpFactor() + this.getJumpBoostPower();
    }

    @Override
    public void onPlayerJump(int pJumpPower) {
        if (pJumpPower < 0) {
            pJumpPower = 0;
        } else {
            this.allowStandSliding = true;
        }

        this.playerJumpPendingScale = this.getPlayerJumpPendingScale(pJumpPower);
    }

    @Override
    public boolean canJump() {
        return this.isTrusting();
    }

    @Override
    public void handleStartJump(int pJumpPower) {
        this.allowStandSliding = true;
    }

    @Override
    public void handleStopJump() {}

    private void setRiding(Player pPlayer) {
        this.setInSittingPose(false);

        pPlayer.setYRot(this.getYRot());
        pPlayer.setXRot(this.getXRot());
        pPlayer.startRiding(this);
    }

    @Override
    public @Nullable LivingEntity getControllingPassenger() {
        if (this.getFirstPassenger() instanceof Player player) {
            return player;
        }
        return super.getControllingPassenger();
    }

    // Sprinting while riding doubles the speed, see getRiddenSpeed
    @Override
    public boolean canSprint() {
        return true;
    }

    @Override
    protected void tickRidden(Player pController, Vec3 pRiddenInput) {
        super.tickRidden(pController, pRiddenInput);
        this.setRot(pController.getYRot(), pController.getXRot() * 0.5F);
        this.yRotO = this.yBodyRot = this.yHeadRot = this.getYRot();

        if (this.isLocalInstanceAuthoritative() && this.onGround()) {
            this.setRiderJumping(false);
            if (this.playerJumpPendingScale > 0.0F && !this.isRiderJumping()) {
                this.executeRidersJump(this.playerJumpPendingScale, pRiddenInput);
            }

            this.playerJumpPendingScale = 0.0F;
        }
    }

    @Override
    protected Vec3 getRiddenInput(Player pController, Vec3 pSelfInput) {
        return new Vec3(pController.xxa * 0.5F, pSelfInput.y, pController.zza);
    }

    @Override
    protected float getRiddenSpeed(Player pController) {
        float newSpeed = (float) this.getAttributeValue(Attributes.MOVEMENT_SPEED);
        // increasing speed by 100% if the sprint key is held down
        if (pController.isSprinting()) {
            newSpeed *= 2f;
        }
        return newSpeed;
    }

    private boolean isRiderJumping() {
        return this.isJumping;
    }

    private void setRiderJumping(boolean pJumping) {
        this.isJumping = pJumping;
    }

    protected void executeRidersJump(float pPlayerJumpPendingScale, Vec3 pTravelVector) {
        double d1 = this.getJumpPower(pPlayerJumpPendingScale);
        Vec3 vec3 = this.getDeltaMovement();
        this.setDeltaMovement(vec3.x, d1, vec3.z);
        this.setRiderJumping(true);
        this.needsSync = true;
        CommonHooks.onLivingJump(this);
        if (pTravelVector.z > 0.0D) {
            float f = Mth.sin(this.getYRot() * ((float)Math.PI / 180F));
            float f1 = Mth.cos(this.getYRot() * ((float)Math.PI / 180F));
            this.setDeltaMovement(this.getDeltaMovement().add((double)(-0.4F * f * pPlayerJumpPendingScale), 0.0D, (double)(0.4F * f1 * pPlayerJumpPendingScale)));
        }

    }

    @Override
    public Vec3 getDismountLocationForPassenger(LivingEntity pLivingEntity) {
        Direction direction = this.getMotionDirection();
        if (direction.getAxis() != Direction.Axis.Y) {
            int[][] offsets = DismountHelper.offsetsForDirection(direction);
            BlockPos blockpos = this.blockPosition();
            BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

            for (Pose pose : pLivingEntity.getDismountPoses()) {
                AABB aabb = pLivingEntity.getLocalBoundsForPose(pose);

                for (int[] offset : offsets) {
                    blockpos$mutableblockpos.set(blockpos.getX() + offset[0], blockpos.getY(), blockpos.getZ() + offset[1]);
                    double d0 = this.level().getBlockFloorHeight(blockpos$mutableblockpos);
                    if (DismountHelper.isBlockFloorValid(d0)) {
                        Vec3 vec3 = Vec3.upFromBottomCenterOf(blockpos$mutableblockpos, d0);
                        if (DismountHelper.canDismountTo(this.level(), pLivingEntity, aabb.move(vec3))) {
                            pLivingEntity.setPose(pose);
                            return vec3;
                        }
                    }
                }
            }
        }

        return super.getDismountLocationForPassenger(pLivingEntity);
    }

    static class WungusPanicGoal extends PanicGoal {
        private final WungusEntity wungus;
        public WungusPanicGoal(WungusEntity pWungus, double pSpeedModifier) {
            super(pWungus, pSpeedModifier);
            this.wungus = pWungus;
        }

        @Override
        public void start() {
            this.wungus.isBeingChased = true;
            super.start();
        }

        @Override
        public void stop() {
            this.wungus.isBeingChased = false;
            this.wungus.teleport();
            super.stop();
        }
    }

    static class WungusAvoidEntityGoal<T extends LivingEntity> extends AvoidEntityGoal<T> {
        private final WungusEntity wungus;

        public WungusAvoidEntityGoal(WungusEntity pWungus, Class<T> pEntityClassToAvoid, float pMaxDist, double pWalkSpeedModifier, double pSprintSpeedModifier) {
            super(pWungus, pEntityClassToAvoid, pMaxDist, pWalkSpeedModifier, pSprintSpeedModifier, EntitySelector.NO_CREATIVE_OR_SPECTATOR::test);
            this.wungus = pWungus;
        }

        @Override
        public boolean canUse() {
            return !this.wungus.isTrusting() && super.canUse();
        }

        @Override
        public boolean canContinueToUse() {
            return !this.wungus.isTrusting() && super.canContinueToUse();
        }

        @Override
        public void start() {
            this.wungus.isBeingChased = true;
            wungus.setupAnimationStates();
            super.start();
        }

        @Override
        public void stop() {
            this.wungus.isBeingChased = false;
            this.wungus.teleport();
            wungus.setupAnimationStates();
            super.stop();
        }
    }

    @Override
    public boolean isOrderedToSit() {
        return this.entityData.get(SITTING);
    }

    @Override
    public void setOrderedToSit(boolean pOrderedToSit) {
        this.entityData.set(SITTING, pOrderedToSit);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
        super.defineSynchedData(pBuilder);
        pBuilder.define(SITTING, false);
        pBuilder.define(TRUSTING, false);
        pBuilder.define(DATA_ID_TYPE_VARIANT, 0);
        pBuilder.define(TOTAL_STEROID_USES, 0);
        pBuilder.define(HEALTH_STEROID_USES, 0);
        pBuilder.define(SPEED_STEROID_USES, 0);
        pBuilder.define(JUMP_STEROID_USES, 0);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putBoolean("Sitting", this.isOrderedToSit());
        output.putBoolean("Trusting", this.isTrusting());
        output.putInt("Variant", this.getTypeVariant());
        output.putInt("TotalSteroids", this.getTotalSteroidUses());
        output.putInt("HealthSteroids", this.getHealthSteroidUses());
        output.putInt("SpeedSteroids", this.getSpeedSteroidUses());
        output.putInt("JumpSteroids", this.getJumpSteroidUses());
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.setOrderedToSit(input.getBooleanOr("Sitting", false));
        this.setTrusting(input.getBooleanOr("Trusting", false));
        this.setTypeVariant(input.getIntOr("Variant", 0));
        this.setTotalSteroidUses(input.getIntOr("TotalSteroids", 0));
        this.setHealthSteroidUses(input.getIntOr("HealthSteroids", 0));
        this.setSpeedSteroidUses(input.getIntOr("SpeedSteroids", 0));
        this.setJumpSteroidUses(input.getIntOr("JumpSteroids", 0));
    }

    private int getTotalSteroidUses() {
        return this.entityData.get(TOTAL_STEROID_USES);
    }

    public void setTotalSteroidUses(int uses) {
        this.entityData.set(TOTAL_STEROID_USES, uses);
    }

    private int getHealthSteroidUses() {
        return this.entityData.get(HEALTH_STEROID_USES);
    }

    public void setHealthSteroidUses(int uses) {
        this.entityData.set(HEALTH_STEROID_USES, uses);
    }

    private int getSpeedSteroidUses() {
        return this.entityData.get(SPEED_STEROID_USES);
    }

    public void setSpeedSteroidUses(int uses) {
        this.entityData.set(SPEED_STEROID_USES, uses);
    }

    private int getJumpSteroidUses() {
        return this.entityData.get(JUMP_STEROID_USES);
    }

    public void setJumpSteroidUses(int uses) {
        this.entityData.set(JUMP_STEROID_USES, uses);
    }

    public WungusVariant getVariant() {
        return WungusVariant.byId(this.getTypeVariant() & 255);
    }

    private int getTypeVariant() {
        return this.entityData.get(DATA_ID_TYPE_VARIANT);
    }

    public void setVariant(WungusVariant variant) {
        this.entityData.set(DATA_ID_TYPE_VARIANT, variant.getId() & 255);
    }

    private void setTypeVariant(int variant) {
        this.entityData.set(DATA_ID_TYPE_VARIANT, variant);
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, EntitySpawnReason pReason, @Nullable SpawnGroupData pSpawnData) {
        this.setVariant(variantForBiome(pLevel.getBiome(this.getOnPos()), this.random));
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData);
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        if (this.nameContains("papi")) { return ModSounds.MANGUNGUS_AMBIENT.get(); }
        return ModSounds.WUNGUS_AMBIENT.get();
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(DamageSource pDamageSource) {
        return ModSounds.WUNGUS_HURT.get();
    }

    @Override
    protected @Nullable SoundEvent getDeathSound() {
        return ModSounds.WUNGUS_DEATH.get();
    }
}
