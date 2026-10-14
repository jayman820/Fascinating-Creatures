package net.josh.wungus.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.josh.wungus.WungusMod;
import net.josh.wungus.entity.custom.WungingGiantEntity;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

public class WungingGiantRenderer extends MobRenderer<WungingGiantEntity, WungingGiantRenderState, WungingGiantModel> {
    private static final Identifier TEXTURE =
            Identifier.fromNamespaceAndPath(WungusMod.MOD_ID, "textures/entity/wunging_giant/wunging_giant.png");
    // The outstretched arms reach about this far (in blocks) past the sides of the hitbox
    private static final double ARM_REACH = 2.3D;
    // How far (in blocks) the whole giant jerks back and forth while it shakes someone
    private static final float SHAKE_DISTANCE = 0.05F;

    public WungingGiantRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new WungingGiantModel(pContext.bakeLayer(ModModelLayers.WUNGING_GIANT_LAYER)), 0.7f);
    }

    @Override
    public WungingGiantRenderState createRenderState() {
        return new WungingGiantRenderState();
    }

    @Override
    public void extractRenderState(WungingGiantEntity pEntity, WungingGiantRenderState pState, float pPartialTicks) {
        super.extractRenderState(pEntity, pState, pPartialTicks);
        pState.wheelRoll = pEntity.getWheelRoll(pPartialTicks);
        pState.shaking = pEntity.getHeldEntityId() >= 0;
    }

    @Override
    public void submit(WungingGiantRenderState pState, PoseStack pPoseStack, SubmitNodeCollector pSubmitNodeCollector, CameraRenderState pCamera) {
        if (!pState.shaking) {
            super.submit(pState, pPoseStack, pSubmitNodeCollector, pCamera);
            return;
        }
        // Shaking the player it holds: the whole puppet jerks around with it
        float time = pState.ageInTicks;
        pPoseStack.pushPose();
        pPoseStack.translate((Mth.sin(time * 3.1F) + 0.5F * Mth.sin(time * 7.7F)) * SHAKE_DISTANCE, 0.0F,
                (Mth.cos(time * 2.7F) + 0.5F * Mth.sin(time * 8.3F)) * SHAKE_DISTANCE);
        super.submit(pState, pPoseStack, pSubmitNodeCollector, pCamera);
        pPoseStack.popPose();
    }

    // Only the body is inside the hitbox. Without this the arms would vanish while the body is just off screen.
    @Override
    public boolean shouldRender(WungingGiantEntity pEntity, Frustum pFrustum, double pCamX, double pCamY, double pCamZ) {
        if (super.shouldRender(pEntity, pFrustum, pCamX, pCamY, pCamZ)) {
            return true;
        }
        return pEntity.shouldRender(pCamX, pCamY, pCamZ)
                && pFrustum.isVisible(pEntity.getBoundingBox().inflate(ARM_REACH, 0.0D, ARM_REACH));
    }

    @Override
    public Identifier getTextureLocation(WungingGiantRenderState pState) {
        return TEXTURE;
    }
}
