package net.josh.wungus.entity.client;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import net.josh.wungus.WungusMod;
import net.josh.wungus.entity.custom.WungusEntity;
import net.josh.wungus.entity.variant.WungusVariant;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public class WungusRenderer extends MobRenderer<WungusEntity, WungusModel<WungusEntity>> {
    public static final Map<WungusVariant, ResourceLocation> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(WungusVariant.class), map -> {
                map.put(WungusVariant.DEFAULT,
                        new ResourceLocation(WungusMod.MOD_ID, "textures/entity/wungus.png"));
                map.put(WungusVariant.WHITE,
                        new ResourceLocation(WungusMod.MOD_ID, "textures/entity/nonegus.png"));
                map.put(WungusVariant.GREEN,
                        new ResourceLocation(WungusMod.MOD_ID, "textures/entity/greengus.png"));
                map.put(WungusVariant.BLUE,
                        new ResourceLocation(WungusMod.MOD_ID, "textures/entity/bluegus.png"));
            });
    public WungusRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new WungusModel<>(pContext.bakeLayer(ModModelLayers.WUNGUS_LAYER)), 1f);
    }

    @Override
    public WungusModel<WungusEntity> getModel() {
        return super.getModel();
    }

    @Override
    public ResourceLocation getTextureLocation(WungusEntity wungusEntity) {
        if (wungusEntity.getName().toString().toLowerCase().contains("sakura")) {
            return new ResourceLocation(WungusMod.MOD_ID, "textures/entity/pinkgus.png");
        }
        return LOCATION_BY_VARIANT.get(wungusEntity.getVariant());
    }

    @Override
    public void render(WungusEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        if(pEntity.isBaby()) {
            pPoseStack.scale(0.5f, 0.5f, 0.5f);
        }

        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
    }
}
