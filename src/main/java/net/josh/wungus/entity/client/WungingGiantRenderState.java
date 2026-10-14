package net.josh.wungus.entity.client;

import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;

public class WungingGiantRenderState extends LivingEntityRenderState {
    // How far the wheels have turned, in ticks of the Rolling animation (see WungingGiantEntity#getWheelRoll)
    public float wheelRoll;
}
