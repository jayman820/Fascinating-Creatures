package net.josh.wungus.villager;

import com.google.common.collect.ImmutableSet;
import net.josh.wungus.WungusMod;
import net.josh.wungus.block.ModBlocks;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.VillageSiege;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModVillagers {
    public static final DeferredRegister<PoiType> POI_TYPES =
            DeferredRegister.create(ForgeRegistries.POI_TYPES, WungusMod.MOD_ID);
    public static final DeferredRegister<VillagerProfession> VILLAGER_PROFESSIONS =
            DeferredRegister.create(ForgeRegistries.VILLAGER_PROFESSIONS, WungusMod.MOD_ID);

    public static final RegistryObject<PoiType> BBL_POI = POI_TYPES.register("bbl_poi",
            () -> new PoiType(ImmutableSet.copyOf(ModBlocks.BBL_TABLE.get().getStateDefinition().getPossibleStates()),
            1, 1));

    public static final RegistryObject<VillagerProfession> BBL_DOCTOR =
            VILLAGER_PROFESSIONS.register("bbl_doctor", () -> new VillagerProfession("bbl_doctor",
                    holder -> holder.get() == BBL_POI.get(), holder -> holder.get() == BBL_POI.get(),
                    ImmutableSet.of(), ImmutableSet.of(), SoundEvents.VILLAGER_WORK_WEAPONSMITH));

    public static void register(IEventBus eventBus) {
        POI_TYPES.register(eventBus);
        VILLAGER_PROFESSIONS.register(eventBus);
    }
}
