package net.josh.wungus.entity.client;

// Made with Blockbench 5.1.6 (prigus_java.bbmodel)

import net.josh.wungus.entity.animations.BabyWungusAnimations;
import net.minecraft.client.animation.KeyframeAnimation;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

/** The baby wungus, used instead of WungusModel for babies (see WungusRenderer). */
public class BabyWungusModel extends EntityModel<WungusRenderState> {
	private final ModelPart prigus;
	private final ModelPart body;
	private final ModelPart snout;
	private final ModelPart legL;
	private final ModelPart legR;

	private final KeyframeAnimation walkAnimation;
	private final KeyframeAnimation idleAnimation;
	private final KeyframeAnimation sitAnimation;
	private final KeyframeAnimation standAnimation;

	public BabyWungusModel(ModelPart root) {
		super(root);
		this.prigus = root.getChild("prigus");
		this.body = this.prigus.getChild("body");
		this.snout = this.body.getChild("snout");
		this.legL = this.prigus.getChild("legL");
		this.legR = this.prigus.getChild("legR");

		this.walkAnimation = BabyWungusAnimations.BABY_WALK.bake(root);
		this.idleAnimation = BabyWungusAnimations.BABY_IDLE.bake(root);
		this.sitAnimation = BabyWungusAnimations.BABY_SIT.bake(root);
		this.standAnimation = BabyWungusAnimations.BABY_STAND.bake(root);
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

	@Override
	public void setupAnim(WungusRenderState state) {
		super.setupAnim(state);

		// The baby has no running animation, so it keeps walking while it runs away
		this.walkAnimation.applyWalk(state.walkAnimationPos, state.walkAnimationSpeed, 1f, 2.5f);
		this.idleAnimation.apply(state.idleAnimationState, state.ageInTicks, 1f);
		this.sitAnimation.apply(state.sittingAnimationState, state.ageInTicks, 1f);
		this.standAnimation.apply(state.standingAnimationState, state.ageInTicks, 1f);
	}
}
