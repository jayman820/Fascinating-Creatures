package net.josh.wungus.effect;

import net.josh.wungus.network.WungdigestionPayload;
import net.josh.wungus.sound.ModSounds;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.network.PacketDistributor;

public class WungdigestionEffect extends MobEffect {

    protected WungdigestionEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    // Effects only tick on the server now, so the particle bursts are sent to nearby clients
    // (see WungdigestionPayload) which spawn them exactly like the old client-side code did.
    @Override
    public boolean applyEffectTick(ServerLevel pLevel, LivingEntity pLivingEntity, int pAmplifier) {
        RandomSource rand = pLivingEntity.getRandom();
        int hit = rand.nextInt(1000);
        int hit2 = rand.nextInt(1000);
        if(hit < 10) {
            pLivingEntity.playSound(ModSounds.BURP.get());
            PacketDistributor.sendToPlayersTrackingEntityAndSelf(pLivingEntity, new WungdigestionPayload(pLivingEntity.getId(), true));
        }
        if(hit2 < -1) {
            pLivingEntity.playSound(ModSounds.FART.get());
            PacketDistributor.sendToPlayersTrackingEntityAndSelf(pLivingEntity, new WungdigestionPayload(pLivingEntity.getId(), false));
            pLivingEntity.push(0, 4, 0);
        }
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int pDuration, int pAmplifier) {
        return true;
    }
}
