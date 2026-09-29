package net.josh.wungus.item;

import net.josh.wungus.WungusMod;
import net.minecraft.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Armor materials are registry entries in 1.21. Each material has one texture layer:
 * assets/wungus/textures/models/armor/{texture}_layer_1.png (layer_2 for leggings, which use the inner model).
 * Only the boots are made of wungus hide. The mask and the bbl are separate pieces (found as loot) with their own
 * material. They can't be repaired for now: put the repair item in their Ingredient.
 */
public class ModArmorMaterials {
    public static final DeferredRegister<ArmorMaterial> ARMOR_MATERIALS =
            DeferredRegister.create(Registries.ARMOR_MATERIAL, WungusMod.MOD_ID);

    // Durability multiplier, the durability of a piece is its type's base durability times this
    public static final int DURABILITY_MULTIPLIER = 5;

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> WUNGUS_HIDE = register("wungus_hide", "wungus_boots",
            () -> Ingredient.of(ModItems.WUNGUS_HIDE.get()));

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> WUNGUS_MASK = register("wungus_mask", "wungus_mask",
            () -> Ingredient.EMPTY);

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> BBL = register("bbl", "bbl",
            () -> Ingredient.EMPTY);

    private static DeferredHolder<ArmorMaterial, ArmorMaterial> register(String name, String texture, Supplier<Ingredient> repairIngredient) {
        return ARMOR_MATERIALS.register(name, () -> new ArmorMaterial(defense(4, 2, 3, 1), 15,
                SoundEvents.ARMOR_EQUIP_LEATHER, repairIngredient,
                List.of(new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(WungusMod.MOD_ID, texture))),
                0.0F, 0.0F));
    }

    private static Map<ArmorItem.Type, Integer> defense(int boots, int leggings, int chestplate, int helmet) {
        return Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
            map.put(ArmorItem.Type.BOOTS, boots);
            map.put(ArmorItem.Type.LEGGINGS, leggings);
            map.put(ArmorItem.Type.CHESTPLATE, chestplate);
            map.put(ArmorItem.Type.HELMET, helmet);
            map.put(ArmorItem.Type.BODY, chestplate);
        });
    }

    public static void register(IEventBus eventBus) {
        ARMOR_MATERIALS.register(eventBus);
    }
}
