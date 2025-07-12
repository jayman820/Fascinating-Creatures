package net.josh.wungus.block;

import net.josh.wungus.WungusMod;
import net.josh.wungus.block.custom.QuartzLandmine;
import net.josh.wungus.block.custom.WungusEgg;
import net.josh.wungus.block.custom.WungusStatue;
import net.josh.wungus.item.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

import static net.minecraft.world.item.Items.registerBlock;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, WungusMod.MOD_ID);

    public static final RegistryObject<Block> WUNGUS_EGG = registerBlock("wungus_egg",
            () -> new WungusEgg(BlockBehaviour.Properties.copy(Blocks.SAND).sound(SoundType.METAL).randomTicks()));

    public static final RegistryObject<Block> QUARTZ_LANDMINE = registerBlock("quartz_landmine",
            () -> new QuartzLandmine(BlockBehaviour.Properties.copy(Blocks.QUARTZ_BLOCK).sound(SoundType.NETHER_ORE).randomTicks()));

    public static final RegistryObject<Block> WUNGUS_STATUE = registerBlock("wungus_statue",
            () -> new WungusStatue(BlockBehaviour.Properties.copy(Blocks.GLASS_PANE).sound(SoundType.STONE).randomTicks()));

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
