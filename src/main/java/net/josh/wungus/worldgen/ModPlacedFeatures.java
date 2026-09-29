package net.josh.wungus.worldgen;

import net.josh.wungus.WungusMod;
import net.josh.wungus.block.ModBlocks;
import net.minecraft.core.Holder;
import net.minecraft.core.Vec3i;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.placement.BlockPredicateFilter;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;

import java.util.ArrayList;
import java.util.List;

public class ModPlacedFeatures {
    public static final ResourceKey<PlacedFeature> AILANTHUS_PLACED_KEY = registerKey("ailanthus_placed");

    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

        // Rare: one tree in about every 10th chunk of the allowed biomes (raise the number to make them rarer).
        // Like vanilla trees it is only placed on ground with open sky above (the heightmap stops at leaves, and
        // a sapling can't survive on leaves), and only if no other tree trunk is close by.
        List<PlacementModifier> modifiers = new ArrayList<>(VegetationPlacements.treePlacement(
                RarityFilter.onAverageOnceEvery(10), ModBlocks.AILANTHUS_SAPLING.get()));
        modifiers.add(BlockPredicateFilter.forPredicate(noLogsNearby()));
        register(context, AILANTHUS_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.AILANTHUS_KEY), modifiers);
    }

    // Distance (in blocks) to other trees' trunks. The ailanthus branches and leaves reach about this far.
    private static final int TRUNK_CLEARANCE = 4;

    /** True when there are no logs within TRUNK_CLEARANCE blocks, checked at 2 heights so both low and tall trees count. */
    private static BlockPredicate noLogsNearby() {
        List<BlockPredicate> logChecks = new ArrayList<>();
        for (int y : new int[] {2, 5}) {
            for (int x = -TRUNK_CLEARANCE; x <= TRUNK_CLEARANCE; x++) {
                for (int z = -TRUNK_CLEARANCE; z <= TRUNK_CLEARANCE; z++) {
                    logChecks.add(BlockPredicate.matchesTag(new Vec3i(x, y, z), BlockTags.LOGS));
                }
            }
        }
        return BlockPredicate.not(BlockPredicate.anyOf(logChecks));
    }

    public static ResourceKey<PlacedFeature> registerKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, Identifier.fromNamespaceAndPath(WungusMod.MOD_ID, name));
    }

    private static void register(BootstrapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, Holder<ConfiguredFeature<?, ?>> configuration,
                                 List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
    }
}
