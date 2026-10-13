package net.josh.wungus.entity.client;

import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.AnimationState;

public class LanternflyRenderState extends LivingEntityRenderState {
    public final AnimationState flyingAnimationState = new AnimationState();
    // Sitting on the side of a trunk or wall
    public boolean climbing;
}
