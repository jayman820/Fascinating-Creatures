package net.josh.wungus.event;

import net.josh.wungus.WungusMod;
import net.josh.wungus.effect.HeartPalpitationsEffect;
import net.josh.wungus.effect.ModEffects;
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
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.client.event.ViewportEvent;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
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

    // Heart Palpitations (from the steroid heart failure) shakes the camera. It starts barely noticeable and slowly
    // gets stronger. Scaled by the "Distortion Effects" accessibility slider, like nausea.
    @SubscribeEvent
    public static void shakeCamera(ViewportEvent.ComputeCameraAngles event) {
        Minecraft minecraft = Minecraft.getInstance();
        int level = palpitationsLevel(minecraft.player);
        if (level < 0) {
            return;
        }

        float intensity = (float) (level + 1) / (HeartPalpitationsEffect.MAX_LEVEL + 1);   // 0.05 to 1
        // About 0.05 degrees at the start up to about 1 at the end (the waves below add up to at most 1.5 times this)
        float strength = (0.05F + 1.0F * intensity * intensity) * minecraft.options.screenEffectScale().get().floatValue();
        // Slow sway at first, faster trembling later
        float time = (minecraft.player.tickCount + (float) event.getPartialTick()) * Mth.lerp(intensity, 0.5F, 1.2F);
        event.setYaw(event.getYaw() + (Mth.sin(time * 1.3F) + 0.5F * Mth.sin(time * 2.9F)) * strength);
        event.setPitch(event.getPitch() + (Mth.cos(time * 1.7F) + 0.5F * Mth.sin(time * 3.1F)) * strength);
        event.setRoll(event.getRoll() + Mth.sin(time * 2.3F) * strength);
    }

    // Near the end of the heart failure the edges of the screen pulse red with the heartbeat
    private static final float RED_PULSE_START = 0.7F;

    @SubscribeEvent
    public static void registerGuiLayers(RegisterGuiLayersEvent event) {
        event.registerAbove(VanillaGuiLayers.CAMERA_OVERLAYS, Identifier.fromNamespaceAndPath(WungusMod.MOD_ID, "heart_failure_pulse"),
                ModEventBusClientEvents::renderRedPulse);
    }

    private static void renderRedPulse(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
        Minecraft minecraft = Minecraft.getInstance();
        int level = palpitationsLevel(minecraft.player);
        if (level < 0) {
            return;
        }
        float progress = HeartPalpitationsEffect.progressFor(level);
        if (progress < RED_PULSE_START) {
            return;
        }

        // A quick flash on every beat that fades until the next one, stronger towards the end
        float interval = HeartPalpitationsEffect.heartbeatInterval(progress);
        float time = minecraft.player.tickCount + deltaTracker.getGameTimeDeltaPartialTick(false);
        float phase = (time % interval) / interval;
        float pulse = (1.0F - phase) * (1.0F - phase);
        float strength = Mth.clamp((progress - RED_PULSE_START) / (1.0F - RED_PULSE_START), 0.0F, 1.0F);
        int alpha = (int) (255 * pulse * Mth.lerp(strength, 0.25F, 0.7F));
        if (alpha <= 0) {
            return;
        }

        int width = graphics.guiWidth();
        int height = graphics.guiHeight();
        int edge = Math.max(8, Math.min(width, height) / 6);
        int red = alpha << 24 | 0xB00010;
        int clear = 0x00B00010;
        // Top and bottom: vertical gradients
        graphics.fillGradient(0, 0, width, edge, red, clear);
        graphics.fillGradient(0, height - edge, width, height, clear, red);
        // Left and right: thin columns that fade out towards the middle
        int columns = 16;
        for (int i = 0; i < columns; i++) {
            float fade = 1.0F - (float) i / columns;
            int color = ((int) (alpha * fade * fade) << 24) | 0xB00010;
            int x0 = edge * i / columns;
            int x1 = edge * (i + 1) / columns;
            graphics.fill(x0, edge, x1, height - edge, color);
            graphics.fill(width - x1, edge, width - x0, height - edge, color);
        }
    }

    /** The Heart Palpitations amplifier of the player, or -1 when it doesn't have it. */
    private static int palpitationsLevel(LocalPlayer player) {
        if (player == null) {
            return -1;
        }
        // Compared by value: the effects synced from the server use different holder objects than ModEffects
        for (MobEffectInstance effect : player.getActiveEffects()) {
            if (effect.getEffect().value() == ModEffects.HEART_PALPITATIONS_EFFECT.get()) {
                return effect.getAmplifier();
            }
        }
        return -1;
    }

    @SubscribeEvent
    public static void registerPayloadHandlers(RegisterClientPayloadHandlersEvent event) {
        event.register(WungdigestionPayload.TYPE, ClientPayloadHandler::handleWungdigestion);
    }
}
