package net.josh.wungus.network;

import net.josh.wungus.WungusMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModNetworking {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(WungusMod.MOD_ID, "main"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);

    private static int id = 0;

    public static void register() {
        CHANNEL.messageBuilder(WungdigestionMessage.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(WungdigestionMessage::encode)
                .decoder(WungdigestionMessage::decode)
                .consumerMainThread(WungdigestionMessage::handle)
                .add();
    }

    /** Sends a message to every player that can see the entity (and the entity itself if it is a player). */
    public static <MSG> void sendToTrackingAndSelf(Entity entity, MSG message) {
        CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), message);
    }
}
