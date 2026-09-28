package net.josh.wungus.item.custom.armor.provider;

import net.josh.wungus.item.custom.armor.model.ArmorModel;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.world.item.ItemStack;

/**
 * the armor model provider
 */
public interface ArmorModelProvider {

    /**
     * provides a custom armor model.
     * cache the model if possible as it's needed each <i>rendering</i> tick
     * @param stack the stack for the model
     * @param layerType the equipment layer being rendered
     * @return the model
     */
    ArmorModel getModel(ItemStack stack, EquipmentClientInfo.LayerType layerType);
}
