package net.josh.wungus.item.custom.armor.provider;

import net.minecraft.client.model.Model;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

/**
 * Replaces the vanilla armor model with a custom one while the item is worn.
 * The texture comes from the item's equipment asset (assets/wungus/equipment).
 */
public class CustomArmorModelExtensions implements IClientItemExtensions {
    private final ArmorModelProvider provider;

    public CustomArmorModelExtensions(ArmorModelProvider provider) {
        this.provider = provider;
    }

    @Override
    public Model getHumanoidArmorModel(ItemStack itemStack, EquipmentClientInfo.LayerType layerType, Model original) {
        return provider.getModel(itemStack, layerType);
    }
}
