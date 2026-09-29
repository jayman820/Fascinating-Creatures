package net.josh.wungus.event;

import net.josh.wungus.WungusMod;
import net.josh.wungus.effect.HeartPalpitationsEffect;
import net.josh.wungus.effect.ModEffects;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ViewportEvent;

/** Client game events: the Heart Palpitations screen shake and red pulse (from the steroid heart failure). */
@EventBusSubscriber(modid = WungusMod.MOD_ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class ModClientGameEvents {
    // Near the end of the heart failure the edges of the screen pulse red with the heartbeat
    private static final float RED_PULSE_START = 0.7F;

    // Shakes the camera. It starts barely noticeable and slowly gets stronger.
    // Scaled by the "Distortion Effects" accessibility slider, like nausea.
    @SubscribeEvent
    public static void shakeCamera(ViewportEvent.ComputeCameraAngles event) {
        Minecraft minecraft = Minecraft.getInstance();
        int level = palpitationsLevel(minecraft.player);
        if (level < 0) {
            return;
        }

        float intensity = (float) (level + 1) / (HeartPalpitationsEffect.MAX_LEVEL + 1);   // 0.05 to 1
        // About 0.05 degrees at the start up to about 1 at the end (the waves below add up to at most 1.5 times this)
        float strength = (0.05F + 1.0F * intensity * intensity) * minecraft.options.screenEffectScale().get().floatValue();
        // Slow sway at first, faster trembling later
        float time = (minecraft.player.tickCount + (float) event.getPartialTick()) * Mth.lerp(intensity, 0.5F, 1.2F);
        event.setYaw(event.getYaw() + (Mth.sin(time * 1.3F) + 0.5F * Mth.sin(time * 2.9F)) * strength);
        event.setPitch(event.getPitch() + (Mth.cos(time * 1.7F) + 0.5F * Mth.sin(time * 3.1F)) * strength);
        event.setRoll(event.getRoll() + Mth.sin(time * 2.3F) * strength);
    }

    // Registered as a gui layer in ModEventBusClientEvents
    public static void renderRedPulse(GuiGraphics graphics, DeltaTracker deltaTracker) {
        Minecraft minecraft = Minecraft.getInstance();
        int level = palpitationsLevel(minecraft.player);
        if (level < 0) {
            return;
        }
        float progress = HeartPalpitationsEffect.progressFor(level);
        if (progress < RED_PULSE_START) {
            return;
        }

        // A quick flash on every beat that fades until the next one, stronger towards the end
        float interval = HeartPalpitationsEffect.heartbeatInterval(progress);
        float time = minecraft.player.tickCount + deltaTracker.getGameTimeDeltaPartialTick(false);
        float phase = (time % interval) / interval;
        float pulse = (1.0F - phase) * (1.0F - phase);
        float strength = Mth.clamp((progress - RED_PULSE_START) / (1.0F - RED_PULSE_START), 0.0F, 1.0F);
        int alpha = (int) (255 * pulse * Mth.lerp(strength, 0.25F, 0.7F));
        if (alpha <= 0) {
            return;
        }

        int width = graphics.guiWidth();
        int height = graphics.guiHeight();
        int edge = Math.max(8, Math.min(width, height) / 6);
        int red = alpha << 24 | 0xB00010;
        int clear = 0x00B00010;
        // Top and bottom: vertical gradients
        graphics.fillGradient(0, 0, width, edge, red, clear);
        graphics.fillGradient(0, height - edge, width, height, clear, red);
        // Left and right: thin columns that fade out towards the middle
        int columns = 16;
        for (int i = 0; i < columns; i++) {
            float fade = 1.0F - (float) i / columns;
            int color = ((int) (alpha * fade * fade) << 24) | 0xB00010;
            int x0 = edge * i / columns;
            int x1 = edge * (i + 1) / columns;
            graphics.fill(x0, edge, x1, height - edge, color);
            graphics.fill(width - x1, edge, width - x0, height - edge, color);
        }
    }

    /** The Heart Palpitations amplifier of the player, or -1 when it doesn't have it. */
    private static int palpitationsLevel(LocalPlayer player) {
        if (player == null) {
            return -1;
        }
        // Compared by value: the effects synced from the server use the registry's holders, not ModEffects' ones
        for (MobEffectInstance effect : player.getActiveEffects()) {
            if (effect.getEffect().value() == ModEffects.HEART_PALPITATIONS_EFFECT.get()) {
                return effect.getAmplifier();
            }
        }
        return -1;
    }
}
