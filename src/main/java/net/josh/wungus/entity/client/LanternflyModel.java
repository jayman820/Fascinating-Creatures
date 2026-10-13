package net.josh.wungus.entity.client;

// Made with Blockbench (blockbench exports/lanternfly.bbmodel)

import net.josh.wungus.entity.animations.LanternflyAnimations;
import net.minecraft.client.animation.KeyframeAnimation;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.rendertype.RenderTypes;

public class LanternflyModel extends EntityModel<LanternflyRenderState> {
	// While climbing the body is moved this far (in pixels) forward, so it sits on the trunk instead of
	// in the middle of the hitbox (half the hitbox width is 2.4 pixels)
	private static final float CLIMB_FORWARD_OFFSET = 2.2F;
	// In the Blockbench model the wings lie exactly on the top of the body, which made them flicker through it,
	// and the flap tilts their outer edges up to 0.074 pixels down into it. Lifted a little (less than one
	// texel of the 128x texture) so they always stay above it.
	private static final float WING_LIFT = 0.1F;

	private final ModelPart lanternfly;
	private final KeyframeAnimation flyAnimation;

	public LanternflyModel(ModelPart root) {
		// Rendered with backface culling: the wings are flat, from above they show the grey front wings
		// and from below the red hind wings (without culling one side would show through the other)
		super(root, RenderTypes::entityCutout);
		this.lanternfly = root.getChild("Lanternfly");
		this.flyAnimation = LanternflyAnimations.FLY.bake(root);
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		// The Blockbench model looks south (its eyes are on the south side) and entity models look north,
		// so it is turned around here. Otherwise it would fly backwards.
		PartDefinition lanternfly = partdefinition.addOrReplaceChild("Lanternfly", CubeListBuilder.create()
				.texOffs(0, 0).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, (float) Math.PI, 0.0F));

		PartDefinition wings = lanternfly.addOrReplaceChild("Wings", CubeListBuilder.create(), PartPose.offset(0.0F, -WING_LIFT, 0.0F));

		wings.addOrReplaceChild("WingR", CubeListBuilder.create()
				.texOffs(0, 4).addBox(-0.5F, 0.0F, -2.0F, 1.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, -1.0F, 1.0F));

		wings.addOrReplaceChild("WingL", CubeListBuilder.create()
				.texOffs(0, 6).addBox(-0.5F, 0.0F, -2.0F, 1.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, -1.0F, 1.0F));

		// The UVs are in 16x16 units, the 128x128 texture is the same layout at 8 times the resolution
		return LayerDefinition.create(meshdefinition, 16, 16);
	}

	@Override
	public void setupAnim(LanternflyRenderState state) {
		super.setupAnim(state);

		if (state.climbing) {
			// Sitting on a trunk or wall: head up, belly against it (the renderer turns it to face the trunk)
			this.lanternfly.xRot = (float) Math.PI / 2F;
			this.lanternfly.z -= CLIMB_FORWARD_OFFSET;
			this.lanternfly.y -= 1.0F;
		}

		this.flyAnimation.apply(state.flyingAnimationState, state.ageInTicks, 1f);
	}
}
