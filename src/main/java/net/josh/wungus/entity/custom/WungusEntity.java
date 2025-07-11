package net.josh.wungus.entity.custom;

import net.josh.wungus.entity.ModEntities;
import net.josh.wungus.entity.variant.WungusVariant;
import net.josh.wungus.item.ModItems;
import net.josh.wungus.sound.ModSounds;
import net.josh.wungus.worldgen.ModBiomeModifiers;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Ocelot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.DismountHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.Tags.Items;
import net.minecraftforge.event.level.NoteBlockEvent;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class WungusEntity extends TamableAnimal implements PlayerRideableJumping {
    private boolean allowStandSliding;
    private float playerJumpPendingScale;
    private boolean isJumping = false;

    public WungusEntity(EntityType<? extends TamableAnimal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setMaxUpStep(1f);
    }

    private static final EntityDataAccessor<Boolean> SITTING =
            SynchedEntityData.defineId(WungusEntity.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> TRUSTING =
            SynchedEntityData.defineId(WungusEntity.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Integer> DATA_ID_TYPE_VARIANT =
            SynchedEntityData.defineId(WungusEntity.class, EntityDataSerializers.INT);


    public final AnimationState runningAnimationState = new AnimationState();
    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState sittingAnimation = new AnimationState();
    public final AnimationState standingAnimation = new AnimationState();
    private boolean isBeingChased = false;
    private int idleAnimationTimeout = 1;
    private boolean orderedToSit = false;
    private WungusAvoidEntityGoal avoidEntityGoal;
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
            this.runningAnimationState.startIfStopped(this.tickCount);
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
    protected void updateWalkAnimation(float pPartialTick) {
        float f;
        if (this.getPose() == Pose.STANDING) {
            f = Math.min(pPartialTick * 6F, 1f);
        } else {
            f = 0f;
        }

        this.walkAnimation.update(f, 0.2f);
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
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 100)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.FOLLOW_RANGE, 240)
                .add(Attributes.ARMOR_TOUGHNESS, .05f);
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        WungusVariant baby;
        if(serverLevel.getBiome(this.getOnPos()).is(ModBiomeModifiers.SPAWN_WUNGUS_TAG)) {
            WungusVariant variant = WungusVariant.byId(0);
            baby = variant;
        } else if (serverLevel.getBiome(this.getOnPos()).is(ModBiomeModifiers.SPAWN_WHITE_WUNGUS_TAG)) {
            WungusVariant variant = WungusVariant.byId(1);
            baby = variant;
        } else if (serverLevel.getBiome(this.getOnPos()).is(ModBiomeModifiers.SPAWN_GREEN_WUNGUS_TAG)) {
            WungusVariant variant = WungusVariant.byId(2);
            baby = variant;
        } else if (serverLevel.getBiome(this.getOnPos()).is(ModBiomeModifiers.SPAWN_BLUE_WUNGUS_TAG)) {
            WungusVariant variant = WungusVariant.byId(3);
            baby = variant;
        } else {
            WungusVariant variant = Util.getRandom(WungusVariant.values(), this.random);
            baby = variant;
        }
        WungusEntity wungus =  ModEntities.WUNGUS.get().create(serverLevel);
        Player closest = serverLevel.getNearestPlayer(ageableMob.getX() + 0.5F, ageableMob.getY() + 0.5F, ageableMob.getZ() + 0.5F, 20, EntitySelector.NO_SPECTATORS);
        if (closest != null) {
            wungus.tame(closest);
        }
        wungus.setVariant(baby);
        return wungus;
    }

    @Override
    public boolean isFood(ItemStack pStack) {
        return pStack.is(Items.SEEDS);
    }

    public boolean isTrusting() {
        return this.entityData.get(TRUSTING);
    }

    public void setTrusting(boolean pTrusting) {
        this.entityData.set(TRUSTING, pTrusting);
    }


    public void setTame(boolean pTamed) {
        super.setTame(pTamed);
        if (pTamed) {
            this.orderedToSit = false;
            this.setTrusting(true);
            this.goalSelector.addGoal(2, new SitWhenOrderedToGoal(this));
            this.goalSelector.addGoal(3, new BreedGoal(this, 1.15D));
            this.goalSelector.addGoal(4, new FollowOwnerGoal(this, 1.0D, 10.0F, 2.0F, false));
            this.goalSelector.removeGoal(this.followParentGoal);
            this.goalSelector.removeGoal(this.avoidEntityGoal);
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(50.0D);
            this.setHealth(50.0F);
        } else {
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(50.0D);
        }
    }

    public InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        if (itemstack.is(net.minecraft.world.item.Items.BUCKET) && !this.isBaby()) {
            pPlayer.playSound(SoundEvents.COW_MILK, 1.0F, 1.0F);
            ItemStack itemstack1 = ItemUtils.createFilledResult(itemstack, pPlayer, new ItemStack(ModItems.WUNGUS_MILK.get()));
            pPlayer.setItemInHand(pHand, itemstack1);
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        } else {
            boolean sit = this.isOrderedToSit();
            super.mobInteract(pPlayer, pHand);
            InteractionResult interactionresult = super.mobInteract(pPlayer, pHand);
            if (interactionresult.consumesAction()) {
                return interactionresult;
            }
            if (!pPlayer.isCrouching() && !this.isBaby() && !this.isOrderedToSit()) {
                setRiding(pPlayer);
            } else {
                if (this.isOwnedBy(pPlayer)) {
                    if (sit) {
                        this.setOrderedToSit(false);
                        this.jumping = false;
                        this.navigation.stop();
                        return InteractionResult.SUCCESS;
                    } else {
                        this.setOrderedToSit(true);
                        this.jumping = false;
                        this.navigation.stop();
                        return InteractionResult.SUCCESS;
                    }
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

        while(blockpos$mutableblockpos.getY() > this.level().getMinBuildHeight() && !this.level().getBlockState(blockpos$mutableblockpos).blocksMotion()) {
            blockpos$mutableblockpos.move(Direction.DOWN);
        }

        BlockState blockstate = this.level().getBlockState(blockpos$mutableblockpos);
        boolean flag = blockstate.blocksMotion();
        boolean flag1 = blockstate.getFluidState().is(FluidTags.WATER);
        if (flag && !flag1) {
            net.minecraftforge.event.entity.EntityTeleportEvent.EnderEntity event = net.minecraftforge.event.ForgeEventFactory.onEnderTeleport(this, pX, pY, pZ);
            if (event.isCanceled()) return false;
            Vec3 vec3 = this.position();
            boolean flag2 = this.randomTeleport(event.getTargetX(), event.getTargetY(), event.getTargetZ(), true);
            if (flag2) {
                this.level().gameEvent(GameEvent.TELEPORT, vec3, GameEvent.Context.of(this));
                if (!this.isSilent()) {
                    this.level().playSound((Player)null, this.xo, this.yo, this.zo, SoundEvents.ENDERMAN_TELEPORT, this.getSoundSource(), 1.0F, 1.0F);
                    this.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
                }
            }

            return flag2;
        } else {
            return false;
        }
    }

    @Override
    public void onPlayerJump(int pJumpPower) {
        if (pJumpPower < 0) {
            pJumpPower = 0;
        } else {
            this.allowStandSliding = true;
        }

        if (pJumpPower >= 90) {
            this.playerJumpPendingScale = 1.0F;
        } else {
            this.playerJumpPendingScale = 0.4F + 0.4F * (float)pJumpPower / 90.0F;
        }
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

    @Nullable
    @Override
    public LivingEntity getControllingPassenger() {
        return ((LivingEntity) this.getFirstPassenger());
    }

    @Override
    public void travel(Vec3 pTravelVector) {
        if(this.isVehicle() && getControllingPassenger() instanceof Player) {
            LivingEntity livingentity = this.getControllingPassenger();
            this.setYRot(livingentity.getYRot());
            this.yRotO = this.getYRot();
            this.setXRot(livingentity.getXRot() * 0.5F);
            this.setRot(this.getYRot(), this.getXRot());
            this.yBodyRot = this.getYRot();
            this.yHeadRot = this.yBodyRot;
            float f = livingentity.xxa * 0.5F;
            float f1 = livingentity.zza;

            // Inside this if statement, we are on the client!
            if (this.isControlledByLocalInstance()) {
                if (this.onGround()) {
                    this.setIsJumping(false);
                    if (this.playerJumpPendingScale > 0.0F && !this.isJumping()) {
                        this.executeRidersJump(this.playerJumpPendingScale, pTravelVector);
                    }

                    this.playerJumpPendingScale = 0.0F;
                }

                float newSpeed = (float) this.getAttributeValue(Attributes.MOVEMENT_SPEED);
                // increasing speed by 100% if the spring key is held down (number for testing purposes)
                if(Minecraft.getInstance().options.keySprint.isDown()) {
                    newSpeed *= 2f;
                }

                this.setSpeed(newSpeed);
                super.travel(new Vec3(f, pTravelVector.y, f1));
            }
        } else {
            super.travel(pTravelVector);
        }
    }

    private boolean isJumping() {
        return this.isJumping;
    }

    private void setIsJumping(boolean pJumping) {
        this.isJumping = pJumping;
    }

    protected void executeRidersJump(float pPlayerJumpPendingScale, Vec3 pTravelVector) {
        double d0 = 0.7D * (double)pPlayerJumpPendingScale * (double)this.getBlockJumpFactor();
        double d1 = d0 + (double)this.getJumpBoostPower();
        Vec3 vec3 = this.getDeltaMovement();
        this.setDeltaMovement(vec3.x, d1, vec3.z);
        this.setIsJumping(true);
        this.hasImpulse = true;
        net.minecraftforge.common.ForgeHooks.onLivingJump(this);
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
        public WungusPanicGoal(PathfinderMob pMob, double pSpeedModifier) {
            super(pMob, pSpeedModifier);
            this.wungus = (WungusEntity) pMob;
        }

        public void start() {
            this.wungus.isBeingChased = true;
            super.start();
        }

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

        public boolean canUse() {
            return !this.wungus.isTrusting() && super.canUse();
        }

        public boolean canContinueToUse() {
            return !this.wungus.isTrusting() && super.canContinueToUse();
        }

        public void start() {
            this.wungus.isBeingChased = true;
            super.start();
        }

        public void stop() {
            this.wungus.isBeingChased = false;
            this.wungus.teleport();
            super.stop();
        }
    }

    public boolean isOrderedToSit() {
        return this.entityData.get(SITTING);
    }

    public void setOrderedToSit(boolean pOrderedToSit) {
        this.entityData.set(SITTING, pOrderedToSit);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SITTING, false);
        this.entityData.define(TRUSTING, false);
        this.entityData.define(DATA_ID_TYPE_VARIANT, 0);
    }

    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("Sitting", this.isOrderedToSit());
        compound.putBoolean("Trusting", this.isTrusting());
        compound.putInt("Variant", this.getTypeVariant());
    }

    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setOrderedToSit(compound.getBoolean("Sitting"));
        this.setTrusting(compound.getBoolean("Trusting"));
        this.setTypeVariant(compound.getInt("Variant"));
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
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        if(pLevel.getBiome(this.getOnPos()).is(ModBiomeModifiers.SPAWN_WUNGUS_TAG)) {
            WungusVariant variant = WungusVariant.byId(0);
            this.setVariant(variant);
        } else if (pLevel.getBiome(this.getOnPos()).is(ModBiomeModifiers.SPAWN_WHITE_WUNGUS_TAG)) {
            WungusVariant variant = WungusVariant.byId(1);
            this.setVariant(variant);
        } else if (pLevel.getBiome(this.getOnPos()).is(ModBiomeModifiers.SPAWN_GREEN_WUNGUS_TAG)) {
            WungusVariant variant = WungusVariant.byId(2);
            this.setVariant(variant);
        } else if (pLevel.getBiome(this.getOnPos()).is(ModBiomeModifiers.SPAWN_BLUE_WUNGUS_TAG)) {
            WungusVariant variant = WungusVariant.byId(3);
            this.setVariant(variant);
        } else {
            WungusVariant variant = Util.getRandom(WungusVariant.values(), this.random);
            this.setVariant(variant);
        }
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return ModSounds.WUNGUS_AMBIENT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return ModSounds.WUNGUS_HURT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.WUNGUS_DEATH.get();
    }
}
