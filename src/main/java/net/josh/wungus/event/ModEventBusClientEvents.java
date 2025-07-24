package net.josh.wungus.event;
import net.josh.wungus.WungusMod;
import net.josh.wungus.entity.client.ModModelLayers;
import net.josh.wungus.entity.client.WungusModel;
import net.josh.wungus.particle.DiarrheaParticle;
import net.josh.wungus.particle.ModParticles;
import net.josh.wungus.particle.SparkleParticle;
import net.josh.wungus.particle.VomitParticle;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = WungusMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEventBusClientEvents {
    @SubscribeEvent
    public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModModelLayers.WUNGUS_LAYER, WungusModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerParticleFactories(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ModParticles.DARK_SPARKLE_PARTICLES.get(), SparkleParticle.Provider::new);
        event.registerSpriteSet(ModParticles.LIGHT_SPARKLE_PARTICLES.get(), SparkleParticle.Provider::new);
        event.registerSpriteSet(ModParticles.BLUE_SPARKLE_PARTICLES.get(), SparkleParticle.Provider::new);
        event.registerSpriteSet(ModParticles.DIARRHEA_PARTICLE_1.get(), DiarrheaParticle.Provider::new);
        event.registerSpriteSet(ModParticles.DIARRHEA_PARTICLE_2.get(), DiarrheaParticle.Provider::new);
        event.registerSpriteSet(ModParticles.VOMIT_PARTICLE_1.get(), VomitParticle.Provider::new);
    }

}
