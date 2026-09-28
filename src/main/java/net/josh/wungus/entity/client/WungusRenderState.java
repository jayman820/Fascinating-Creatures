package net.josh.wungus.entity.client;

import net.josh.wungus.entity.variant.WungusVariant;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.AnimationState;
import org.jspecify.annotations.Nullable;

public class WungusRenderState extends LivingEntityRenderState {
    public final AnimationState runningAnimationState = new AnimationState();
    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState sittingAnimationState = new AnimationState();
    public final AnimationState standingAnimationState = new AnimationState();
    public WungusVariant variant = WungusVariant.DEFAULT;
    // Set for the named easter egg wungi, overrides the variant texture
    public @Nullable Identifier textureOverride;
}
