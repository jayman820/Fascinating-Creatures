package net.josh.wungus.item;

import net.josh.wungus.WungusMod;
import net.josh.wungus.entity.ModEntities;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, WungusMod.MOD_ID);

    public static final RegistryObject<Item> WUNGUS_SPAWN_EGG = ITEMS.register("wungus_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.WUNGUS, 0xa6a079, 0xeee0d9, new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}