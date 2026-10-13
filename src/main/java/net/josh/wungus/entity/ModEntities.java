package net.josh.wungus.entity;

import net.josh.wungus.WungusMod;
import net.josh.wungus.entity.custom.LanternflyEntity;
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
            // About as wide as the body (like a cow). The head and tail stick out in front and behind, like on a horse:
            // a wider hitbox rests on block edges while the legs are over air, which makes the wungus look like it floats.
            // Babies are half this size automatically.
            ENTITY_TYPES.register("wungus", () -> EntityType.Builder.of(WungusEntity::new, MobCategory.CREATURE).sized(0.9f, 1.65f).build("wungus"));

    public static final RegistryObject<EntityType<LanternflyEntity>> LANTERNFLY =
            // A bit bigger than the tiny model, so it can still be hit. It doesn't spawn naturally, it comes out of
            // infested ailanthus logs.
            ENTITY_TYPES.register("lanternfly", () -> EntityType.Builder.of(LanternflyEntity::new, MobCategory.CREATURE)
                    .sized(0.3f, 0.2f).build("lanternfly"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
