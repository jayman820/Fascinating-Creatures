package net.josh.wungus.block;

import net.josh.wungus.WungusMod;
import net.josh.wungus.block.custom.*;
import net.josh.wungus.item.ModItems;
import net.josh.wungus.util.ModWoodTypes;
import net.josh.wungus.worldgen.tree.ModTreeGrowers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;
import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(WungusMod.MOD_ID);

    public static final DeferredBlock<Block> WUNGUS_EGG = registerBlock("wungus_egg",
            properties -> new WungusEgg(properties.sound(SoundType.METAL).randomTicks()),
            () -> BlockBehaviour.Properties.ofFullCopy(Blocks.SAND));

    public static final DeferredBlock<Block> WUNGUS_STATUE = registerBlock("wungus_statue",
            properties -> new WungusStatue(properties.noOcclusion().sound(SoundType.STONE).randomTicks()),
            () -> BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS_PANE));

    public static final DeferredBlock<Block> STONE_STATUE = registerBlock("stone_statue",
            properties -> new WungusStatue(properties.noOcclusion().sound(SoundType.STONE).randomTicks()),
            () -> BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS_PANE));

    public static final DeferredBlock<Block> GLOWSTONE_STATUE = registerBlock("glowstone_statue",
            properties -> new WungusStatue(properties.noOcclusion().lightLevel(s -> 15).sound(SoundType.STONE).randomTicks()),
            () -> BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS_PANE));

    public static final DeferredBlock<Block> GOLD_STATUE = registerBlock("gold_statue",
            properties -> new WungusStatue(properties.noOcclusion().sound(SoundType.STONE).randomTicks()),
            () -> BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS_PANE));

    public static final DeferredBlock<Block> WUNGUS_HEDGE = registerBlock("wungus_hedge",
            properties -> new WungusStatue(properties.noOcclusion().sound(SoundType.AZALEA_LEAVES).randomTicks()),
            () -> BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS_PANE));

    public static final DeferredBlock<Block> BBL_TABLE = registerBlock("bbl_table",
            properties -> new BBLTable(properties.noOcclusion().sound(SoundType.METAL).randomTicks()),
            () -> BlockBehaviour.Properties.ofFullCopy(Blocks.SMITHING_TABLE));


    public static final DeferredBlock<Block> AILANTHUS_LOG = registerBlock("ailanthus_log",
            properties -> new ModFlammableRotatedPillarBlock(properties.strength(3f)),
            () -> BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LOG));
    public static final DeferredBlock<Block> AILANTHUS_WOOD = registerBlock("ailanthus_wood",
            properties -> new ModFlammableRotatedPillarBlock(properties.strength(3f)),
            () -> BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WOOD));
    public static final DeferredBlock<Block> STRIPPED_AILANTHUS_LOG = registerBlock("stripped_ailanthus_log",
            properties -> new ModFlammableRotatedPillarBlock(properties.strength(3f)),
            () -> BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_LOG));
    public static final DeferredBlock<Block> STRIPPED_AILANTHUS_WOOD = registerBlock("stripped_ailanthus_wood",
            properties -> new ModFlammableRotatedPillarBlock(properties.strength(3f)),
            () -> BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_WOOD));

    public static final DeferredBlock<Block> AILANTHUS_PLANKS = registerBlock("ailanthus_planks",
            properties -> new Block(properties){
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
            }, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS));

    public static final DeferredBlock<Block> AILANTHUS_SAPLING = registerBlock("ailanthus_sapling",
            properties -> new SaplingBlock(ModTreeGrowers.AILANTHUS, properties),
            () -> BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SAPLING));

    public static final DeferredBlock<Block> AILANTHUS_STAIRS = registerBlock("ailanthus_stairs",
            properties -> new StairBlock(ModBlocks.AILANTHUS_PLANKS.get().defaultBlockState(), properties.sound(SoundType.WOOD)),
            () -> BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS));
    public static final DeferredBlock<Block> AILANTHUS_SLAB = registerBlock("ailanthus_slab",
            properties -> new SlabBlock(properties.sound(SoundType.WOOD)),
            () -> BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS));

    public static final DeferredBlock<Block> AILANTHUS_BUTTON = registerBlock("ailanthus_button",
            properties -> new ButtonBlock(BlockSetType.OAK, 10, properties.sound(SoundType.WOOD)),
            () -> BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_BUTTON));
    public static final DeferredBlock<Block> AILANTHUS_PRESSURE_PLATE = registerBlock("ailanthus_pressure_plate",
            properties -> new PressurePlateBlock(BlockSetType.OAK, properties.sound(SoundType.WOOD)),
            () -> BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS));

    public static final DeferredBlock<Block> AILANTHUS_FENCE = registerBlock("ailanthus_fence",
            properties -> new FenceBlock(properties.sound(SoundType.WOOD)),
            () -> BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS));
    public static final DeferredBlock<Block> AILANTHUS_FENCE_GATE = registerBlock("ailanthus_fence_gate",
            properties -> new FenceGateBlock(ModWoodTypes.AILANTHUS, properties.sound(SoundType.WOOD)),
            () -> BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS));

    public static final DeferredBlock<Block> AILANTHUS_DOOR = registerBlock("ailanthus_door",
            properties -> new DoorBlock(BlockSetType.OAK, properties.sound(SoundType.WOOD).noOcclusion()),
            () -> BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS));
    public static final DeferredBlock<Block> AILANTHUS_TRAPDOOR = registerBlock("ailanthus_trapdoor",
            properties -> new TrapDoorBlock(BlockSetType.OAK, properties.sound(SoundType.WOOD).noOcclusion()),
            () -> BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS));

    // The leaf textures are not tinted, so the falling leaf particles use a fixed color matching the texture
    public static final DeferredBlock<Block> AILANTHUS_LEAVES = registerBlock("ailanthus_leaves",
            properties -> new UntintedParticleLeavesBlock(0.01F, ColorParticleOption.create(ParticleTypes.TINTED_LEAVES, 0xFF987D3D), properties){
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
            }, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES));

    public static final DeferredBlock<Block> AILANTHUS_LEAVES_2 = registerBlock("ailanthus_leaves_2",
            properties -> new UntintedParticleLeavesBlock(0.01F, ColorParticleOption.create(ParticleTypes.TINTED_LEAVES, 0xFF7D8639), properties){
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
            }, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES));

    // Signs use the vanilla sign block entities, the blocks are added to them in ModEventBusEvents.
    // Like vanilla, the wall variants drop and are named after the standing variants.
    public static final DeferredBlock<Block> AILANTHUS_SIGN = registerBlockNoItem("ailanthus_sign",
            properties -> new StandingSignBlock(ModWoodTypes.AILANTHUS, properties),
            () -> BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SIGN));
    public static final DeferredBlock<Block> AILANTHUS_WALL_SIGN = registerBlockNoItem("ailanthus_wall_sign",
            properties -> new WallSignBlock(ModWoodTypes.AILANTHUS, properties),
            () -> wallVariant(AILANTHUS_SIGN.get(), Blocks.OAK_WALL_SIGN));
    public static final DeferredBlock<Block> AILANTHUS_HANGING_SIGN = registerBlockNoItem("ailanthus_hanging_sign",
            properties -> new CeilingHangingSignBlock(ModWoodTypes.AILANTHUS, properties),
            () -> BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_HANGING_SIGN));
    public static final DeferredBlock<Block> AILANTHUS_WALL_HANGING_SIGN = registerBlockNoItem("ailanthus_wall_hanging_sign",
            properties -> new WallHangingSignBlock(ModWoodTypes.AILANTHUS, properties),
            () -> wallVariant(AILANTHUS_HANGING_SIGN.get(), Blocks.OAK_WALL_HANGING_SIGN));

    public static final DeferredBlock<Block> ANDARAN_GRASS_BLOCK = registerBlock("andaran_grass_block",
            properties -> new Block(properties.sound(SoundType.GRASS)),
            () -> BlockBehaviour.Properties.ofFullCopy(Blocks.GRASS_BLOCK));
    public static final DeferredBlock<Block> ANDARAN_DIRT = registerBlock("andaran_dirt",
            properties -> new Block(properties.sound(SoundType.GRASS)),
            () -> BlockBehaviour.Properties.ofFullCopy(Blocks.DIRT));

    private static BlockBehaviour.Properties wallVariant(Block standingBlock, Block vanillaWallBlock) {
        return BlockBehaviour.Properties.ofFullCopy(vanillaWallBlock)
                .overrideLootTable(standingBlock.getLootTable())
                .overrideDescription(standingBlock.getDescriptionId());
    }

    private static <T extends Block> DeferredBlock<T> registerBlockNoItem(String name, Function<BlockBehaviour.Properties, T> block, Supplier<BlockBehaviour.Properties> properties) {
        return BLOCKS.registerBlock(name, block, properties);
    }

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Function<BlockBehaviour.Properties, T> block, Supplier<BlockBehaviour.Properties> properties) {
        DeferredBlock<T> toReturn = BLOCKS.registerBlock(name, block, properties);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        ModItems.ITEMS.registerItem(name, properties -> new BlockItem(block.get(), properties.useBlockDescriptionPrefix()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
