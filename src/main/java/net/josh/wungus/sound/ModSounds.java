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


    public static RegistryObject<SoundEvent> registerSoundEvents(String name) {
        ResourceLocation id = new ResourceLocation(WungusMod.MOD_ID, name);
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(id));
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}
