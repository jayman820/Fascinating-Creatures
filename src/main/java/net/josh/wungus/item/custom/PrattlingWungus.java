package net.josh.wungus.item.custom;

import net.josh.wungus.sound.ModSounds;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.level.Level;

public class PrattlingWungus extends Item {
    private int VARIANT;

    public PrattlingWungus(Item.Properties pProperties, int variant) {
        super(pProperties);
        this.VARIANT = variant;
    }

    @Override
    public ItemUseAnimation getUseAnimation(ItemStack pStack) {
        return ItemUseAnimation.BOW;
    }

    @Override
    public InteractionResult use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        switch (this.VARIANT) {
            case 1:
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
