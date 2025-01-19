package net.josh.wungus.entity.client;// Made with Blockbench 4.12.1
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.josh.wungus.entity.animations.ModAnimationDefinitions;
import net.josh.wungus.entity.custom.WungusEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Animal;

public class WungusModel<T extends Entity> extends HierarchicalModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "wungus"), "main");
	private final ModelPart main;
	private final ModelPart legR;
	private final ModelPart legL;
	private final ModelPart neck;
	private final ModelPart head;
	private final ModelPart snout;
	private final ModelPart torso;
	private final ModelPart wingL;
	private final ModelPart wingR;
	private final ModelPart tail;
	private final ModelPart tailsec2;
	private final ModelPart tailsec3;
	private final ModelPart tailsec4;

	public WungusModel(ModelPart root) {
		this.main = root.getChild("main");
		this.legR = this.main.getChild("legR");
		this.legL = this.main.getChild("legL");
		this.neck = this.main.getChild("neck");
		this.head = this.neck.getChild("head");
		this.snout = this.head.getChild("snout");
		this.torso = this.main.getChild("torso");
		this.wingL = this.torso.getChild("wingL");
		this.wingR = this.torso.getChild("wingR");
		this.tail = this.torso.getChild("tail");
		this.tailsec2 = this.tail.getChild("tailsec2");
		this.tailsec3 = this.tailsec2.getChild("tailsec3");
		this.tailsec4 = this.tailsec3.getChild("tailsec4");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition main = partdefinition.addOrReplaceChild("main", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 24.0F, 9.0F, 0.0F, -1.5708F, 0.0F));

		PartDefinition legR = main.addOrReplaceChild("legR", CubeListBuilder.create().texOffs(60, 85).addBox(2.0F, 10.0F, -1.5F, 0.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(66, 83).addBox(-1.0F, 12.0F, -1.5F, 3.0F, 0.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(0, 86).addBox(1.0F, 8.0F, -1.5F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(84, 76).addBox(0.0F, 6.0F, -1.5F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(54, 80).addBox(-1.0F, 4.0F, -1.5F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(0, 79).addBox(-2.0F, 0.0F, -1.5F, 4.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.0F, -12.0F, 3.5F));

		PartDefinition legL = main.addOrReplaceChild("legL", CubeListBuilder.create().texOffs(8, 86).addBox(2.0F, 10.0F, -1.5F, 0.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(78, 83).addBox(-1.0F, 12.0F, -1.5F, 3.0F, 0.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(52, 85).addBox(1.0F, 8.0F, -1.5F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(42, 85).addBox(0.0F, 6.0F, -1.5F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(42, 80).addBox(-1.0F, 4.0F, -1.5F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(14, 80).addBox(-2.0F, 0.0F, -1.5F, 4.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.0F, -12.0F, -3.5F));

		PartDefinition neck = main.addOrReplaceChild("neck", CubeListBuilder.create(), PartPose.offset(-15.0F, -22.0F, 0.0F));

		PartDefinition topneck_r1 = neck.addOrReplaceChild("topneck_r1", CubeListBuilder.create().texOffs(34, 22).addBox(-3.0F, -4.0F, -4.0F, 5.0F, 12.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.825F, -1.325F, 0.0F, 0.0F, 0.0F, -1.1345F));

		PartDefinition bottomneck_r1 = neck.addOrReplaceChild("bottomneck_r1", CubeListBuilder.create().texOffs(0, 38).addBox(-4.0F, -5.0F, -4.0F, 5.0F, 11.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.0F, 0.0F, 0.0F, 0.0F, -0.48F));

		PartDefinition head = neck.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 22).addBox(-22.0F, -27.0F, -5.0F, 7.0F, 6.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(15.0F, 22.0F, 0.0F));

		PartDefinition snout = head.addOrReplaceChild("snout", CubeListBuilder.create().texOffs(54, 42).addBox(-2.45F, 0.0F, -2.0F, 3.0F, 23.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-21.55F, -24.0F, 0.0F));

		PartDefinition snoutbridge_r1 = snout.addOrReplaceChild("snoutbridge_r1", CubeListBuilder.create().texOffs(48, 14).addBox(-1.0F, -2.0F, -2.0F, 2.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.45F, 0.6F, 0.0F, 0.0F, 0.0F, 1.1781F));

		PartDefinition torso = main.addOrReplaceChild("torso", CubeListBuilder.create().texOffs(0, 0).addBox(-16.0F, -22.0F, -6.0F, 12.0F, 10.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition wingL = torso.addOrReplaceChild("wingL", CubeListBuilder.create(), PartPose.offset(-13.325F, -18.25F, -6.0F));

		PartDefinition wing_r1 = wingL.addOrReplaceChild("wing_r1", CubeListBuilder.create().texOffs(28, 80).addBox(-0.1063F, -2.6039F, -1.0F, 6.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.425F, -0.25F, 0.0F, 0.0F, 0.0F, 0.6545F));

		PartDefinition wingR = torso.addOrReplaceChild("wingR", CubeListBuilder.create(), PartPose.offset(-13.175F, -18.375F, 6.0F));

		PartDefinition wing_r2 = wingR.addOrReplaceChild("wing_r2", CubeListBuilder.create().texOffs(76, 8).addBox(-0.1063F, -2.6039F, -1.0F, 6.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.575F, -0.125F, 1.0F, 0.0F, 0.0F, 0.6545F));

		PartDefinition tail = torso.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(26, 42).addBox(-4.0F, -16.0F, -5.0F, 4.0F, 4.0F, 10.0F, new CubeDeformation(0.0F))
				.texOffs(48, 0).addBox(-5.0F, -17.0F, -5.0F, 4.0F, 4.0F, 10.0F, new CubeDeformation(0.0F))
				.texOffs(26, 56).addBox(-5.0F, -18.0F, -5.0F, 3.0F, 4.0F, 10.0F, new CubeDeformation(0.0F))
				.texOffs(68, 62).addBox(-5.0F, -19.0F, -5.0F, 2.0F, 4.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition tailsec2 = tail.addOrReplaceChild("tailsec2", CubeListBuilder.create().texOffs(0, 57).addBox(0.0F, -11.0F, -4.0F, 5.0F, 4.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(60, 14).addBox(-1.0F, -12.0F, -4.0F, 5.0F, 4.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(60, 26).addBox(-2.0F, -13.0F, -4.0F, 5.0F, 4.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(68, 38).addBox(-3.0F, -14.0F, -4.0F, 5.0F, 4.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(68, 50).addBox(-4.0F, -15.0F, -4.0F, 5.0F, 4.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition tailsec3 = tailsec2.addOrReplaceChild("tailsec3", CubeListBuilder.create().texOffs(0, 69).addBox(3.0F, -8.0F, -3.0F, 5.0F, 4.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(22, 70).addBox(2.0F, -9.0F, -3.0F, 5.0F, 4.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(44, 70).addBox(1.0F, -10.0F, -3.0F, 5.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition tailsec4 = tailsec3.addOrReplaceChild("tailsec4", CubeListBuilder.create().texOffs(66, 76).addBox(5.0F, -5.0F, -2.0F, 5.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(76, 0).addBox(4.0F, -7.0F, -2.0F, 5.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		main.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	private void applyHeadRotation(float pNetHeadYaw, float pHeadPitch, float pAgeInTicks) {
		pNetHeadYaw = Mth.clamp(pNetHeadYaw, 0F, 0F);
		pHeadPitch = Mth.clamp(pHeadPitch, -5.0F, 5F);

		this.head.yRot = pNetHeadYaw * ((float)Math.PI / 180F);
		this.head.xRot = pHeadPitch * ((float)Math.PI / 180F);
	}

	@Override
	public ModelPart root() {
		return main;
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		this.applyHeadRotation(netHeadYaw, headPitch, ageInTicks);

		this.animateWalk(ModAnimationDefinitions.WUNGUS_WALK, limbSwing, limbSwingAmount, 1f, 2.5f);
		this.animate(((WungusEntity)entity).runningAnimationState, ModAnimationDefinitions.WUNGUS_RUN, ageInTicks, 1f);
	}
}