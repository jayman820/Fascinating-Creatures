package net.josh.wungus.attachment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/**
 * Progress of one steroid heart attack for one entity. Stored on the entity (see ModAttachments) and saved with it,
 * instead of in the effect: there is only one effect object shared by every entity that has the effect.
 */
public class SteroidState {
    public static final MapCodec<SteroidState> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Codec.INT.fieldOf("ticks_active").forGetter(s -> s.ticksActive),
            Codec.INT.fieldOf("heartbeat_interval").forGetter(s -> s.heartbeatInterval),
            Codec.INT.fieldOf("heartbeat_modifier").forGetter(s -> s.heartbeatModifier),
            Codec.INT.fieldOf("cardiac_arrest_level").forGetter(s -> s.cardiacArrestLevel),
            Codec.DOUBLE.fieldOf("cardiac_arrest_level_modifier").forGetter(s -> s.cardiacArrestLevelModifier),
            Codec.INT.fieldOf("level_up_cooldown").forGetter(s -> s.levelUpCooldown),
            Codec.INT.fieldOf("applied_level").forGetter(s -> s.appliedLevel)
    ).apply(inst, SteroidState::new));

    public int ticksActive = 0;
    public int heartbeatInterval = 80;
    public int heartbeatModifier = 1;
    public int cardiacArrestLevel = 1;
    public double cardiacArrestLevelModifier = 1;
    public int levelUpCooldown = 60;
    // Highest level whose side effects were already given (replaces LEVEL_ONE/TWO/THREE)
    public int appliedLevel = 0;

    public SteroidState() {}

    private SteroidState(int ticksActive, int heartbeatInterval, int heartbeatModifier, int cardiacArrestLevel,
                         double cardiacArrestLevelModifier, int levelUpCooldown, int appliedLevel) {
        this.ticksActive = ticksActive;
        this.heartbeatInterval = heartbeatInterval;
        this.heartbeatModifier = heartbeatModifier;
        this.cardiacArrestLevel = cardiacArrestLevel;
        this.cardiacArrestLevelModifier = cardiacArrestLevelModifier;
        this.levelUpCooldown = levelUpCooldown;
        this.appliedLevel = appliedLevel;
    }
}
