package net.josh.wungus.worldgen;

import net.josh.wungus.WungusMod;
import net.josh.wungus.block.ModBlocks;
import net.josh.wungus.worldgen.tree.custom.AilanthusFoliagePlacer;
import net.josh.wungus.worldgen.tree.custom.AilanthusTrunkPlacer;
import net.josh.wungus.worldgen.tree.custom.InfestedLogDecorator;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.CherryTrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;

import java.util.List;

public class ModConfiguredFeatures {
    // Grown from saplings
    public static final ResourceKey<ConfiguredFeature<?, ?>> AILANTHUS_KEY = registerKey("ailanthus");
    // Generated with the world, 1 in 9 of them has an infested log with lanternflies
    public static final ResourceKey<ConfiguredFeature<?, ?>> NATURAL_AILANTHUS_KEY = registerKey("natural_ailanthus");
    private static final float INFESTED_TREE_CHANCE = 1.0F / 9.0F;

    public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) {
        register(context, AILANTHUS_KEY, Feature.TREE, ailanthusTree().build());
        register(context, NATURAL_AILANTHUS_KEY, Feature.TREE, ailanthusTree()
                .decorators(List.of(new InfestedLogDecorator(INFESTED_TREE_CHANCE))).build());
    }

    private static TreeConfiguration.TreeConfigurationBuilder ailanthusTree() {
        return new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(ModBlocks.AILANTHUS_LOG.get()),
                // Trunk of 5 to 7 blocks
                new AilanthusTrunkPlacer(5, 2, 0),
                // Mostly plain leaves with some flowering (seed cluster) leaves mixed in
                new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder()
                        .add(ModBlocks.AILANTHUS_LEAVES_2.get().defaultBlockState(), 4)
                        .add(ModBlocks.AILANTHUS_LEAVES.get().defaultBlockState(), 1)),
                new AilanthusFoliagePlacer(ConstantInt.of(2), ConstantInt.of(1), 3),
                new TwoLayersFeatureSize(1, 0, 1));
    }

    public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(WungusMod.MOD_ID, name));
    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstapContext<ConfiguredFeature<?, ?>> context,
                                                                                          ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}
