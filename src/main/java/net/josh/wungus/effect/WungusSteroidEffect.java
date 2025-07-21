package net.josh.wungus.effect;

import net.josh.wungus.item.custom.WungusSteroid;
import net.josh.wungus.sound.ModSounds;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Random;

public class WungusSteroidEffect extends MobEffect {
    private WungusSteroid.Type type;
    private boolean soundPlaying = false;
    private int tickCount = 0;
    protected WungusSteroidEffect(MobEffectCategory pCategory, int pColor, WungusSteroid.Type type) {
        super(pCategory, pColor);
        this.type = type;
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        if(this.soundPlaying == false) {
            this.soundPlaying = true;
            pLivingEntity.playSound(ModSounds.HEARTBEAT.get());
        }
        if(tickCount == 200) {
            pLivingEntity.addEffect(new MobEffectInstance(MobEffects.HEALTH_BOOST, 500, 10));
            pLivingEntity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 500, 20));
        }
        if(tickCount == 600) {
            pLivingEntity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 460, 1));
        }
        if(tickCount == 700) {
            pLivingEntity.removeEffect(MobEffects.HEALTH_BOOST);
            pLivingEntity.removeEffect(MobEffects.DAMAGE_BOOST);
            pLivingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 360, 10));
            pLivingEntity.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 360, 10));
            pLivingEntity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 360, 10));
        }
        if(tickCount == 1000) {
            pLivingEntity.removeEffect(MobEffects.CONFUSION);
            pLivingEntity.removeEffect(MobEffects.MOVEMENT_SLOWDOWN);
            pLivingEntity.removeEffect(MobEffects.DIG_SLOWDOWN);
            pLivingEntity.removeEffect(MobEffects.WEAKNESS);
            pLivingEntity.kill();
        }
        this.tickCount++;
        super.applyEffectTick(pLivingEntity, pAmplifier);
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }
}
