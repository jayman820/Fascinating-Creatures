package net.josh.wungus.item.custom;

import net.josh.wungus.effect.ModEffects;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * Eating (32 ticks), stats and the 1 second cooldown are handled by the food, consumable and
 * use cooldown components set up in ModItems.
 */
public class WungusShawarma extends Item {
    public WungusShawarma(Item.Properties pProperties) {
        super(pProperties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pEntityLiving) {
        ItemStack consumedStack = pStack.copy();
        if (!pLevel.isClientSide()) {
            int hit = pEntityLiving.getRandom().nextInt(10);
            if (hit <= 1) {
                pEntityLiving.addEffect(new MobEffectInstance(MobEffects.NAUSEA, 600, 0));
                pEntityLiving.addEffect(new MobEffectInstance(ModEffects.WUNGDIGESTION_EFFECT, 600, 0));
            }
        }

        ItemStack itemstack = super.finishUsingItem(pStack, pLevel, pEntityLiving);
        if (pLevel instanceof ServerLevel serverLevel) {
            ChorusLikeTeleport.teleport(serverLevel, pEntityLiving, consumedStack, 60.0D, 24, 8);
        }
        return itemstack;
    }
}
