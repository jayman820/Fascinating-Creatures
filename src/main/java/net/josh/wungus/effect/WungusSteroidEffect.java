package net.josh.wungus.effect;

import net.josh.wungus.attachment.SteroidState;
import net.josh.wungus.item.custom.WungusSteroid;
import net.josh.wungus.misc.ModDamageTypes;
import net.josh.wungus.sound.ModSounds;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.attachment.AttachmentType;

import java.util.function.Supplier;

/**
 * Heart failure from a steroid dose. It is timed to the length of the dose:
 * - at the start a strong boost (strength and the steroid's own effect),
 * - the heartbeat speeds up (and gets louder and higher) the whole time,
 * - the screen starts shaking and shakes harder and harder (Heart Palpitations),
 * - late in the dose slowness, mining fatigue and weakness,
 * - and near the end the heart gives out.
 * The progress is stored per entity (SteroidState), not in this class: one effect object is shared by every entity.
 */
public class WungusSteroidEffect extends MobEffect {
    // Length of a dose from a steroid item, used if the length can't be read from the effect
    public static final int DEFAULT_DOSE_TICKS = 2000;

    // Heartbeat interval in ticks at the start and at the end of the dose (20 ticks = 1 second)
    private static final int FIRST_HEARTBEAT_INTERVAL = 30;
    private static final int LAST_HEARTBEAT_INTERVAL = 4;
    // Progress (0 to 1) at which the screen shake gets stronger, one step per entry
    private static final float[] SHAKE_STEPS = {0.2F, 0.45F, 0.65F, 0.8F, 0.9F};
    private static final float WEAKNESS_PROGRESS = 0.7F;
    private static final float DEATH_PROGRESS = 0.95F;

    private final WungusSteroid.Type type;
    private final Supplier<AttachmentType<SteroidState>> state;

    public WungusSteroidEffect(MobEffectCategory pCategory, int pColor, WungusSteroid.Type type, Supplier<AttachmentType<SteroidState>> state) {
        super(pCategory, pColor);
        this.type = type;
        this.state = state;
    }

    // Called when the effect is newly added (not when an active dose is refreshed): start a new heart failure
    @Override
    public void onEffectAdded(LivingEntity pLivingEntity, int pAmplifier) {
        super.onEffectAdded(pLivingEntity, pAmplifier);
        int duration = DEFAULT_DOSE_TICKS;
        for (MobEffectInstance instance : pLivingEntity.getActiveEffects()) {
            if (instance.getEffect().value() == this && !instance.isInfiniteDuration()) {
                duration = instance.getDuration();
            }
        }
        pLivingEntity.setData(this.state, new SteroidState(duration));
    }

    @Override
    public boolean applyEffectTick(ServerLevel pLevel, LivingEntity pLivingEntity, int pAmplifier) {
        SteroidState s = pLivingEntity.getData(this.state);
        s.ticksActive++;
        float progress = s.progress();

        if (s.stage < 1) {
            pLivingEntity.addEffect(new MobEffectInstance(MobEffects.STRENGTH, 500, 10));
            switch (this.type) {
                case HEALTH -> pLivingEntity.addEffect(new MobEffectInstance(MobEffects.RESISTANCE, 500, 10));
                case SPEED -> pLivingEntity.addEffect(new MobEffectInstance(MobEffects.SPEED, 500, 5));
                case JUMP -> pLivingEntity.addEffect(new MobEffectInstance(MobEffects.JUMP_BOOST, 500, 5));
            }
            s.stage = 1;
        }
        if (s.stage < 2 && progress >= WEAKNESS_PROGRESS) {
            pLivingEntity.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 1000, 10));
            pLivingEntity.addEffect(new MobEffectInstance(MobEffects.MINING_FATIGUE, 1000, 10));
            pLivingEntity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 1000, 10));
            s.stage = 2;
        }

        // The heart beats faster and faster
        if (--s.nextHeartbeat <= 0) {
            // Played through the level so the entity itself hears it too (entity.playSound skips the player itself)
            pLevel.playSound(null, pLivingEntity.getX(), pLivingEntity.getY(), pLivingEntity.getZ(), ModSounds.HEARTBEAT.get(),
                    pLivingEntity.getSoundSource(), Mth.lerp(progress, 0.7F, 1.3F), Mth.lerp(progress, 0.9F, 1.3F));
            s.nextHeartbeat = Math.round(Mth.lerp(progress * progress, FIRST_HEARTBEAT_INTERVAL, LAST_HEARTBEAT_INTERVAL));
        }

        // Screen shake, refreshed often with a short duration so it stops soon after the heart failure ends
        int shake = shakeLevel(progress);
        if (shake >= 0 && s.ticksActive % 10 == 0) {
            pLivingEntity.addEffect(new MobEffectInstance(ModEffects.HEART_PALPITATIONS_EFFECT, 30, shake, false, false, false));
        }

        if (progress >= DEATH_PROGRESS) {
            // The steroids damage type bypasses armor, resistance and protection (see data/minecraft/tags/damage_type)
            pLivingEntity.hurtServer(pLevel, ModDamageTypes.causeWungusSteroids(pLevel.registryAccess()), 10000);
        }
        return true;
    }

    /** -1 for no shaking yet, otherwise the Heart Palpitations amplifier (stronger shaking). */
    private static int shakeLevel(float progress) {
        int level = -1;
        for (float step : SHAKE_STEPS) {
            if (progress >= step) {
                level++;
            }
        }
        return level;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int pDuration, int pAmplifier) {
        return true;
    }
}
