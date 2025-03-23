package net.josh.wungus.item.custom;

import net.josh.wungus.effect.ModEffects;
import net.josh.wungus.sound.ModSounds;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

public class PrattlingWungus extends Item {
    private int VARIANT;

    public PrattlingWungus(Item.Properties pProperties, int variant) {
        super(pProperties);
        this.VARIANT = variant;
    }

    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.BOW;
    }

    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        switch (this.VARIANT) {
            case 1:
                System.out.println("hi");
                pPlayer.playSound(ModSounds.PRATTLING_WUNGUS_1.get());
                break;
            case 2:
                pPlayer.playSound(ModSounds.PRATTLING_WUNGUS_2.get());
                break;
            case 3:
                pPlayer.playSound(ModSounds.PRATTLING_WUNGUS_3.get());
                break;
            case 4:
                pPlayer.playSound(ModSounds.PRATTLING_WUNGUS_4.get());
                break;
        }
        return ItemUtils.startUsingInstantly(pLevel, pPlayer, pHand);
    }
}
