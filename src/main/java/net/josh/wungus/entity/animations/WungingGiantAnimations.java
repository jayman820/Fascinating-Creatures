package net.josh.wungus.entity.animations;

import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;

/**
 * Wunging giant animations, converted from the Blockbench model (blockbench exports/wunging_giant.bbmodel).
 * Blockbench mirrors x and flips y compared to Minecraft models, so rotations use (-x, -y, z).
 */
public class WungingGiantAnimations {
    // Rolling: all four wheels make one full forward turn
    public static final AnimationDefinition ROLLING = AnimationDefinition.Builder.withLength(1.0F).looping()
            .addAnimation("lfwheel", wheelTurn())
            .addAnimation("lbwheel", wheelTurn())
            .addAnimation("rfwheel", wheelTurn())
            .addAnimation("rbwheel", wheelTurn())
            .build();

    private static AnimationChannel wheelTurn() {
        return new AnimationChannel(AnimationChannel.Targets.ROTATION,
                new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(0.25F, KeyframeAnimations.degreeVec(90.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(0.5F, KeyframeAnimations.degreeVec(180.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(0.75F, KeyframeAnimations.degreeVec(270.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(1.0F, KeyframeAnimations.degreeVec(360.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR));
    }
}
