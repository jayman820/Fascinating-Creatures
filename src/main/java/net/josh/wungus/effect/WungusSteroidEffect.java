package net.josh.wungus.effect;

import net.josh.wungus.item.custom.WungusSteroid;
import net.josh.wungus.misc.ModDamageTypes;
import net.josh.wungus.sound.ModSounds;
import net.minecraft.world.damagesource.*;
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
    private int HEARTBEAT_INTERVAL;
    private int HEARTBEAT_MODIFIER;
    private int CARDIAC_ARREST_LEVEL;
    private double CARDIAC_ARREST_LEVEL_MODIFIER;
    private int LEVEL_UP_COOLDOWN;
    private boolean LEVEL_ONE;
    private boolean LEVEL_TWO;
    private boolean LEVEL_THREE;
    public WungusSteroidEffect(MobEffectCategory pCategory, int pColor, WungusSteroid.Type type) {
        super(pCategory, pColor);
        this.type = type;
        this.HEARTBEAT_INTERVAL = 80;
        this.HEARTBEAT_MODIFIER = 1;
        this.CARDIAC_ARREST_LEVEL = 1;
        this.CARDIAC_ARREST_LEVEL_MODIFIER = 1;
        this.LEVEL_UP_COOLDOWN = 1000;
        this.LEVEL_ONE = false;
        this.LEVEL_TWO = false;
        this.LEVEL_THREE = false;
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        HEARTBEAT_INTERVAL--;
        LEVEL_UP_COOLDOWN--;
        if(HEARTBEAT_INTERVAL <= 0) {
            pLivingEntity.playSound(ModSounds.HEARTBEAT.get());
            HEARTBEAT_INTERVAL = (80 / HEARTBEAT_MODIFIER);
        }
        double rand = Math.floor(Math.random() * 10000) + 1;

        if(!LEVEL_ONE && CARDIAC_ARREST_LEVEL == 1) {
            pLivingEntity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 500, 10));
            switch (this.type) {
                case HEALTH:
                    pLivingEntity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 500, 10));
                    break;
                case SPEED:
                    pLivingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 500, 5));
                    break;
                case JUMP:
                    pLivingEntity.addEffect(new MobEffectInstance(MobEffects.JUMP, 500, 5));
                    break;
            }
            LEVEL_ONE = true;
        } else
        if(!LEVEL_TWO && CARDIAC_ARREST_LEVEL == 2) {
            pLivingEntity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 1800, 1));
            LEVEL_TWO = true;
        } else
        if(!LEVEL_THREE && CARDIAC_ARREST_LEVEL == 3) {
            pLivingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 1000, 10));
            pLivingEntity.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 1000, 10));
            pLivingEntity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 1000, 10));
            LEVEL_THREE = true;
        } else
        if(CARDIAC_ARREST_LEVEL == 4) {
            pLivingEntity.hurt(ModDamageTypes.causeWungusSteroids(pLivingEntity.level().registryAccess()), 10000);
        }

        if(rand <= 750 * CARDIAC_ARREST_LEVEL_MODIFIER && LEVEL_UP_COOLDOWN <= 0) {
            CARDIAC_ARREST_LEVEL++;
            CARDIAC_ARREST_LEVEL_MODIFIER++;
            HEARTBEAT_MODIFIER *= 2;
            LEVEL_UP_COOLDOWN = 1000;
        }

        super.applyEffectTick(pLivingEntity, pAmplifier);
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        if (pDuration == 2000) {
            resetAttributes();
        } else if (pDuration == 500) {
            CARDIAC_ARREST_LEVEL_MODIFIER *= 2;
        } else if (pDuration == 1000) {
            CARDIAC_ARREST_LEVEL_MODIFIER *= 2;
        }
        return true;
    }

    private void resetAttributes() {
        this.HEARTBEAT_INTERVAL = 80;
        this.HEARTBEAT_MODIFIER = 1;
        this.CARDIAC_ARREST_LEVEL = 1;
        this.CARDIAC_ARREST_LEVEL_MODIFIER = 1;
        this.LEVEL_UP_COOLDOWN = 60;
        this.LEVEL_ONE = false;
        this.LEVEL_TWO = false;
        this.LEVEL_THREE = false;
    }
}
