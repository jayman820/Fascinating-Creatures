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
                        pOutput.accept(ModBlocks.WUNGUS_STATUE.get());
                        pOutput.accept(ModBlocks.STONE_STATUE.get());
                        pOutput.accept(ModBlocks.GOLD_STATUE.get());
                        pOutput.accept(ModBlocks.GLOWSTONE_STATUE.get());
                        pOutput.accept(ModBlocks.WUNGUS_HEDGE.get());
                        pOutput.accept(ModItems.WUNGUS_BOOTS.get());
                        pOutput.accept(ModItems.WUNGUS_MASK.get());
                        pOutput.accept(ModItems.RAW_WUNGUS_FLESH.get());
                        pOutput.accept(ModItems.COOKED_WUNGUS_FLESH.get());
                        pOutput.accept(ModItems.WUNGUS_SHAWARMA.get());
                        pOutput.accept(ModItems.HEALTH_STEROID.get());
                        pOutput.accept(ModItems.SPEED_STEROID.get());
                        pOutput.accept(ModItems.JUMP_STEROID.get());
                        pOutput.accept(ModItems.SANTONIO_CASHEW.get());
                        pOutput.accept(ModItems.PRATTLING_WUNGUS_1.get());
                        pOutput.accept(ModItems.PRATTLING_WUNGUS_2.get());
                        pOutput.accept(ModItems.PRATTLING_WUNGUS_3.get());
                        pOutput.accept(ModItems.PRATTLING_WUNGUS_4.get());
                        pOutput.accept(ModItems.WUNGUS_AMBROSIA.get());
                        pOutput.accept(ModBlocks.AILANTHUS_LOG.get());
                        pOutput.accept(ModBlocks.STRIPPED_AILANTHUS_LOG.get());
                        pOutput.accept(ModBlocks.AILANTHUS_WOOD.get());
                        pOutput.accept(ModBlocks.STRIPPED_AILANTHUS_WOOD.get());
                        pOutput.accept(ModBlocks.AILANTHUS_LEAVES.get());
                        pOutput.accept(ModBlocks.AILANTHUS_PLANKS.get());
                        pOutput.accept(ModBlocks.AILANTHUS_SIGN.get());
                        pOutput.accept(ModBlocks.AILANTHUS_HANGING_SIGN.get());
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}