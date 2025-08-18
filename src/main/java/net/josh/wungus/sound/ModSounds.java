package net.josh.wungus.sound;

import net.josh.wungus.WungusMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, WungusMod.MOD_ID);

    public static final RegistryObject<SoundEvent> BURP = registerSoundEvents("burp");
    public static final RegistryObject<SoundEvent> FART = registerSoundEvents("fart");
    public static final RegistryObject<SoundEvent> WUNGUS_AMBIENT = registerSoundEvents("wungus_ambient");
    public static final RegistryObject<SoundEvent> WUNGUS_HURT = registerSoundEvents("wungus_hurt");
    public static final RegistryObject<SoundEvent> WUNGUS_DEATH = registerSoundEvents("wungus_death");
    public static final RegistryObject<SoundEvent> PRATTLING_WUNGUS_1 = registerSoundEvents("prattling_wungus_1");
    public static final RegistryObject<SoundEvent> PRATTLING_WUNGUS_2 = registerSoundEvents("prattling_wungus_2");
    public static final RegistryObject<SoundEvent> PRATTLING_WUNGUS_3 = registerSoundEvents("prattling_wungus_3");
    public static final RegistryObject<SoundEvent> PRATTLING_WUNGUS_4 = registerSoundEvents("prattling_wungus_4");
    public static final RegistryObject<SoundEvent> WUNGUS_STATUE = registerSoundEvents("wungus_statue");
    public static final RegistryObject<SoundEvent> HEARTBEAT = registerSoundEvents("heartbeat");
    public static final RegistryObject<SoundEvent> MANGUNGUS_AMBIENT = registerSoundEvents("mangungus_ambient");

    // Wungus Game 2 sound events
    public static final RegistryObject<SoundEvent> LANDMINE_TRIGGER = registerSoundEvents("landmine_trigger");
    public static final RegistryObject<SoundEvent> RED_LIGHT = registerSoundEvents("red_light");
    public static final RegistryObject<SoundEvent> GREEN_LIGHT = registerSoundEvents("green_light");
    public static final RegistryObject<SoundEvent> MINGLE = registerSoundEvents("mingle");
    public static final RegistryObject<SoundEvent> PLAYER_1_ELIM = registerSoundEvents("player_1_elim");
    public static final RegistryObject<SoundEvent> PLAYER_2_ELIM = registerSoundEvents("player_2_elim");
    public static final RegistryObject<SoundEvent> PLAYER_3_ELIM = registerSoundEvents("player_3_elim");
    public static final RegistryObject<SoundEvent> PLAYER_4_ELIM = registerSoundEvents("player_4_elim");
    public static final RegistryObject<SoundEvent> PLAYER_5_ELIM = registerSoundEvents("player_5_elim");
    public static final RegistryObject<SoundEvent> PLAYER_6_ELIM = registerSoundEvents("player_6_elim");
    public static final RegistryObject<SoundEvent> PLAYER_7_ELIM = registerSoundEvents("player_7_elim");
    public static final RegistryObject<SoundEvent> PLAYER_8_ELIM = registerSoundEvents("player_8_elim");
    public static final RegistryObject<SoundEvent> PLAYER_9_ELIM = registerSoundEvents("player_9_elim");
    public static final RegistryObject<SoundEvent> PLAYER_10_ELIM = registerSoundEvents("player_10_elim");
    public static final RegistryObject<SoundEvent> PLAYER_11_ELIM = registerSoundEvents("player_11_elim");
    public static final RegistryObject<SoundEvent> PLAYER_12_ELIM = registerSoundEvents("player_12_elim");
    public static final RegistryObject<SoundEvent> PLAYER_13_ELIM = registerSoundEvents("player_13_elim");
    public static final RegistryObject<SoundEvent> PLAYER_14_ELIM = registerSoundEvents("player_14_elim");
    public static final RegistryObject<SoundEvent> PLAYER_15_ELIM = registerSoundEvents("player_15_elim");
    public static final RegistryObject<SoundEvent> SMILE = registerSoundEvents("smile");

    public static final RegistryObject<SoundEvent> CONCERTO = registerSoundEvents("concerto");
    public static final RegistryObject<SoundEvent> MINGLE_DISC = registerSoundEvents("mingle_disc");
    public static final RegistryObject<SoundEvent> MOON = registerSoundEvents("moon");
    public static final RegistryObject<SoundEvent> WUNGUS_RACING = registerSoundEvents("wungus-racing");
    public static final RegistryObject<SoundEvent> JUMP_ROPE_DISC = registerSoundEvents("jump-rope-disc");

    public static final RegistryObject<SoundEvent> JUMP_ROPE_INSTRUCTIONS = registerSoundEvents("jump-rope");
    public static final RegistryObject<SoundEvent> RED_LIGHT_GREEN_LIGHT_INSTRUCTIONS = registerSoundEvents("red-light-green-light");
    public static final RegistryObject<SoundEvent> MINGLE_INSTRUCTIONS = registerSoundEvents("mingle-instructions");
    public static final RegistryObject<SoundEvent> TNT_STEPPING_STONES_INSTRUCTIONS = registerSoundEvents("tnt-stepping-stones");
    public static final RegistryObject<SoundEvent> NUMBERS_INSTRUCTIONS = registerSoundEvents("numbers");
    public static final RegistryObject<SoundEvent> FLOWERS_INSTRUCTIONS = registerSoundEvents("flowers");
    public static final RegistryObject<SoundEvent> CRAFTING_INSTRUCTIONS = registerSoundEvents("crafting");
    public static final RegistryObject<SoundEvent> WUNGER_GAMES_INSTRUCTIONS = registerSoundEvents("wunger-games");
    public static final RegistryObject<SoundEvent> WUNGUS_RACING_INSTRUCTIONS = registerSoundEvents("wungus-racing-instructions");
    public static final RegistryObject<SoundEvent> SAND_DALGONA_INSTRUCTIONS = registerSoundEvents("sand-dalgona");

    public static final RegistryObject<SoundEvent> CONGRATULATIONS_LONG = registerSoundEvents("congratulations-long");
    public static final RegistryObject<SoundEvent> CONGRATULATIONS_SHORT = registerSoundEvents("congratulations-short");



    public static RegistryObject<SoundEvent> registerSoundEvents(String name) {
        ResourceLocation id = new ResourceLocation(WungusMod.MOD_ID, name);
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(id));
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}
