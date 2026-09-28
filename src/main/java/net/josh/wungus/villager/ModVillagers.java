package net.josh.wungus.villager;

import com.google.common.collect.ImmutableSet;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.josh.wungus.WungusMod;
import net.josh.wungus.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import net.minecraft.world.item.trading.TradeSet;
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

    // The trades are data driven, see data/wungus/trade_set/bbl_doctor and data/wungus/villager_trade/bbl_doctor
    public static final ResourceKey<TradeSet> BBL_DOCTOR_LEVEL_1 = tradeSet("bbl_doctor/level_1");
    public static final ResourceKey<TradeSet> BBL_DOCTOR_LEVEL_2 = tradeSet("bbl_doctor/level_2");

    public static final DeferredHolder<VillagerProfession, VillagerProfession> BBL_DOCTOR =
            VILLAGER_PROFESSIONS.register("bbl_doctor", id -> new VillagerProfession(
                    Component.translatable("entity." + id.getNamespace() + ".villager." + id.getPath()),
                    holder -> holder.is(BBL_POI.getKey()), holder -> holder.is(BBL_POI.getKey()),
                    ImmutableSet.of(), ImmutableSet.of(), SoundEvents.VILLAGER_WORK_WEAPONSMITH,
                    Int2ObjectMap.ofEntries(
                            Int2ObjectMap.entry(1, BBL_DOCTOR_LEVEL_1),
                            Int2ObjectMap.entry(2, BBL_DOCTOR_LEVEL_2))));

    private static ResourceKey<TradeSet> tradeSet(String path) {
        return ResourceKey.create(Registries.TRADE_SET, Identifier.fromNamespaceAndPath(WungusMod.MOD_ID, path));
    }

    public static void register(IEventBus eventBus) {
        POI_TYPES.register(eventBus);
        VILLAGER_PROFESSIONS.register(eventBus);
    }
}
