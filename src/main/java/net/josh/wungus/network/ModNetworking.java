package net.josh.wungus.network;

import net.josh.wungus.WungusMod;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = WungusMod.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ModNetworking {
    @SubscribeEvent
    public static void registerPayloads(RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");

        // Handled on the client's main thread. The handler only spawns particles in the client level.
        registrar.playToClient(WungdigestionPayload.TYPE, WungdigestionPayload.STREAM_CODEC, ClientPayloadHandler::handleWungdigestion);
    }
}
