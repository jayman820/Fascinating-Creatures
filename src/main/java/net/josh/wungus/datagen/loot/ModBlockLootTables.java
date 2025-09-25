package net.josh.wungus.datagen.loot;

import net.josh.wungus.item.ModItems;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.josh.wungus.block.ModBlocks;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

import java.util.Set;

public class ModBlockLootTables extends BlockLootSubProvider {
    public ModBlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        this.dropSelf(ModBlocks.AILANTHUS_LOG.get());
        this.dropSelf(ModBlocks.AILANTHUS_WOOD.get());
        this.dropSelf(ModBlocks.STRIPPED_AILANTHUS_LOG.get());
        this.dropSelf(ModBlocks.STRIPPED_AILANTHUS_WOOD.get());
        this.dropSelf(ModBlocks.AILANTHUS_PLANKS.get());
        this.dropSelf(ModBlocks.AILANTHUS_SAPLING.get());
        this.dropSelf(ModBlocks.BBL_TABLE.get());

        this.dropSelf(ModBlocks.WUNGUS_STATUE.get());
        this.dropSelf(ModBlocks.STONE_STATUE.get());
        this.dropSelf(ModBlocks.GOLD_STATUE.get());
        this.dropSelf(ModBlocks.GLOWSTONE_STATUE.get());
        this.dropSelf(ModBlocks.WUNGUS_HEDGE.get());
        this.dropSelf(ModBlocks.ANDARAN_DIRT.get());
        this.dropWhenSilkTouch(ModBlocks.ANDARAN_GRASS_BLOCK.get());

        this.dropWhenSilkTouch(ModBlocks.WUNGUS_EGG.get());

        this.add(ModBlocks.AILANTHUS_LEAVES.get(), block ->
                createLeavesDrops(block, ModBlocks.AILANTHUS_LEAVES.get(), NORMAL_LEAVES_SAPLING_CHANCES)); //todo: change to sapling
        this.add(ModBlocks.AILANTHUS_LEAVES_2.get(), block ->
                createLeavesDrops(block, ModBlocks.AILANTHUS_LEAVES_2.get(), NORMAL_LEAVES_SAPLING_CHANCES));

        this.add(ModBlocks.AILANTHUS_SIGN.get(), block ->
                createSingleItemTable(ModItems.AILANTHUS_SIGN.get()));
        this.add(ModBlocks.AILANTHUS_WALL_SIGN.get(), block ->
                createSingleItemTable(ModItems.AILANTHUS_SIGN.get()));
        this.add(ModBlocks.AILANTHUS_HANGING_SIGN.get(), block ->
                createSingleItemTable(ModItems.AILANTHUS_HANGING_SIGN.get()));
        this.add(ModBlocks.AILANTHUS_WALL_HANGING_SIGN.get(), block ->
                createSingleItemTable(ModItems.AILANTHUS_HANGING_SIGN.get()));

        this.dropSelf(ModBlocks.AILANTHUS_STAIRS.get());
        this.dropSelf(ModBlocks.AILANTHUS_BUTTON.get());
        this.dropSelf(ModBlocks.AILANTHUS_PRESSURE_PLATE.get());
        this.dropSelf(ModBlocks.AILANTHUS_TRAPDOOR.get());
        this.dropSelf(ModBlocks.AILANTHUS_FENCE.get());
        this.dropSelf(ModBlocks.AILANTHUS_FENCE_GATE.get());

        this.add(ModBlocks.AILANTHUS_SLAB.get(),
                block -> createSlabItemTable(ModBlocks.AILANTHUS_SLAB.get()));
        this.add(ModBlocks.AILANTHUS_DOOR.get(),
                block -> createDoorTable(ModBlocks.AILANTHUS_DOOR.get()));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}
