package net.josh.wungus.entity.client;

import net.josh.wungus.WungusMod;
import net.josh.wungus.entity.custom.LanternflyEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;

public class LanternflyRenderer extends MobRenderer<LanternflyEntity, LanternflyRenderState, LanternflyModel> {
    private static final Identifier TEXTURE =
            Identifier.fromNamespaceAndPath(WungusMod.MOD_ID, "textures/entity/lanternfly/lanternfly.png");

    public LanternflyRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new LanternflyModel(pContext.bakeLayer(ModModelLayers.LANTERNFLY_LAYER)), 0.15f);
    }

    @Override
    public LanternflyRenderState createRenderState() {
        return new LanternflyRenderState();
    }

    @Override
    public void extractRenderState(LanternflyEntity pEntity, LanternflyRenderState pState, float pPartialTicks) {
        super.extractRenderState(pEntity, pState, pPartialTicks);
        pState.flyingAnimationState.copyFrom(pEntity.flyingAnimationState);

        Direction wall = pEntity.getWallFace();
        pState.climbing = wall != null && pState.deathTime <= 0.0F;
        if (pState.climbing) {
            // Face the trunk it is sitting on, whichever way it was looking
            pState.bodyRot = wall.toYRot();
        }
    }

    // No sideways death roll: on a flat insect that looks just like it is climbing a wall.
    // The model flips it onto its back instead (see LanternflyModel#setupAnim).
    @Override
    protected float getFlipDegrees() {
        return 0.0F;
    }

    // Like a parrot, a lanternfly on your shoulder isn't drawn in first person (it would sit right next to the camera)
    @Override
    public boolean shouldRender(LanternflyEntity pEntity, Frustum pFrustum, double pCamX, double pCamY, double pCamZ) {
        Minecraft minecraft = Minecraft.getInstance();
        if (pEntity.getVehicle() != null && pEntity.getVehicle() == minecraft.getCameraEntity()
                && minecraft.options.getCameraType().isFirstPerson()) {
            return false;
        }
        return super.shouldRender(pEntity, pFrustum, pCamX, pCamY, pCamZ);
    }

    @Override
    public Identifier getTextureLocation(LanternflyRenderState pState) {
        return TEXTURE;
    }
}
