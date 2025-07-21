package net.josh.wungus.effect;

import net.josh.wungus.WungusMod;
import net.josh.wungus.item.custom.WungusSteroid;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, WungusMod.MOD_ID);

    public static final RegistryObject<MobEffect> WUNGDIGESTION_EFFECT =
            MOB_EFFECTS.register("wungdigestion", () -> new WungdigestionEffect(MobEffectCategory.NEUTRAL, 0x36ebab));

    public static final RegistryObject<MobEffect> WUNGUS_STEROID_EFFECT =
            MOB_EFFECTS.register("wungus_steroid", () -> new WungusSteroidEffect(MobEffectCategory.NEUTRAL, 0x36ebab, WungusSteroid.Type.JUMP));

    public static void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }
}
