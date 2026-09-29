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

/**
 * Places a rounded, slightly flat clump of leaves around every branch end of the AilanthusTrunkPlacer.
 * The clumps overlap into one broad, rounded crown, like the umbrella shaped crown of a tree of heaven.
 * Layers go from {@code offset} (top, one smaller) down to {@code offset - height + 1}.
 */
public class AilanthusFoliagePlacer extends FoliagePlacer {
    public static final MapCodec<AilanthusFoliagePlacer> CODEC = RecordCodecBuilder.mapCodec(instance -> foliagePlacerParts(instance)
            .and(Codec.intRange(1, 16).fieldOf("height").forGetter(fp -> fp.height)).apply(instance, AilanthusFoliagePlacer::new));
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
        int radius = pFoliageRadius + pAttachment.radiusOffset();
        for (int y = pOffset; y > pOffset - pFoliageHeight; y--) {
            // The top layer is one smaller, which rounds off the clump
            int layerRadius = y == pOffset ? radius - 1 : radius;
            if (layerRadius >= 0) {
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
        // Always cut the corners, and randomly thin out the rest of the edge so the crown doesn't look boxy
        boolean corner = pLocalX == pRange && pLocalZ == pRange;
        boolean edge = pLocalX == pRange || pLocalZ == pRange;
        return corner || (edge && pRandom.nextFloat() < 0.2F);
    }
}
