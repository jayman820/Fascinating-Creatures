package net.josh.wungus.item.custom;

import net.josh.wungus.misc.ModDamageTypes;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

public class SantonioCashew extends Item {
    public SantonioCashew(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pEntityLiving) {
        if (pEntityLiving instanceof ServerPlayer serverplayer) {
            pEntityLiving.hurt(ModDamageTypes.causeSantonioCashew(pLevel.registryAccess()), 10000);
            explode(pLevel, pEntityLiving.getOnPos());
            CriteriaTriggers.CONSUME_ITEM.trigger(serverplayer, pStack);
            serverplayer.awardStat(Stats.ITEM_USED.get(this));
        }

        if (pEntityLiving instanceof Player player && !player.getAbilities().instabuild) {
            pStack.shrink(1);
        }

        return pStack;
    }

    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        pAttacker.setInvulnerable(true);
        pTarget.hurt(ModDamageTypes.causeSantonioCashew(pTarget.level().registryAccess()), 10000);
        explode(pTarget.level(), pTarget.getOnPos());
        pAttacker.setInvulnerable(false);
        if (pAttacker instanceof Player player && !player.getAbilities().instabuild) {
            pStack.shrink(1);
        }
        return super.hurtEnemy(pStack, pTarget, pAttacker);
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

    private static void explode(Level pLevel, BlockPos pPos) {
        if (!pLevel.isClientSide()) {
            pLevel.playSound((Player)null, pPos.getX(), pPos.getY(), pPos.getZ(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
            pLevel.explode(null, pPos.getX() + 0.5f, pPos.getY() + 1.0f, pPos.getZ() + 0.5f, 4.0F, Level.ExplosionInteraction.NONE);
        }
    }
}
