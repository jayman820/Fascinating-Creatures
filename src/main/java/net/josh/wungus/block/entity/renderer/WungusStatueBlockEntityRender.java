package net.josh.wungus.block.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.josh.wungus.block.entity.WungusStatueBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class WungusStatueBlockEntityRender implements BlockEntityRenderer<WungusStatueBlockEntity> {
    @Override
    public void render(WungusStatueBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {

    }

    public WungusStatueBlockEntityRender(BlockEntityRendererProvider.Context context) {}
}
