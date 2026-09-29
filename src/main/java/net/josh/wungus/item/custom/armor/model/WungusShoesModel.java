package net.josh.wungus.item.custom.armor.model;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

// The boots are children of the vanilla leg parts, so they follow the leg animation
public class WungusShoesModel extends ArmorModel {
    public WungusShoesModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = createEmptyHumanoidMesh();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition right_leg = partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.offset(-1.9F, 12.0F, 0.0F));
        PartDefinition left_leg = partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.offset(1.9F, 12.0F, 0.0F));

        PartDefinition right_boot = right_leg.addOrReplaceChild("right_boot", CubeListBuilder.create().texOffs(0, 28).addBox(-2.0F, 10.0F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(1.0F))
                .texOffs(0, 0).addBox(-1.0F, 10.0F, -15.0F, 4.0F, 2.0F, 12.0F, new CubeDeformation(0.0F))
                .texOffs(32, 10).addBox(-1.0F, 7.4F, -17.5F, 4.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.ZERO);

        PartDefinition rsnout2_r1 = right_boot.addOrReplaceChild("rsnout2_r1", CubeListBuilder.create().texOffs(32, 0).addBox(2.1F, -2.0F, -1.0F, 4.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.1F, 10.95F, -16.55F, -0.5672F, 0.0F, 0.0F));

        PartDefinition left_boot = left_leg.addOrReplaceChild("left_boot", CubeListBuilder.create().texOffs(16, 28).addBox(-2.0F, 10.0F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(1.0F))
                .texOffs(0, 14).addBox(-2.9F, 10.0F, -15.0F, 4.0F, 2.0F, 12.0F, new CubeDeformation(0.0F))
                .texOffs(32, 15).addBox(-2.9F, 7.4F, -17.5F, 4.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.ZERO);

        PartDefinition lsnout2_r1 = left_boot.addOrReplaceChild("lsnout2_r1", CubeListBuilder.create().texOffs(32, 5).addBox(-3.0F, -2.0F, -1.0F, 4.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.1F, 10.95F, -16.55F, -0.5672F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }
}
