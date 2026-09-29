package net.josh.wungus.effect;

import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

/**
 * Given (hidden) by the steroid heart failure. On the client it shakes the camera and, near the end, pulses the
 * screen edges red (see ModClientGameEvents). The amplifier tells the client how far the heart failure is.
 * The timing math is here so the server and the client agree on it.
 */
public class HeartPalpitationsEffect extends MobEffect {
    // The shake starts at this part of the dose and gets a bit stronger every PROGRESS_PER_LEVEL
    public static final float START_PROGRESS = 0.15F;
    public static final float PROGRESS_PER_LEVEL = 0.04F;
    public static final int MAX_LEVEL = 19;

    // Heartbeat interval in ticks at the start and at the end of the dose (20 ticks = 1 second)
    private static final int FIRST_HEARTBEAT_INTERVAL = 30;
    private static final int LAST_HEARTBEAT_INTERVAL = 4;

    public HeartPalpitationsEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    /** The amplifier for a point in the dose (0 to 1), or -1 before the shaking starts. */
    public static int levelFor(float progress) {
        if (progress < START_PROGRESS) {
            return -1;
        }
        return Math.min(MAX_LEVEL, (int) ((progress - START_PROGRESS) / PROGRESS_PER_LEVEL));
    }

    /** Roughly the point in the dose (0 to 1) for an amplifier. */
    public static float progressFor(int level) {
        return Math.min(1.0F, START_PROGRESS + (level + 0.5F) * PROGRESS_PER_LEVEL);
    }

    /** Ticks between two heartbeats, getting shorter towards the end of the dose. */
    public static float heartbeatInterval(float progress) {
        return Mth.lerp(progress * progress, FIRST_HEARTBEAT_INTERVAL, LAST_HEARTBEAT_INTERVAL);
    }
}
