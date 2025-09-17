package net.josh.wungus.block;

import net.josh.wungus.WungusMod;
import net.josh.wungus.block.custom.*;
import net.josh.wungus.item.ModItems;
import net.josh.wungus.util.ModWoodTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
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


    public static final RegistryObject<Block> AILANTHUS_LOG = registerBlock("ailanthus_log",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG).strength(3f)));
    public static final RegistryObject<Block> AILANTHUS_WOOD = registerBlock("ailanthus_wood",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_WOOD).strength(3f)));
    public static final RegistryObject<Block> STRIPPED_AILANTHUS_LOG = registerBlock("stripped_ailanthus_log",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_OAK_LOG).strength(3f)));
    public static final RegistryObject<Block> STRIPPED_AILANTHUS_WOOD = registerBlock("stripped_ailanthus_wood",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_OAK_WOOD).strength(3f)));

    public static final RegistryObject<Block> AILANTHUS_PLANKS = registerBlock("ailanthus_planks",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)){
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

    public static final RegistryObject<Block> AILANTHUS_STAIRS = registerBlock("ailanthus_stairs",
            () -> new StairBlock(() -> ModBlocks.AILANTHUS_PLANKS.get().defaultBlockState(),
                    BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).sound(SoundType.WOOD)));
    public static final RegistryObject<Block> AILANTHUS_SLAB = registerBlock("ailanthus_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).sound(SoundType.WOOD)));

    public static final RegistryObject<Block> AILANTHUS_BUTTON = registerBlock("ailanthus_button",
            () -> new ButtonBlock(BlockBehaviour.Properties.copy(Blocks.OAK_BUTTON).sound(SoundType.WOOD),
                    BlockSetType.OAK, 10, true));
    public static final RegistryObject<Block> AILANTHUS_PRESSURE_PLATE = registerBlock("ailanthus_pressure_plate",
            () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).sound(SoundType.WOOD),
                    BlockSetType.OAK));

    public static final RegistryObject<Block> AILANTHUS_FENCE = registerBlock("ailanthus_fence",
            () -> new FenceBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).sound(SoundType.WOOD)));
    public static final RegistryObject<Block> AILANTHUS_FENCE_GATE = registerBlock("ailanthus_fence_gate",
            () -> new FenceGateBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).sound(SoundType.WOOD), SoundEvents.FENCE_GATE_OPEN, SoundEvents.WOOD_BREAK));

    public static final RegistryObject<Block> AILANTHUS_DOOR = registerBlock("ailanthus_door",
            () -> new DoorBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).sound(SoundType.WOOD).noOcclusion(), BlockSetType.OAK));
    public static final RegistryObject<Block> AILANTHUS_TRAPDOOR = registerBlock("ailanthus_trapdoor",
            () -> new TrapDoorBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).sound(SoundType.WOOD).noOcclusion(), BlockSetType.OAK));

    public static final RegistryObject<Block> AILANTHUS_LEAVES = registerBlock("ailanthus_leaves",
            () -> new LeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES)){
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

    public static final RegistryObject<Block> AILANTHUS_LEAVES_2 = registerBlock("ailanthus_leaves_2",
            () -> new LeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES)){
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

    public static final RegistryObject<Block> AILANTHUS_SIGN = registerBlockNoItem("ailanthus_sign",
            () -> new ModStandingSignBlock(BlockBehaviour.Properties.copy(Blocks.OAK_SIGN), ModWoodTypes.AILANTHUS));
    public static final RegistryObject<Block> AILANTHUS_WALL_SIGN = registerBlockNoItem("ailanthus_wall_sign",
            () -> new ModWallSignBlock(BlockBehaviour.Properties.copy(Blocks.OAK_WALL_SIGN), ModWoodTypes.AILANTHUS));
    public static final RegistryObject<Block> AILANTHUS_HANGING_SIGN = registerBlockNoItem("ailanthus_hanging_sign",
            () -> new ModHangingSignBlock(BlockBehaviour.Properties.copy(Blocks.OAK_HANGING_SIGN), ModWoodTypes.AILANTHUS));
    public static final RegistryObject<Block> AILANTHUS_WALL_HANGING_SIGN = registerBlockNoItem("ailanthus_wall_hanging_sign",
            () -> new ModWallHangingSignBlock(BlockBehaviour.Properties.copy(Blocks.OAK_WALL_HANGING_SIGN), ModWoodTypes.AILANTHUS));

    public static final RegistryObject<Block> ANDARAN_GRASS_BLOCK = registerBlock("andaran_grass_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.GRASS_BLOCK).sound(SoundType.GRASS)));
    public static final RegistryObject<Block> ANDARAN_DIRT = registerBlock("andaran_dirt",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.DIRT).sound(SoundType.GRASS)));

    private static <T extends Block> RegistryObject<T> registerBlockNoItem(String name, Supplier<T> block) {
        return BLOCKS.register(name, block);
    }

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
