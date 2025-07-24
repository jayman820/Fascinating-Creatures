package net.josh.wungus.effect;

import net.josh.wungus.particle.ModParticles;
import net.josh.wungus.sound.ModSounds;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

import java.util.Random;

public class WungdigestionEffect extends MobEffect {

    protected WungdigestionEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        Random rand = new Random();
        int hit = rand.nextInt(1000);
        int hit2 = rand.nextInt(1000);
        if(hit < 100) {
            pLivingEntity.playSound(ModSounds.BURP.get());
            for(int i = 0; i < 2000; i++) {
                pLivingEntity.level().addParticle(ModParticles.VOMIT_PARTICLE_1.get(),
                        pLivingEntity.getRandomX(0.3D), pLivingEntity.getY(1.0D), pLivingEntity.getRandomZ(0.3D),
                        (rand.nextDouble() - 0.2D) * 2D, -((rand.nextDouble() + 0.3D)), (rand.nextDouble() - 0.2D) * 2D);
            }
        }
        if(hit2 < 6) {
            pLivingEntity.playSound(ModSounds.FART.get());
            for(int i = 0; i < 2000; i++) {
                pLivingEntity.level().addParticle(ModParticles.DIARRHEA_PARTICLE_1.get(),
                        pLivingEntity.getRandomX(0.5D), pLivingEntity.getRandomY() - 1, pLivingEntity.getRandomZ(0.5D),
                        (rand.nextDouble() - 0.2D) * 0.4D, -((rand.nextDouble() + 0.3D) * 1.0D), (rand.nextDouble() - 0.2D) * 0.4D);
            }
        }
        super.applyEffectTick(pLivingEntity, pAmplifier);
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }
}
