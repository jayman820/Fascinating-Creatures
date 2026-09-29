package net.josh.wungus.datagen;

import net.josh.wungus.WungusMod;
import net.josh.wungus.block.ModBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;

/**
 * Generates the block states and block models of the ailanthus blocks.
 * Blocks with hand made models and block states (statues, eggs, ...) live in src/main/resources.
 */
public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, WungusMod.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        logBlock(((RotatedPillarBlock) ModBlocks.AILANTHUS_LOG.get()));
        axisBlock(((RotatedPillarBlock) ModBlocks.AILANTHUS_WOOD.get()), blockTexture(ModBlocks.AILANTHUS_LOG.get()), blockTexture(ModBlocks.AILANTHUS_LOG.get()));

        axisBlock(((RotatedPillarBlock) ModBlocks.STRIPPED_AILANTHUS_LOG.get()), blockTexture(ModBlocks.STRIPPED_AILANTHUS_LOG.get()),
                ResourceLocation.fromNamespaceAndPath(WungusMod.MOD_ID, "block/stripped_ailanthus_log_top"));
        axisBlock(((RotatedPillarBlock) ModBlocks.STRIPPED_AILANTHUS_WOOD.get()), blockTexture(ModBlocks.STRIPPED_AILANTHUS_LOG.get()),
                blockTexture(ModBlocks.STRIPPED_AILANTHUS_LOG.get()));

        blockItem(ModBlocks.AILANTHUS_LOG);
        blockItem(ModBlocks.AILANTHUS_WOOD);
        blockItem(ModBlocks.STRIPPED_AILANTHUS_LOG);
        blockItem(ModBlocks.STRIPPED_AILANTHUS_WOOD);

        blockWithItem(ModBlocks.AILANTHUS_PLANKS);

        leavesBlock(ModBlocks.AILANTHUS_LEAVES);
        leavesBlock(ModBlocks.AILANTHUS_LEAVES_2);

        saplingBlock(ModBlocks.AILANTHUS_SAPLING);

        stairsBlock(((StairBlock) ModBlocks.AILANTHUS_STAIRS.get()), blockTexture(ModBlocks.AILANTHUS_PLANKS.get()));
        slabBlock(((SlabBlock) ModBlocks.AILANTHUS_SLAB.get()), blockTexture(ModBlocks.AILANTHUS_PLANKS.get()), blockTexture(ModBlocks.AILANTHUS_PLANKS.get()));

        buttonBlock(((ButtonBlock) ModBlocks.AILANTHUS_BUTTON.get()), blockTexture(ModBlocks.AILANTHUS_PLANKS.get()));
        pressurePlateBlock(((PressurePlateBlock) ModBlocks.AILANTHUS_PRESSURE_PLATE.get()), blockTexture(ModBlocks.AILANTHUS_PLANKS.get()));

        fenceBlock(((FenceBlock) ModBlocks.AILANTHUS_FENCE.get()), blockTexture(ModBlocks.AILANTHUS_PLANKS.get()));
        fenceGateBlock(((FenceGateBlock) ModBlocks.AILANTHUS_FENCE_GATE.get()), blockTexture(ModBlocks.AILANTHUS_PLANKS.get()));

        doorBlockWithRenderType(((DoorBlock) ModBlocks.AILANTHUS_DOOR.get()), modLoc("block/ailanthus_door_bottom"), modLoc("block/ailanthus_door_top"), "cutout");
        trapdoorBlockWithRenderType(((TrapDoorBlock) ModBlocks.AILANTHUS_TRAPDOOR.get()), modLoc("block/ailanthus_trapdoor"), true, "cutout");

        signBlock(((StandingSignBlock) ModBlocks.AILANTHUS_SIGN.get()), ((WallSignBlock) ModBlocks.AILANTHUS_WALL_SIGN.get()),
                blockTexture(ModBlocks.AILANTHUS_PLANKS.get()));

        hangingSignBlock(ModBlocks.AILANTHUS_HANGING_SIGN.get(), ModBlocks.AILANTHUS_WALL_HANGING_SIGN.get(), blockTexture(ModBlocks.AILANTHUS_PLANKS.get()));
    }

    private void leavesBlock(DeferredBlock<Block> block) {
        simpleBlockWithItem(block.get(),
                models().singleTexture(blockName(block.get()), ResourceLocation.withDefaultNamespace("block/leaves"),
                        "all", blockTexture(block.get())).renderType("cutout"));
    }

    private void blockItem(DeferredBlock<Block> block) {
        simpleBlockItem(block.get(), new ModelFile.UncheckedModelFile(WungusMod.MOD_ID + ":block/" + blockName(block.get())));
    }

    private void blockWithItem(DeferredBlock<Block> block) {
        simpleBlockWithItem(block.get(), cubeAll(block.get()));
    }

    public void hangingSignBlock(Block signBlock, Block wallSignBlock, ResourceLocation texture) {
        ModelFile sign = models().sign(blockName(signBlock), texture);
        simpleBlock(signBlock, sign);
        simpleBlock(wallSignBlock, sign);
    }

    private void saplingBlock(DeferredBlock<Block> block) {
        simpleBlock(block.get(),
                models().cross(blockName(block.get()), blockTexture(block.get())).renderType("cutout"));
    }

    private String blockName(Block block) {
        return BuiltInRegistries.BLOCK.getKey(block).getPath();
    }
}
