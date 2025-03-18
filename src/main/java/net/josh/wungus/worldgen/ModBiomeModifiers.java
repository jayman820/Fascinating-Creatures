package net.josh.wungus.worldgen;

import net.josh.wungus.WungusMod;
import net.josh.wungus.entity.ModEntities;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public class ModBiomeModifiers {
    public static final ResourceKey<BiomeModifier> SPAWN_WUNGUS = registerKey("spawn_wungus");
    public static final TagKey<Biome> SPAWN_WUNGUS_TAG = tag("can_spawn_wungus");

    public static final ResourceKey<BiomeModifier> SPAWN_WUNGUS_WHITE = registerKey("spawn_wungus_white");
    public static final TagKey<Biome> SPAWN_WHITE_WUNGUS_TAG = tag("can_spawn_white_wungus");

    public static final ResourceKey<BiomeModifier> SPAWN_WUNGUS_GREEN = registerKey("spawn_wungus_green");
    public static final TagKey<Biome> SPAWN_GREEN_WUNGUS_TAG = tag("can_spawn_green_wungus");

    public static final ResourceKey<BiomeModifier> SPAWN_WUNGUS_BLUE = registerKey("spawn_wungus_blue");
    public static final TagKey<Biome> SPAWN_BLUE_WUNGUS_TAG = tag("can_spawn_blue_wungus");

    public static void bootstrap(BootstapContext<BiomeModifier> context) {
        var placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        var biomes = context.lookup(Registries.BIOME);

        context.register(SPAWN_WUNGUS, new ForgeBiomeModifiers.AddSpawnsBiomeModifier(
                biomes.getOrThrow(SPAWN_WUNGUS_TAG),
                List.of(new MobSpawnSettings.SpawnerData(ModEntities.WUNGUS.get(), 50000, 1, 10))));

        context.register(SPAWN_WUNGUS_WHITE, new ForgeBiomeModifiers.AddSpawnsBiomeModifier(
                biomes.getOrThrow(SPAWN_WHITE_WUNGUS_TAG),
                List.of(new MobSpawnSettings.SpawnerData(ModEntities.WUNGUS.get(), 50000, 1, 10))));

        context.register(SPAWN_WUNGUS_GREEN, new ForgeBiomeModifiers.AddSpawnsBiomeModifier(
                biomes.getOrThrow(SPAWN_GREEN_WUNGUS_TAG),
                List.of(new MobSpawnSettings.SpawnerData(ModEntities.WUNGUS.get(), 50000, 1, 10))));

        context.register(SPAWN_WUNGUS_BLUE, new ForgeBiomeModifiers.AddSpawnsBiomeModifier(
                biomes.getOrThrow(SPAWN_BLUE_WUNGUS_TAG),
                List.of(new MobSpawnSettings.SpawnerData(ModEntities.WUNGUS.get(), 50000, 1, 10))));

    }

    private static ResourceKey<BiomeModifier> registerKey(String name) {
        return ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, new ResourceLocation(WungusMod.MOD_ID, name));
    }

    private static TagKey<Biome> tag(String name)
    {
        return TagKey.create(Registries.BIOME, new ResourceLocation(WungusMod.MOD_ID, "can_spawn/" + name));
    }
}
