package net.josh.wungus.villager;

import com.google.common.collect.ImmutableSet;
import net.josh.wungus.WungusMod;
import net.josh.wungus.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModVillagers {
    public static final DeferredRegister<PoiType> POI_TYPES =
            DeferredRegister.create(Registries.POINT_OF_INTEREST_TYPE, WungusMod.MOD_ID);
    public static final DeferredRegister<VillagerProfession> VILLAGER_PROFESSIONS =
            DeferredRegister.create(Registries.VILLAGER_PROFESSION, WungusMod.MOD_ID);

    public static final DeferredHolder<PoiType, PoiType> BBL_POI = POI_TYPES.register("bbl_poi",
            () -> new PoiType(ImmutableSet.copyOf(ModBlocks.BBL_TABLE.get().getStateDefinition().getPossibleStates()),
            1, 1));

    // The trades are added in ModEvents#addCustomTrades
    public static final DeferredHolder<VillagerProfession, VillagerProfession> BBL_DOCTOR =
            VILLAGER_PROFESSIONS.register("bbl_doctor", () -> new VillagerProfession("bbl_doctor",
                    holder -> holder.is(BBL_POI.getKey()), holder -> holder.is(BBL_POI.getKey()),
                    ImmutableSet.of(), ImmutableSet.of(), SoundEvents.VILLAGER_WORK_WEAPONSMITH));

    public static void register(IEventBus eventBus) {
        POI_TYPES.register(eventBus);
        VILLAGER_PROFESSIONS.register(eventBus);
    }
}
