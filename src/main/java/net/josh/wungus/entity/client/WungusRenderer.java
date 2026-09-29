package net.josh.wungus.entity.client;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import net.josh.wungus.WungusMod;
import net.josh.wungus.entity.custom.WungusEntity;
import net.josh.wungus.entity.variant.WungusVariant;
import net.minecraft.Util;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

// Adults and babies have their own model and texture
public class WungusRenderer extends MobRenderer<WungusEntity, HierarchicalModel<WungusEntity>> {
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
    // One texture for all baby wungi (every variant and name)
    private static final ResourceLocation BABY_TEXTURE = new ResourceLocation(WungusMod.MOD_ID, "textures/entity/wungus_baby.png");

    private final HierarchicalModel<WungusEntity> adultModel;
    private final HierarchicalModel<WungusEntity> babyModel;

    public WungusRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new WungusModel<>(pContext.bakeLayer(ModModelLayers.WUNGUS_LAYER)), 0.7f);
        this.adultModel = this.model;
        this.babyModel = new BabyWungusModel<>(pContext.bakeLayer(ModModelLayers.WUNGUS_BABY_LAYER));
    }

    @Override
    public ResourceLocation getTextureLocation(WungusEntity wungusEntity) {
        if (wungusEntity.isBaby()) {
            return BABY_TEXTURE;
        }
        if (wungusEntity.getName().toString().toLowerCase().contains("sakura")) {
            return new ResourceLocation(WungusMod.MOD_ID, "textures/entity/pinkgus.png");
        } else if (wungusEntity.getName().toString().toLowerCase().contains("papi")) {
            return new ResourceLocation(WungusMod.MOD_ID, "textures/entity/mangungus.png");
        }
        return LOCATION_BY_VARIANT.get(wungusEntity.getVariant());
    }

    @Override
    public void render(WungusEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        // Babies have their own, smaller model (instead of drawing the adult at half size)
        this.model = pEntity.isBaby() ? this.babyModel : this.adultModel;

        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
    }
}
