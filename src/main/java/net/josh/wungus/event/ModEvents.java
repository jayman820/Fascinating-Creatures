package net.josh.wungus.event;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.josh.wungus.WungusMod;
import net.josh.wungus.block.ModBlocks;
import net.josh.wungus.effect.WungusSteroidEffect;
import net.josh.wungus.entity.custom.LanternflyEntity;
import net.josh.wungus.item.ModItems;
import net.josh.wungus.villager.ModVillagers;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.EventHooks;
import net.neoforged.neoforge.event.entity.living.LivingFallEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.event.entity.player.PlayerFlyableFallEvent;
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

    @SubscribeEvent
    public static void onLivingFall(LivingFallEvent event) {
        squishLanternflies(event.getEntity(), event.getDistance());
    }

    // Players who can fly (creative) don't take fall damage and get this event instead
    @SubscribeEvent
    public static void onPlayerFlyableFall(PlayerFlyableFallEvent event) {
        squishLanternflies(event.getEntity(), event.getDistance());
    }

    /**
     * Landing on a lanternfly can squish it, with the same rules as trampling farmland: the chance is the fall
     * distance minus 0.5 (a normal jump is about 75%), and it has to be a player, or a mob while mob griefing is on,
     * that is at least about as big as a chicken.
     */
    private static void squishLanternflies(LivingEntity entity, float fallDistance) {
        if (!(entity.level() instanceof ServerLevel level) || entity instanceof LanternflyEntity || fallDistance <= 0.5F) {
            return;
        }
        if (!(entity instanceof Player || EventHooks.canEntityGrief(level, entity))
                || entity.getBbWidth() * entity.getBbWidth() * entity.getBbHeight() <= 0.512F) {
            return;
        }

        // The space right under its feet
        AABB box = entity.getBoundingBox();
        AABB feet = new AABB(box.minX, box.minY - 0.1D, box.minZ, box.maxX, box.minY + 0.3D, box.maxZ);
        for (LanternflyEntity lanternfly : level.getEntitiesOfClass(LanternflyEntity.class, feet,
                candidate -> candidate.isAlive() && !candidate.isPassenger())) {
            if (level.getRandom().nextFloat() < fallDistance - 0.5F) {
                DamageSource source = entity instanceof Player player
                        ? level.damageSources().playerAttack(player)
                        : level.damageSources().mobAttack(entity);
                lanternfly.hurt(source, Float.MAX_VALUE);
            }
        }
    }
}
