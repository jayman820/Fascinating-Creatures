package net.josh.wungus.block.entity;

import net.josh.wungus.WungusMod;
import net.josh.wungus.block.ModBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, WungusMod.MOD_ID);

    public static final RegistryObject<BlockEntityType<WungusStatueBlockEntity>> WUNGUS_STATUE =
            BLOCK_ENTITIES.register("wungus_statue_block_entity", () ->
                    BlockEntityType.Builder.of(WungusStatueBlockEntity::new,
                            ModBlocks.WUNGUS_STATUE.get()).build(null));

    public static void register (IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
