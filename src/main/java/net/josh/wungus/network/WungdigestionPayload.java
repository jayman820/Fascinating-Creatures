package net.josh.wungus.network;

import net.josh.wungus.WungusMod;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

/**
 * Sent from the server to nearby clients when an entity with the Wungdigestion effect burps (vomit = true)
 * or farts (vomit = false), so the clients can spawn the particle burst.
 */
public record WungdigestionPayload(int entityId, boolean vomit) implements CustomPacketPayload {
    public static final Type<WungdigestionPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(WungusMod.MOD_ID, "wungdigestion"));

    public static final StreamCodec<RegistryFriendlyByteBuf, WungdigestionPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, WungdigestionPayload::entityId,
            ByteBufCodecs.BOOL, WungdigestionPayload::vomit,
            WungdigestionPayload::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
