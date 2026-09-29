package net.josh.wungus.item;

import net.josh.wungus.WungusMod;
import net.josh.wungus.block.ModBlocks;
import net.josh.wungus.entity.ModEntities;
import net.josh.wungus.item.custom.*;
import net.josh.wungus.item.custom.armor.WungusBoots;
import net.minecraft.world.item.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(WungusMod.MOD_ID);

    public static final DeferredItem<Item> WUNGUS_SPAWN_EGG = ITEMS.register("wungus_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.WUNGUS, 0xa6a079, 0xeee0d9, new Item.Properties()));

    public static final DeferredItem<Item> WUNGUS_MILK = ITEMS.register("wungus_milk",
            () -> new WungusMilk(new Item.Properties().food(ModFoods.WUNGUS_MILK).stacksTo(1)));

    public static final DeferredItem<Item> WUNGUS_HIDE = ITEMS.register("wungus_hide",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> WUNGUS_AMBROSIA = ITEMS.register("wungus_ambrosia",
            () -> new Item(new Item.Properties()));

    // The custom armor models are registered on the client in ModEventBusClientEvents
    public static final DeferredItem<Item> WUNGUS_BOOTS = ITEMS.register("wungus_boots",
            () -> new WungusBoots(ModArmorMaterials.WUNGUS_HIDE, ArmorItem.Type.BOOTS, new Item.Properties()
                    .durability(ArmorItem.Type.BOOTS.getDurability(ModArmorMaterials.DURABILITY_MULTIPLIER))));
    public static final DeferredItem<Item> WUNGUS_MASK = ITEMS.register("wungus_mask",
            () -> new ArmorItem(ModArmorMaterials.WUNGUS_MASK, ArmorItem.Type.HELMET, new Item.Properties().rarity(Rarity.EPIC)
                    .durability(ArmorItem.Type.HELMET.getDurability(ModArmorMaterials.DURABILITY_MULTIPLIER))));
    public static final DeferredItem<Item> BBL = ITEMS.register("bbl",
            () -> new ArmorItem(ModArmorMaterials.BBL, ArmorItem.Type.LEGGINGS, new Item.Properties().rarity(Rarity.EPIC)
                    .durability(ArmorItem.Type.LEGGINGS.getDurability(ModArmorMaterials.DURABILITY_MULTIPLIER))));

    public static final DeferredItem<Item> RAW_WUNGUS_FLESH = ITEMS.register("raw_wungus_flesh",
            () -> new Item(new Item.Properties().food(ModFoods.RAW_WUNGUS_FLESH)));
    public static final DeferredItem<Item> COOKED_WUNGUS_FLESH = ITEMS.register("cooked_wungus_flesh",
            () -> new WungusCookedFlesh(new Item.Properties().food(ModFoods.COOKED_WUNGUS_FLESH)));

    public static final DeferredItem<Item> WUNGUS_SHAWARMA = ITEMS.register("wungus_shawarma",
            () -> new WungusShawarma(new Item.Properties().food(ModFoods.WUNGUS_SHAWARMA)));

    public static final DeferredItem<Item> SANTONIO_CASHEW = ITEMS.register("santonio_cashew",
            () -> new SantonioCashew(new Item.Properties().food(ModFoods.SANTONIO_CASHEW)));

    public static final DeferredItem<Item> HEALTH_STEROID = ITEMS.register("health_steroid",
            () -> new WungusSteroid(new Item.Properties().food(ModFoods.STEROIDS), WungusSteroid.Type.HEALTH));
    public static final DeferredItem<Item> SPEED_STEROID = ITEMS.register("speed_steroid",
            () -> new WungusSteroid(new Item.Properties().food(ModFoods.STEROIDS), WungusSteroid.Type.SPEED));
    public static final DeferredItem<Item> JUMP_STEROID = ITEMS.register("jump_steroid",
            () -> new WungusSteroid(new Item.Properties().food(ModFoods.STEROIDS), WungusSteroid.Type.JUMP));

    public static final DeferredItem<Item> PRATTLING_WUNGUS_1 = ITEMS.register("prattling_wungus_1",
            () -> new PrattlingWungus(new Item.Properties(), 1));
    public static final DeferredItem<Item> PRATTLING_WUNGUS_2 = ITEMS.register("prattling_wungus_2",
            () -> new PrattlingWungus(new Item.Properties(), 2));
    public static final DeferredItem<Item> PRATTLING_WUNGUS_3 = ITEMS.register("prattling_wungus_3",
            () -> new PrattlingWungus(new Item.Properties(), 3));
    public static final DeferredItem<Item> PRATTLING_WUNGUS_4 = ITEMS.register("prattling_wungus_4",
            () -> new PrattlingWungus(new Item.Properties(), 4));

    public static final DeferredItem<Item> AILANTHUS_SIGN = ITEMS.register("ailanthus_sign",
            () -> new SignItem(new Item.Properties().stacksTo(16), ModBlocks.AILANTHUS_SIGN.get(), ModBlocks.AILANTHUS_WALL_SIGN.get()));
    public static final DeferredItem<Item> AILANTHUS_HANGING_SIGN = ITEMS.register("ailanthus_hanging_sign",
            () -> new HangingSignItem(ModBlocks.AILANTHUS_HANGING_SIGN.get(), ModBlocks.AILANTHUS_WALL_HANGING_SIGN.get(), new Item.Properties().stacksTo(16)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
