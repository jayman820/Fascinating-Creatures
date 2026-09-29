package net.josh.wungus.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Sent from the server to nearby clients when an entity with the Wungdigestion effect burps (vomit = true)
 * or farts (vomit = false), so everyone sees the particle burst at the same time as the sound.
 */
public record WungdigestionMessage(int entityId, boolean vomit) {
    public static void encode(WungdigestionMessage message, FriendlyByteBuf buffer) {
        buffer.writeVarInt(message.entityId);
        buffer.writeBoolean(message.vomit);
    }

    public static WungdigestionMessage decode(FriendlyByteBuf buffer) {
        return new WungdigestionMessage(buffer.readVarInt(), buffer.readBoolean());
    }

    public static void handle(WungdigestionMessage message, Supplier<NetworkEvent.Context> context) {
        // The particles are client only code
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientPacketHandler.handleWungdigestion(message));
    }
}
