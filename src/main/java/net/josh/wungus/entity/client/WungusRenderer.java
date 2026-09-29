package net.josh.wungus.entity.client;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import net.josh.wungus.WungusMod;
import net.josh.wungus.entity.custom.WungusEntity;
import net.josh.wungus.entity.variant.WungusVariant;
import net.minecraft.client.model.AdultAndBabyModelPair;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;

import java.util.Map;

// Adults and babies have their own model and texture, like vanilla's split adult/baby animals
public class WungusRenderer extends MobRenderer<WungusEntity, WungusRenderState, EntityModel<WungusRenderState>> {
    public static final Map<WungusVariant, Identifier> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(WungusVariant.class), map -> {
                map.put(WungusVariant.DEFAULT, texture("wungus"));
                map.put(WungusVariant.WHITE, texture("nonegus"));
                map.put(WungusVariant.GREEN, texture("greengus"));
                map.put(WungusVariant.BLUE, texture("bluegus"));
            });
    private static final Identifier SAKURA_TEXTURE = texture("pinkgus");
    private static final Identifier PAPI_TEXTURE = texture("mangungus");
    // One texture for all baby wungi (every variant and name)
    private static final Identifier BABY_TEXTURE = texture("wungus_baby");

    private final AdultAndBabyModelPair<EntityModel<WungusRenderState>> models;

    public WungusRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new WungusModel(pContext.bakeLayer(ModModelLayers.WUNGUS_LAYER)), 0.7f);
        this.models = new AdultAndBabyModelPair<>(this.model, new BabyWungusModel(pContext.bakeLayer(ModModelLayers.WUNGUS_BABY_LAYER)));
    }

    private static Identifier texture(String name) {
        return Identifier.fromNamespaceAndPath(WungusMod.MOD_ID, "textures/entity/wungus/" + name + ".png");
    }

    @Override
    public WungusRenderState createRenderState() {
        return new WungusRenderState();
    }

    @Override
    public void extractRenderState(WungusEntity pEntity, WungusRenderState pState, float pPartialTicks) {
        super.extractRenderState(pEntity, pState, pPartialTicks);
        pState.runningAnimationState.copyFrom(pEntity.runningAnimationState);
        pState.idleAnimationState.copyFrom(pEntity.idleAnimationState);
        pState.sittingAnimationState.copyFrom(pEntity.sittingAnimation);
        pState.standingAnimationState.copyFrom(pEntity.standingAnimation);
        pState.variant = pEntity.getVariant();

        if (pEntity.nameContains("sakura")) {
            pState.textureOverride = SAKURA_TEXTURE;
        } else if (pEntity.nameContains("papi")) {
            pState.textureOverride = PAPI_TEXTURE;
        } else {
            pState.textureOverride = null;
        }
    }

    @Override
    public void submit(WungusRenderState pState, PoseStack pPoseStack, SubmitNodeCollector pSubmitNodeCollector, CameraRenderState pCamera) {
        this.model = this.models.getModel(pState.isBaby);
        super.submit(pState, pPoseStack, pSubmitNodeCollector, pCamera);
    }

    @Override
    public Identifier getTextureLocation(WungusRenderState pState) {
        if (pState.isBaby) {
            return BABY_TEXTURE;
        }
        if (pState.textureOverride != null) {
            return pState.textureOverride;
        }
        return LOCATION_BY_VARIANT.get(pState.variant);
    }
}
