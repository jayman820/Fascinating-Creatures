package net.josh.wungus.item;

import net.josh.wungus.WungusMod;
import net.josh.wungus.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, WungusMod.MOD_ID);

    public static final RegistryObject<CreativeModeTab> WUNGUS_TAB = CREATIVE_MODE_TABS.register("tutorial_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.WUNGUS_SPAWN_EGG.get()))
                    .title(Component.translatable("creativetab.wungus_tab"))
                    .displayItems((pParameters, pOutput) -> {
                        pOutput.accept(ModItems.WUNGUS_SPAWN_EGG.get());
                        pOutput.accept(ModItems.WUNGUS_MILK.get());
                        pOutput.accept(ModItems.WUNGUS_HIDE.get());
                        pOutput.accept(ModBlocks.WUNGUS_EGG.get());
                        pOutput.accept(ModBlocks.QUARTZ_LANDMINE.get());
                        pOutput.accept(ModBlocks.WUNGUS_STATUE.get());
                        pOutput.accept(ModItems.WUNGUS_BOOTS.get());
                        pOutput.accept(ModItems.RAW_WUNGUS_FLESH.get());
                        pOutput.accept(ModItems.COOKED_WUNGUS_FLESH.get());
                        pOutput.accept(ModItems.WUNGUS_SHAWARMA.get());
                        pOutput.accept(ModItems.PRATTLING_WUNGUS_1.get());
                        pOutput.accept(ModItems.PRATTLING_WUNGUS_2.get());
                        pOutput.accept(ModItems.PRATTLING_WUNGUS_3.get());
                        pOutput.accept(ModItems.PRATTLING_WUNGUS_4.get());
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}