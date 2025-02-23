package net.josh.wungus.item;

import net.josh.wungus.WungusMod;
import net.josh.wungus.entity.ModEntities;
import net.josh.wungus.item.custom.WungusBoots;
import net.josh.wungus.item.custom.WungusCookedFlesh;
import net.josh.wungus.item.custom.WungusMilk;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.Items;
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

    public static final RegistryObject<Item> WUNGUS_MILK = ITEMS.register("wungus_milk",
            () -> new WungusMilk(new Item.Properties().food(ModFoods.WUNGUS_MILK).stacksTo(1)));

    public static final RegistryObject<Item> WUNGUS_HIDE = ITEMS.register("wungus_hide",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> WUNGUS_BOOTS = ITEMS.register("wungus_boots",
            () -> new WungusBoots(ModArmorMaterials.WUNGUS_HIDE, ArmorItem.Type.BOOTS, new Item.Properties()));

    public static final RegistryObject<Item> RAW_WUNGUS_FLESH = ITEMS.register("raw_wungus_flesh",
            () -> new Item(new Item.Properties().food(ModFoods.RAW_WUNGUS_FLESH)));

    public static final RegistryObject<Item> COOKED_WUNGUS_FLESH = ITEMS.register("cooked_wungus_flesh",
            () -> new WungusCookedFlesh(new Item.Properties().food(ModFoods.COOKED_WUNGUS_FLESH)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}