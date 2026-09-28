package net.josh.wungus.network;

import net.josh.wungus.particle.ModParticles;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;

// Only referenced from client-side code
public class ClientPayloadHandler {
    public static void handleWungdigestion(WungdigestionPayload payload, IPayloadContext context) {
        Level level = context.player().level();
        Entity entity = level.getEntity(payload.entityId());
        if (entity == null) {
            return;
        }

        RandomSource rand = level.getRandom();
        if (payload.vomit()) {
            Vec3 vec = entity.getViewVector(0).scale(0.8d);
            for(int i = 0; i < 2000; i++) {
                level.addParticle(ModParticles.VOMIT_PARTICLE_1.get(),
                        entity.getX() + vec.get(Direction.Axis.X), entity.getY() + 1.5, entity.getZ() + vec.get(Direction.Axis.Z),
                        -(rand.nextDouble() - 0.2D) * 1.3D * Math.cos(vec.get(Direction.Axis.X) * 2 * Math.PI), -((rand.nextDouble() + 0.8D)), -(rand.nextDouble() - 0.2D) * 1.3D * Math.sin(vec.get(Direction.Axis.Z) * 2 * Math.PI));
            }
        } else {
            Vec3 vec = entity.getViewVector(0);
            for(int i = 0; i < 2000; i++) {
                level.addParticle(ModParticles.DIARRHEA_PARTICLE_1.get(),
                        entity.getX() - vec.scale(1.2).get(Direction.Axis.X), entity.getY() + 1, entity.getZ() - vec.scale(1.2D).get(Direction.Axis.Z),
                        (rand.nextDouble() - 0.2D), -((rand.nextDouble() + 0.3D) * 1.0D), (rand.nextDouble() - 0.2D));
            }
        }
    }
}
