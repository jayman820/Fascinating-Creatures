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

    public static final RegistryObject<Block> QUARTZ_LANDMINE = registerBlock("quartz_landmine",
            () -> new BlockLandmine(BlockBehaviour.Properties.copy(Blocks.QUARTZ_BLOCK).sound(SoundType.NETHER_ORE).randomTicks()));

    public static final RegistryObject<Block> SAND_LANDMINE = registerBlock("sand_landmine",
            () -> new BlockLandmine(BlockBehaviour.Properties.copy(Blocks.SAND).sound(SoundType.SAND).randomTicks()));

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

    public static final RegistryObject<Block> SANDSTONE_LAMP = registerBlock("sandstone_lamp",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.SANDSTONE).noOcclusion().sound(SoundType.STONE).lightLevel(s -> 15)));
    public static final RegistryObject<Block> QUARTZ_LAMP = registerBlock("quartz_lamp",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.QUARTZ_BLOCK).noOcclusion().sound(SoundType.STONE).lightLevel(s -> 15)));
    public static final RegistryObject<Block> TERRACOTTA_LAMP = registerBlock("terracotta_lamp",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.TERRACOTTA).noOcclusion().sound(SoundType.STONE).lightLevel(s -> 15)));
    public static final RegistryObject<Block> LB_CONCRETE_LAMP = registerBlock("lb_concrete_lamp",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.BLACK_CONCRETE).noOcclusion().sound(SoundType.STONE).lightLevel(s -> 15)));
    public static final RegistryObject<Block> L_CONCRETE_LAMP = registerBlock("l_concrete_lamp",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.CYAN_CONCRETE).noOcclusion().sound(SoundType.STONE).lightLevel(s -> 15)));
    public static final RegistryObject<Block> P_CONCRETE_LAMP = registerBlock("p_concrete_lamp",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.BLACK_CONCRETE).noOcclusion().sound(SoundType.STONE).lightLevel(s -> 15)));
    public static final RegistryObject<Block> Y_WOOL_LAMP = registerBlock("y_wool_lamp",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.BLACK_WOOL).noOcclusion().sound(SoundType.WOOL).lightLevel(s -> 15)));
    public static final RegistryObject<Block> B_WOOL_LAMP = registerBlock("b_wool_lamp",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.WHITE_WOOL).noOcclusion().sound(SoundType.WOOL).lightLevel(s -> 15)));

    public static final RegistryObject<Block> FRAME_CORNER_R = registerBlock("frame_corner_r",
            () -> new Frame(BlockBehaviour.Properties.copy(Blocks.STONE).noOcclusion().sound(SoundType.STONE)));
    public static final RegistryObject<Block> FRAME_CORNER_L = registerBlock("frame_corner_l",
            () -> new Frame(BlockBehaviour.Properties.copy(Blocks.STONE).noOcclusion().sound(SoundType.STONE)));
    public static final RegistryObject<Block> FRAME_MIDDLE = registerBlock("frame_middle",
            () -> new Frame(BlockBehaviour.Properties.copy(Blocks.STONE).noOcclusion().sound(SoundType.STONE)));
    public static final RegistryObject<Block> FRAME_SIDE = registerBlock("frame_side",
            () -> new Frame(BlockBehaviour.Properties.copy(Blocks.STONE).noOcclusion().sound(SoundType.STONE)));
    
    public static final RegistryObject<Block> BOARD_1 = registerBlock("board_1",
            () -> new NumberBoard(BlockBehaviour.Properties.copy(Blocks.STONE).noOcclusion().sound(SoundType.STONE)));
    public static final RegistryObject<Block> BOARD_2 = registerBlock("board_2",
            () -> new NumberBoard(BlockBehaviour.Properties.copy(Blocks.STONE).noOcclusion().sound(SoundType.STONE)));
    public static final RegistryObject<Block> BOARD_3 = registerBlock("board_3",
            () -> new NumberBoard(BlockBehaviour.Properties.copy(Blocks.STONE).noOcclusion().sound(SoundType.STONE)));
    public static final RegistryObject<Block> BOARD_4 = registerBlock("board_4",
            () -> new NumberBoard(BlockBehaviour.Properties.copy(Blocks.STONE).noOcclusion().sound(SoundType.STONE)));
    public static final RegistryObject<Block> BOARD_5 = registerBlock("board_5",
            () -> new NumberBoard(BlockBehaviour.Properties.copy(Blocks.STONE).noOcclusion().sound(SoundType.STONE)));
    public static final RegistryObject<Block> BOARD_6 = registerBlock("board_6",
            () -> new NumberBoard(BlockBehaviour.Properties.copy(Blocks.STONE).noOcclusion().sound(SoundType.STONE)));
    public static final RegistryObject<Block> BOARD_7 = registerBlock("board_7",
            () -> new NumberBoard(BlockBehaviour.Properties.copy(Blocks.STONE).noOcclusion().sound(SoundType.STONE)));
    public static final RegistryObject<Block> BOARD_8 = registerBlock("board_8",
            () -> new NumberBoard(BlockBehaviour.Properties.copy(Blocks.STONE).noOcclusion().sound(SoundType.STONE)));
    public static final RegistryObject<Block> BOARD_9 = registerBlock("board_9",
            () -> new NumberBoard(BlockBehaviour.Properties.copy(Blocks.STONE).noOcclusion().sound(SoundType.STONE)));


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
