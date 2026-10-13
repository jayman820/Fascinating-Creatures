package net.josh.wungus.datagen;

import net.josh.wungus.WungusMod;
import net.josh.wungus.block.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModItemTagGenerator extends ItemTagsProvider {
    public static final TagKey<Item> AILANTHUS_LOGS =
            TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(WungusMod.MOD_ID, "ailanthus_logs"));

    public ModItemTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                               CompletableFuture<TagLookup<Block>> blockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, WungusMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        this.tag(ItemTags.LOGS_THAT_BURN)
                .add(ModBlocks.AILANTHUS_LOG.get().asItem())
                .add(ModBlocks.AILANTHUS_WOOD.get().asItem())
                .add(ModBlocks.STRIPPED_AILANTHUS_LOG.get().asItem())
                .add(ModBlocks.STRIPPED_AILANTHUS_WOOD.get().asItem())
                .add(ModBlocks.INFESTED_AILANTHUS_LOG.get().asItem());

        this.tag(ItemTags.PLANKS)
                .add(ModBlocks.AILANTHUS_PLANKS.get().asItem());

        // Everything that crafts into ailanthus planks (see data/wungus/recipe/ailanthus_planks.json)
        this.tag(AILANTHUS_LOGS)
                .add(ModBlocks.AILANTHUS_LOG.get().asItem())
                .add(ModBlocks.AILANTHUS_WOOD.get().asItem())
                .add(ModBlocks.STRIPPED_AILANTHUS_LOG.get().asItem())
                .add(ModBlocks.STRIPPED_AILANTHUS_WOOD.get().asItem())
                .add(ModBlocks.INFESTED_AILANTHUS_LOG.get().asItem());
    }
}
