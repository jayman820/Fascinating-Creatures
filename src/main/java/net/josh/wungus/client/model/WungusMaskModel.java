package net.josh.wungus.client.model;// Made with Blockbench 4.12.5
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.josh.wungus.WungusMod;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class WungusMaskModel<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(WungusMod.MOD_ID, "wungus_mask"), "main");
	public final ModelPart main;

	public WungusMaskModel(ModelPart root) {
		this.main = root.getChild("main");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition main = partdefinition.addOrReplaceChild("main", CubeListBuilder.create().texOffs(0, 0).addBox(5.0F, -5.0F, -3.6F, 0.0F, 1.0F, 10.0F, new CubeDeformation(0.0F))
				.texOffs(0, 11).addBox(-5.0F, -5.0F, -3.6F, 0.0F, 1.0F, 10.0F, new CubeDeformation(0.0F))
				.texOffs(20, 9).addBox(-5.0F, -5.0F, 6.4F, 10.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(20, 0).addBox(-5.0F, -8.0F, -4.6F, 10.0F, 8.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(20, 10).addBox(-1.0F, -4.0F, -5.6F, 2.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -1.4F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}
	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		main.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}