package net.josh.wungus.event;

import net.josh.wungus.WungusMod;
import net.josh.wungus.entity.custom.LanternflyEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.EventHooks;
import net.neoforged.neoforge.event.entity.living.LivingFallEvent;
import net.neoforged.neoforge.event.entity.player.PlayerFlyableFallEvent;

// Game events (the mod bus events are in ModEventBusEvents)
@EventBusSubscriber(modid = WungusMod.MOD_ID)
public class ModEvents {
    @SubscribeEvent
    public static void onLivingFall(LivingFallEvent event) {
        squishLanternflies(event.getEntity(), event.getDistance());
    }

    // Players who can fly (creative) get this event instead. Its distance is wrong (NeoForge passes the damage
    // multiplier there), but the player's own fall distance hasn't been reset yet when it fires.
    @SubscribeEvent
    public static void onPlayerFlyableFall(PlayerFlyableFallEvent event) {
        squishLanternflies(event.getEntity(), event.getEntity().fallDistance);
    }

    /**
     * Landing on a lanternfly can squish it, with the same rules as trampling farmland: the chance is the fall
     * distance minus 0.5 (a normal jump is about 75%), and it has to be a player, or a mob while mob griefing is on,
     * that is at least about as big as a chicken.
     */
    private static void squishLanternflies(LivingEntity entity, double fallDistance) {
        if (!(entity.level() instanceof ServerLevel level) || entity instanceof LanternflyEntity || fallDistance <= 0.5D) {
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
            if (level.getRandom().nextFloat() < fallDistance - 0.5D) {
                DamageSource source = entity instanceof Player player
                        ? level.damageSources().playerAttack(player)
                        : level.damageSources().mobAttack(entity);
                lanternfly.hurtServer(level, source, Float.MAX_VALUE);
            }
        }
    }
}
