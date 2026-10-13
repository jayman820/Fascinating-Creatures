package net.josh.wungus.block.custom;

import net.josh.wungus.block.ModBlocks;
import net.josh.wungus.entity.custom.LanternflyEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;

import javax.annotation.Nullable;

/**
 * An ailanthus log with lanternflies living in it, a bit like a beehive. The block state counts the lanternflies inside.
 * - At night and in the rain they stay in, during the day they come out one by one (on random ticks, about once a minute).
 * - Now and then, during the day, a new one hatches from the egg masses, unless there are plenty around already.
 * - Breaking it without silk touch drops a normal log and 1 or 2 lanternflies jump out. With silk touch the log
 *   keeps the lanternflies that are inside (the loot table copies the count), stripping it lets them out.
 * Everything runs on random ticks, so an infested log costs nothing the rest of the time.
 */
public class InfestedAilanthusLogBlock extends ModFlammableRotatedPillarBlock {
    public static final int MAX_LANTERNFLIES = 3;
    public static final IntegerProperty LANTERNFLIES = IntegerProperty.create("lanternflies", 0, MAX_LANTERNFLIES);

    // Chance (1 in this) per random tick during the day that one hatches: about once in half an hour of daylight
    private static final int HATCH_CHANCE = 30;
    // No hatching when this many lanternflies are already around (within NEARBY_RANGE blocks)
    private static final int MAX_NEARBY_LANTERNFLIES = 8;
    private static final double NEARBY_RANGE = 16.0D;

    public InfestedAilanthusLogBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any().setValue(AXIS, Direction.Axis.Y).setValue(LANTERNFLIES, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
        pBuilder.add(LANTERNFLIES);
    }

    @Override
    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        if (LanternflyEntity.isStayInsideTime(pLevel, pPos)) {
            return;
        }

        int inside = pState.getValue(LANTERNFLIES);
        if (inside < MAX_LANTERNFLIES && pRandom.nextInt(HATCH_CHANCE) == 0
                && pLevel.getEntitiesOfClass(LanternflyEntity.class, new AABB(pPos).inflate(NEARBY_RANGE)).size() < MAX_NEARBY_LANTERNFLIES) {
            inside++;
        }

        if (inside > 0) {
            Direction side = findOpenSide(pLevel, pPos, pRandom);
            if (side != null) {
                inside--;
                LanternflyEntity.spawnFromLog(pLevel, pPos, side);
            }
        }

        if (inside != pState.getValue(LANTERNFLIES)) {
            pLevel.setBlockAndUpdate(pPos, pState.setValue(LANTERNFLIES, inside));
        }
    }

    // Like silverfish from infested blocks: silk touch keeps them in, otherwise 1 or 2 jump out
    // (this also runs when the log is blown up)
    @Override
    public void spawnAfterBreak(BlockState pState, ServerLevel pLevel, BlockPos pPos, ItemStack pTool, boolean pDropExperience) {
        super.spawnAfterBreak(pState, pLevel, pPos, pTool, pDropExperience);
        if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, pTool) == 0) {
            int count = 1 + pLevel.getRandom().nextInt(2);
            for (int i = 0; i < count; i++) {
                LanternflyEntity.spawnStartled(pLevel, pPos);
            }
        }
    }

    // Stripping turns it into a normal stripped log, the lanternflies inside jump out
    @Override
    public @Nullable BlockState getToolModifiedState(BlockState state, UseOnContext context, ToolAction toolAction, boolean simulate) {
        if (toolAction == ToolActions.AXE_STRIP && context.getItemInHand().canPerformAction(toolAction)) {
            if (!simulate && context.getLevel() instanceof ServerLevel serverLevel) {
                BlockPos pos = context.getClickedPos();
                Direction side = isOpen(serverLevel, pos.relative(context.getClickedFace()))
                        ? context.getClickedFace() : findOpenSide(serverLevel, pos, serverLevel.getRandom());
                if (side == null) {
                    side = context.getClickedFace();
                }
                for (int i = 0; i < state.getValue(LANTERNFLIES); i++) {
                    LanternflyEntity.spawnStartled(serverLevel, pos.relative(side));
                }
            }
            return ModBlocks.STRIPPED_AILANTHUS_LOG.get().defaultBlockState().setValue(AXIS, state.getValue(AXIS));
        }

        return super.getToolModifiedState(state, context, toolAction, simulate);
    }

    /** An infested log with lanternflies in it, the axis taken from the log it replaces. */
    public static BlockState infestedLog(Direction.Axis axis, int lanternflies) {
        return ModBlocks.INFESTED_AILANTHUS_LOG.get().defaultBlockState()
                .setValue(RotatedPillarBlock.AXIS, axis)
                .setValue(LANTERNFLIES, lanternflies);
    }

    /** True for an infested log with room for another lanternfly. */
    public static boolean hasRoom(BlockState state) {
        return state.getBlock() instanceof InfestedAilanthusLogBlock && state.getValue(LANTERNFLIES) < MAX_LANTERNFLIES;
    }

    /**
     * A lanternfly goes into the log at the given position. A plain ailanthus log becomes infested.
     * Returns false when there is no room or no fitting log there.
     */
    public static boolean addLanternfly(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        if (state.getBlock() instanceof InfestedAilanthusLogBlock) {
            int inside = state.getValue(LANTERNFLIES);
            if (inside >= MAX_LANTERNFLIES) {
                return false;
            }
            level.setBlockAndUpdate(pos, state.setValue(LANTERNFLIES, inside + 1));
            return true;
        }
        if (state.is(ModBlocks.AILANTHUS_LOG.get())) {
            level.setBlockAndUpdate(pos, infestedLog(state.getValue(RotatedPillarBlock.AXIS), 1));
            return true;
        }
        return false;
    }

    /** A side of the log where a lanternfly can get in or out, sides before top and bottom. Null when it's covered. */
    public static @Nullable Direction findOpenSide(BlockGetter level, BlockPos pos, RandomSource random) {
        int start = random.nextInt(4);
        for (int i = 0; i < 4; i++) {
            Direction side = Direction.from2DDataValue((start + i) % 4);
            if (isOpen(level, pos.relative(side))) {
                return side;
            }
        }
        if (isOpen(level, pos.above())) {
            return Direction.UP;
        }
        return isOpen(level, pos.below()) ? Direction.DOWN : null;
    }

    private static boolean isOpen(BlockGetter level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        return state.getCollisionShape(level, pos).isEmpty() && state.getFluidState().isEmpty();
    }
}
