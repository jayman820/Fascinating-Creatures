package net.josh.wungus.item.custom.armor;

import net.josh.wungus.item.ModArmorMaterials;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * Wungus hide boots: speed while worn, as long as every other armor slot is empty or holds a piece of armor.
 * The custom 3D model is registered on the client (see ModEventBusClientEvents).
 */
public class WungusBoots extends ArmorItem {
    private static final EquipmentSlot[] ARMOR_SLOTS = {EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};

    public WungusBoots(Holder<ArmorMaterial> pMaterial, Type pType, Properties pProperties) {
        super(pMaterial, pType, pProperties);
    }

    // Worn armor is ticked like every other item in the inventory, so check that the boots are actually worn
    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        super.inventoryTick(pStack, pLevel, pEntity, pSlotId, pIsSelected);
        if (!pLevel.isClientSide() && pEntity instanceof Player player
                && player.getItemBySlot(EquipmentSlot.FEET) == pStack && hasOnlyArmorEquipped(player)) {
            addEffectToPlayer(player, new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 200, 1));
        }
    }

    private void addEffectToPlayer(Player player, MobEffectInstance mapEffect) {
        boolean hasPlayerEffect = player.hasEffect(mapEffect.getEffect());

        if(!hasPlayerEffect) {
            player.addEffect(new MobEffectInstance(mapEffect.getEffect(),
                    mapEffect.getDuration(), mapEffect.getAmplifier()));
        }
    }

    // Every armor slot has to be empty or hold a piece of armor (not e.g. an elytra or a carved pumpkin),
    // and the boots have to be wungus hide
    private boolean hasOnlyArmorEquipped(Player player) {
        for (EquipmentSlot armorSlot : ARMOR_SLOTS) {
            ItemStack armorStack = player.getItemBySlot(armorSlot);
            if (!armorStack.isEmpty() && !(armorStack.getItem() instanceof ArmorItem)) {
                return false;
            }
        }
        return player.getItemBySlot(EquipmentSlot.FEET).getItem() instanceof ArmorItem boots
                && boots.getMaterial().is(ModArmorMaterials.WUNGUS_HIDE.getKey());
    }
}
