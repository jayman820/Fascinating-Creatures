package net.josh.wungus.item.custom;

import net.josh.wungus.effect.ModEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * Eating (32 ticks, stats and consuming the item) is handled by the consumable component set up in ModItems.
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
                case HEALTH -> pEntityLiving.addEffect(new MobEffectInstance(ModEffects.WUNGUS_HEALTH_STEROID_EFFECT, 2000, 0));
                case SPEED -> pEntityLiving.addEffect(new MobEffectInstance(ModEffects.WUNGUS_SPEED_STEROID_EFFECT, 2000, 0));
                case JUMP -> pEntityLiving.addEffect(new MobEffectInstance(ModEffects.WUNGUS_JUMP_STEROID_EFFECT, 2000, 0));
            }
        }

        return super.finishUsingItem(pStack, pLevel, pEntityLiving);
    }
}
