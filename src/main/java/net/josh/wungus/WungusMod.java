package net.josh.wungus;

import com.mojang.logging.LogUtils;
import net.josh.wungus.block.ModBlocks;
import net.josh.wungus.block.entity.ModBlockEntities;
import net.josh.wungus.effect.ModEffects;
import net.josh.wungus.entity.ModEntities;
import net.josh.wungus.item.ModCreativeModeTabs;
import net.josh.wungus.item.ModItems;
import net.josh.wungus.loot.ModLootModifiers;
import net.josh.wungus.particle.ModParticles;
import net.josh.wungus.sound.ModSounds;
import net.josh.wungus.villager.ModVillagers;
import net.josh.wungus.worldgen.tree.ModFoliagePlacerTypes;
import net.josh.wungus.worldgen.tree.ModTrunkPlacerTypes;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(WungusMod.MOD_ID)
public class WungusMod
{
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "wungus";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public WungusMod(IEventBus modEventBus, ModContainer modContainer)
    {
        ModEntities.register(modEventBus);

        modEventBus.addListener(this::commonSetup);

        ModCreativeModeTabs.register(modEventBus);

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);

        ModLootModifiers.register(modEventBus);

        ModEffects.register(modEventBus);

        ModSounds.register(modEventBus);

        ModParticles.register(modEventBus);

        ModBlockEntities.register(modEventBus);

        ModTrunkPlacerTypes.register(modEventBus);
        ModFoliagePlacerTypes.register(modEventBus);

        ModVillagers.register(modEventBus);

        NeoForge.EVENT_BUS.register(this);
        //modEventBus.addListener(this::addCreative);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {

    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {

    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {

    }

    // Client only setup (renderers, wood type textures, ...) lives in event/ModEventBusClientEvents
}
