package net.josh.wungus.datagen;

import net.josh.wungus.WungusMod;
import net.josh.wungus.block.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagGenerator extends BlockTagsProvider {
    public ModBlockTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, WungusMod.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        this.tag(BlockTags.LOGS_THAT_BURN)
                .add(ModBlocks.AILANTHUS_LOG.get())
                .add(ModBlocks.AILANTHUS_WOOD.get())
                .add(ModBlocks.STRIPPED_AILANTHUS_LOG.get())
                .add(ModBlocks.STRIPPED_AILANTHUS_WOOD.get())
                .add(ModBlocks.INFESTED_AILANTHUS_LOG.get());

        this.tag(BlockTags.PLANKS)
                .add(ModBlocks.AILANTHUS_PLANKS.get());

        this.tag(BlockTags.LEAVES)
                .add(ModBlocks.AILANTHUS_LEAVES.get())
                .add(ModBlocks.AILANTHUS_LEAVES_2.get());

        this.tag(BlockTags.FENCES)
                .add(ModBlocks.AILANTHUS_FENCE.get());
        this.tag(BlockTags.FENCE_GATES)
                .add(ModBlocks.AILANTHUS_FENCE_GATE.get());

        // Like vanilla stone, the andaran stone blocks need a pickaxe to drop anything
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ModBlocks.ANDARAN_STONE.get())
                .add(ModBlocks.ANDARAN_STONE_STAIRS.get())
                .add(ModBlocks.ANDARAN_STONE_SLAB.get())
                .add(ModBlocks.ANDARAN_STONE_WALL.get())
                .add(ModBlocks.ANDARAN_STONE_BUTTON.get())
                .add(ModBlocks.ANDARAN_STONE_PRESSURE_PLATE.get())
                .add(ModBlocks.ANDARAN_COBBLESTONE.get())
                .add(ModBlocks.ANDARAN_COBBLESTONE_STAIRS.get())
                .add(ModBlocks.ANDARAN_COBBLESTONE_SLAB.get())
                .add(ModBlocks.ANDARAN_COBBLESTONE_WALL.get())
                .add(ModBlocks.POLISHED_ANDARAN_STONE.get())
                .add(ModBlocks.POLISHED_ANDARAN_STONE_STAIRS.get())
                .add(ModBlocks.POLISHED_ANDARAN_STONE_SLAB.get())
                .add(ModBlocks.POLISHED_ANDARAN_STONE_WALL.get())
                .add(ModBlocks.ANDARAN_BRICKS.get())
                .add(ModBlocks.ANDARAN_BRICK_STAIRS.get())
                .add(ModBlocks.ANDARAN_BRICK_SLAB.get())
                .add(ModBlocks.ANDARAN_BRICK_WALL.get())
                .add(ModBlocks.GLIMMERING_ANDARAN_BRICKS.get())
                .add(ModBlocks.GLIMMERING_ANDARAN_BRICK_STAIRS.get())
                .add(ModBlocks.GLIMMERING_ANDARAN_BRICK_SLAB.get())
                .add(ModBlocks.GLIMMERING_ANDARAN_BRICK_WALL.get());

        this.tag(BlockTags.STAIRS)
                .add(ModBlocks.ANDARAN_STONE_STAIRS.get())
                .add(ModBlocks.ANDARAN_COBBLESTONE_STAIRS.get())
                .add(ModBlocks.POLISHED_ANDARAN_STONE_STAIRS.get())
                .add(ModBlocks.ANDARAN_BRICK_STAIRS.get())
                .add(ModBlocks.GLIMMERING_ANDARAN_BRICK_STAIRS.get());
        this.tag(BlockTags.SLABS)
                .add(ModBlocks.ANDARAN_STONE_SLAB.get())
                .add(ModBlocks.ANDARAN_COBBLESTONE_SLAB.get())
                .add(ModBlocks.POLISHED_ANDARAN_STONE_SLAB.get())
                .add(ModBlocks.ANDARAN_BRICK_SLAB.get())
                .add(ModBlocks.GLIMMERING_ANDARAN_BRICK_SLAB.get());
        // Walls only connect to each other when they are in this tag
        this.tag(BlockTags.WALLS)
                .add(ModBlocks.ANDARAN_STONE_WALL.get())
                .add(ModBlocks.ANDARAN_COBBLESTONE_WALL.get())
                .add(ModBlocks.POLISHED_ANDARAN_STONE_WALL.get())
                .add(ModBlocks.ANDARAN_BRICK_WALL.get())
                .add(ModBlocks.GLIMMERING_ANDARAN_BRICK_WALL.get());
        // These are part of minecraft:buttons and minecraft:pressure_plates
        this.tag(BlockTags.STONE_BUTTONS)
                .add(ModBlocks.ANDARAN_STONE_BUTTON.get());
        this.tag(BlockTags.STONE_PRESSURE_PLATES)
                .add(ModBlocks.ANDARAN_STONE_PRESSURE_PLATE.get());
    }
}
