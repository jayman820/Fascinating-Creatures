package net.josh.wungus.item.custom.armor.model;// Made with Blockbench 4.12.5
// Exported for Minecraft version 1.17 or later with Mojang mappings
// The cubes live in the vanilla head part (pivot 0, 0, 0)


import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class WungusMaskModel extends ArmorModel {
	public WungusMaskModel(ModelPart root) {
		super(root);
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = createEmptyHumanoidMesh();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create()
				.texOffs(0, 0).addBox(5.0F, -6.0F, -5.0F, 0.0F, 1.0F, 10.0F, new CubeDeformation(0.0F))
				.texOffs(0, 11).addBox(-5.0F, -6.0F, -5.0F, 0.0F, 1.0F, 10.0F, new CubeDeformation(0.0F))
				.texOffs(20, 9).addBox(-5.0F, -6.0F, 4.0F, 10.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(20, 0).addBox(-5.0F, -8.0F, -5.0F, 10.0F, 8.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(20, 10).addBox(-1.0F, -3.0F, -6.0F, 2.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.ZERO);

		//PartDefinition cube_r1 = head.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(36, 0).addBox(-1.5F, -5.0F, -2.0F, 3.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, -17.0F, 0.0F, -0.1745F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}
}
