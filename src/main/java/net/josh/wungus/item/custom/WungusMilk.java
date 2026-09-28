package net.josh.wungus.item.custom;

import net.josh.wungus.effect.ModEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * Drinking behaviour (32 ticks, drink animation, stats, returning the bucket) is handled by the
 * consumable and use remainder components set up in ModItems.
 */
public class WungusMilk extends Item {
    public WungusMilk(Item.Properties pProperties) {
        super(pProperties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pEntityLiving) {
        if (!pLevel.isClientSide()) {
            pEntityLiving.addEffect(new MobEffectInstance(MobEffects.NAUSEA, 450, 0));
            pEntityLiving.addEffect(new MobEffectInstance(ModEffects.WUNGDIGESTION_EFFECT, 450, 0));
        }

        return super.finishUsingItem(pStack, pLevel, pEntityLiving);
    }
}
