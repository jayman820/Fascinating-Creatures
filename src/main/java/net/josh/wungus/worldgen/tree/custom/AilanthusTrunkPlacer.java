package net.josh.wungus.worldgen.tree.custom;

import com.google.common.collect.Lists;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.josh.wungus.worldgen.tree.ModTrunkPlacerTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;

import java.util.List;
import java.util.function.BiConsumer;

public class AilanthusTrunkPlacer extends TrunkPlacer {
    public static final MapCodec<AilanthusTrunkPlacer> CODEC = RecordCodecBuilder.mapCodec(ailanthusTrunkPlacerInstance ->
            trunkPlacerParts(ailanthusTrunkPlacerInstance).apply(ailanthusTrunkPlacerInstance, AilanthusTrunkPlacer::new));

    public AilanthusTrunkPlacer(int pBaseHeight, int pHeightRandA, int pHeightRandB) {
        super(pBaseHeight, pHeightRandA, pHeightRandB);
    }

    @Override
    protected TrunkPlacerType<?> type() {
        return ModTrunkPlacerTypes.AILANTHUS_TRUNK_PLACER.get();
    }

    @Override
    public List<FoliagePlacer.FoliageAttachment> placeTrunk(WorldGenLevel pLevel, BiConsumer<BlockPos, BlockState> pBlockSetter, RandomSource pRandom, int pFreeTreeHeight, BlockPos pPos, TreeConfiguration pConfig) {
        List<FoliagePlacer.FoliageAttachment> list = Lists.newArrayList();

        // BLOCK PLACING LOGIC
        placeBelowTrunkBlock(pLevel, pBlockSetter, pRandom, pPos.below(), pConfig);
        int height = pFreeTreeHeight + pRandom.nextInt(heightRandA, heightRandA + 3) + pRandom.nextInt(heightRandB - 1, heightRandB + 1);
        list.add(new FoliagePlacer.FoliageAttachment(pPos.above(height), 0, false));

        for(int i = 0; i < height; i++) {
            placeLog(pLevel, pBlockSetter, pRandom, pPos.above(i), pConfig);
            if(i == height - 1) {
                continue;
            }
            if(i == height - 5) {
                for(int x = 0; x < 3; x++) {
                    placeLog(pLevel, pBlockSetter, pRandom, pPos.above(i).relative(Direction.NORTH, x), pConfig);
                    placeLog(pLevel, pBlockSetter, pRandom, pPos.above(i).relative(Direction.EAST, x), pConfig);
                    placeLog(pLevel, pBlockSetter, pRandom, pPos.above(i).relative(Direction.SOUTH, x), pConfig);
                    placeLog(pLevel, pBlockSetter, pRandom, pPos.above(i).relative(Direction.WEST, x), pConfig);
                }
            }
            if(i >= height - 4) {
                placeLog(pLevel, pBlockSetter, pRandom, pPos.above(i).relative(Direction.NORTH, 3), pConfig);
                list.add(new FoliagePlacer.FoliageAttachment(pPos.above(i).relative(Direction.NORTH, 3), 0, false));
                placeLog(pLevel, pBlockSetter, pRandom, pPos.above(i).relative(Direction.EAST, 3), pConfig);
                list.add(new FoliagePlacer.FoliageAttachment(pPos.above(i).relative(Direction.EAST, 3), 0, false));
                placeLog(pLevel, pBlockSetter, pRandom, pPos.above(i).relative(Direction.SOUTH, 3), pConfig);
                list.add(new FoliagePlacer.FoliageAttachment(pPos.above(i).relative(Direction.SOUTH, 3), 0, false));
                placeLog(pLevel, pBlockSetter, pRandom, pPos.above(i).relative(Direction.WEST, 3), pConfig);
                list.add(new FoliagePlacer.FoliageAttachment(pPos.above(i).relative(Direction.WEST, 3), 0, false));
            }
        }

        return list;
    }
}
