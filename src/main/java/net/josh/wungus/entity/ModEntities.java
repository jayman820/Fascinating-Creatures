package net.josh.wungus.entity;

import net.josh.wungus.WungusMod;
import net.josh.wungus.entity.custom.WungusEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(Registries.ENTITY_TYPE, WungusMod.MOD_ID);

    public static final ResourceKey<EntityType<?>> WUNGUS_KEY = ResourceKey.create(Registries.ENTITY_TYPE,
            Identifier.fromNamespaceAndPath(WungusMod.MOD_ID, "wungus"));

    public static final DeferredHolder<EntityType<?>, EntityType<WungusEntity>> WUNGUS =
            ENTITY_TYPES.register("wungus", () -> EntityType.Builder.of(WungusEntity::new, MobCategory.CREATURE)
                    .sized(1.4f, 1.65f).build(WUNGUS_KEY));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
