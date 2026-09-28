package net.josh.wungus.worldgen.tree.custom;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.josh.wungus.worldgen.tree.ModFoliagePlacerTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;

public class AilanthusFoliagePlacer extends FoliagePlacer {
    public static final MapCodec<AilanthusFoliagePlacer> CODEC = RecordCodecBuilder.mapCodec(ailanthusFoliagePlacerInstance -> foliagePlacerParts(ailanthusFoliagePlacerInstance)
            .and(Codec.intRange(0, 16).fieldOf("height").forGetter(fp -> fp.height)).apply(ailanthusFoliagePlacerInstance, AilanthusFoliagePlacer::new));
    protected final int height;
    public AilanthusFoliagePlacer(IntProvider pRadius, IntProvider pOffset, int height) {
        super(pRadius, pOffset);
        this.height = height;
    }

    @Override
    protected FoliagePlacerType<?> type() {
        return ModFoliagePlacerTypes.AILANTHUS_FOLIAGE_PLACER.get();
    }

    @Override
    protected void createFoliage(WorldGenLevel pLevel, FoliageSetter foliageSetter, RandomSource pRandom, TreeConfiguration pConfig, int pMaxFreeTreeHeight, FoliageAttachment pAttachment, int pFoliageHeight, int pFoliageRadius, int pOffset) {
        // Creating the foliage
        // attachment.pos() is the block directly ABOVE the last placed log

        // tryPlaceLeaf() places one leaf at a given position

        for(int i = 0; i < 5; i++) {
            if (i == 0) {
                this.placeLeavesRowWithHangingLeavesBelow(pLevel, foliageSetter, pRandom, pConfig, pAttachment.pos().above(i), 2, -2, false, 0.5f, 0.7f);
            }
            else if (i <= 2) {
                this.placeLeavesRow(pLevel, foliageSetter, pRandom, pConfig, pAttachment.pos().above(i), 2, -2, pAttachment.doubleTrunk());
            }
            else if (i == 3) {
                this.placeLeavesRow(pLevel, foliageSetter, pRandom, pConfig, pAttachment.pos().above(i), 1, -2, pAttachment.doubleTrunk());

                tryPlaceLeaf(pLevel, foliageSetter, pRandom, pConfig, pAttachment.pos().east(2).north(1).above(i - 2));
                tryPlaceLeaf(pLevel, foliageSetter, pRandom, pConfig, pAttachment.pos().east(2).above(i - 2));
                tryPlaceLeaf(pLevel, foliageSetter, pRandom, pConfig, pAttachment.pos().east(2).south(1).above(i - 2));

                tryPlaceLeaf(pLevel, foliageSetter, pRandom, pConfig, pAttachment.pos().north(2).east(1).above(i - 2));
                tryPlaceLeaf(pLevel, foliageSetter, pRandom, pConfig, pAttachment.pos().north(2).above(i - 2));
                tryPlaceLeaf(pLevel, foliageSetter, pRandom, pConfig, pAttachment.pos().north(2).west(1).above(i - 2));

                tryPlaceLeaf(pLevel, foliageSetter, pRandom, pConfig, pAttachment.pos().west(2).north(1).above(i - 2));
                tryPlaceLeaf(pLevel, foliageSetter, pRandom, pConfig, pAttachment.pos().west(2).above(i - 2));
                tryPlaceLeaf(pLevel, foliageSetter, pRandom, pConfig, pAttachment.pos().west(2).south(1).above(i - 2));

                tryPlaceLeaf(pLevel, foliageSetter, pRandom, pConfig, pAttachment.pos().south(2).east(1).above(i - 2));
                tryPlaceLeaf(pLevel, foliageSetter, pRandom, pConfig, pAttachment.pos().south(2).above(i - 2));
                tryPlaceLeaf(pLevel, foliageSetter, pRandom, pConfig, pAttachment.pos().south(2).west(1).above(i - 2));
            }
            else if (i == 4) {
                tryPlaceLeaf(pLevel, foliageSetter, pRandom, pConfig, pAttachment.pos().above(i - 2));
                tryPlaceLeaf(pLevel, foliageSetter, pRandom, pConfig, pAttachment.pos().east(1).above(i - 2));
                tryPlaceLeaf(pLevel, foliageSetter, pRandom, pConfig, pAttachment.pos().north(1).above(i - 2));
                tryPlaceLeaf(pLevel, foliageSetter, pRandom, pConfig, pAttachment.pos().west(1).above(i - 2));
                tryPlaceLeaf(pLevel, foliageSetter, pRandom, pConfig, pAttachment.pos().south(1).above(i - 2));
            }

        }
    }

    @Override
    public int foliageHeight(RandomSource pRandom, int pHeight, TreeConfiguration pConfig) {
        return 0;
    }

    @Override
    protected boolean shouldSkipLocation(RandomSource pRandom, int pLocalX, int pLocalY, int pLocalZ, int pRange, boolean pLarge) {
        if (pLocalY == -2 && (pLocalX == pRange || pLocalZ == pRange) && pRandom.nextFloat() < 0.4) {
            return true;
        } else {
            boolean flag = pLocalX == pRange && pLocalZ == pRange;
            boolean flag1 = pRange > 2;
            if (flag1) {
                return flag || pLocalX + pLocalZ > pRange * 2 - 2 && pRandom.nextFloat() < 0.1;
            } else {
                return flag && pRandom.nextFloat() < 0.1;
            }
        }
    }
}
