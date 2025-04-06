package net.josh.wungus.datagen;

import net.josh.wungus.WungusMod;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.concurrent.CompletableFuture;


@Mod.EventBusSubscriber(modid = WungusMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        //generator.addProvider(event.includeServer(), new ModRecipeProvider(packOutput));
        //generator.addProvider(event.includeServer(), ModLootTableProvider.create(packOutput));

        //generator.addProvider(event.includeClient(), new ModBlockStateProvider(packOutput, existingFileHelper));
        //generator.addProvider(event.includeClient(), new ModItemModelProvider(packOutput, existingFileHelper));

        //ModBlockTagGenerator blockTagGenerator = generator.addProvider(event.includeServer(),
        //        new ModBlockTagGenerator(packOutput, lookupProvider, existingFileHelper));
        //generator.addProvider(event.includeServer(), new ModItemTagGenerator(packOutput, lookupProvider, blockTagGenerator.contentsGetter(), existingFileHelper));

        //generator.addProvider(event.includeServer(), new ModGlobalLootModifiersProvider(packOutput));
        //generator.addProvider(event.includeServer(), new ModPoiTypeTagsProvider(packOutput, lookupProvider, existingFileHelper));

        //generator.addProvider(event.includeServer(), new ModWorldGenProvider(packOutput, lookupProvider));

        generator.addProvider(event.includeServer(), new ModGlobalLootModifierProvider(packOutput));

        //generator.addProvider(event.includeClient(), new ForgeAdvancementProvider(packOutput, lookupProvider, existingFileHelper, List.of(new ModAdvancementProvider())));
    }
}