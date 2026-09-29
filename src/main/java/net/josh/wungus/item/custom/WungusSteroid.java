package net.josh.wungus.item.custom;

import net.josh.wungus.effect.ModEffects;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * Eaten like food (see ModFoods.STEROIDS), but the food logic is skipped: it only gives the steroid effect.
 */
public class WungusSteroid extends Item {
    public enum Type {
        HEALTH,
        SPEED,
        JUMP
    }

    private Type type;

    public WungusSteroid(Properties pProperties, Type type) {
        super(pProperties);
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pEntityLiving) {
        if (!pLevel.isClientSide()) {
            switch (this.type) {
                case HEALTH -> pEntityLiving.addEffect(new MobEffectInstance(ModEffects.holder(ModEffects.WUNGUS_HEALTH_STEROID_EFFECT), 2000, 0));
                case SPEED -> pEntityLiving.addEffect(new MobEffectInstance(ModEffects.holder(ModEffects.WUNGUS_SPEED_STEROID_EFFECT), 2000, 0));
                case JUMP -> pEntityLiving.addEffect(new MobEffectInstance(ModEffects.holder(ModEffects.WUNGUS_JUMP_STEROID_EFFECT), 2000, 0));
            }
        }
        if (pEntityLiving instanceof ServerPlayer serverplayer) {
            CriteriaTriggers.CONSUME_ITEM.trigger(serverplayer, pStack);
            serverplayer.awardStat(Stats.ITEM_USED.get(this));
        }

        if (pEntityLiving instanceof Player player && !player.getAbilities().instabuild) {
            pStack.shrink(1);
        }

        return pStack;
    }

    @Override
    public int getUseDuration(ItemStack pStack, LivingEntity pEntity) {
        return 32;
    }
}
