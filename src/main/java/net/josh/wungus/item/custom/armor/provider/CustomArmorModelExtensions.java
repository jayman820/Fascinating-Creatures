package net.josh.wungus.item.custom.armor.provider;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

/**
 * Replaces the vanilla armor model with a custom one while the item is worn.
 * NeoForge copies the pose and the part visibility of the vanilla armor model onto it before it is rendered.
 * The texture comes from the item's armor material (see ModArmorMaterials).
 */
public class CustomArmorModelExtensions implements IClientItemExtensions {
    private final ArmorModelProvider provider;

    public CustomArmorModelExtensions(ArmorModelProvider provider) {
        this.provider = provider;
    }

    @Override
    public HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
        return provider.getModel(livingEntity, itemStack, equipmentSlot);
    }
}
