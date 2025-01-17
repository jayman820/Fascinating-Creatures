package net.josh.wungus.entity;

import net.josh.wungus.WungusMod;
import net.josh.wungus.entity.custom.WungusEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, WungusMod.MOD_ID);

    public static final RegistryObject<EntityType<WungusEntity>> WUNGUS =
            ENTITY_TYPES.register("wungus", () -> EntityType.Builder.of(WungusEntity::new, MobCategory.CREATURE).sized(2.5f, 2.5f).build("wungus"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
