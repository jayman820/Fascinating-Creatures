package net.josh.wungus.worldgen.tree.custom;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.josh.wungus.worldgen.tree.ModFoliagePlacerTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;

/**
 * Places a leafy tuft at every branch end of the AilanthusTrunkPlacer: a small dome that is a bit ragged and open,
 * with leaves hanging down from its edge like the long drooping leaves (and seed clusters) of a tree of heaven.
 * Layers go from {@code offset} (top, one smaller) down to {@code offset - height + 1}.
 */
public class AilanthusFoliagePlacer extends FoliagePlacer {
    public static final MapCodec<AilanthusFoliagePlacer> CODEC = RecordCodecBuilder.mapCodec(instance -> foliagePlacerParts(instance)
            .and(Codec.intRange(1, 16).fieldOf("height").forGetter(fp -> fp.height)).apply(instance, AilanthusFoliagePlacer::new));

    private static final float EDGE_HOLE_CHANCE = 0.25F;
    private static final float INNER_HOLE_CHANCE = 0.08F;
    private static final float HANGING_LEAVES_CHANCE = 0.3F;
    private static final float HANGING_LEAVES_EXTENSION_CHANCE = 0.4F;

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
    protected void createFoliage(LevelSimulatedReader pLevel, FoliageSetter foliageSetter, RandomSource pRandom, TreeConfiguration pConfig, int pMaxFreeTreeHeight, FoliageAttachment pAttachment, int pFoliageHeight, int pFoliageRadius, int pOffset) {
        int radius = pFoliageRadius + pAttachment.radiusOffset();
        int bottom = pOffset - pFoliageHeight + 1;
        for (int y = pOffset; y >= bottom; y--) {
            // The top layer is one smaller, which rounds off the tuft
            int layerRadius = y == pOffset ? radius - 1 : radius;
            if (layerRadius < 0) {
                continue;
            }
            if (y == bottom) {
                this.placeLeavesRowWithHangingLeavesBelow(pLevel, foliageSetter, pRandom, pConfig, pAttachment.pos(), layerRadius, y,
                        pAttachment.doubleTrunk(), HANGING_LEAVES_CHANCE, HANGING_LEAVES_EXTENSION_CHANCE);
            } else {
                this.placeLeavesRow(pLevel, foliageSetter, pRandom, pConfig, pAttachment.pos(), layerRadius, y, pAttachment.doubleTrunk());
            }
        }
    }

    @Override
    public int foliageHeight(RandomSource pRandom, int pHeight, TreeConfiguration pConfig) {
        return this.height;
    }

    @Override
    protected boolean shouldSkipLocation(RandomSource pRandom, int pLocalX, int pLocalY, int pLocalZ, int pRange, boolean pLarge) {
        if (pRange == 0) {
            return false;
        }
        boolean corner = pLocalX == pRange && pLocalZ == pRange;
        boolean edge = pLocalX == pRange || pLocalZ == pRange;
        if (corner) {
            return true;
        }
        if (edge) {
            return pRandom.nextFloat() < EDGE_HOLE_CHANCE;
        }
        // A few holes inside the lower layers keep the tufts from looking like solid blocks
        return pLocalY <= 0 && pRandom.nextFloat() < INNER_HOLE_CHANCE;
    }
}
