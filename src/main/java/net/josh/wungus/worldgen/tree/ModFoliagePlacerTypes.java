package net.josh.wungus.worldgen.tree;

import net.josh.wungus.WungusMod;
import net.josh.wungus.worldgen.tree.custom.AilanthusFoliagePlacer;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModFoliagePlacerTypes {
    public static final DeferredRegister<FoliagePlacerType<?>> FOLIAGE_PLACERS =
            DeferredRegister.create(Registries.FOLIAGE_PLACER_TYPE, WungusMod.MOD_ID);

    public static final RegistryObject<FoliagePlacerType<AilanthusFoliagePlacer>> AILANTHUS_FOLIAGE_PLACER =
            FOLIAGE_PLACERS.register("ailanthus_foliage_placer", () -> new FoliagePlacerType<>(AilanthusFoliagePlacer.CODEC));

    public static void register(IEventBus eventBus) {
        FOLIAGE_PLACERS.register(eventBus);
    }
}
