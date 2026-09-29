package net.josh.wungus.item.custom;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

/**
 * Eating it (the food logic, stats and consuming the item happen in super.finishUsingItem) teleports you like a
 * chorus fruit, with a 1 second cooldown. It can always be eaten, even when not hungry.
 */
public class WungusCookedFlesh extends Item {
    public WungusCookedFlesh(Item.Properties pProperties) {
        super(pProperties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pEntityLiving) {
        ItemStack itemstack = super.finishUsingItem(pStack, pLevel, pEntityLiving);
        if (pLevel instanceof ServerLevel serverLevel) {
            ChorusLikeTeleport.teleport(serverLevel, pEntityLiving, 40.0D, 16, 8);

            if (pEntityLiving instanceof Player player) {
                player.getCooldowns().addCooldown(this, 20);
            }
        }
        return itemstack;
    }

    @Override
    public int getUseDuration(ItemStack pStack, LivingEntity pEntity) {
        return 32;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.EAT;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        return ItemUtils.startUsingInstantly(pLevel, pPlayer, pHand);
    }
}
