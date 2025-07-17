package net.josh.wungus.client;

import net.josh.wungus.WungusMod;
import net.josh.wungus.client.model.WungusMaskModel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = WungusMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)

public class ModClientEvent {
    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(WungusMaskModel.LAYER_LOCATION, WungusMaskModel::createBodyLayer);
    }
}
