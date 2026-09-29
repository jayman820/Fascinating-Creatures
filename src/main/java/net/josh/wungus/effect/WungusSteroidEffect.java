package net.josh.wungus.effect;

import net.josh.wungus.attachment.SteroidState;
import net.josh.wungus.item.custom.WungusSteroid;
import net.josh.wungus.misc.ModDamageTypes;
import net.josh.wungus.sound.ModSounds;
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
 * - the screen starts shaking a little and slowly shakes harder, and near the end the screen edges pulse red
 *   with the heartbeat (Heart Palpitations),
 * - late in the dose slowness, mining fatigue and weakness,
 * - and near the end the heart gives out.
 * The progress is stored per entity (SteroidState), not in this class: one effect object is shared by every entity.
 * A new heart failure starts when the effect is newly added (see ModEvents#onEffectAdded).
 */
public class WungusSteroidEffect extends MobEffect {
    // Length of a dose from a steroid item, used if the length isn't known
    public static final int DEFAULT_DOSE_TICKS = 2000;

    // The heartbeat speed and the screen shake steps are in HeartPalpitationsEffect (shared with the client)
    private static final float WEAKNESS_PROGRESS = 0.7F;
    private static final float DEATH_PROGRESS = 0.95F;

    private final WungusSteroid.Type type;
    private final Supplier<AttachmentType<SteroidState>> state;

    public WungusSteroidEffect(MobEffectCategory pCategory, int pColor, WungusSteroid.Type type, Supplier<AttachmentType<SteroidState>> state) {
        super(pCategory, pColor);
        this.type = type;
        this.state = state;
    }

    /** Starts a new heart failure for a dose of the given length. */
    public void startHeartFailure(LivingEntity pLivingEntity, int pDuration) {
        pLivingEntity.setData(this.state, new SteroidState(pDuration));
    }

    // The effect ticks on both sides, everything happens on the server
    @Override
    public boolean applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        if (pLivingEntity.level().isClientSide()) {
            return true;
        }
        SteroidState s = pLivingEntity.getData(this.state);
        s.ticksActive++;
        float progress = s.progress();

        if (s.stage < 1) {
            pLivingEntity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 500, 10));
            switch (this.type) {
                case HEALTH -> pLivingEntity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 500, 10));
                case SPEED -> pLivingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 500, 5));
                case JUMP -> pLivingEntity.addEffect(new MobEffectInstance(MobEffects.JUMP, 500, 5));
            }
            s.stage = 1;
        }
        if (s.stage < 2 && progress >= WEAKNESS_PROGRESS) {
            pLivingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 1000, 10));
            pLivingEntity.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 1000, 10));
            pLivingEntity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 1000, 10));
            s.stage = 2;
        }

        // The heart beats faster and faster
        if (--s.nextHeartbeat <= 0) {
            // Played through the level so the entity itself hears it too (entity.playSound skips the player itself)
            pLivingEntity.level().playSound(null, pLivingEntity.getX(), pLivingEntity.getY(), pLivingEntity.getZ(), ModSounds.HEARTBEAT.get(),
                    pLivingEntity.getSoundSource(), Mth.lerp(progress, 0.7F, 1.3F), Mth.lerp(progress, 0.9F, 1.3F));
            s.nextHeartbeat = Math.round(HeartPalpitationsEffect.heartbeatInterval(progress));
        }

        // Screen shake (and red pulse near the end), getting a bit stronger in small steps. Refreshed often with a
        // short duration so it stops soon after the heart failure ends.
        int shake = HeartPalpitationsEffect.levelFor(progress);
        if (shake >= 0 && s.ticksActive % 10 == 0) {
            pLivingEntity.addEffect(new MobEffectInstance(ModEffects.holder(ModEffects.HEART_PALPITATIONS_EFFECT), 30, shake, false, false, false));
        }

        if (progress >= DEATH_PROGRESS) {
            // The steroids damage type bypasses armor, resistance and protection (see data/minecraft/tags/damage_type)
            pLivingEntity.hurt(ModDamageTypes.causeWungusSteroids(pLivingEntity.level().registryAccess()), 10000);
        }
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int pDuration, int pAmplifier) {
        return true;
    }
}
