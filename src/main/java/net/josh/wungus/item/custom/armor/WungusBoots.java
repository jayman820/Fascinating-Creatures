package net.josh.wungus.item.custom.armor;

import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.Equippable;
import org.jspecify.annotations.Nullable;

/**
 * Wungus hide boots. The armor stats and equipment asset come from the item properties
 * (see ModItems / ModArmorMaterials), the custom 3D model is registered on the client
 * (see ModEventBusClientEvents).
 */
public class WungusBoots extends Item {
    private static final EquipmentSlot[] ARMOR_SLOTS = {EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};

    public WungusBoots(Properties pProperties) {
        super(pProperties);
    }

    // Replaces the old onArmorTick: equipped armor is ticked with the slot it is worn in (server side only)
    @Override
    public void inventoryTick(ItemStack stack, ServerLevel level, Entity owner, @Nullable EquipmentSlot slot) {
        super.inventoryTick(stack, level, owner, slot);
        if (slot == EquipmentSlot.FEET && owner instanceof Player player && hasOnlyArmorEquipped(player)) {
            addEffectToPlayer(player, new MobEffectInstance(MobEffects.SPEED, 200, 1));
        }
    }

    private void addEffectToPlayer(Player player, MobEffectInstance mapEffect) {
        boolean hasPlayerEffect = player.hasEffect(mapEffect.getEffect());

        if(!hasPlayerEffect) {
            player.addEffect(new MobEffectInstance(mapEffect.getEffect(),
                    mapEffect.getDuration(), mapEffect.getAmplifier()));
        }
    }

    // Every armor slot has to be empty or hold a piece of armor (not e.g. an elytra or a carved pumpkin)
    private boolean hasOnlyArmorEquipped(Player player) {
        for (EquipmentSlot armorSlot : ARMOR_SLOTS) {
            ItemStack armorStack = player.getItemBySlot(armorSlot);
            if (armorStack.isEmpty()) {
                continue;
            }
            Equippable equippable = armorStack.get(DataComponents.EQUIPPABLE);
            if (equippable == null || equippable.assetId().isEmpty() || armorStack.has(DataComponents.GLIDER)) {
                return false;
            }
        }
        return true;
    }
}
