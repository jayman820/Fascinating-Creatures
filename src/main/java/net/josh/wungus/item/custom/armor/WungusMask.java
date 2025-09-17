package net.josh.wungus.item.custom.armor;

import net.josh.wungus.WungusMod;
import net.josh.wungus.item.ModArmorMaterials;
import net.josh.wungus.item.custom.armor.model.WungusMaskModel;
import net.josh.wungus.item.custom.armor.provider.ArmorModelProvider;
import net.josh.wungus.item.custom.armor.provider.SimpleModelProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Map;
import java.util.function.Consumer;

public class WungusMask extends AbstractArmorItem {
    private static final String TEXTURE_LOCATION = "wungus:textures/armor/wungus_mask.png";

    public WungusMask() {
        super(ModArmorMaterials.WUNGUS_HIDE, Type.HELMET, new Properties().rarity(Rarity.EPIC));
    }

    @Override
    protected boolean withCustomModel() {
        return true;
    }

    @Override
    protected ArmorModelProvider createModelProvider() {
        return new SimpleModelProvider(WungusMaskModel::createBodyLayer, WungusMaskModel::new);
    }

    @Override
    public @Nullable String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return TEXTURE_LOCATION;
    }
}
