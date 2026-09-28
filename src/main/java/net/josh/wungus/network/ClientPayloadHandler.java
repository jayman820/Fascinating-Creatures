package net.josh.wungus.network;

import net.josh.wungus.particle.ModParticles;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;

// Only referenced from client-side code
public class ClientPayloadHandler {
    private static final int PARTICLE_COUNT = 2000;

    // Vomit: out of the mouth, in the direction the entity is looking
    private static final double VOMIT_HALF_ANGLE = 22.0;   // degrees, width of the cone
    private static final double VOMIT_MIN_SPEED = 0.18;
    private static final double VOMIT_MAX_SPEED = 0.30;

    // Diarrhea: out of the butt, backwards and a bit down
    private static final double DIARRHEA_HALF_ANGLE = 18.0;
    private static final double DIARRHEA_DOWN_TILT = 25.0; // degrees below horizontal
    private static final double DIARRHEA_MIN_SPEED = 0.55;
    private static final double DIARRHEA_MAX_SPEED = 0.85;

    public static void handleWungdigestion(WungdigestionPayload payload, IPayloadContext context) {
        Level level = context.player().level();
        Entity entity = level.getEntity(payload.entityId());
        if (entity == null) {
            return;
        }

        RandomSource rand = level.getRandom();
        if (payload.vomit()) {
            Vec3 look = entity.getViewVector(1.0F);
            // Roughly the mouth: a bit below the eyes and in front of the face
            Vec3 mouth = entity.getEyePosition().add(0, -0.15, 0).add(look.scale(0.3));
            spawnCone(level, ModParticles.VOMIT_PARTICLE_1.get(), mouth, look,
                    VOMIT_HALF_ANGLE, VOMIT_MIN_SPEED, VOMIT_MAX_SPEED, rand);
        } else {
            // Use the body rotation, the head can be turned away from the body
            float bodyYaw = entity instanceof LivingEntity living ? living.yBodyRot : entity.getYRot();
            Vec3 backwards = Vec3.directionFromRotation((float) DIARRHEA_DOWN_TILT, bodyYaw + 180.0F);
            Vec3 bodyForward = Vec3.directionFromRotation(0.0F, bodyYaw);
            // Roughly the butt: at hip height, just behind the body
            Vec3 butt = entity.position()
                    .add(0, entity.getBbHeight() * 0.45, 0)
                    .subtract(bodyForward.scale(entity.getBbWidth() * 0.5 + 0.05));
            spawnCone(level, ModParticles.DIARRHEA_PARTICLE_1.get(), butt, backwards,
                    DIARRHEA_HALF_ANGLE, DIARRHEA_MIN_SPEED, DIARRHEA_MAX_SPEED, rand);
        }
    }

    /**
     * Spawns particles from {@code origin} with velocities spread evenly inside a cone around {@code direction}.
     * All particles travel roughly the same distance, so the far end of the cone is rounded (a spherical cap)
     * instead of flat. The spread of speeds fills the inside of the cone.
     */
    private static void spawnCone(Level level, ParticleOptions particle, Vec3 origin, Vec3 direction,
                                  double halfAngleDegrees, double minSpeed, double maxSpeed, RandomSource rand) {
        Vec3 axis = direction.normalize();
        // Two directions perpendicular to the cone axis
        Vec3 helper = Math.abs(axis.y) < 0.99 ? new Vec3(0, 1, 0) : new Vec3(1, 0, 0);
        Vec3 side = axis.cross(helper).normalize();
        Vec3 up = side.cross(axis).normalize();

        double cosMax = Math.cos(Math.toRadians(halfAngleDegrees));
        for (int i = 0; i < PARTICLE_COUNT; i++) {
            // Uniform over the spherical cap, so the particles are not bunched up in the middle of the cone
            double cosTheta = 1.0 - rand.nextDouble() * (1.0 - cosMax);
            double sinTheta = Math.sqrt(1.0 - cosTheta * cosTheta);
            double phi = rand.nextDouble() * Mth.TWO_PI;

            Vec3 dir = axis.scale(cosTheta)
                    .add(side.scale(sinTheta * Math.cos(phi)))
                    .add(up.scale(sinTheta * Math.sin(phi)));
            double speed = Mth.lerp(rand.nextDouble(), minSpeed, maxSpeed);

            level.addParticle(particle, origin.x, origin.y, origin.z, dir.x * speed, dir.y * speed, dir.z * speed);
        }
    }
}
