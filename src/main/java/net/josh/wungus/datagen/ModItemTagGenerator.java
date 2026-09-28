package net.josh.wungus.datagen;

import net.josh.wungus.WungusMod;
import net.josh.wungus.block.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.ItemTags;
import net.neoforged.neoforge.common.data.ItemTagsProvider;

import java.util.concurrent.CompletableFuture;

public class ModItemTagGenerator extends ItemTagsProvider {
    public ModItemTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, WungusMod.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        this.tag(ItemTags.LOGS_THAT_BURN)
                .add(ModBlocks.AILANTHUS_LOG.get().asItem())
                .add(ModBlocks.AILANTHUS_WOOD.get().asItem())
                .add(ModBlocks.STRIPPED_AILANTHUS_LOG.get().asItem())
                .add(ModBlocks.STRIPPED_AILANTHUS_WOOD.get().asItem());

        this.tag(ItemTags.PLANKS)
                .add(ModBlocks.AILANTHUS_PLANKS.get().asItem());
    }
}
