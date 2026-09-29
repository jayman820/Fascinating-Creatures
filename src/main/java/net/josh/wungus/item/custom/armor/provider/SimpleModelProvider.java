package net.josh.wungus.item.custom.armor.provider;

import net.josh.wungus.item.custom.armor.model.ArmorModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * model provider that uses a single model, baked the first time it is needed
 */
public class SimpleModelProvider implements ArmorModelProvider {
    private final Supplier<LayerDefinition> definitionSupplier;
    private final Function<ModelPart, ArmorModel> modelFactory;
    private ArmorModel model;

    public SimpleModelProvider(Supplier<LayerDefinition> definitionSupplier, Function<ModelPart, ArmorModel> model) {
        this.definitionSupplier = definitionSupplier;
        this.modelFactory = model;
    }

    @Override
    public ArmorModel getModel(LivingEntity living, ItemStack stack, EquipmentSlot slot) {
        if (model == null) {
            model = modelFactory.apply(definitionSupplier.get().bakeRoot());
        }
        return model;
    }
}
