package net.josh.wungus.datagen;

import net.josh.wungus.WungusMod;
import net.josh.wungus.datagen.loot.ModBlockLootTables;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.advancements.AdvancementProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = WungusMod.MOD_ID)
public class DataGenerators {
    // The "data" run uses the client data generator, so this generates both the assets and the data
    @SubscribeEvent
    public static void gatherData(GatherDataEvent.Client event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        generator.addProvider(true, new ModBlockTagGenerator(packOutput, lookupProvider));
        generator.addProvider(true, new ModItemTagGenerator(packOutput, lookupProvider));

        generator.addProvider(true, new LootTableProvider(packOutput, Set.of(), List.of(
                new LootTableProvider.SubProviderEntry(ModBlockLootTables::new, LootContextParamSets.BLOCK)
        ), lookupProvider));

        generator.addProvider(true, new ModModelProvider(packOutput));

        generator.addProvider(true, new ModWorldGenProvider(packOutput, lookupProvider));

        generator.addProvider(true, new ModGlobalLootModifierProvider(packOutput, lookupProvider));

        generator.addProvider(true, new AdvancementProvider(packOutput, lookupProvider, List.of(new ModAdvancementProvider())));

        generator.addProvider(true, new ModPoiTypeTagsProvider(packOutput, lookupProvider));
    }
}
