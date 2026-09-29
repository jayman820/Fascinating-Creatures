package net.josh.wungus.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

/**
 * Shakes the camera of the player that has it, stronger with a higher amplifier (see ModClientEvents).
 * Given (hidden) by the steroid heart failure, replacing nausea.
 */
public class HeartPalpitationsEffect extends MobEffect {
    public HeartPalpitationsEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }
}
