package net.josh.wungus.effect;

import net.josh.wungus.WungusMod;
import net.josh.wungus.item.custom.WungusSteroid;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;

import java.util.Locale;

/**
 * Progress of one steroid heart failure for one entity, stored in the entity's persistent data (saved with it).
 * It can't be stored in the effect: there is only one effect object shared by every entity that has the effect.
 */
public class SteroidState {
    public int ticksActive;
    // Length of the dose, the heart failure is timed to it
    public int totalTicks;
    public int nextHeartbeat;
    // Which one time effects were already given (the boost at the start, the weakness later)
    public int stage;

    private static String key(WungusSteroid.Type type) {
        return WungusMod.MOD_ID + ":steroid_" + type.name().toLowerCase(Locale.ROOT);
    }

    /** Starts a new heart failure for a dose of the given length. */
    public static void start(LivingEntity entity, WungusSteroid.Type type, int totalTicks) {
        SteroidState state = new SteroidState();
        state.totalTicks = totalTicks;
        state.save(entity, type);
    }

    public static SteroidState load(LivingEntity entity, WungusSteroid.Type type) {
        CompoundTag tag = entity.getPersistentData().getCompound(key(type));
        SteroidState state = new SteroidState();
        state.ticksActive = tag.getInt("TicksActive");
        state.totalTicks = tag.contains("TotalTicks") ? tag.getInt("TotalTicks") : WungusSteroidEffect.DEFAULT_DOSE_TICKS;
        state.nextHeartbeat = tag.getInt("NextHeartbeat");
        state.stage = tag.getInt("Stage");
        return state;
    }

    public void save(LivingEntity entity, WungusSteroid.Type type) {
        CompoundTag tag = new CompoundTag();
        tag.putInt("TicksActive", this.ticksActive);
        tag.putInt("TotalTicks", this.totalTicks);
        tag.putInt("NextHeartbeat", this.nextHeartbeat);
        tag.putInt("Stage", this.stage);
        entity.getPersistentData().put(key(type), tag);
    }

    /** 0 at the start of the dose, 1 at the end. */
    public float progress() {
        return Math.min(1.0F, (float) this.ticksActive / Math.max(1, this.totalTicks));
    }
}
