package net.josh.wungus.attachment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/**
 * Progress of one steroid heart failure for one entity. Stored on the entity (see ModAttachments) and saved with it,
 * instead of in the effect: there is only one effect object shared by every entity that has the effect.
 */
public class SteroidState {
    public static final Codec<SteroidState> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            Codec.INT.fieldOf("ticks_active").forGetter(s -> s.ticksActive),
            Codec.INT.fieldOf("total_ticks").forGetter(s -> s.totalTicks),
            Codec.INT.fieldOf("next_heartbeat").forGetter(s -> s.nextHeartbeat),
            Codec.INT.fieldOf("stage").forGetter(s -> s.stage)
    ).apply(inst, SteroidState::new));

    public int ticksActive = 0;
    // Length of the dose, the heart failure is timed to it
    public int totalTicks;
    public int nextHeartbeat = 0;
    // Which one time effects were already given (the boost at the start, the weakness later)
    public int stage = 0;

    public SteroidState(int totalTicks) {
        this.totalTicks = totalTicks;
    }

    private SteroidState(int ticksActive, int totalTicks, int nextHeartbeat, int stage) {
        this.ticksActive = ticksActive;
        this.totalTicks = totalTicks;
        this.nextHeartbeat = nextHeartbeat;
        this.stage = stage;
    }

    /** 0 at the start of the dose, 1 at the end. */
    public float progress() {
        return Math.min(1.0F, (float) this.ticksActive / Math.max(1, this.totalTicks));
    }
}
