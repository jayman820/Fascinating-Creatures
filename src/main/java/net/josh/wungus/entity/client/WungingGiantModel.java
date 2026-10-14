package net.josh.wungus.entity.client;

// Made with Blockbench (blockbench exports/wunging_giant.bbmodel)

import net.josh.wungus.entity.animations.WungingGiantAnimations;
import net.minecraft.client.animation.KeyframeAnimation;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class WungingGiantModel extends EntityModel<WungingGiantRenderState> {
	private final KeyframeAnimation rollingAnimation;

	public WungingGiantModel(ModelPart root) {
		super(root);
		this.rollingAnimation = WungingGiantAnimations.ROLLING.bake(root);
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(84, 22).addBox(-7.0F, -50.0F, 5.0F, 14.0F, 37.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));
		bb_main.addOrReplaceChild("neckhump_r1", CubeListBuilder.create().texOffs(108, 94).addBox(-7.0F, -14.0F, -1.0F, 14.0F, 14.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -47.0F, 4.0F, 0.6109F, 0.0F, 0.0F));

		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(76, 62).addBox(-20.0F, -35.0F, -8.0F, 7.0F, 16.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.0F, 14.0F, 0.0F, -1.5708F, 0.0F));
		PartDefinition snout = head.addOrReplaceChild("snout", CubeListBuilder.create().texOffs(122, 30).addBox(-2.45F, 0.0F, -2.0F, 3.0F, 35.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-20.55F, -28.0F, 0.0F));
		snout.addOrReplaceChild("snout_bridge_r1", CubeListBuilder.create().texOffs(116, 142).addBox(-1.0F, -4.0F, -2.0F, 2.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.45F, 0.6F, 0.0F, 0.0F, 0.0F, 1.1781F));

		PartDefinition rightarm = partdefinition.addOrReplaceChild("rightarm", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
		rightarm.addOrReplaceChild("rightforearm_r1", CubeListBuilder.create().texOffs(0, 62).addBox(-10.0F, -34.0F, -4.0F, 11.0F, 34.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-14.0F, -25.0F, 0.0F, 0.0F, 0.0F, -0.7854F));
		rightarm.addOrReplaceChild("rightbicep_r1", CubeListBuilder.create().texOffs(0, 104).addBox(-9.0F, -2.0F, 1.0F, 10.0F, 29.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.0F, -44.25F, -4.0F, 0.0F, 0.0F, 0.5236F));
		PartDefinition righthand = rightarm.addOrReplaceChild("righthand", CubeListBuilder.create(), PartPose.offsetAndRotation(-18.0F, -33.25F, 0.0F, 0.0F, 0.0F, -0.1309F));
		righthand.addOrReplaceChild("palm_r1", CubeListBuilder.create().texOffs(64, 134).addBox(-1.0F, -11.0F, -1.0F, 7.0F, 11.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-24.0F, -13.0F, 0.0F, 0.0F, 0.0F, -0.5236F));
		PartDefinition finger6 = righthand.addOrReplaceChild("finger6", CubeListBuilder.create(), PartPose.offsetAndRotation(-22.0F, -20.0F, -1.0F, 0.0F, 0.0F, 0.5672F));
		finger6.addOrReplaceChild("finger2_r1", CubeListBuilder.create().texOffs(136, 57).addBox(-1.0F, -2.0F, -6.0F, 2.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, 0.0F, -1.5708F, 0.0F, 0.0F));
		finger6.addOrReplaceChild("digit2_r1", CubeListBuilder.create().texOffs(140, 66).addBox(-1.0F, -2.0F, -5.0F, 2.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -7.0F, -0.25F, -1.1781F, 0.0F, 0.0F));
		PartDefinition finger7 = righthand.addOrReplaceChild("finger7", CubeListBuilder.create(), PartPose.offsetAndRotation(-24.0F, -22.0F, -1.0F, 0.0F, 0.0F, 0.1745F));
		finger7.addOrReplaceChild("finger3_r1", CubeListBuilder.create().texOffs(136, 114).addBox(-1.0F, -2.0F, -6.0F, 2.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, 0.0F, -1.5708F, 0.0F, 0.0F));
		finger7.addOrReplaceChild("digit3_r1", CubeListBuilder.create().texOffs(140, 74).addBox(-1.0F, -2.0F, -5.0F, 2.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -7.0F, -0.25F, -1.1781F, 0.0F, 0.0F));
		PartDefinition finger8 = righthand.addOrReplaceChild("finger8", CubeListBuilder.create(), PartPose.offsetAndRotation(-26.0F, -23.0F, -1.0F, 0.0F, 0.0F, -0.1745F));
		finger8.addOrReplaceChild("finger4_r1", CubeListBuilder.create().texOffs(136, 123).addBox(-1.0F, -2.0F, -6.0F, 2.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, 0.0F, -1.5708F, 0.0F, 0.0F));
		finger8.addOrReplaceChild("digit4_r1", CubeListBuilder.create().texOffs(140, 82).addBox(-1.0F, -2.0F, -5.0F, 2.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -7.0F, -0.25F, -1.1781F, 0.0F, 0.0F));
		PartDefinition finger9 = righthand.addOrReplaceChild("finger9", CubeListBuilder.create(), PartPose.offsetAndRotation(-27.25F, -20.0F, -1.0F, 0.0F, 0.0F, -0.4363F));
		finger9.addOrReplaceChild("finger5_r1", CubeListBuilder.create().texOffs(136, 132).addBox(-1.0F, -2.0F, -6.0F, 2.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, 0.0F, -1.5708F, 0.0F, 0.0F));
		finger9.addOrReplaceChild("digit5_r1", CubeListBuilder.create().texOffs(136, 141).addBox(-1.0F, -2.0F, -5.0F, 2.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -7.0F, -0.25F, -1.1781F, 0.0F, 0.0F));
		PartDefinition finger10 = righthand.addOrReplaceChild("finger10", CubeListBuilder.create(), PartPose.offsetAndRotation(-27.0F, -19.0F, -1.0F, 0.0F, 0.0F, -1.1781F));
		finger10.addOrReplaceChild("finger6_r1", CubeListBuilder.create().texOffs(138, 0).addBox(-1.0F, -2.0F, -6.0F, 2.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, 0.0F, -1.5708F, 0.0F, 0.0F));
		finger10.addOrReplaceChild("digit6_r1", CubeListBuilder.create().texOffs(100, 142).addBox(-1.0F, -2.0F, -5.0F, 2.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -7.0F, -0.25F, -1.1781F, 0.0F, 0.0F));

		PartDefinition leftarm = partdefinition.addOrReplaceChild("leftarm", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
		leftarm.addOrReplaceChild("leftforearm_r1", CubeListBuilder.create().texOffs(38, 62).addBox(-1.0F, -34.0F, -4.0F, 11.0F, 34.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(14.0F, -25.0F, 0.0F, 0.0F, 0.0F, 0.7854F));
		leftarm.addOrReplaceChild("leftbicep_r1", CubeListBuilder.create().texOffs(32, 104).addBox(-1.0F, -2.0F, 1.0F, 10.0F, 29.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.0F, -44.25F, -4.0F, 0.0F, 0.0F, -0.5236F));
		PartDefinition lefthand = leftarm.addOrReplaceChild("lefthand", CubeListBuilder.create(), PartPose.offsetAndRotation(18.0F, -33.25F, 0.0F, 0.0F, 0.0F, 0.1309F));
		lefthand.addOrReplaceChild("palm_r2", CubeListBuilder.create().texOffs(120, 0).addBox(-6.0F, -11.0F, -1.0F, 7.0F, 11.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(24.0F, -13.0F, 0.0F, 0.0F, 0.0F, 0.5236F));
		PartDefinition finger1 = lefthand.addOrReplaceChild("finger1", CubeListBuilder.create(), PartPose.offsetAndRotation(22.0F, -20.0F, -1.0F, 0.0F, 0.0F, -0.5672F));
		finger1.addOrReplaceChild("finger1_r1", CubeListBuilder.create().texOffs(120, 13).addBox(-1.0F, -2.0F, -6.0F, 2.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, 0.0F, -1.5708F, 0.0F, 0.0F));
		finger1.addOrReplaceChild("digit1_r1", CubeListBuilder.create().texOffs(138, 9).addBox(-1.0F, -2.0F, -5.0F, 2.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -7.0F, -0.25F, -1.1781F, 0.0F, 0.0F));
		PartDefinition finger2 = lefthand.addOrReplaceChild("finger2", CubeListBuilder.create(), PartPose.offsetAndRotation(24.0F, -22.0F, -1.0F, 0.0F, 0.0F, -0.1745F));
		finger2.addOrReplaceChild("finger2_r2", CubeListBuilder.create().texOffs(82, 134).addBox(-1.0F, -2.0F, -6.0F, 2.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, 0.0F, -1.5708F, 0.0F, 0.0F));
		finger2.addOrReplaceChild("digit2_r2", CubeListBuilder.create().texOffs(0, 139).addBox(-1.0F, -2.0F, -5.0F, 2.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -7.0F, -0.25F, -1.1781F, 0.0F, 0.0F));
		PartDefinition finger3 = lefthand.addOrReplaceChild("finger3", CubeListBuilder.create(), PartPose.offsetAndRotation(26.0F, -23.0F, -1.0F, 0.0F, 0.0F, 0.1745F));
		finger3.addOrReplaceChild("finger3_r2", CubeListBuilder.create().texOffs(136, 30).addBox(-1.0F, -2.0F, -6.0F, 2.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, 0.0F, -1.5708F, 0.0F, 0.0F));
		finger3.addOrReplaceChild("digit3_r2", CubeListBuilder.create().texOffs(16, 139).addBox(-1.0F, -2.0F, -5.0F, 2.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -7.0F, -0.25F, -1.1781F, 0.0F, 0.0F));
		PartDefinition finger4 = lefthand.addOrReplaceChild("finger4", CubeListBuilder.create(), PartPose.offsetAndRotation(27.25F, -20.0F, -1.0F, 0.0F, 0.0F, 0.4363F));
		finger4.addOrReplaceChild("finger4_r2", CubeListBuilder.create().texOffs(136, 39).addBox(-1.0F, -2.0F, -6.0F, 2.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, 0.0F, -1.5708F, 0.0F, 0.0F));
		finger4.addOrReplaceChild("digit4_r2", CubeListBuilder.create().texOffs(32, 139).addBox(-1.0F, -2.0F, -5.0F, 2.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -7.0F, -0.25F, -1.1781F, 0.0F, 0.0F));
		PartDefinition finger5 = lefthand.addOrReplaceChild("finger5", CubeListBuilder.create(), PartPose.offsetAndRotation(27.0F, -19.0F, -1.0F, 0.0F, 0.0F, 1.1781F));
		finger5.addOrReplaceChild("finger5_r2", CubeListBuilder.create().texOffs(136, 48).addBox(-1.0F, -2.0F, -6.0F, 2.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, 0.0F, -1.5708F, 0.0F, 0.0F));
		finger5.addOrReplaceChild("digit5_r2", CubeListBuilder.create().texOffs(48, 139).addBox(-1.0F, -2.0F, -5.0F, 2.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -7.0F, -0.25F, -1.1781F, 0.0F, 0.0F));

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-9.0F, -48.0F, -5.0F, 18.0F, 37.0F, 10.0F, new CubeDeformation(0.0F))
				.texOffs(0, 47).addBox(-15.0F, -51.0F, -5.0F, 30.0F, 3.0F, 12.0F, new CubeDeformation(0.0F))
				.texOffs(56, 0).addBox(-10.0F, -12.75F, -6.0F, 20.0F, 10.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));
		PartDefinition wheels = body.addOrReplaceChild("wheels", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition lfwheel = wheels.addOrReplaceChild("lfwheel", CubeListBuilder.create(), PartPose.offset(7.5F, -1.25F, -5.0F));
		lfwheel.addOrReplaceChild("leftfrontwheel_r1", CubeListBuilder.create().texOffs(70, 104).addBox(-0.5F, -0.9393F, -1.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.25F, 0.0F, -0.7854F, 0.0F, 0.0F));
		PartDefinition lbwheel = wheels.addOrReplaceChild("lbwheel", CubeListBuilder.create(), PartPose.offset(7.5F, -1.25F, 5.0F));
		lbwheel.addOrReplaceChild("leftbackwheel_r1", CubeListBuilder.create().texOffs(70, 108).addBox(-0.5F, -1.1161F, -1.1768F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));
		PartDefinition rfwheel = wheels.addOrReplaceChild("rfwheel", CubeListBuilder.create(), PartPose.offset(-7.5F, -1.5F, -5.0F));
		rfwheel.addOrReplaceChild("rightfrontwheel_r1", CubeListBuilder.create().texOffs(70, 112).addBox(-0.5F, -1.2929F, -1.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.25F, -0.25F, -0.7854F, 0.0F, 0.0F));
		PartDefinition rbwheel = wheels.addOrReplaceChild("rbwheel", CubeListBuilder.create(), PartPose.offset(-7.5F, -1.5F, 5.25F));
		rbwheel.addOrReplaceChild("rightbackwheel_r1", CubeListBuilder.create().texOffs(70, 116).addBox(-0.5F, -1.1161F, -1.1768F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.25F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition hair = partdefinition.addOrReplaceChild("hair", CubeListBuilder.create().texOffs(74, 22).addBox(-8.0F, -67.0F, -7.0F, 2.0F, 24.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(64, 104).addBox(6.0F, -67.0F, -7.0F, 2.0F, 24.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(108, 114).addBox(-7.0F, -54.0F, -6.25F, 14.0F, 28.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(118, 22).addBox(-8.0F, -68.0F, -5.0F, 16.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));
		hair.addOrReplaceChild("sidehair1_r1", CubeListBuilder.create().texOffs(56, 22).addBox(0.0F, -15.0F, -7.0F, 1.0F, 16.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(11.5F, -52.25F, 1.0F, 0.0F, 0.0F, -0.3054F));
		hair.addOrReplaceChild("sidehair2_r1", CubeListBuilder.create().texOffs(122, 69).addBox(-1.0F, -15.0F, -7.0F, 1.0F, 16.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-11.5F, -52.25F, 1.0F, 0.0F, 0.0F, 0.3054F));
		hair.addOrReplaceChild("backhair_r1", CubeListBuilder.create().texOffs(76, 94).addBox(-1.0F, -15.0F, -7.0F, 1.0F, 25.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, -53.25F, 8.0F, 1.5708F, 1.0908F, 1.5708F));

		return LayerDefinition.create(meshdefinition, 256, 256);
	}

	@Override
	public void setupAnim(WungingGiantRenderState state) {
		super.setupAnim(state);

		// The wheels are turned by how far it rolled, not by time, so they stop dead the moment it does
		this.rollingAnimation.applyWalk(state.wheelRoll, 1.0F, 1.0F, 1.0F);
	}
}
