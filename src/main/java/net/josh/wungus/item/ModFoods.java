package net.josh.wungus.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;

public class ModFoods {
    public static final FoodProperties RAW_WUNGUS_FLESH = new FoodProperties.Builder().nutrition(2)
            .saturationModifier(0.5f).build();
    public static final Consumable RAW_WUNGUS_FLESH_CONSUMABLE = Consumables.defaultFood()
            .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.HUNGER, 300), 1f)).build();

    // These could always be eaten before (their use() skipped the hunger check), like chorus fruit
    public static final FoodProperties COOKED_WUNGUS_FLESH = new FoodProperties.Builder().nutrition(7).saturationModifier(0.8F).alwaysEdible().build();

    public static final FoodProperties WUNGUS_SHAWARMA = new FoodProperties.Builder().nutrition(10).saturationModifier(1.6F).alwaysEdible().build();

    // The following items never restored hunger (their custom finishUsingItem skipped the food logic),
    // so they are only consumable and do not carry a food component.
    public static final Consumable WUNGUS_MILK = Consumables.defaultDrink().build();

    public static final Consumable SANTONIO_CASHEW = Consumables.defaultFood().build();

    public static final Consumable STEROIDS = Consumables.defaultFood().build();
}
