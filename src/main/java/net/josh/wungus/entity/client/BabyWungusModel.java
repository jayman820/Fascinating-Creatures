package net.josh.wungus.entity.client;

// Made with Blockbench 5.1.6 (prigus_java.bbmodel)

import net.josh.wungus.entity.animations.BabyWungusAnimations;
import net.josh.wungus.entity.custom.WungusEntity;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

/** The baby wungus, used instead of WungusModel for babies (see WungusRenderer). */
public class BabyWungusModel extends HierarchicalModel<WungusEntity> {
	private final ModelPart root;
	private final ModelPart prigus;
	private final ModelPart body;
	private final ModelPart snout;
	private final ModelPart legL;
	private final ModelPart legR;

	public BabyWungusModel(ModelPart root) {
		this.root = root;
		this.prigus = root.getChild("prigus");
		this.body = this.prigus.getChild("body");
		this.snout = this.body.getChild("snout");
		this.legL = this.prigus.getChild("legL");
		this.legR = this.prigus.getChild("legR");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition prigus = partdefinition.addOrReplaceChild("prigus", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition body = prigus.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -7.0F, -5.0F, 8.0F, 5.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(0, 13).addBox(-3.0F, -9.0F, -4.0F, 6.0F, 2.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(0, 21).addBox(-3.0F, -2.0F, -4.0F, 6.0F, 2.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(24, 13).addBox(-2.0F, -10.0F, -3.0F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(30, 18).addBox(-1.0F, -11.0F, -2.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -4.0F, 1.0F));

		PartDefinition snout = body.addOrReplaceChild("snout", CubeListBuilder.create().texOffs(24, 18).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -5.0F, -5.0F));

		PartDefinition bridge_r1 = snout.addOrReplaceChild("bridge_r1", CubeListBuilder.create().texOffs(30, 21).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 1.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition legL = prigus.addOrReplaceChild("legL", CubeListBuilder.create().texOffs(10, 29).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(30, 24).addBox(-1.0F, 2.0F, 0.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(24, 27).addBox(-1.0F, 4.0F, -2.0F, 2.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, -4.0F, 1.0F));

		PartDefinition legR = prigus.addOrReplaceChild("legR", CubeListBuilder.create().texOffs(18, 30).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(26, 30).addBox(-1.0F, 2.0F, 0.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 29).addBox(-1.0F, 4.0F, -2.0F, 2.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, -4.0F, 1.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	// The animations look up the bones by name below this part, so it has to be the real root (it holds "prigus")
	@Override
	public ModelPart root() {
		return this.root;
	}

	@Override
	public void setupAnim(WungusEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);

		// The baby has no running animation, so it keeps walking while it runs away
		this.animateWalk(BabyWungusAnimations.BABY_WALK, limbSwing, limbSwingAmount, 1f, 2.5f);
		this.animate(entity.idleAnimationState, BabyWungusAnimations.BABY_IDLE, ageInTicks, 1f);
		this.animate(entity.sittingAnimation, BabyWungusAnimations.BABY_SIT, ageInTicks, 1f);
		this.animate(entity.standingAnimation, BabyWungusAnimations.BABY_STAND, ageInTicks, 1f);
	}
}
