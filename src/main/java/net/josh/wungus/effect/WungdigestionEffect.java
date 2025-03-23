package net.josh.wungus.effect;

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
        if(hit < 10) {
            pLivingEntity.playSound(ModSounds.BURP.get());
        }
        if(hit2 < 6) {
            pLivingEntity.playSound(ModSounds.FART.get());
        }
        super.applyEffectTick(pLivingEntity, pAmplifier);
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }
}
