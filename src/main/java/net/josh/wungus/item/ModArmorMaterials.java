package net.josh.wungus.item;

import net.josh.wungus.WungusMod;
import net.josh.wungus.util.ModTags;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;

import java.util.EnumMap;
import java.util.Map;

/**
 * Armor materials are plain records now, each one points at its own equipment asset (assets/wungus/equipment/*.json).
 * Only the boots are made of wungus hide. The mask and the bbl are separate pieces (found as loot) with their own
 * material, and can be repaired with whatever is added to their repair tags (empty for now, so not repairable).
 */
public class ModArmorMaterials {
    public static final ResourceKey<EquipmentAsset> WUNGUS_BOOTS_ASSET = createAsset("wungus_boots");
    public static final ResourceKey<EquipmentAsset> WUNGUS_MASK_ASSET = createAsset("wungus_mask");
    public static final ResourceKey<EquipmentAsset> BBL_ASSET = createAsset("bbl");

    public static final ArmorMaterial WUNGUS_HIDE = new ArmorMaterial(5, defense(4, 2, 3, 1), 15,
            SoundEvents.ARMOR_EQUIP_LEATHER, 0.0F, 0.0F, ModTags.Items.REPAIRS_WUNGUS_HIDE_ARMOR, WUNGUS_BOOTS_ASSET);

    public static final ArmorMaterial WUNGUS_MASK = new ArmorMaterial(5, defense(4, 2, 3, 1), 15,
            SoundEvents.ARMOR_EQUIP_LEATHER, 0.0F, 0.0F, ModTags.Items.REPAIRS_WUNGUS_MASK, WUNGUS_MASK_ASSET);

    public static final ArmorMaterial BBL = new ArmorMaterial(5, defense(4, 2, 3, 1), 15,
            SoundEvents.ARMOR_EQUIP_LEATHER, 0.0F, 0.0F, ModTags.Items.REPAIRS_BBL, BBL_ASSET);

    private static Map<ArmorType, Integer> defense(int boots, int leggings, int chestplate, int helmet) {
        Map<ArmorType, Integer> defense = new EnumMap<>(ArmorType.class);
        defense.put(ArmorType.BOOTS, boots);
        defense.put(ArmorType.LEGGINGS, leggings);
        defense.put(ArmorType.CHESTPLATE, chestplate);
        defense.put(ArmorType.HELMET, helmet);
        return defense;
    }

    private static ResourceKey<EquipmentAsset> createAsset(String name) {
        return ResourceKey.create(EquipmentAssets.ROOT_ID, Identifier.fromNamespaceAndPath(WungusMod.MOD_ID, name));
    }
}
