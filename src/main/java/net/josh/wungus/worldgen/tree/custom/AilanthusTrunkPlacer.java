package net.josh.wungus.worldgen.tree.custom;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.josh.wungus.worldgen.tree.ModTrunkPlacerTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Util;
import net.minecraft.world.level.WorldGenLevel;
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
 * Tree of heaven style trunk: a straight trunk that forks near the top into 2 or 3 branches.
 * Each branch goes sideways for 1-2 blocks and then rises for 1-2 blocks, which gives the open, vase shaped crown.
 * Leaves are placed at the end of every branch and on top of the trunk (see AilanthusFoliagePlacer).
 */
public class AilanthusTrunkPlacer extends TrunkPlacer {
    public static final MapCodec<AilanthusTrunkPlacer> CODEC = RecordCodecBuilder.mapCodec(instance ->
            trunkPlacerParts(instance).apply(instance, AilanthusTrunkPlacer::new));

    public AilanthusTrunkPlacer(int pBaseHeight, int pHeightRandA, int pHeightRandB) {
        super(pBaseHeight, pHeightRandA, pHeightRandB);
    }

    @Override
    protected TrunkPlacerType<?> type() {
        return ModTrunkPlacerTypes.AILANTHUS_TRUNK_PLACER.get();
    }

    @Override
    public List<FoliagePlacer.FoliageAttachment> placeTrunk(WorldGenLevel pLevel, BiConsumer<BlockPos, BlockState> pBlockSetter, RandomSource pRandom, int pFreeTreeHeight, BlockPos pPos, TreeConfiguration pConfig) {
        List<FoliagePlacer.FoliageAttachment> attachments = new ArrayList<>();
        placeBelowTrunkBlock(pLevel, pBlockSetter, pRandom, pPos.below(), pConfig);

        for (int y = 0; y < pFreeTreeHeight; y++) {
            placeLog(pLevel, pBlockSetter, pRandom, pPos.above(y), pConfig);
        }
        // A smaller clump of leaves on top of the trunk fills the middle of the crown
        attachments.add(new FoliagePlacer.FoliageAttachment(pPos.above(pFreeTreeHeight), -1, false));

        // 2 or 3 branches, each in a different direction
        List<Direction> directions = new ArrayList<>(Direction.Plane.HORIZONTAL.stream().toList());
        Util.shuffle(directions, pRandom);
        int branchCount = 2 + pRandom.nextInt(2);
        for (int i = 0; i < branchCount; i++) {
            // The branches start 1 or 2 blocks below the top of the trunk, so they don't all fork at the same height
            int startY = pFreeTreeHeight - 1 - pRandom.nextInt(2);
            attachments.add(placeBranch(pLevel, pBlockSetter, pRandom, pPos.above(startY), directions.get(i), pConfig));
        }

        return attachments;
    }

    private FoliagePlacer.FoliageAttachment placeBranch(WorldGenLevel level, BiConsumer<BlockPos, BlockState> blockSetter, RandomSource random,
                                                        BlockPos start, Direction direction, TreeConfiguration config) {
        BlockPos.MutableBlockPos pos = start.mutable();

        int sideways = 1 + random.nextInt(2);
        for (int i = 0; i < sideways; i++) {
            pos.move(direction);
            placeLog(level, blockSetter, random, pos, config,
                    state -> state.trySetValue(RotatedPillarBlock.AXIS, direction.getAxis()));
        }

        int upwards = 1 + random.nextInt(2);
        for (int i = 0; i < upwards; i++) {
            pos.move(Direction.UP);
            placeLog(level, blockSetter, random, pos, config);
        }

        return new FoliagePlacer.FoliageAttachment(pos.above(), 0, false);
    }
}
