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
                    BlockEntityType.Builder.of(WungusStatueBlockEntity::new,
                            ModBlocks.WUNGUS_STATUE.get(), ModBlocks.STONE_STATUE.get(), ModBlocks.GOLD_STATUE.get(),
                            ModBlocks.GLOWSTONE_STATUE.get(), ModBlocks.WUNGUS_HEDGE.get()).build(null));

    // The ailanthus signs have their own block entity types, rendered by the vanilla sign renderers
    // (see ModEventBusClientEvents#registerRenderers)
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ModSignBlockEntity>> MOD_SIGN =
            BLOCK_ENTITIES.register("mod_sign", () ->
                    BlockEntityType.Builder.of(ModSignBlockEntity::new,
                            ModBlocks.AILANTHUS_SIGN.get(), ModBlocks.AILANTHUS_WALL_SIGN.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ModHangingSignBlockEntity>> MOD_HANGING_SIGN =
            BLOCK_ENTITIES.register("mod_hanging_sign", () ->
                    BlockEntityType.Builder.of(ModHangingSignBlockEntity::new,
                            ModBlocks.AILANTHUS_HANGING_SIGN.get(), ModBlocks.AILANTHUS_WALL_HANGING_SIGN.get()).build(null));

    public static void register (IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
