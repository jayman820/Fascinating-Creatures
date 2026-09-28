package net.josh.wungus.item;

import net.josh.wungus.WungusMod;
import net.josh.wungus.block.ModBlocks;
import net.josh.wungus.entity.ModEntities;
import net.josh.wungus.item.custom.*;
import net.josh.wungus.item.custom.armor.WungusBoots;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.*;
import net.minecraft.world.item.equipment.ArmorType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(WungusMod.MOD_ID);

    public static final DeferredItem<Item> WUNGUS_SPAWN_EGG = ITEMS.registerItem("wungus_spawn_egg",
            properties -> new SpawnEggItem(properties.spawnEgg(ModEntities.WUNGUS.get())));

    public static final DeferredItem<Item> WUNGUS_MILK = ITEMS.registerItem("wungus_milk",
            properties -> new WungusMilk(properties.stacksTo(1)
                    .component(DataComponents.CONSUMABLE, ModFoods.WUNGUS_MILK)
                    .usingConvertsTo(Items.BUCKET)));

    public static final DeferredItem<Item> WUNGUS_HIDE = ITEMS.registerSimpleItem("wungus_hide");

    public static final DeferredItem<Item> WUNGUS_AMBROSIA = ITEMS.registerSimpleItem("wungus_ambrosia");

    // The custom armor models are registered on the client in ModEventBusClientEvents
    public static final DeferredItem<Item> WUNGUS_BOOTS = ITEMS.registerItem("wungus_boots",
            properties -> new WungusBoots(properties.humanoidArmor(ModArmorMaterials.WUNGUS_HIDE, ArmorType.BOOTS)));
    public static final DeferredItem<Item> WUNGUS_MASK = ITEMS.registerItem("wungus_mask",
            properties -> new Item(properties.humanoidArmor(ModArmorMaterials.WUNGUS_HIDE_MASK, ArmorType.HELMET).rarity(Rarity.EPIC)));
    public static final DeferredItem<Item> BBL = ITEMS.registerItem("bbl",
            properties -> new Item(properties.humanoidArmor(ModArmorMaterials.WUNGUS_HIDE_BBL, ArmorType.LEGGINGS).rarity(Rarity.EPIC)));

    public static final DeferredItem<Item> RAW_WUNGUS_FLESH = ITEMS.registerItem("raw_wungus_flesh",
            properties -> new Item(properties.food(ModFoods.RAW_WUNGUS_FLESH, ModFoods.RAW_WUNGUS_FLESH_CONSUMABLE)));
    public static final DeferredItem<Item> COOKED_WUNGUS_FLESH = ITEMS.registerItem("cooked_wungus_flesh",
            properties -> new WungusCookedFlesh(properties.food(ModFoods.COOKED_WUNGUS_FLESH).useCooldown(1.0F)));

    public static final DeferredItem<Item> WUNGUS_SHAWARMA = ITEMS.registerItem("wungus_shawarma",
            properties -> new WungusShawarma(properties.food(ModFoods.WUNGUS_SHAWARMA).useCooldown(1.0F)));

    public static final DeferredItem<Item> SANTONIO_CASHEW = ITEMS.registerItem("santonio_cashew",
            properties -> new SantonioCashew(properties.component(DataComponents.CONSUMABLE, ModFoods.SANTONIO_CASHEW)));

    public static final DeferredItem<Item> HEALTH_STEROID = ITEMS.registerItem("health_steroid",
            properties -> new WungusSteroid(properties.component(DataComponents.CONSUMABLE, ModFoods.STEROIDS), WungusSteroid.Type.HEALTH));
    public static final DeferredItem<Item> SPEED_STEROID = ITEMS.registerItem("speed_steroid",
            properties -> new WungusSteroid(properties.component(DataComponents.CONSUMABLE, ModFoods.STEROIDS), WungusSteroid.Type.SPEED));
    public static final DeferredItem<Item> JUMP_STEROID = ITEMS.registerItem("jump_steroid",
            properties -> new WungusSteroid(properties.component(DataComponents.CONSUMABLE, ModFoods.STEROIDS), WungusSteroid.Type.JUMP));

    public static final DeferredItem<Item> PRATTLING_WUNGUS_1 = ITEMS.registerItem("prattling_wungus_1",
            properties -> new PrattlingWungus(properties, 1));
    public static final DeferredItem<Item> PRATTLING_WUNGUS_2 = ITEMS.registerItem("prattling_wungus_2",
            properties -> new PrattlingWungus(properties, 2));
    public static final DeferredItem<Item> PRATTLING_WUNGUS_3 = ITEMS.registerItem("prattling_wungus_3",
            properties -> new PrattlingWungus(properties, 3));
    public static final DeferredItem<Item> PRATTLING_WUNGUS_4 = ITEMS.registerItem("prattling_wungus_4",
            properties -> new PrattlingWungus(properties, 4));

    public static final DeferredItem<Item> AILANTHUS_SIGN = ITEMS.registerItem("ailanthus_sign",
            properties -> new SignItem(ModBlocks.AILANTHUS_SIGN.get(), ModBlocks.AILANTHUS_WALL_SIGN.get(), properties.stacksTo(16).useBlockDescriptionPrefix()));
    public static final DeferredItem<Item> AILANTHUS_HANGING_SIGN = ITEMS.registerItem("ailanthus_hanging_sign",
            properties -> new HangingSignItem(ModBlocks.AILANTHUS_HANGING_SIGN.get(), ModBlocks.AILANTHUS_WALL_HANGING_SIGN.get(), properties.stacksTo(16).useBlockDescriptionPrefix()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
