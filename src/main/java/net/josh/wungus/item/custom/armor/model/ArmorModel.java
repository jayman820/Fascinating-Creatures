package net.josh.wungus.item.custom.armor.model;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.LivingEntity;

/**
 * pre-made model for armors.
 * <br> extend to make custom armors models.
 * <p>
 * The pose and the part visibility are copied from the vanilla armor model of the entity wearing it
 * (see CustomArmorModelExtensions), so the parts have to be positioned like the vanilla humanoid model.
 * Only the parts containing cubes are visible, so there is no need to hide the parts of other slots.
 */
public class ArmorModel extends HumanoidModel<LivingEntity> {

    public ArmorModel(ModelPart pRoot) {
        super(pRoot);
    }

    /**
     * Creates a mesh containing every part required by {@link HumanoidModel}, without any cubes,
     * using the same pivots as the vanilla humanoid model.
     */
    protected static MeshDefinition createEmptyHumanoidMesh() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        partdefinition.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.ZERO);
        partdefinition.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);
        partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.ZERO);
        partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offset(-5.0F, 2.0F, 0.0F));
        partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.offset(5.0F, 2.0F, 0.0F));
        partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.offset(-1.9F, 12.0F, 0.0F));
        partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.offset(1.9F, 12.0F, 0.0F));

        return meshdefinition;
    }
}
