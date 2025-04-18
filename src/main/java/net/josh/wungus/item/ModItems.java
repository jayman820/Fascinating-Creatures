package net.josh.wungus.item;

import net.josh.wungus.WungusMod;
import net.josh.wungus.entity.ModEntities;
import net.josh.wungus.item.custom.*;
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

    public static final RegistryObject<Item> WUNGUS_SHAWARMA = ITEMS.register("wungus_shawarma",
            () -> new WungusShawarma(new Item.Properties().food(ModFoods.WUNGUS_SHAWARMA)));

    public static final RegistryObject<Item> PRATTLING_WUNGUS_1 = ITEMS.register("prattling_wungus_1",
            () -> new PrattlingWungus(new Item.Properties(), 1));

    public static final RegistryObject<Item> PRATTLING_WUNGUS_2 = ITEMS.register("prattling_wungus_2",
            () -> new PrattlingWungus(new Item.Properties(), 2));

    public static final RegistryObject<Item> PRATTLING_WUNGUS_3 = ITEMS.register("prattling_wungus_3",
            () -> new PrattlingWungus(new Item.Properties(), 3));

    public static final RegistryObject<Item> PRATTLING_WUNGUS_4 = ITEMS.register("prattling_wungus_4",
            () -> new PrattlingWungus(new Item.Properties(), 4));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}