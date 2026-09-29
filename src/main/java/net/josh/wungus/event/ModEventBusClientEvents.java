package net.josh.wungus.event;

import net.josh.wungus.WungusMod;
import net.josh.wungus.block.entity.ModBlockEntities;
import net.josh.wungus.entity.ModEntities;
import net.josh.wungus.entity.client.BabyWungusModel;
import net.josh.wungus.entity.client.ModModelLayers;
import net.josh.wungus.entity.client.WungusModel;
import net.josh.wungus.entity.client.WungusRenderer;
import net.josh.wungus.item.ModItems;
import net.josh.wungus.item.custom.armor.model.BBLModel;
import net.josh.wungus.item.custom.armor.model.WungusMaskModel;
import net.josh.wungus.item.custom.armor.model.WungusShoesModel;
import net.josh.wungus.item.custom.armor.provider.CustomArmorModelExtensions;
import net.josh.wungus.item.custom.armor.provider.SimpleModelProvider;
import net.josh.wungus.particle.DiarrheaParticle;
import net.josh.wungus.particle.ModParticles;
import net.josh.wungus.particle.SparkleParticle;
import net.josh.wungus.particle.VomitParticle;
import net.josh.wungus.util.ModWoodTypes;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.HangingSignRenderer;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

/** Client setup on the mod event bus. The client game events are in ModClientGameEvents. */
@EventBusSubscriber(modid = WungusMod.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEventBusClientEvents {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> Sheets.addWoodType(ModWoodTypes.AILANTHUS));
    }

    @SubscribeEvent
    public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModModelLayers.WUNGUS_LAYER, WungusModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.WUNGUS_BABY_LAYER, BabyWungusModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.WUNGUS.get(), WungusRenderer::new);

        event.registerBlockEntityRenderer(ModBlockEntities.MOD_SIGN.get(), SignRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.MOD_HANGING_SIGN.get(), HangingSignRenderer::new);
    }

    // The custom 3D armor models
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

    // Near the end of the steroid heart failure the edges of the screen pulse red with the heartbeat
    @SubscribeEvent
    public static void registerGuiLayers(RegisterGuiLayersEvent event) {
        event.registerAbove(VanillaGuiLayers.CAMERA_OVERLAYS, ResourceLocation.fromNamespaceAndPath(WungusMod.MOD_ID, "heart_failure_pulse"),
                ModClientGameEvents::renderRedPulse);
    }
}
