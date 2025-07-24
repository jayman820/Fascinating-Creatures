package net.josh.wungus.particle;

import net.josh.wungus.WungusMod;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, WungusMod.MOD_ID);

    public static final RegistryObject<SimpleParticleType> DARK_SPARKLE_PARTICLES =
            PARTICLE_TYPES.register("dark_sparkle_particles", () -> new SimpleParticleType(true));

    public static final RegistryObject<SimpleParticleType> LIGHT_SPARKLE_PARTICLES =
            PARTICLE_TYPES.register("light_sparkle_particles", () -> new SimpleParticleType(true));

    public static final RegistryObject<SimpleParticleType> BLUE_SPARKLE_PARTICLES =
            PARTICLE_TYPES.register("blue_sparkle_particles", () -> new SimpleParticleType(true));

    public static void register(IEventBus eventBus) {
        PARTICLE_TYPES.register(eventBus);
    }
}
