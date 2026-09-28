package net.josh.wungus.item.custom;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.fox.Fox;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

/**
 * Shared chorus fruit style teleport used by the cooked wungus flesh and the wungus shawarma.
 */
public final class ChorusLikeTeleport {
    private ChorusLikeTeleport() {}

    /**
     * @param horizontalRange the total width of the square the entity can land in
     * @param verticalRange   the number of possible vertical offsets
     * @param verticalOffset  subtracted from the random vertical offset
     */
    public static void teleport(ServerLevel pLevel, LivingEntity pEntityLiving, ItemStack consumedStack, double horizontalRange, int verticalRange, int verticalOffset) {
        double d0 = pEntityLiving.getX();
        double d1 = pEntityLiving.getY();
        double d2 = pEntityLiving.getZ();

        for (int i = 0; i < 16; ++i) {
            double d3 = pEntityLiving.getX() + (pEntityLiving.getRandom().nextDouble() - 0.5D) * horizontalRange;
            double d4 = Mth.clamp(pEntityLiving.getY() + (double) (pEntityLiving.getRandom().nextInt(verticalRange) - verticalOffset), (double) pLevel.getMinY(), (double) (pLevel.getMinY() + pLevel.getLogicalHeight() - 1));
            double d5 = pEntityLiving.getZ() + (pEntityLiving.getRandom().nextDouble() - 0.5D) * horizontalRange;
            if (pEntityLiving.isPassenger()) {
                pEntityLiving.stopRiding();
            }

            Vec3 vec3 = pEntityLiving.position();
            // Passing the consumed stack fires NeoForge's EntityTeleportEvent.ItemConsumption
            if (pEntityLiving.randomTeleport(d3, d4, d5, true, consumedStack)) {
                pLevel.gameEvent(GameEvent.TELEPORT, vec3, GameEvent.Context.of(pEntityLiving));
                SoundEvent soundevent = pEntityLiving instanceof Fox ? SoundEvents.FOX_TELEPORT : SoundEvents.CHORUS_FRUIT_TELEPORT;
                pLevel.playSound((Entity) null, d0, d1, d2, soundevent, SoundSource.PLAYERS, 1.0F, 1.0F);
                pEntityLiving.playSound(soundevent, 1.0F, 1.0F);
                pEntityLiving.resetFallDistance();
                break;
            }
        }
    }
}
