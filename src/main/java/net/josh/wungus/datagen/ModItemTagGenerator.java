package net.josh.wungus.datagen;

import net.josh.wungus.WungusMod;
import net.josh.wungus.block.ModBlocks;
import net.josh.wungus.item.ModItems;
import net.josh.wungus.util.ModTags;
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
                .add(ModBlocks.STRIPPED_AILANTHUS_WOOD.get().asItem())
                .add(ModBlocks.INFESTED_AILANTHUS_LOG.get().asItem());

        this.tag(ItemTags.PLANKS)
                .add(ModBlocks.AILANTHUS_PLANKS.get().asItem());

        this.tag(ItemTags.STAIRS)
                .add(ModBlocks.ANDARAN_STONE_STAIRS.get().asItem())
                .add(ModBlocks.ANDARAN_COBBLESTONE_STAIRS.get().asItem())
                .add(ModBlocks.POLISHED_ANDARAN_STONE_STAIRS.get().asItem())
                .add(ModBlocks.ANDARAN_BRICK_STAIRS.get().asItem())
                .add(ModBlocks.GLIMMERING_ANDARAN_BRICK_STAIRS.get().asItem());
        this.tag(ItemTags.SLABS)
                .add(ModBlocks.ANDARAN_STONE_SLAB.get().asItem())
                .add(ModBlocks.ANDARAN_COBBLESTONE_SLAB.get().asItem())
                .add(ModBlocks.POLISHED_ANDARAN_STONE_SLAB.get().asItem())
                .add(ModBlocks.ANDARAN_BRICK_SLAB.get().asItem())
                .add(ModBlocks.GLIMMERING_ANDARAN_BRICK_SLAB.get().asItem());
        this.tag(ItemTags.WALLS)
                .add(ModBlocks.ANDARAN_STONE_WALL.get().asItem())
                .add(ModBlocks.ANDARAN_COBBLESTONE_WALL.get().asItem())
                .add(ModBlocks.POLISHED_ANDARAN_STONE_WALL.get().asItem())
                .add(ModBlocks.ANDARAN_BRICK_WALL.get().asItem())
                .add(ModBlocks.GLIMMERING_ANDARAN_BRICK_WALL.get().asItem());
        this.tag(ItemTags.STONE_BUTTONS)
                .add(ModBlocks.ANDARAN_STONE_BUTTON.get().asItem());

        // Like cobbled deepslate and blackstone, andaran cobblestone works for stone tools, furnaces, ...
        this.tag(ItemTags.STONE_TOOL_MATERIALS)
                .add(ModBlocks.ANDARAN_COBBLESTONE.get().asItem());
        this.tag(ItemTags.STONE_CRAFTING_MATERIALS)
                .add(ModBlocks.ANDARAN_COBBLESTONE.get().asItem());

        // Everything that crafts into ailanthus planks (see data/wungus/recipe/ailanthus_planks.json)
        this.tag(ModTags.Items.AILANTHUS_LOGS)
                .add(ModBlocks.AILANTHUS_LOG.get().asItem())
                .add(ModBlocks.AILANTHUS_WOOD.get().asItem())
                .add(ModBlocks.STRIPPED_AILANTHUS_LOG.get().asItem())
                .add(ModBlocks.STRIPPED_AILANTHUS_WOOD.get().asItem())
                .add(ModBlocks.INFESTED_AILANTHUS_LOG.get().asItem());

        this.tag(ModTags.Items.REPAIRS_WUNGUS_HIDE_ARMOR)
                .add(ModItems.WUNGUS_HIDE.get());
        // Add the repair items for the mask and the bbl here (empty tags mean they can't be repaired)
        this.tag(ModTags.Items.REPAIRS_WUNGUS_MASK);
        this.tag(ModTags.Items.REPAIRS_BBL);
    }
}
