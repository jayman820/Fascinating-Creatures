package net.josh.wungus.worldgen.tree;

import net.josh.wungus.WungusMod;
import net.josh.wungus.worldgen.tree.custom.InfestedLogDecorator;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModTreeDecoratorTypes {
    public static final DeferredRegister<TreeDecoratorType<?>> TREE_DECORATORS =
            DeferredRegister.create(Registries.TREE_DECORATOR_TYPE, WungusMod.MOD_ID);

    public static final DeferredHolder<TreeDecoratorType<?>, TreeDecoratorType<InfestedLogDecorator>> INFESTED_LOG =
            TREE_DECORATORS.register("infested_log", () -> new TreeDecoratorType<>(InfestedLogDecorator.CODEC));

    public static void register(IEventBus eventBus) {
        TREE_DECORATORS.register(eventBus);
    }
}
