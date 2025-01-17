package net.josh.wungus.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.josh.wungus.WungusMod;
import net.josh.wungus.entity.custom.WungusEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class WungusRenderer extends MobRenderer<WungusEntity, WungusModel<WungusEntity>> {
    public WungusRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new WungusModel<>(pContext.bakeLayer(ModModelLayers.WUNGUS_LAYER)), 2f);
    }

    @Override
    public ResourceLocation getTextureLocation(WungusEntity wungusEntity) {
        return new ResourceLocation(WungusMod.MOD_ID, "textures/entity/wungus.png");
    }

    @Override
    public void render(WungusEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        if(pEntity.isBaby()) {
            pPoseStack.scale(0.5f, 0.5f, 0.5f);
        }

        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
    }
}
