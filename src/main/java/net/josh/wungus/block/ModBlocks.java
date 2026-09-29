package net.josh.wungus.block;

import net.josh.wungus.WungusMod;
import net.josh.wungus.block.custom.*;
import net.josh.wungus.item.ModItems;
import net.josh.wungus.util.ModWoodTypes;
import net.josh.wungus.worldgen.tree.ModTreeGrowers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(WungusMod.MOD_ID);

    public static final DeferredBlock<Block> WUNGUS_EGG = registerBlock("wungus_egg",
            () -> new WungusEgg(BlockBehaviour.Properties.ofFullCopy(Blocks.SAND).sound(SoundType.METAL).randomTicks()));

    public static final DeferredBlock<Block> WUNGUS_STATUE = registerBlock("wungus_statue",
            () -> new WungusStatue(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS_PANE).noOcclusion().sound(SoundType.STONE).randomTicks()));

    public static final DeferredBlock<Block> STONE_STATUE = registerBlock("stone_statue",
            () -> new WungusStatue(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS_PANE).noOcclusion().sound(SoundType.STONE).randomTicks()));

    public static final DeferredBlock<Block> GLOWSTONE_STATUE = registerBlock("glowstone_statue",
            () -> new WungusStatue(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS_PANE).noOcclusion().lightLevel(s -> 15).sound(SoundType.STONE).randomTicks()));

    public static final DeferredBlock<Block> GOLD_STATUE = registerBlock("gold_statue",
            () -> new WungusStatue(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS_PANE).noOcclusion().sound(SoundType.STONE).randomTicks()));

    public static final DeferredBlock<Block> WUNGUS_HEDGE = registerBlock("wungus_hedge",
            () -> new WungusStatue(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS_PANE).noOcclusion().sound(SoundType.AZALEA_LEAVES).randomTicks()));

    public static final DeferredBlock<Block> BBL_TABLE = registerBlock("bbl_table",
            () -> new BBLTable(BlockBehaviour.Properties.ofFullCopy(Blocks.SMITHING_TABLE).noOcclusion().sound(SoundType.METAL).randomTicks()));


    public static final DeferredBlock<Block> AILANTHUS_LOG = registerBlock("ailanthus_log",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LOG).strength(3f)));
    public static final DeferredBlock<Block> AILANTHUS_WOOD = registerBlock("ailanthus_wood",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WOOD).strength(3f)));
    public static final DeferredBlock<Block> STRIPPED_AILANTHUS_LOG = registerBlock("stripped_ailanthus_log",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_LOG).strength(3f)));
    public static final DeferredBlock<Block> STRIPPED_AILANTHUS_WOOD = registerBlock("stripped_ailanthus_wood",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_WOOD).strength(3f)));

    public static final DeferredBlock<Block> AILANTHUS_PLANKS = registerBlock("ailanthus_planks",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS)){
                @Override
                public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return true;
                }

                @Override
                public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return 20;
                }

                @Override
                public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return 5;
                }
            });

    public static final DeferredBlock<Block> AILANTHUS_SAPLING = registerBlock("ailanthus_sapling",
            () -> new SaplingBlock(ModTreeGrowers.AILANTHUS, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SAPLING)));

    public static final DeferredBlock<Block> AILANTHUS_STAIRS = registerBlock("ailanthus_stairs",
            () -> new StairBlock(ModBlocks.AILANTHUS_PLANKS.get().defaultBlockState(),
                    BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS).sound(SoundType.WOOD)));
    public static final DeferredBlock<Block> AILANTHUS_SLAB = registerBlock("ailanthus_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS).sound(SoundType.WOOD)));

    public static final DeferredBlock<Block> AILANTHUS_BUTTON = registerBlock("ailanthus_button",
            () -> new ButtonBlock(BlockSetType.OAK, 10, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_BUTTON).sound(SoundType.WOOD)));
    public static final DeferredBlock<Block> AILANTHUS_PRESSURE_PLATE = registerBlock("ailanthus_pressure_plate",
            () -> new PressurePlateBlock(BlockSetType.OAK, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS).sound(SoundType.WOOD)));

    public static final DeferredBlock<Block> AILANTHUS_FENCE = registerBlock("ailanthus_fence",
            () -> new FenceBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS).sound(SoundType.WOOD)));
    public static final DeferredBlock<Block> AILANTHUS_FENCE_GATE = registerBlock("ailanthus_fence_gate",
            () -> new FenceGateBlock(ModWoodTypes.AILANTHUS, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS).sound(SoundType.WOOD)));

    public static final DeferredBlock<Block> AILANTHUS_DOOR = registerBlock("ailanthus_door",
            () -> new DoorBlock(BlockSetType.OAK, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS).sound(SoundType.WOOD).noOcclusion()));
    public static final DeferredBlock<Block> AILANTHUS_TRAPDOOR = registerBlock("ailanthus_trapdoor",
            () -> new TrapDoorBlock(BlockSetType.OAK, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS).sound(SoundType.WOOD).noOcclusion()));

    public static final DeferredBlock<Block> AILANTHUS_LEAVES = registerBlock("ailanthus_leaves",
            () -> new LeavesBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES)){
                @Override
                public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return true;
                }

                @Override
                public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return 60;
                }

                @Override
                public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return 30;
                }
            });

    public static final DeferredBlock<Block> AILANTHUS_LEAVES_2 = registerBlock("ailanthus_leaves_2",
            () -> new LeavesBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES)){
                @Override
                public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return true;
                }

                @Override
                public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return 60;
                }

                @Override
                public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return 30;
                }
            });

    // Signs have their own block entities (see ModBlockEntities), which use the vanilla sign renderers
    public static final DeferredBlock<Block> AILANTHUS_SIGN = registerBlockNoItem("ailanthus_sign",
            () -> new ModStandingSignBlock(ModWoodTypes.AILANTHUS, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SIGN)));
    public static final DeferredBlock<Block> AILANTHUS_WALL_SIGN = registerBlockNoItem("ailanthus_wall_sign",
            () -> new ModWallSignBlock(ModWoodTypes.AILANTHUS, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WALL_SIGN)));
    public static final DeferredBlock<Block> AILANTHUS_HANGING_SIGN = registerBlockNoItem("ailanthus_hanging_sign",
            () -> new ModHangingSignBlock(ModWoodTypes.AILANTHUS, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_HANGING_SIGN)));
    public static final DeferredBlock<Block> AILANTHUS_WALL_HANGING_SIGN = registerBlockNoItem("ailanthus_wall_hanging_sign",
            () -> new ModWallHangingSignBlock(ModWoodTypes.AILANTHUS, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WALL_HANGING_SIGN)));

    public static final DeferredBlock<Block> ANDARAN_GRASS_BLOCK = registerBlock("andaran_grass_block",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.GRASS_BLOCK).sound(SoundType.GRASS)));
    public static final DeferredBlock<Block> ANDARAN_DIRT = registerBlock("andaran_dirt",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.DIRT).sound(SoundType.GRASS)));

    private static <T extends Block> DeferredBlock<T> registerBlockNoItem(String name, Supplier<T> block) {
        return BLOCKS.register(name, block);
    }

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
