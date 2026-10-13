package net.josh.wungus.sound;

import net.josh.wungus.WungusMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(Registries.SOUND_EVENT, WungusMod.MOD_ID);

    public static final DeferredHolder<SoundEvent, SoundEvent> BURP = registerSoundEvents("burp");
    public static final DeferredHolder<SoundEvent, SoundEvent> FART = registerSoundEvents("fart");
    public static final DeferredHolder<SoundEvent, SoundEvent> WUNGUS_AMBIENT = registerSoundEvents("wungus_ambient");
    public static final DeferredHolder<SoundEvent, SoundEvent> WUNGUS_HURT = registerSoundEvents("wungus_hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> WUNGUS_DEATH = registerSoundEvents("wungus_death");
    public static final DeferredHolder<SoundEvent, SoundEvent> PRATTLING_WUNGUS_1 = registerSoundEvents("prattling_wungus_1");
    public static final DeferredHolder<SoundEvent, SoundEvent> PRATTLING_WUNGUS_2 = registerSoundEvents("prattling_wungus_2");
    public static final DeferredHolder<SoundEvent, SoundEvent> PRATTLING_WUNGUS_3 = registerSoundEvents("prattling_wungus_3");
    public static final DeferredHolder<SoundEvent, SoundEvent> PRATTLING_WUNGUS_4 = registerSoundEvents("prattling_wungus_4");
    public static final DeferredHolder<SoundEvent, SoundEvent> WUNGUS_STATUE = registerSoundEvents("wungus_statue");
    public static final DeferredHolder<SoundEvent, SoundEvent> HEARTBEAT = registerSoundEvents("heartbeat");
    public static final DeferredHolder<SoundEvent, SoundEvent> MANGUNGUS_AMBIENT = registerSoundEvents("mangungus_ambient");

    // Wungus Game 2 sound events
    public static final DeferredHolder<SoundEvent, SoundEvent> LANDMINE_TRIGGER = registerSoundEvents("landmine_trigger");
    public static final DeferredHolder<SoundEvent, SoundEvent> RED_LIGHT = registerSoundEvents("red_light");
    public static final DeferredHolder<SoundEvent, SoundEvent> GREEN_LIGHT = registerSoundEvents("green_light");
    public static final DeferredHolder<SoundEvent, SoundEvent> MINGLE = registerSoundEvents("mingle");
    public static final DeferredHolder<SoundEvent, SoundEvent> PLAYER_1_ELIM = registerSoundEvents("player_1_elim");
    public static final DeferredHolder<SoundEvent, SoundEvent> PLAYER_2_ELIM = registerSoundEvents("player_2_elim");
    public static final DeferredHolder<SoundEvent, SoundEvent> PLAYER_3_ELIM = registerSoundEvents("player_3_elim");
    public static final DeferredHolder<SoundEvent, SoundEvent> PLAYER_4_ELIM = registerSoundEvents("player_4_elim");
    public static final DeferredHolder<SoundEvent, SoundEvent> PLAYER_5_ELIM = registerSoundEvents("player_5_elim");
    public static final DeferredHolder<SoundEvent, SoundEvent> PLAYER_6_ELIM = registerSoundEvents("player_6_elim");
    public static final DeferredHolder<SoundEvent, SoundEvent> PLAYER_7_ELIM = registerSoundEvents("player_7_elim");
    public static final DeferredHolder<SoundEvent, SoundEvent> PLAYER_8_ELIM = registerSoundEvents("player_8_elim");
    public static final DeferredHolder<SoundEvent, SoundEvent> PLAYER_9_ELIM = registerSoundEvents("player_9_elim");
    public static final DeferredHolder<SoundEvent, SoundEvent> PLAYER_10_ELIM = registerSoundEvents("player_10_elim");
    public static final DeferredHolder<SoundEvent, SoundEvent> PLAYER_11_ELIM = registerSoundEvents("player_11_elim");
    public static final DeferredHolder<SoundEvent, SoundEvent> PLAYER_12_ELIM = registerSoundEvents("player_12_elim");
    public static final DeferredHolder<SoundEvent, SoundEvent> PLAYER_13_ELIM = registerSoundEvents("player_13_elim");
    public static final DeferredHolder<SoundEvent, SoundEvent> PLAYER_14_ELIM = registerSoundEvents("player_14_elim");
    public static final DeferredHolder<SoundEvent, SoundEvent> PLAYER_15_ELIM = registerSoundEvents("player_15_elim");
    public static final DeferredHolder<SoundEvent, SoundEvent> SMILE = registerSoundEvents("smile");
    public static final DeferredHolder<SoundEvent, SoundEvent> SDIYBT = registerSoundEvents("sdiybt");

    public static final DeferredHolder<SoundEvent, SoundEvent> CONCERTO = registerSoundEvents("concerto");
    public static final DeferredHolder<SoundEvent, SoundEvent> MINGLE_DISC = registerSoundEvents("mingle_disc");
    public static final DeferredHolder<SoundEvent, SoundEvent> MOON = registerSoundEvents("moon");
    public static final DeferredHolder<SoundEvent, SoundEvent> WUNGUS_RACING = registerSoundEvents("wungus-racing");
    public static final DeferredHolder<SoundEvent, SoundEvent> JUMP_ROPE_DISC = registerSoundEvents("jump-rope-disc");

    public static final DeferredHolder<SoundEvent, SoundEvent> JUMP_ROPE_INSTRUCTIONS = registerSoundEvents("jump-rope");
    public static final DeferredHolder<SoundEvent, SoundEvent> RED_LIGHT_GREEN_LIGHT_INSTRUCTIONS = registerSoundEvents("red-light-green-light");
    public static final DeferredHolder<SoundEvent, SoundEvent> MINGLE_INSTRUCTIONS = registerSoundEvents("mingle-instructions");
    public static final DeferredHolder<SoundEvent, SoundEvent> TNT_STEPPING_STONES_INSTRUCTIONS = registerSoundEvents("tnt-stepping-stones");
    public static final DeferredHolder<SoundEvent, SoundEvent> NUMBERS_INSTRUCTIONS = registerSoundEvents("numbers");
    public static final DeferredHolder<SoundEvent, SoundEvent> FLOWERS_INSTRUCTIONS = registerSoundEvents("flowers");
    public static final DeferredHolder<SoundEvent, SoundEvent> CRAFTING_INSTRUCTIONS = registerSoundEvents("crafting");
    public static final DeferredHolder<SoundEvent, SoundEvent> WUNGER_GAMES_INSTRUCTIONS = registerSoundEvents("wunger-games");
    public static final DeferredHolder<SoundEvent, SoundEvent> WUNGUS_RACING_INSTRUCTIONS = registerSoundEvents("wungus-racing-instructions");
    public static final DeferredHolder<SoundEvent, SoundEvent> SAND_DALGONA_INSTRUCTIONS = registerSoundEvents("sand-dalgona");

    public static final DeferredHolder<SoundEvent, SoundEvent> CONGRATULATIONS_LONG = registerSoundEvents("congratulations-long");
    public static final DeferredHolder<SoundEvent, SoundEvent> CONGRATULATIONS_SHORT = registerSoundEvents("congratulations-short");
    public static final DeferredHolder<SoundEvent, SoundEvent> DR_SONG = registerSoundEvents("dr-song");

    public static DeferredHolder<SoundEvent, SoundEvent> registerSoundEvents(String name) {
        Identifier id = Identifier.fromNamespaceAndPath(WungusMod.MOD_ID, name);
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(id));
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}
