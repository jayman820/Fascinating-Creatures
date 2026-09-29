package net.josh.wungus.event;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.josh.wungus.WungusMod;
import net.josh.wungus.block.ModBlocks;
import net.josh.wungus.effect.WungusSteroidEffect;
import net.josh.wungus.item.ModItems;
import net.josh.wungus.villager.ModVillagers;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;

import java.util.List;

@EventBusSubscriber(modid = WungusMod.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class ModEvents {
    @SubscribeEvent
    public static void addCustomTrades(VillagerTradesEvent event) {
        if(event.getType() == ModVillagers.BBL_DOCTOR.get()) {
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();

            trades.get(1).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 50),
                    new ItemStack(ModItems.BBL.get(), 1),
                    16, 8, 0.02f
            ));

            trades.get(2).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 11),
                    new ItemStack(ModBlocks.BBL_TABLE.get(), 1),
                    5, 12, 0.02f
            ));

        }
    }

    // A newly added steroid effect (not a refreshed one) starts a new heart failure, timed to the dose
    @SubscribeEvent
    public static void onEffectAdded(MobEffectEvent.Added event) {
        MobEffectInstance instance = event.getEffectInstance();
        if (event.getOldEffectInstance() == null && !event.getEntity().level().isClientSide()
                && instance.getEffect().value() instanceof WungusSteroidEffect steroid) {
            int duration = instance.isInfiniteDuration() ? WungusSteroidEffect.DEFAULT_DOSE_TICKS : instance.getDuration();
            steroid.startHeartFailure(event.getEntity(), duration);
        }
    }
}
