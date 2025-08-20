package net.josh.wungus.block;

import net.josh.wungus.WungusMod;
import net.josh.wungus.block.custom.*;
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

    public static final RegistryObject<Block> WUNGUS_STATUE = registerBlock("wungus_statue",
            () -> new WungusStatue(BlockBehaviour.Properties.copy(Blocks.GLASS_PANE).noOcclusion().sound(SoundType.STONE).randomTicks()));

    public static final RegistryObject<Block> STONE_STATUE = registerBlock("stone_statue",
            () -> new WungusStatue(BlockBehaviour.Properties.copy(Blocks.GLASS_PANE).noOcclusion().sound(SoundType.STONE).randomTicks()));

    public static final RegistryObject<Block> GLOWSTONE_STATUE = registerBlock("glowstone_statue",
            () -> new WungusStatue(BlockBehaviour.Properties.copy(Blocks.GLASS_PANE).noOcclusion().lightLevel(s -> 15).sound(SoundType.STONE).randomTicks()));

    public static final RegistryObject<Block> GOLD_STATUE = registerBlock("gold_statue",
            () -> new WungusStatue(BlockBehaviour.Properties.copy(Blocks.GLASS_PANE).noOcclusion().sound(SoundType.STONE).randomTicks()));

    public static final RegistryObject<Block> WUNGUS_HEDGE = registerBlock("wungus_hedge",
            () -> new WungusStatue(BlockBehaviour.Properties.copy(Blocks.GLASS_PANE).noOcclusion().sound(SoundType.AZALEA_LEAVES).randomTicks()));



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
