package net.josh.wungus.item.custom;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * Eating (32 ticks), stats and the 1 second cooldown are handled by the food, consumable and
 * use cooldown components set up in ModItems.
 */
public class WungusCookedFlesh extends Item {
    public WungusCookedFlesh(Item.Properties pProperties) {
        super(pProperties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pEntityLiving) {
        ItemStack consumedStack = pStack.copy();
        ItemStack itemstack = super.finishUsingItem(pStack, pLevel, pEntityLiving);
        if (pLevel instanceof ServerLevel serverLevel) {
            ChorusLikeTeleport.teleport(serverLevel, pEntityLiving, consumedStack, 40.0D, 16, 8);
        }
        return itemstack;
    }
}
