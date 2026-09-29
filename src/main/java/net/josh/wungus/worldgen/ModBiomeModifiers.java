package net.josh.wungus.worldgen;

import net.josh.wungus.WungusMod;
import net.josh.wungus.entity.ModEntities;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.util.random.Weighted;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class ModBiomeModifiers {
    public static final ResourceKey<BiomeModifier> SPAWN_WUNGUS = registerKey("spawn_wungus");
    public static final TagKey<Biome> SPAWN_WUNGUS_TAG = tag("can_spawn_wungus");

    public static final ResourceKey<BiomeModifier> SPAWN_WUNGUS_WHITE = registerKey("spawn_wungus_white");
    public static final TagKey<Biome> SPAWN_WHITE_WUNGUS_TAG = tag("can_spawn_white_wungus");

    public static final ResourceKey<BiomeModifier> SPAWN_WUNGUS_GREEN = registerKey("spawn_wungus_green");
    public static final TagKey<Biome> SPAWN_GREEN_WUNGUS_TAG = tag("can_spawn_green_wungus");

    public static final ResourceKey<BiomeModifier> SPAWN_WUNGUS_BLUE = registerKey("spawn_wungus_blue");
    public static final TagKey<Biome> SPAWN_BLUE_WUNGUS_TAG = tag("can_spawn_blue_wungus");

    public static final ResourceKey<BiomeModifier> ADD_TREE_AILANTHUS = registerKey("add_tree_ailanthus");
    // Forest and birch forests, see data/wungus/tags/worldgen/biome/has_ailanthus_trees.json
    public static final TagKey<Biome> HAS_AILANTHUS_TREES_TAG =
            TagKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(WungusMod.MOD_ID, "has_ailanthus_trees"));

    public static void bootstrap(BootstrapContext<BiomeModifier> context) {
        var placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        var biomes = context.lookup(Registries.BIOME);

        context.register(SPAWN_WUNGUS, BiomeModifiers.AddSpawnsBiomeModifier.singleSpawn(
                biomes.getOrThrow(SPAWN_WUNGUS_TAG), wungusSpawn()));

        context.register(SPAWN_WUNGUS_WHITE, BiomeModifiers.AddSpawnsBiomeModifier.singleSpawn(
                biomes.getOrThrow(SPAWN_WHITE_WUNGUS_TAG), wungusSpawn()));

        context.register(SPAWN_WUNGUS_GREEN, BiomeModifiers.AddSpawnsBiomeModifier.singleSpawn(
                biomes.getOrThrow(SPAWN_GREEN_WUNGUS_TAG), wungusSpawn()));

        context.register(SPAWN_WUNGUS_BLUE, BiomeModifiers.AddSpawnsBiomeModifier.singleSpawn(
                biomes.getOrThrow(SPAWN_BLUE_WUNGUS_TAG), wungusSpawn()));

        context.register(ADD_TREE_AILANTHUS, new BiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(HAS_AILANTHUS_TREES_TAG),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.AILANTHUS_PLACED_KEY)),
                GenerationStep.Decoration.VEGETAL_DECORATION));

    }

    private static Weighted<MobSpawnSettings.SpawnerData> wungusSpawn() {
        return new Weighted<>(new MobSpawnSettings.SpawnerData(ModEntities.WUNGUS.get(), 1, 2), 1);
    }

    private static ResourceKey<BiomeModifier> registerKey(String name) {
        return ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, Identifier.fromNamespaceAndPath(WungusMod.MOD_ID, name));
    }

    private static TagKey<Biome> tag(String name)
    {
        return TagKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(WungusMod.MOD_ID, "can_spawn/" + name));
    }
}
