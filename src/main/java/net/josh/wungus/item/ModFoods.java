package net.josh.wungus.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;

public class ModFoods {
    public static final FoodProperties WUNGUS_MILK = new FoodProperties.Builder().nutrition(4).saturationMod(0.2F).alwaysEat().build();

    public static final FoodProperties RAW_WUNGUS_FLESH = new FoodProperties.Builder().nutrition(2)
            .saturationMod(0.5f).effect(() -> new MobEffectInstance(MobEffects.HUNGER, 300), 1f).build();

    public static final FoodProperties COOKED_WUNGUS_FLESH = new FoodProperties.Builder().nutrition(7).saturationMod(0.8F).build();

    public static final FoodProperties WUNGUS_SHAWARMA = new FoodProperties.Builder().nutrition(10).saturationMod(1.6F).build();
}
