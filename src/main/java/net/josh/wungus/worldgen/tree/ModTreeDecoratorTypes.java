package net.josh.wungus.worldgen.tree;

import net.josh.wungus.WungusMod;
import net.josh.wungus.worldgen.tree.custom.InfestedLogDecorator;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModTreeDecoratorTypes {
    public static final DeferredRegister<TreeDecoratorType<?>> TREE_DECORATORS =
            DeferredRegister.create(Registries.TREE_DECORATOR_TYPE, WungusMod.MOD_ID);

    public static final RegistryObject<TreeDecoratorType<InfestedLogDecorator>> INFESTED_LOG =
            TREE_DECORATORS.register("infested_log", () -> new TreeDecoratorType<>(InfestedLogDecorator.CODEC));

    public static void register(IEventBus eventBus) {
        TREE_DECORATORS.register(eventBus);
    }
}
