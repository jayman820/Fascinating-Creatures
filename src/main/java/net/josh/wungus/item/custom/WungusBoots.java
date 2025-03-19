package net.josh.wungus.item.custom;

import com.google.common.collect.ImmutableMap;
import net.josh.wungus.item.ModArmorMaterials;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

import java.util.Map;

@SuppressWarnings("removal")
public class WungusBoots extends ArmorItem {
    private static final Map<ArmorMaterial, MobEffectInstance> MATERIAL_TO_EFFECT_MAP =
            (new ImmutableMap.Builder<ArmorMaterial, MobEffectInstance>())
                    .put(ModArmorMaterials.WUNGUS_HIDE, new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 200, 1))
                    .build();


    public WungusBoots(ArmorMaterial pMaterial, Type pType, Properties pProperties) {
        super(pMaterial, pType, pProperties);
    }

    @Override
    public void onArmorTick(ItemStack stack, Level level, Player player) {
        if(!level.isClientSide() && hasFullSuitOfArmorOn(player)) {
            evaluateArmorEffects(player);
        }
    }

    private void evaluateArmorEffects(Player player) {
        for(Map.Entry<ArmorMaterial, MobEffectInstance> entry : MATERIAL_TO_EFFECT_MAP.entrySet()) {
            ArmorMaterial mapArmorMaterial = entry.getKey();
            MobEffectInstance mapEffect = entry.getValue();
            if(hasPlayerCorrectArmorOn(mapArmorMaterial, player)) {
                addEffectToPlayer(player, mapEffect);
            }
        }
    }

    private void addEffectToPlayer(Player player, MobEffectInstance mapEffect) {
        boolean hasPlayerEffect = player.hasEffect(mapEffect.getEffect());

        if(!hasPlayerEffect) {
            player.addEffect(new MobEffectInstance(mapEffect.getEffect(),
                    mapEffect.getDuration(), mapEffect.getAmplifier()));
        }
    }

    private boolean hasPlayerCorrectArmorOn(ArmorMaterial mapArmorMaterial, Player player) {
        for(ItemStack armorStack : player.getArmorSlots()) {
            if(!(armorStack.getItem() instanceof ArmorItem)) {
                if(!(armorStack.getItem().equals(Items.AIR))) {
                    return false;
                }
            }
        }
        ArmorItem boots = ((ArmorItem) player.getInventory().getArmor(0).getItem());

        //ArmorItem helmet = ((ArmorItem) player.getInventory().getArmor(2).getItem());
        return boots.getMaterial() == mapArmorMaterial;// && leggings.getMaterial() == mapArmorMaterial
                //&& chestplate.getMaterial() == mapArmorMaterial && helmet.getMaterial() == mapArmorMaterial;
    }

    private boolean hasFullSuitOfArmorOn(Player player) {
        ItemStack boots = player.getInventory().getArmor(0);
        ItemStack leggings = player.getInventory().getArmor(1);
        ItemStack chestplate = player.getInventory().getArmor(2);
        ItemStack helmet = player.getInventory().getArmor(3);

        return !boots.isEmpty();
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
       return "wungus:textures/armor/wungus_boots.png";
    }
}
