package net.josh.wungus.entity.client;

import net.josh.wungus.WungusMod;
import net.josh.wungus.entity.custom.WungingGiantEntity;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;

public class WungingGiantRenderer extends MobRenderer<WungingGiantEntity, WungingGiantRenderState, WungingGiantModel> {
    private static final Identifier TEXTURE =
            Identifier.fromNamespaceAndPath(WungusMod.MOD_ID, "textures/entity/wunging_giant/wunging_giant.png");
    // The outstretched arms reach about this far (in blocks) past the sides of the hitbox
    private static final double ARM_REACH = 2.3D;

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
