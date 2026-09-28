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

    public static DeferredHolder<SoundEvent, SoundEvent> registerSoundEvents(String name) {
        Identifier id = Identifier.fromNamespaceAndPath(WungusMod.MOD_ID, name);
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(id));
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}
