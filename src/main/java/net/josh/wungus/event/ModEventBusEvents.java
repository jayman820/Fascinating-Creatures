package net.josh.wungus.event;

import net.josh.wungus.WungusMod;
import net.josh.wungus.block.ModBlocks;
import net.josh.wungus.entity.ModEntities;
import net.josh.wungus.entity.custom.WungusEntity;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BlockEntityTypeAddBlocksEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;

@EventBusSubscriber(modid = WungusMod.MOD_ID)
public class ModEventBusEvents {
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.WUNGUS.get(), WungusEntity.createAttributes().build());
    }

    @SubscribeEvent
    public static void registerSpawnPlacement(RegisterSpawnPlacementsEvent event) {
        event.register(ModEntities.WUNGUS.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Animal::checkAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
    }

    // The ailanthus signs reuse the vanilla sign block entities (and their renderers)
    @SubscribeEvent
    public static void addBlocksToBlockEntities(BlockEntityTypeAddBlocksEvent event) {
        event.modify(BlockEntityType.SIGN, ModBlocks.AILANTHUS_SIGN.get(), ModBlocks.AILANTHUS_WALL_SIGN.get());
        event.modify(BlockEntityType.HANGING_SIGN, ModBlocks.AILANTHUS_HANGING_SIGN.get(), ModBlocks.AILANTHUS_WALL_HANGING_SIGN.get());
    }
}
