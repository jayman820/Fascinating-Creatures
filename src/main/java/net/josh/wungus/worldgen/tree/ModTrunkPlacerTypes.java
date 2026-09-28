package net.josh.wungus.worldgen.tree;

import net.josh.wungus.WungusMod;
import net.josh.wungus.worldgen.tree.custom.AilanthusTrunkPlacer;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModTrunkPlacerTypes {
    public static final DeferredRegister<TrunkPlacerType<?>> TRUNK_PLACERS =
            DeferredRegister.create(Registries.TRUNK_PLACER_TYPE, WungusMod.MOD_ID);

    public static final DeferredHolder<TrunkPlacerType<?>, TrunkPlacerType<AilanthusTrunkPlacer>> AILANTHUS_TRUNK_PLACER =
            TRUNK_PLACERS.register("ailanthus_trunk_placer", () -> new TrunkPlacerType<>(AilanthusTrunkPlacer.CODEC));

    public static void register(IEventBus eventBus) {
        TRUNK_PLACERS.register(eventBus);
    }
}
