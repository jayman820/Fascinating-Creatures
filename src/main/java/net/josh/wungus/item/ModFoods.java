package net.josh.wungus.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

public class ModFoods {
    public static final FoodProperties WUNGUS_MILK = new FoodProperties.Builder().nutrition(4).alwaysEat().fast()
            .saturationMod(0.5f).effect(() -> new MobEffectInstance(MobEffects.CONFUSION, 200), 1f).build();
}
