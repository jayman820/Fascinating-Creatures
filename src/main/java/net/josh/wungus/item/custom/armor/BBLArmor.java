package net.josh.wungus.item.custom.armor;

import net.josh.wungus.item.ModArmorMaterials;
import net.josh.wungus.item.custom.armor.model.BBLModel;
import net.josh.wungus.item.custom.armor.provider.ArmorModelProvider;
import net.josh.wungus.item.custom.armor.provider.SimpleModelProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

import javax.annotation.Nullable;

public class BBLArmor extends AbstractArmorItem {
    private static final String TEXTURE_LOCATION = "wungus:textures/armor/bbl.png";

    public BBLArmor() {
        super(ModArmorMaterials.BBL, Type.LEGGINGS, new Properties().rarity(Rarity.EPIC));
    }

    @Override
    protected boolean withCustomModel() {
        return true;
    }

    @Override
    protected ArmorModelProvider createModelProvider() {
        return new SimpleModelProvider(BBLModel::createBodyLayer, BBLModel::new);
    }

    @Override
    public @Nullable String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return TEXTURE_LOCATION;
    }
}
