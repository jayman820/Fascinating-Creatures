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
 * Armor materials are plain records now. Each wungus hide armor piece has its own look, so each one points
 * at its own equipment asset (assets/wungus/equipment/*.json) while sharing the same stats.
 */
public class ModArmorMaterials {
    public static final ResourceKey<EquipmentAsset> WUNGUS_BOOTS_ASSET = createAsset("wungus_boots");
    public static final ResourceKey<EquipmentAsset> WUNGUS_MASK_ASSET = createAsset("wungus_mask");
    public static final ResourceKey<EquipmentAsset> BBL_ASSET = createAsset("bbl");

    public static final ArmorMaterial WUNGUS_HIDE = wungusHide(WUNGUS_BOOTS_ASSET);
    public static final ArmorMaterial WUNGUS_HIDE_MASK = wungusHide(WUNGUS_MASK_ASSET);
    public static final ArmorMaterial WUNGUS_HIDE_BBL = wungusHide(BBL_ASSET);

    private static ArmorMaterial wungusHide(ResourceKey<EquipmentAsset> asset) {
        Map<ArmorType, Integer> defense = new EnumMap<>(ArmorType.class);
        defense.put(ArmorType.BOOTS, 4);
        defense.put(ArmorType.LEGGINGS, 2);
        defense.put(ArmorType.CHESTPLATE, 3);
        defense.put(ArmorType.HELMET, 1);

        return new ArmorMaterial(5, defense, 15, SoundEvents.ARMOR_EQUIP_LEATHER, 0.0F, 0.0F,
                ModTags.Items.REPAIRS_WUNGUS_HIDE_ARMOR, asset);
    }

    private static ResourceKey<EquipmentAsset> createAsset(String name) {
        return ResourceKey.create(EquipmentAssets.ROOT_ID, Identifier.fromNamespaceAndPath(WungusMod.MOD_ID, name));
    }
}
