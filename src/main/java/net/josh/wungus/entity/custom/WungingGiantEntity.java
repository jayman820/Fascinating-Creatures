package net.josh.wungus.entity.custom;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

/**
 * The wunging giant, a huge puppet that rolls around on a wheeled base (blockbench exports/wunging_giant.bbmodel).
 * It has no AI yet: its stalking behavior and movement still have to be worked out, until then it just stands there.
 */
public class WungingGiantEntity extends Monster {
    // The wheels in the model are 2x2 pixel squares stood on a corner, so about 1.4 pixels from the axle to the
    // ground. One full turn rolls it this far (in blocks).
    private static final float WHEEL_TURN_DISTANCE = 2.0F * (float) Math.PI * (float) Math.sqrt(2.0D) / 16.0F;
    // One full turn of the wheels is one loop of the Rolling animation, which is 20 ticks long
    private static final float ROLL_ANIMATION_TICKS = 20.0F;

    // How far the wheels have turned, in ticks of the Rolling animation (client side only)
    private float wheelRoll;
    private float wheelRollO;

    public WungingGiantEntity(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    // Placeholder values until the behavior is worked out
    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 100.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.ATTACK_DAMAGE, 10.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
                .add(Attributes.FOLLOW_RANGE, 64.0D);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide()) {
            this.turnWheels();
        }
    }

    // Client side only. The wheels turn by how far it rolled, so they never spin while it stands still.
    private void turnWheels() {
        this.wheelRollO = this.wheelRoll;
        // Only movement along the way it faces turns them (backwards turns them back), being pushed sideways doesn't
        float yaw = this.yBodyRot * ((float) Math.PI / 180.0F);
        double forward = -(this.getX() - this.xo) * Mth.sin(yaw) + (this.getZ() - this.zo) * Mth.cos(yaw);
        this.wheelRoll += (float) (forward / WHEEL_TURN_DISTANCE) * ROLL_ANIMATION_TICKS;
        // Both values are kept within about one turn (and never below 0, the animation can't play backwards) so the
        // float doesn't lose precision. They move together, so the turning stays smooth between ticks.
        float wrap = Mth.floor(Math.min(this.wheelRoll, this.wheelRollO) / ROLL_ANIMATION_TICKS) * ROLL_ANIMATION_TICKS;
        this.wheelRoll -= wrap;
        this.wheelRollO -= wrap;
    }

    /** How far the wheels have turned, in ticks of the Rolling animation. */
    public float getWheelRoll(float partialTick) {
        return Mth.lerp(partialTick, this.wheelRollO, this.wheelRoll);
    }
}
