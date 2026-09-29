package net.josh.wungus.effect;

import net.josh.wungus.network.WungdigestionPayload;
import net.josh.wungus.sound.ModSounds;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.network.PacketDistributor;

public class WungdigestionEffect extends MobEffect {

    protected WungdigestionEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    // Decided on the server only (the effect ticks on both sides, which would roll separately, so the sound and
    // the particles wouldn't happen together). The particle bursts are sent to nearby clients (WungdigestionPayload).
    @Override
    public boolean applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        if (!pLivingEntity.level().isClientSide()) {
            RandomSource rand = pLivingEntity.getRandom();
            int hit = rand.nextInt(1000);
            int hit2 = rand.nextInt(1000);
            if (hit < 10) {
                playSound(pLivingEntity, ModSounds.BURP.get());
                PacketDistributor.sendToPlayersTrackingEntityAndSelf(pLivingEntity, new WungdigestionPayload(pLivingEntity.getId(), true));
            }
            if (hit2 < 10) {
                playSound(pLivingEntity, ModSounds.FART.get());
                PacketDistributor.sendToPlayersTrackingEntityAndSelf(pLivingEntity, new WungdigestionPayload(pLivingEntity.getId(), false));
                pLivingEntity.push(0, 4, 0);
                // Sends the new motion to the clients, players move themselves on their own client
                pLivingEntity.hurtMarked = true;
            }
        }
        return true;
    }

    // Played through the level so the entity itself hears it too: entity.playSound skips the player itself,
    // because the player's own client normally plays it.
    private static void playSound(LivingEntity entity, SoundEvent sound) {
        entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(), sound, entity.getSoundSource(), 1.0F, 1.0F);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int pDuration, int pAmplifier) {
        return true;
    }
}
