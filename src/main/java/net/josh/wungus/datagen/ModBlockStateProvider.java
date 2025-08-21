package net.josh.wungus.datagen;

import net.josh.wungus.WungusMod;
import net.josh.wungus.block.ModBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, WungusMod.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        logBlock(((RotatedPillarBlock) ModBlocks.AILANTHUS_LOG.get()));
        axisBlock(((RotatedPillarBlock) ModBlocks.AILANTHUS_WOOD.get()), blockTexture(ModBlocks.AILANTHUS_LOG.get()), blockTexture(ModBlocks.AILANTHUS_LOG.get()));

        axisBlock(((RotatedPillarBlock) ModBlocks.STRIPPED_AILANTHUS_LOG.get()), blockTexture(ModBlocks.STRIPPED_AILANTHUS_LOG.get()),
                new ResourceLocation(WungusMod.MOD_ID, "block/stripped_ailanthus_log_top"));
        axisBlock(((RotatedPillarBlock) ModBlocks.STRIPPED_AILANTHUS_WOOD.get()), blockTexture(ModBlocks.STRIPPED_AILANTHUS_LOG.get()),
                blockTexture(ModBlocks.STRIPPED_AILANTHUS_LOG.get()));

        blockItem(ModBlocks.AILANTHUS_LOG);
        blockItem(ModBlocks.AILANTHUS_WOOD);
        blockItem(ModBlocks.STRIPPED_AILANTHUS_LOG);
        blockItem(ModBlocks.STRIPPED_AILANTHUS_WOOD);

        blockWithItem(ModBlocks.AILANTHUS_PLANKS);

        leavesBlock(ModBlocks.AILANTHUS_LEAVES);

        signBlock(((StandingSignBlock) ModBlocks.AILANTHUS_SIGN.get()), ((WallSignBlock) ModBlocks.AILANTHUS_WALL_SIGN.get()),
                blockTexture(ModBlocks.AILANTHUS_PLANKS.get()));

        hangingSignBlock(ModBlocks.AILANTHUS_HANGING_SIGN.get(), ModBlocks.AILANTHUS_WALL_HANGING_SIGN.get(), blockTexture(ModBlocks.AILANTHUS_PLANKS.get()));
    }

    private void leavesBlock(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(),
                models().singleTexture(ForgeRegistries.BLOCKS.getKey(blockRegistryObject.get()).getPath(), new ResourceLocation("minecraft:block/leaves"),
                        "all", blockTexture(blockRegistryObject.get())).renderType("cutout"));
    }

    private void blockItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockItem(blockRegistryObject.get(), new ModelFile.UncheckedModelFile(WungusMod.MOD_ID +
                ":block/" + ForgeRegistries.BLOCKS.getKey(blockRegistryObject.get()).getPath()));
    }

    private void blockWithItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }

    public void hangingSignBlock(Block signBlock, Block wallSignBlock, ResourceLocation texture) {
        ModelFile sign = models().sign(name(signBlock), texture);
        hangingSignBlock(signBlock, wallSignBlock, sign);
    }

    public void hangingSignBlock(Block signBlock, Block wallSignBlock, ModelFile sign) {
        simpleBlock(signBlock, sign);
        simpleBlock(wallSignBlock, sign);
    }

    private String name(Block block) {
        return key(block).getPath();
    }

    private ResourceLocation key(Block block) {
        return ForgeRegistries.BLOCKS.getKey(block);
    }

}


