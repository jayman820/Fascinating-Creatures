package net.josh.wungus.entity.animations;

import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;

/**
 * Lanternfly animations, converted from the Blockbench model (blockbench exports/lanternfly.bbmodel).
 * Blockbench mirrors x and flips y compared to Minecraft models, so rotations use (-x, -y, z).
 * The model only has this one animation, its position keyframes are all 0 and are left out.
 */
public class LanternflyAnimations {
    // Flying/gliding: a very quick flap of both wings
    public static final AnimationDefinition FLY = AnimationDefinition.Builder.withLength(0.08333F).looping()
            .addAnimation("WingR", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.04167F, KeyframeAnimations.degreeVec(-18.11F, -33.64F, 10.27F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.08333F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("WingL", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.04167F, KeyframeAnimations.degreeVec(-18.11F, 33.64F, -10.27F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.08333F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .build();
}
