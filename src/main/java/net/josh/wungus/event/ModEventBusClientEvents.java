package net.josh.wungus.event;

import net.josh.wungus.WungusMod;
import net.josh.wungus.entity.ModEntities;
import net.josh.wungus.entity.client.ModModelLayers;
import net.josh.wungus.entity.client.WungusModel;
import net.josh.wungus.entity.client.WungusRenderer;
import net.josh.wungus.item.ModItems;
import net.josh.wungus.item.custom.armor.model.BBLModel;
import net.josh.wungus.item.custom.armor.model.WungusMaskModel;
import net.josh.wungus.item.custom.armor.model.WungusShoesModel;
import net.josh.wungus.item.custom.armor.provider.CustomArmorModelExtensions;
import net.josh.wungus.item.custom.armor.provider.SimpleModelProvider;
import net.josh.wungus.network.ClientPayloadHandler;
import net.josh.wungus.network.WungdigestionPayload;
import net.josh.wungus.particle.DiarrheaParticle;
import net.josh.wungus.particle.ModParticles;
import net.josh.wungus.particle.SparkleParticle;
import net.josh.wungus.particle.VomitParticle;
import net.josh.wungus.util.ModWoodTypes;
import net.minecraft.client.renderer.Sheets;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.network.event.RegisterClientPayloadHandlersEvent;

@EventBusSubscriber(modid = WungusMod.MOD_ID, value = Dist.CLIENT)
public class ModEventBusClientEvents {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> Sheets.addWoodType(ModWoodTypes.AILANTHUS));
    }

    @SubscribeEvent
    public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModModelLayers.WUNGUS_LAYER, WungusModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.WUNGUS.get(), WungusRenderer::new);
        // The ailanthus signs use the vanilla sign block entities, which already have their renderers
    }

    @SubscribeEvent
    public static void registerClientExtensions(RegisterClientExtensionsEvent event) {
        event.registerItem(new CustomArmorModelExtensions(
                new SimpleModelProvider(WungusMaskModel::createBodyLayer, WungusMaskModel::new)), ModItems.WUNGUS_MASK.get());
        event.registerItem(new CustomArmorModelExtensions(
                new SimpleModelProvider(WungusShoesModel::createBodyLayer, WungusShoesModel::new)), ModItems.WUNGUS_BOOTS.get());
        event.registerItem(new CustomArmorModelExtensions(
                new SimpleModelProvider(BBLModel::createBodyLayer, BBLModel::new)), ModItems.BBL.get());
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

    @SubscribeEvent
    public static void registerPayloadHandlers(RegisterClientPayloadHandlersEvent event) {
        event.register(WungdigestionPayload.TYPE, ClientPayloadHandler::handleWungdigestion);
    }
}
