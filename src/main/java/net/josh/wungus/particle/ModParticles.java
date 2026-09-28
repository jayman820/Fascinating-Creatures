package net.josh.wungus.particle;

import net.josh.wungus.WungusMod;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(Registries.PARTICLE_TYPE, WungusMod.MOD_ID);

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> DARK_SPARKLE_PARTICLES =
            PARTICLE_TYPES.register("dark_sparkle_particles", () -> new SimpleParticleType(true));

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> LIGHT_SPARKLE_PARTICLES =
            PARTICLE_TYPES.register("light_sparkle_particles", () -> new SimpleParticleType(true));

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> BLUE_SPARKLE_PARTICLES =
            PARTICLE_TYPES.register("blue_sparkle_particles", () -> new SimpleParticleType(true));

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> DIARRHEA_PARTICLE_1 =
            PARTICLE_TYPES.register("diarrhea1", () -> new SimpleParticleType(true));

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> DIARRHEA_PARTICLE_2 =
            PARTICLE_TYPES.register("diarrhea2", () -> new SimpleParticleType(true));

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> VOMIT_PARTICLE_1 =
            PARTICLE_TYPES.register("vomit1", () -> new SimpleParticleType(true));

    public static void register(IEventBus eventBus) {
        PARTICLE_TYPES.register(eventBus);
    }
}
