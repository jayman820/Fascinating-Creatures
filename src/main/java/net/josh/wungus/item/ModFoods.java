package net.josh.wungus.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

public class ModFoods {
    public static final FoodProperties WUNGUS_MILK = new FoodProperties.Builder().nutrition(4).saturationModifier(0.2F).alwaysEdible().build();

    public static final FoodProperties SANTONIO_CASHEW = new FoodProperties.Builder().nutrition(2).saturationModifier(0.0F).alwaysEdible().build();

    public static final FoodProperties STEROIDS = new FoodProperties.Builder().nutrition(0).saturationModifier(0.0F).alwaysEdible().build();

    public static final FoodProperties RAW_WUNGUS_FLESH = new FoodProperties.Builder().nutrition(2)
            .saturationModifier(0.5f).effect(new MobEffectInstance(MobEffects.HUNGER, 300), 1f).build();

    public static final FoodProperties COOKED_WUNGUS_FLESH = new FoodProperties.Builder().nutrition(7).saturationModifier(0.8F).build();

    public static final FoodProperties WUNGUS_SHAWARMA = new FoodProperties.Builder().nutrition(10).saturationModifier(1.6F).build();
}
