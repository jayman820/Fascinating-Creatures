package net.josh.wungus.block.entity;

import net.josh.wungus.WungusMod;
import net.josh.wungus.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, WungusMod.MOD_ID);

    // Every block creating this block entity has to be listed, otherwise placing it crashes
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<WungusStatueBlockEntity>> WUNGUS_STATUE =
            BLOCK_ENTITIES.register("wungus_statue_block_entity", () ->
                    new BlockEntityType<>(WungusStatueBlockEntity::new,
                            ModBlocks.WUNGUS_STATUE.get(), ModBlocks.STONE_STATUE.get(), ModBlocks.GOLD_STATUE.get(),
                            ModBlocks.GLOWSTONE_STATUE.get(), ModBlocks.WUNGUS_HEDGE.get()));

    // The ailanthus signs use the vanilla sign block entities, see ModEventBusEvents#addBlocksToBlockEntities

    public static void register (IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
