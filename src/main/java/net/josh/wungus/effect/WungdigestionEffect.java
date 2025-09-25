package net.josh.wungus.effect;

import net.josh.wungus.particle.ModParticles;
import net.josh.wungus.sound.ModSounds;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.phys.Vec3;

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
        if(hit < 10) {
            pLivingEntity.playSound(ModSounds.BURP.get());
            Vec3 vec = pLivingEntity.getViewVector(0).scale(0.8d);
            for(int i = 0; i < 2000; i++) {
                pLivingEntity.level().addParticle(ModParticles.VOMIT_PARTICLE_1.get(),
                        pLivingEntity.getX() + vec.get(Direction.Axis.X), pLivingEntity.getY() + 1.5, pLivingEntity.getZ() + vec.get(Direction.Axis.Z),
                        -(rand.nextDouble() - 0.2D) * 1.3D * Math.cos(vec.get(Direction.Axis.X) * 2 * Math.PI), -((rand.nextDouble() + 0.8D)), -(rand.nextDouble() - 0.2D) * 1.3D * Math.sin(vec.get(Direction.Axis.Z) * 2 * Math.PI));
            }
        }
        if(hit2 < -1) {
            pLivingEntity.playSound(ModSounds.FART.get());
            Vec3 vec = pLivingEntity.getViewVector(0);
            for(int i = 0; i < 2000; i++) {
                pLivingEntity.level().addParticle(ModParticles.DIARRHEA_PARTICLE_1.get(),
                        pLivingEntity.getX() - vec.scale(1.2).get(Direction.Axis.X), pLivingEntity.getY() + 1, pLivingEntity.getZ() - vec.scale(1.2D).get(Direction.Axis.Z),
                        (rand.nextDouble() - 0.2D), -((rand.nextDouble() + 0.3D) * 1.0D), (rand.nextDouble() - 0.2D));
            }
            pLivingEntity.push(0, 4, 0);
        }
        super.applyEffectTick(pLivingEntity, pAmplifier);
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }
}
