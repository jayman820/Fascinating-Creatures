package net.josh.wungus.effect;

import net.josh.wungus.WungusMod;
import net.josh.wungus.attachment.ModAttachments;
import net.josh.wungus.item.custom.WungusSteroid;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(Registries.MOB_EFFECT, WungusMod.MOD_ID);

    public static final DeferredHolder<MobEffect, MobEffect> WUNGDIGESTION_EFFECT =
            MOB_EFFECTS.register("wungdigestion", () -> new WungdigestionEffect(MobEffectCategory.NEUTRAL, 0x36ebab));

    // Hidden effect that shakes the screen, given by the steroid heart failure
    public static final DeferredHolder<MobEffect, MobEffect> HEART_PALPITATIONS_EFFECT =
            MOB_EFFECTS.register("heart_palpitations", () -> new HeartPalpitationsEffect(MobEffectCategory.HARMFUL, 0xb3122e));

    public static final DeferredHolder<MobEffect, MobEffect> WUNGUS_HEALTH_STEROID_EFFECT =
            MOB_EFFECTS.register("wungus_steroid_health", () -> new WungusSteroidEffect(MobEffectCategory.NEUTRAL, 0x36ebab, WungusSteroid.Type.HEALTH, ModAttachments.HEALTH_STEROID_STATE));

    public static final DeferredHolder<MobEffect, MobEffect> WUNGUS_SPEED_STEROID_EFFECT =
            MOB_EFFECTS.register("wungus_steroid_speed", () -> new WungusSteroidEffect(MobEffectCategory.NEUTRAL, 0x36ebab, WungusSteroid.Type.SPEED, ModAttachments.SPEED_STEROID_STATE));

    public static final DeferredHolder<MobEffect, MobEffect> WUNGUS_JUMP_STEROID_EFFECT =
            MOB_EFFECTS.register("wungus_steroid_jump", () -> new WungusSteroidEffect(MobEffectCategory.NEUTRAL, 0x36ebab, WungusSteroid.Type.JUMP, ModAttachments.JUMP_STEROID_STATE));

    public static void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }
}
