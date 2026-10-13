package net.josh.wungus.item;

import net.josh.wungus.WungusMod;
import net.josh.wungus.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, WungusMod.MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> WUNGUS_TAB = CREATIVE_MODE_TABS.register("tutorial_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.WUNGUS_SPAWN_EGG.get()))
                    .title(Component.translatable("creativetab.wungus_tab"))
                    .displayItems((pParameters, pOutput) -> {
                        pOutput.accept(ModItems.WUNGUS_SPAWN_EGG.get());
                        pOutput.accept(ModItems.LANTERNFLY_SPAWN_EGG.get());
                        pOutput.accept(ModItems.WUNGUS_MILK.get());
                        pOutput.accept(ModItems.WUNGUS_HIDE.get());
                        pOutput.accept(ModBlocks.WUNGUS_EGG.get());
                        pOutput.accept(ModBlocks.QUARTZ_LANDMINE.get());
                        pOutput.accept(ModBlocks.SAND_LANDMINE.get());
                        pOutput.accept(ModBlocks.WUNGUS_STATUE.get());
                        pOutput.accept(ModBlocks.STONE_STATUE.get());
                        pOutput.accept(ModBlocks.GOLD_STATUE.get());
                        pOutput.accept(ModBlocks.GLOWSTONE_STATUE.get());
                        pOutput.accept(ModBlocks.WUNGUS_HEDGE.get());
                        pOutput.accept(ModBlocks.FRAME_CORNER_R.get());
                        pOutput.accept(ModBlocks.FRAME_CORNER_L.get());
                        pOutput.accept(ModBlocks.FRAME_MIDDLE.get());
                        pOutput.accept(ModBlocks.FRAME_SIDE.get());
                        pOutput.accept(ModBlocks.BOARD_1.get());
                        pOutput.accept(ModBlocks.BOARD_2.get());
                        pOutput.accept(ModBlocks.BOARD_3.get());
                        pOutput.accept(ModBlocks.BOARD_4.get());
                        pOutput.accept(ModBlocks.BOARD_5.get());
                        pOutput.accept(ModBlocks.BOARD_6.get());
                        pOutput.accept(ModBlocks.BOARD_7.get());
                        pOutput.accept(ModBlocks.BOARD_8.get());
                        pOutput.accept(ModBlocks.BOARD_9.get());
                        pOutput.accept(ModItems.WUNGUS_BOOTS.get());
                        pOutput.accept(ModItems.WUNGUS_MASK.get());
                        pOutput.accept(ModItems.BBL.get());
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
                        pOutput.accept(ModItems.MOON_DISC.get());
                        pOutput.accept(ModItems.CONCERTO_DISC.get());
                        pOutput.accept(ModItems.MINGLE_DISC.get());
                        pOutput.accept(ModItems.WUNGUS_RACING_DISC.get());
                        pOutput.accept(ModItems.JUMP_ROPE_DISC.get());
                        pOutput.accept(ModItems.WUNGUS_AMBROSIA.get());
                        pOutput.accept(ModBlocks.SANDSTONE_LAMP.get());
                        pOutput.accept(ModBlocks.QUARTZ_LAMP.get());
                        pOutput.accept(ModBlocks.TERRACOTTA_LAMP.get());
                        pOutput.accept(ModBlocks.Y_WOOL_LAMP.get());
                        pOutput.accept(ModBlocks.B_WOOL_LAMP.get());
                        pOutput.accept(ModBlocks.LB_CONCRETE_LAMP.get());
                        pOutput.accept(ModBlocks.L_CONCRETE_LAMP.get());
                        pOutput.accept(ModBlocks.P_CONCRETE_LAMP.get());
                        pOutput.accept(ModBlocks.AILANTHUS_LOG.get());
                        pOutput.accept(ModBlocks.STRIPPED_AILANTHUS_LOG.get());
                        pOutput.accept(ModBlocks.AILANTHUS_WOOD.get());
                        pOutput.accept(ModBlocks.STRIPPED_AILANTHUS_WOOD.get());
                        pOutput.accept(ModBlocks.INFESTED_AILANTHUS_LOG.get());
                        pOutput.accept(ModBlocks.AILANTHUS_LEAVES.get());
                        pOutput.accept(ModBlocks.AILANTHUS_LEAVES_2.get());
                        pOutput.accept(ModBlocks.AILANTHUS_PLANKS.get());
                        pOutput.accept(ModBlocks.AILANTHUS_SIGN.get());
                        pOutput.accept(ModBlocks.AILANTHUS_HANGING_SIGN.get());
                        pOutput.accept(ModBlocks.AILANTHUS_FENCE.get());
                        pOutput.accept(ModBlocks.AILANTHUS_FENCE_GATE.get());
                        pOutput.accept(ModBlocks.AILANTHUS_SLAB.get());
                        pOutput.accept(ModBlocks.AILANTHUS_STAIRS.get());
                        pOutput.accept(ModBlocks.AILANTHUS_BUTTON.get());
                        pOutput.accept(ModBlocks.AILANTHUS_PRESSURE_PLATE.get());
                        pOutput.accept(ModBlocks.AILANTHUS_DOOR.get());
                        pOutput.accept(ModBlocks.AILANTHUS_TRAPDOOR.get());
                        pOutput.accept(ModBlocks.AILANTHUS_SAPLING.get());
                        pOutput.accept(ModBlocks.ANDARAN_GRASS_BLOCK.get());
                        pOutput.accept(ModBlocks.ANDARAN_DIRT.get());
                        pOutput.accept(ModBlocks.BBL_TABLE.get());
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}