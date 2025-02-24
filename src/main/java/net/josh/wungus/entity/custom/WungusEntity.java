package net.josh.wungus.entity.custom;

import net.josh.wungus.entity.ModEntities;
import net.josh.wungus.entity.variant.WungusVariant;
import net.josh.wungus.item.ModItems;
import net.josh.wungus.sound.ModSounds;
import net.minecraft.Util;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.Tags.Items;
import net.minecraftforge.event.level.NoteBlockEvent;
import org.jetbrains.annotations.Nullable;

public class WungusEntity extends TamableAnimal {
    public WungusEntity(EntityType<? extends TamableAnimal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
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
        return ModEntities.WUNGUS.get().create(serverLevel);
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
            System.out.println("line 114");
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
            System.out.println(sit);
            super.mobInteract(pPlayer, pHand);
            InteractionResult interactionresult = super.mobInteract(pPlayer, pHand);
            if ((!interactionresult.consumesAction() || this.isBaby()) && this.isOwnedBy(pPlayer)) {
                if (sit) {
                    this.setOrderedToSit(false);
                    this.jumping = false;
                    this.navigation.stop();
                    pPlayer.displayClientMessage(Component.translatable(this.getName() + " is now following"), true);
                    return InteractionResult.SUCCESS;
                } else {
                    this.setOrderedToSit(true);
                    this.jumping = false;
                    this.navigation.stop();
                    pPlayer.displayClientMessage(Component.translatable(this.getName() + " is now sitting"), true);
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

    private void setVariant(WungusVariant variant) {
        this.entityData.set(DATA_ID_TYPE_VARIANT, variant.getId() & 255);
    }

    private void setTypeVariant(int variant) {
        this.entityData.set(DATA_ID_TYPE_VARIANT, variant);
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        WungusVariant variant = Util.getRandom(WungusVariant.values(), this.random);
        this.setVariant(variant);
        //TODO: USE THIS TO GET VARIANT System.out.println(pLevel.getBiome(this.getOnPos()));
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
