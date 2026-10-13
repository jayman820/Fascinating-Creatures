package net.josh.wungus.block.custom;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/** Wungus Game 2: a thin board on a wall showing one number (1 to 9), facing the player who placed it. */
public class NumberBoard extends Block {
    public static final MapCodec<NumberBoard> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            propertiesCodec(),
            Codec.INT.fieldOf("number").forGetter(board -> board.num)
    ).apply(instance, NumberBoard::new));
    public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;

    protected static final VoxelShape NORTH_SHAPE = Block.box(1.0D, 1.0D, 15.0D, 15.0D, 15.0D, 16.0D);
    protected static final VoxelShape SOUTH_SHAPE = Block.box(1.0D, 1.0D, 0.0D, 15.0D, 15.0D, 1.0D);
    protected static final VoxelShape EAST_SHAPE = Block.box(0.0D, 1.0D, 1.0D, 1.0D, 15.0D, 15.0D);
    protected static final VoxelShape WEST_SHAPE = Block.box(15.0D, 1.0D, 1.0D, 16.0D, 15.0D, 15.0D);

    private final int num;

    public NumberBoard(Properties pProperties, int num) {
        super(pProperties);
        this.num = num;
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite());
    }

    @Override
    protected BlockState rotate(BlockState pState, Rotation pRotation) {
        return pState.setValue(FACING, pRotation.rotate(pState.getValue(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING);
    }

    @Override
    protected boolean isPathfindable(BlockState pState, PathComputationType pType) {
        return false;
    }

    @Override
    protected VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return switch (pState.getValue(FACING)) {
            case EAST -> EAST_SHAPE;
            case SOUTH -> SOUTH_SHAPE;
            case WEST -> WEST_SHAPE;
            default -> NORTH_SHAPE;
        };
    }
}
