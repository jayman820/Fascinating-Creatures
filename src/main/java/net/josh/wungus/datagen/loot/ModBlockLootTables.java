package net.josh.wungus.datagen.loot;

import net.josh.wungus.block.ModBlocks;
import net.josh.wungus.block.custom.InfestedAilanthusLogBlock;
import net.josh.wungus.item.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyBlockState;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.Set;

public class ModBlockLootTables extends BlockLootSubProvider {
    public ModBlockLootTables(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        this.dropSelf(ModBlocks.AILANTHUS_LOG.get());
        this.dropSelf(ModBlocks.AILANTHUS_WOOD.get());
        this.dropSelf(ModBlocks.STRIPPED_AILANTHUS_LOG.get());
        this.dropSelf(ModBlocks.STRIPPED_AILANTHUS_WOOD.get());
        // With silk touch the infested log itself, keeping the lanternflies inside. Otherwise a normal log
        // (and 1 or 2 lanternflies jump out, see InfestedAilanthusLogBlock#spawnAfterBreak).
        this.add(ModBlocks.INFESTED_AILANTHUS_LOG.get(), block -> LootTable.lootTable().withPool(LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1.0F))
                .add(LootItem.lootTableItem(block).when(this.hasSilkTouch())
                        .apply(CopyBlockState.copyState(block).copy(InfestedAilanthusLogBlock.LANTERNFLIES))
                        .otherwise(this.applyExplosionCondition(block, LootItem.lootTableItem(ModBlocks.AILANTHUS_LOG.get()))))));
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

        // Like vanilla stone, andaran stone drops andaran cobblestone without silk touch
        this.add(ModBlocks.ANDARAN_STONE.get(), block ->
                createSingleItemTableWithSilkTouch(block, ModBlocks.ANDARAN_COBBLESTONE.get()));
        this.dropSelf(ModBlocks.ANDARAN_STONE_STAIRS.get());
        this.add(ModBlocks.ANDARAN_STONE_SLAB.get(), this::createSlabItemTable);
        this.dropSelf(ModBlocks.ANDARAN_STONE_WALL.get());
        this.dropSelf(ModBlocks.ANDARAN_STONE_BUTTON.get());
        this.dropSelf(ModBlocks.ANDARAN_STONE_PRESSURE_PLATE.get());
        this.dropSelf(ModBlocks.ANDARAN_COBBLESTONE.get());
        this.dropSelf(ModBlocks.ANDARAN_COBBLESTONE_STAIRS.get());
        this.add(ModBlocks.ANDARAN_COBBLESTONE_SLAB.get(), this::createSlabItemTable);
        this.dropSelf(ModBlocks.ANDARAN_COBBLESTONE_WALL.get());
        this.dropSelf(ModBlocks.POLISHED_ANDARAN_STONE.get());
        this.dropSelf(ModBlocks.POLISHED_ANDARAN_STONE_STAIRS.get());
        this.add(ModBlocks.POLISHED_ANDARAN_STONE_SLAB.get(), this::createSlabItemTable);
        this.dropSelf(ModBlocks.POLISHED_ANDARAN_STONE_WALL.get());
        this.dropSelf(ModBlocks.ANDARAN_BRICKS.get());
        this.dropSelf(ModBlocks.ANDARAN_BRICK_STAIRS.get());
        this.add(ModBlocks.ANDARAN_BRICK_SLAB.get(), this::createSlabItemTable);
        this.dropSelf(ModBlocks.ANDARAN_BRICK_WALL.get());
        this.dropSelf(ModBlocks.GLIMMERING_ANDARAN_BRICKS.get());
        this.dropSelf(ModBlocks.GLIMMERING_ANDARAN_BRICK_STAIRS.get());
        this.add(ModBlocks.GLIMMERING_ANDARAN_BRICK_SLAB.get(), this::createSlabItemTable);
        this.dropSelf(ModBlocks.GLIMMERING_ANDARAN_BRICK_WALL.get());

        this.dropWhenSilkTouch(ModBlocks.WUNGUS_EGG.get());

        // Leaves themselves with shears or silk touch, otherwise a chance for a sapling (and sticks), like vanilla leaves
        this.add(ModBlocks.AILANTHUS_LEAVES.get(), block ->
                createLeavesDrops(block, ModBlocks.AILANTHUS_SAPLING.get(), NORMAL_LEAVES_SAPLING_CHANCES));
        this.add(ModBlocks.AILANTHUS_LEAVES_2.get(), block ->
                createLeavesDrops(block, ModBlocks.AILANTHUS_SAPLING.get(), NORMAL_LEAVES_SAPLING_CHANCES));

        // The wall signs share the loot table of the standing signs (see ModBlocks#wallVariant)
        this.add(ModBlocks.AILANTHUS_SIGN.get(), block ->
                createSingleItemTable(ModItems.AILANTHUS_SIGN.get()));
        this.add(ModBlocks.AILANTHUS_HANGING_SIGN.get(), block ->
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
        return ModBlocks.BLOCKS.getEntries().stream().<Block>map(DeferredHolder::get)::iterator;
    }
}
