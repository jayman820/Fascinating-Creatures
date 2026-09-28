package net.josh.wungus.block.custom;

import com.mojang.serialization.MapCodec;
import net.josh.wungus.entity.ModEntities;
import net.josh.wungus.entity.custom.WungusEntity;
import net.josh.wungus.entity.variant.WungusVariant;
import net.josh.wungus.worldgen.ModBiomeModifiers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Util;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.event.EventHooks;
import org.jspecify.annotations.Nullable;

public class WungusEgg extends Block {
    public static final MapCodec<WungusEgg> CODEC = simpleCodec(WungusEgg::new);
    public static final int MAX_HATCH_LEVEL = 2;
    public static final int MIN_EGGS = 1;
    public static final int MAX_EGGS = 4;
    private static final VoxelShape ONE_EGG_AABB = Block.box(3.0D, 0.0D, 3.0D, 12.0D, 7.0D, 12.0D);
    private static final VoxelShape MULTIPLE_EGGS_AABB = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 7.0D, 15.0D);
    public static final IntegerProperty HATCH = BlockStateProperties.HATCH;
    public static final IntegerProperty EGGS = BlockStateProperties.EGGS;
    protected final RandomSource random = RandomSource.create();

    public WungusEgg(BlockBehaviour.Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any().setValue(HATCH, Integer.valueOf(0)).setValue(EGGS, Integer.valueOf(1)));
    }

    @Override
    protected MapCodec<? extends WungusEgg> codec() {
        return CODEC;
    }

    @Override
    public void stepOn(Level pLevel, BlockPos pPos, BlockState pState, Entity pEntity) {
        if (!pEntity.isSteppingCarefully()) {
            this.destroyEgg(pLevel, pState, pPos, pEntity, 100);
        }

        super.stepOn(pLevel, pPos, pState, pEntity);
    }

    @Override
    public void fallOn(Level pLevel, BlockState pState, BlockPos pPos, Entity pEntity, double pFallDistance) {
        if (!(pEntity instanceof Zombie)) {
            this.destroyEgg(pLevel, pState, pPos, pEntity, 3);
        }

        super.fallOn(pLevel, pState, pPos, pEntity, pFallDistance);
    }

    private void destroyEgg(Level pLevel, BlockState pState, BlockPos pPos, Entity pEntity, int pChance) {
        if (pLevel instanceof ServerLevel serverLevel && this.canDestroyEgg(serverLevel, pEntity)) {
            if (pLevel.getRandom().nextInt(pChance) == 0) {
                this.decreaseEggs(pLevel, pPos, pState);
            }
        }
    }

    private void decreaseEggs(Level pLevel, BlockPos pPos, BlockState pState) {
        pLevel.playSound((Entity)null, pPos, SoundEvents.TURTLE_EGG_BREAK, SoundSource.BLOCKS, 0.7F, 0.9F + pLevel.getRandom().nextFloat() * 0.2F);
        int i = pState.getValue(EGGS);
        if (i <= 1) {
            pLevel.destroyBlock(pPos, false);
        } else {
            pLevel.setBlock(pPos, pState.setValue(EGGS, Integer.valueOf(i - 1)), 2);
            pLevel.gameEvent(GameEvent.BLOCK_DESTROY, pPos, GameEvent.Context.of(pState));
            pLevel.levelEvent(2001, pPos, Block.getId(pState));
        }

    }

    /**
     * Performs a random tick on a block.
     */
    @Override
    protected void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        if (this.shouldUpdateHatchLevel(pLevel) && onPodzol(pLevel, pPos)) {
            int i = pState.getValue(HATCH);
            if (i < 2) {
                pLevel.playSound((Entity)null, pPos, SoundEvents.TURTLE_EGG_CRACK, SoundSource.BLOCKS, 0.7F, 0.9F + pRandom.nextFloat() * 0.2F);
                pLevel.setBlock(pPos, pState.setValue(HATCH, Integer.valueOf(i + 1)), 2);
            } else {
                pLevel.playSound((Entity)null, pPos, SoundEvents.TURTLE_EGG_HATCH, SoundSource.BLOCKS, 0.7F, 0.9F + pRandom.nextFloat() * 0.2F);
                pLevel.removeBlock(pPos, false);

                for(int j = 0; j < pState.getValue(EGGS); ++j) {
                    pLevel.levelEvent(2001, pPos, Block.getId(pState));
                    WungusVariant baby;
                    if(pLevel.getBiome(pPos).is(ModBiomeModifiers.SPAWN_WUNGUS_TAG)) {
                        WungusVariant variant = WungusVariant.byId(0);
                        baby = variant;
                    } else if (pLevel.getBiome(pPos).is(ModBiomeModifiers.SPAWN_WHITE_WUNGUS_TAG)) {
                        WungusVariant variant = WungusVariant.byId(1);
                        baby = variant;
                    } else if (pLevel.getBiome(pPos).is(ModBiomeModifiers.SPAWN_GREEN_WUNGUS_TAG)) {
                        WungusVariant variant = WungusVariant.byId(2);
                        baby = variant;
                    } else if (pLevel.getBiome(pPos).is(ModBiomeModifiers.SPAWN_BLUE_WUNGUS_TAG)) {
                        WungusVariant variant = WungusVariant.byId(3);
                        baby = variant;
                    } else {
                        WungusVariant variant = Util.getRandom(WungusVariant.values(), this.random);
                        baby = variant;
                    }
                    WungusEntity wungus = ModEntities.WUNGUS.get().create(pLevel, EntitySpawnReason.BREEDING);

                    if (wungus != null) {
                        wungus.setVariant(baby);
                        wungus.setAge(-24000);
                        wungus.snapTo((double)pPos.getX() + 0.3D + (double)j * 0.2D, (double)pPos.getY(), (double)pPos.getZ() + 0.3D, 0.0F, 0.0F);
                        pLevel.addFreshEntity(wungus);

                        Player closest = pLevel.getNearestPlayer(pPos.getX() + 0.5F, pPos.getY() + 0.5F, pPos.getZ() + 0.5F, 20, EntitySelector.NO_SPECTATORS);
                        if (closest != null) {
                            wungus.tame(closest);
                        }
                    }
                }
            }
        }

    }

    public static boolean onPodzol(BlockGetter pLevel, BlockPos pPos) {
        return isPodzol(pLevel, pPos.below());
    }

    public static boolean isPodzol(BlockGetter pReader, BlockPos pPos) {
        return pReader.getBlockState(pPos).is(Blocks.PODZOL);
    }

    @Override
    protected void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pIsMoving) {
        if (onPodzol(pLevel, pPos) && !pLevel.isClientSide()) {
            pLevel.levelEvent(2012, pPos, 15);
        }

    }

    private boolean shouldUpdateHatchLevel(Level pLevel) {
        return true;
        /*
        if ((double)f < 0.69D && (double)f > 0.65D) {
            return true;
        } else {
            return pLevel.random.nextInt(500) == 0;
        }
         */
    }

    /**
     * Called after a player has successfully harvested this block. This method will only be called if the player has
     * used the correct tool and drops should be spawned.
     */
    @Override
    public void playerDestroy(Level pLevel, Player pPlayer, BlockPos pPos, BlockState pState, @Nullable BlockEntity pTe, ItemStack pStack) {
        super.playerDestroy(pLevel, pPlayer, pPos, pState, pTe, pStack);
        this.decreaseEggs(pLevel, pPos, pState);
    }

    @Override
    protected boolean canBeReplaced(BlockState pState, BlockPlaceContext pUseContext) {
        return !pUseContext.isSecondaryUseActive() && pUseContext.getItemInHand().is(this.asItem()) && pState.getValue(EGGS) < 4 ? true : super.canBeReplaced(pState, pUseContext);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext pContext) {
        BlockState blockstate = pContext.getLevel().getBlockState(pContext.getClickedPos());
        return blockstate.is(this) ? blockstate.setValue(EGGS, Integer.valueOf(Math.min(4, blockstate.getValue(EGGS) + 1))) : super.getStateForPlacement(pContext);
    }

    @Override
    protected VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return pState.getValue(EGGS) > 1 ? MULTIPLE_EGGS_AABB : ONE_EGG_AABB;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(HATCH, EGGS);
    }

    private boolean canDestroyEgg(ServerLevel pLevel, Entity pEntity) {
        if (!(pEntity instanceof WungusEntity) && !(pEntity instanceof Bat)) {
            if (!(pEntity instanceof LivingEntity)) {
                return false;
            } else {
                return pEntity instanceof Player || EventHooks.canEntityGrief(pLevel, pEntity);
            }
        } else {
            return false;
        }
    }
}
