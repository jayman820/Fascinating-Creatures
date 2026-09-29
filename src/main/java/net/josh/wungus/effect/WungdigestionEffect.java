package net.josh.wungus.effect;

import net.josh.wungus.network.ModNetworking;
import net.josh.wungus.network.WungdigestionMessage;
import net.josh.wungus.sound.ModSounds;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class WungdigestionEffect extends MobEffect {

    protected WungdigestionEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    // Decided on the server only (the effect ticks on both sides, which used to roll separately, so the sound and
    // the particles didn't happen together). The particle bursts are sent to nearby clients (WungdigestionMessage).
    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        if (!pLivingEntity.level().isClientSide()) {
            RandomSource rand = pLivingEntity.getRandom();
            int hit = rand.nextInt(1000);
            int hit2 = rand.nextInt(1000);
            if (hit < 10) {
                playSound(pLivingEntity, ModSounds.BURP.get());
                ModNetworking.sendToTrackingAndSelf(pLivingEntity, new WungdigestionMessage(pLivingEntity.getId(), true));
            }
            if (hit2 < 10) {
                playSound(pLivingEntity, ModSounds.FART.get());
                ModNetworking.sendToTrackingAndSelf(pLivingEntity, new WungdigestionMessage(pLivingEntity.getId(), false));
                pLivingEntity.push(0, 4, 0);
            }
        }
        super.applyEffectTick(pLivingEntity, pAmplifier);
    }

    // Played through the level so the entity itself hears it too: entity.playSound skips the player itself,
    // because the player's own client normally plays it.
    private static void playSound(LivingEntity entity, SoundEvent sound) {
        entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(), sound, entity.getSoundSource(), 1.0F, 1.0F);
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }
}
