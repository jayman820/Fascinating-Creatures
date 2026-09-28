package net.josh.wungus.effect;

import net.josh.wungus.attachment.SteroidState;
import net.josh.wungus.item.custom.WungusSteroid;
import net.josh.wungus.misc.ModDamageTypes;
import net.josh.wungus.sound.ModSounds;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.attachment.AttachmentType;

import java.util.function.Supplier;

/**
 * The heart attack runs in 4 levels: 1 = boost, 2 = nausea, 3 = slowness/weakness, 4 = death.
 * The progress is stored per entity (SteroidState), not in this class: one effect object is shared by every entity.
 */
public class WungusSteroidEffect extends MobEffect {
    // Minimum ticks between two levels. The dose lasts 2000 ticks, so 3 level ups have to fit in that
    // (60 + 2 * 500 ticks + the random rolls, so the heart attack comes after roughly 55 seconds).
    private static final int LEVEL_UP_COOLDOWN = 500;

    private final WungusSteroid.Type type;
    private final Supplier<AttachmentType<SteroidState>> state;

    public WungusSteroidEffect(MobEffectCategory pCategory, int pColor, WungusSteroid.Type type, Supplier<AttachmentType<SteroidState>> state) {
        super(pCategory, pColor);
        this.type = type;
        this.state = state;
    }

    // Called when the effect is newly added (not when an active dose is refreshed): start a new heart attack
    @Override
    public void onEffectAdded(LivingEntity pLivingEntity, int pAmplifier) {
        super.onEffectAdded(pLivingEntity, pAmplifier);
        pLivingEntity.setData(this.state, new SteroidState());
    }

    @Override
    public boolean applyEffectTick(ServerLevel pLevel, LivingEntity pLivingEntity, int pAmplifier) {
        SteroidState s = pLivingEntity.getData(this.state);
        s.ticksActive++;
        s.heartbeatInterval--;
        s.levelUpCooldown--;

        // Level ups get more likely later in the dose (was done at 1000 and 500 ticks left of the 2000 tick dose)
        if (s.ticksActive == 1000 || s.ticksActive == 1500) {
            s.cardiacArrestLevelModifier *= 2;
        }

        if (s.heartbeatInterval <= 0) {
            pLivingEntity.playSound(ModSounds.HEARTBEAT.get());
            s.heartbeatInterval = 80 / s.heartbeatModifier;
        }

        if (s.appliedLevel < 1 && s.cardiacArrestLevel == 1) {
            pLivingEntity.addEffect(new MobEffectInstance(MobEffects.STRENGTH, 500, 10));
            switch (this.type) {
                case HEALTH -> pLivingEntity.addEffect(new MobEffectInstance(MobEffects.RESISTANCE, 500, 10));
                case SPEED -> pLivingEntity.addEffect(new MobEffectInstance(MobEffects.SPEED, 500, 5));
                case JUMP -> pLivingEntity.addEffect(new MobEffectInstance(MobEffects.JUMP_BOOST, 500, 5));
            }
            s.appliedLevel = 1;
        } else if (s.appliedLevel < 2 && s.cardiacArrestLevel == 2) {
            pLivingEntity.addEffect(new MobEffectInstance(MobEffects.NAUSEA, 1800, 1));
            s.appliedLevel = 2;
        } else if (s.appliedLevel < 3 && s.cardiacArrestLevel == 3) {
            pLivingEntity.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 1000, 10));
            pLivingEntity.addEffect(new MobEffectInstance(MobEffects.MINING_FATIGUE, 1000, 10));
            pLivingEntity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 1000, 10));
            s.appliedLevel = 3;
        } else if (s.cardiacArrestLevel >= 4) {
            // The steroids damage type bypasses armor, resistance and protection (see data/minecraft/tags/damage_type)
            pLivingEntity.hurtServer(pLevel, ModDamageTypes.causeWungusSteroids(pLevel.registryAccess()), 10000);
            return true;
        }

        int roll = pLivingEntity.getRandom().nextInt(10000) + 1;
        if (roll <= 750 * s.cardiacArrestLevelModifier && s.levelUpCooldown <= 0) {
            s.cardiacArrestLevel++;
            s.cardiacArrestLevelModifier++;
            s.heartbeatModifier *= 2;
            s.levelUpCooldown = LEVEL_UP_COOLDOWN;
        }

        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int pDuration, int pAmplifier) {
        return true;
    }
}
