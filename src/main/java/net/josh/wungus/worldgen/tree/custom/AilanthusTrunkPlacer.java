package net.josh.wungus.worldgen.tree.custom;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.josh.wungus.worldgen.tree.ModTrunkPlacerTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * Tree of heaven style trunk:
 * - a tall, mostly bare trunk that sometimes has a small jog (the tree leans a bit),
 * - near the top it splits into 2 to 4 long branches that spread out in different directions (also diagonally)
 *   and rise as they go, stepping sideways and up so every log touches the next one with a full face,
 * - leaf tufts at the end of every branch (see AilanthusFoliagePlacer) and sometimes a small one on top of the trunk,
 *   which gives the open crown made of separate leafy clumps.
 */
public class AilanthusTrunkPlacer extends TrunkPlacer {
    public static final Codec<AilanthusTrunkPlacer> CODEC = RecordCodecBuilder.create(instance ->
            trunkPlacerParts(instance).apply(instance, AilanthusTrunkPlacer::new));

    // The 8 horizontal directions (with diagonals), in circular order
    private static final int[][] DIRECTIONS = {{1, 0}, {1, 1}, {0, 1}, {-1, 1}, {-1, 0}, {-1, -1}, {0, -1}, {1, -1}};

    private static final float LEAN_CHANCE = 0.35F;
    private static final float TOP_TUFT_CHANCE = 0.6F;

    public AilanthusTrunkPlacer(int pBaseHeight, int pHeightRandA, int pHeightRandB) {
        super(pBaseHeight, pHeightRandA, pHeightRandB);
    }

    @Override
    protected TrunkPlacerType<?> type() {
        return ModTrunkPlacerTypes.AILANTHUS_TRUNK_PLACER.get();
    }

    @Override
    public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedReader pLevel, BiConsumer<BlockPos, BlockState> pBlockSetter, RandomSource pRandom, int pFreeTreeHeight, BlockPos pPos, TreeConfiguration pConfig) {
        List<FoliagePlacer.FoliageAttachment> attachments = new ArrayList<>();
        setDirtAt(pLevel, pBlockSetter, pRandom, pPos.below(), pConfig);

        // Trunk, sometimes with a one block jog a few blocks up
        int leanHeight = pRandom.nextFloat() < LEAN_CHANCE ? 2 + pRandom.nextInt(2) : -1;
        Direction leanDirection = Direction.Plane.HORIZONTAL.getRandomDirection(pRandom);
        BlockPos.MutableBlockPos trunk = pPos.mutable();
        for (int y = 0; y < pFreeTreeHeight; y++) {
            placeLog(pLevel, pBlockSetter, pRandom, trunk, pConfig);
            if (y == leanHeight) {
                trunk.move(leanDirection);
                placeLog(pLevel, pBlockSetter, pRandom, trunk, pConfig,
                        state -> withAxis(state, leanDirection.getAxis()));
            }
            trunk.move(Direction.UP);
        }
        // trunk is now the block above the top log
        if (pRandom.nextFloat() < TOP_TUFT_CHANCE) {
            attachments.add(new FoliagePlacer.FoliageAttachment(trunk.immutable(), -1, false));
        }

        // 2 to 4 branches, spread evenly around the trunk with a bit of randomness
        int branchCount = 2 + (pRandom.nextBoolean() ? 1 : 0) + (pRandom.nextFloat() < 0.15F ? 1 : 0);
        int firstDirection = pRandom.nextInt(DIRECTIONS.length);
        List<Integer> used = new ArrayList<>();
        for (int i = 0; i < branchCount; i++) {
            int index = (firstDirection + i * DIRECTIONS.length / branchCount) % DIRECTIONS.length;
            int shifted = (index + 1) % DIRECTIONS.length;
            if (i > 0 && pRandom.nextBoolean() && !used.contains(shifted)) {
                index = shifted;
            }
            used.add(index);

            // Branches start 1 or 2 blocks below the top of the trunk
            BlockPos start = trunk.below(1 + pRandom.nextInt(2));
            attachments.add(placeBranch(pLevel, pBlockSetter, pRandom, start, DIRECTIONS[index], pConfig));
        }

        return attachments;
    }

    private FoliagePlacer.FoliageAttachment placeBranch(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> blockSetter, RandomSource random,
                                                        BlockPos start, int[] direction, TreeConfiguration config) {
        BlockPos.MutableBlockPos pos = start.mutable();

        // Every log touches the previous one with a full face (no gaps like diagonal logs have):
        // a step goes sideways (for diagonal branches along x and then along z), then up
        int length = 2 + random.nextInt(2);
        for (int step = 0; step < length; step++) {
            if (direction[0] != 0) {
                pos.move(direction[0], 0, 0);
                placeLog(level, blockSetter, random, pos, config, state -> withAxis(state, Direction.Axis.X));
            }
            if (direction[1] != 0) {
                pos.move(0, 0, direction[1]);
                placeLog(level, blockSetter, random, pos, config, state -> withAxis(state, Direction.Axis.Z));
            }
            // Branches grow outwards and upwards, the first step is usually flat
            if (step > 0 || random.nextFloat() < 0.3F) {
                pos.move(Direction.UP);
                placeLog(level, blockSetter, random, pos, config);
            }
        }
        // Sometimes the tip turns up a bit more
        if (random.nextBoolean()) {
            pos.move(Direction.UP);
            placeLog(level, blockSetter, random, pos, config);
        }

        return new FoliagePlacer.FoliageAttachment(pos.above(), 0, false);
    }

    private static BlockState withAxis(BlockState state, Direction.Axis axis) {
        return state.hasProperty(RotatedPillarBlock.AXIS) ? state.setValue(RotatedPillarBlock.AXIS, axis) : state;
    }
}
