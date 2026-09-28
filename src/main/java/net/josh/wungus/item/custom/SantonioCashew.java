package net.josh.wungus.item.custom;

import net.josh.wungus.misc.ModDamageTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * Eating (32 ticks, eat animation, stats and consuming the item) is handled by the consumable
 * component set up in ModItems.
 */
public class SantonioCashew extends Item {
    public SantonioCashew(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pEntityLiving) {
        if (pEntityLiving instanceof ServerPlayer serverplayer) {
            serverplayer.hurtServer(serverplayer.level(), ModDamageTypes.causeSantonioCashew(pLevel.registryAccess()), 10000);
            explode(pLevel, pEntityLiving.getOnPos());
        }

        return super.finishUsingItem(pStack, pLevel, pEntityLiving);
    }

    @Override
    public void hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        pAttacker.setInvulnerable(true);
        if (pTarget.level() instanceof ServerLevel serverLevel) {
            pTarget.hurtServer(serverLevel, ModDamageTypes.causeSantonioCashew(serverLevel.registryAccess()), 10000);
        }
        explode(pTarget.level(), pTarget.getOnPos());
        pAttacker.setInvulnerable(false);
        if (pAttacker instanceof Player player && !player.getAbilities().instabuild) {
            pStack.shrink(1);
        }
        super.hurtEnemy(pStack, pTarget, pAttacker);
    }

    private static void explode(Level pLevel, BlockPos pPos) {
        if (!pLevel.isClientSide()) {
            pLevel.playSound((Entity)null, pPos.getX(), pPos.getY(), pPos.getZ(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
            pLevel.explode(null, pPos.getX() + 0.5f, pPos.getY() + 1.0f, pPos.getZ() + 0.5f, 4.0F, Level.ExplosionInteraction.NONE);
        }
    }
}
