package net.josh.wungus.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.josh.wungus.WungusMod;
import net.josh.wungus.entity.custom.LanternflyEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;

public class LanternflyRenderer extends MobRenderer<LanternflyEntity, LanternflyModel> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(WungusMod.MOD_ID, "textures/entity/lanternfly/lanternfly.png");

    public LanternflyRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new LanternflyModel(pContext.bakeLayer(ModModelLayers.LANTERNFLY_LAYER)), 0.15f);
    }

    @Override
    public ResourceLocation getTextureLocation(LanternflyEntity pEntity) {
        return TEXTURE;
    }

    // On a trunk or wall it faces it, whichever way it was looking (the model does the rest of the climbing pose)
    @Override
    protected void setupRotations(LanternflyEntity pEntity, PoseStack pPoseStack, float pAgeInTicks, float pRotationYaw, float pPartialTicks) {
        Direction wall = pEntity.getWallFace();
        if (wall != null && pEntity.deathTime <= 0) {
            pRotationYaw = wall.toYRot();
        }
        super.setupRotations(pEntity, pPoseStack, pAgeInTicks, pRotationYaw, pPartialTicks);
    }

    // No sideways death roll: on a flat insect that looks just like it is climbing a wall.
    // The model flips it onto its back instead (see LanternflyModel#setupAnim).
    @Override
    protected float getFlipDegrees(LanternflyEntity pEntity) {
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
}
