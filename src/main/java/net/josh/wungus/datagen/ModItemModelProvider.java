package net.josh.wungus.datagen;

import net.josh.wungus.WungusMod;
import net.josh.wungus.block.ModBlocks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;

/**
 * Generates the item models of the ailanthus and andaran block items.
 * The other item models are hand made, see src/main/resources/assets/wungus/models/item.
 */
public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, WungusMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        evenSimplerBlockItem(ModBlocks.ANDARAN_DIRT);
        evenSimplerBlockItem(ModBlocks.ANDARAN_GRASS_BLOCK);

        simpleBlockItem(ModBlocks.AILANTHUS_DOOR);

        fenceItem(ModBlocks.AILANTHUS_FENCE, ModBlocks.AILANTHUS_PLANKS);
        buttonItem(ModBlocks.AILANTHUS_BUTTON, ModBlocks.AILANTHUS_PLANKS);

        evenSimplerBlockItem(ModBlocks.AILANTHUS_STAIRS);
        evenSimplerBlockItem(ModBlocks.AILANTHUS_SLAB);
        evenSimplerBlockItem(ModBlocks.AILANTHUS_PRESSURE_PLATE);
        evenSimplerBlockItem(ModBlocks.AILANTHUS_FENCE_GATE);

        trapdoorItem(ModBlocks.AILANTHUS_TRAPDOOR);

        saplingItem(ModBlocks.AILANTHUS_SAPLING);
    }

    private ItemModelBuilder saplingItem(DeferredBlock<Block> item) {
        return withExistingParent(item.getId().getPath(),
                ResourceLocation.withDefaultNamespace("item/generated")).texture("layer0",
                ResourceLocation.fromNamespaceAndPath(WungusMod.MOD_ID, "block/" + item.getId().getPath()));
    }

    private ItemModelBuilder simpleBlockItem(DeferredBlock<Block> item) {
        return withExistingParent(item.getId().getPath(),
                ResourceLocation.withDefaultNamespace("item/generated")).texture("layer0",
                ResourceLocation.fromNamespaceAndPath(WungusMod.MOD_ID, "item/" + item.getId().getPath()));
    }

    public void trapdoorItem(DeferredBlock<Block> block) {
        this.withExistingParent(block.getId().getPath(), modLoc("block/" + block.getId().getPath() + "_bottom"));
    }

    public void fenceItem(DeferredBlock<Block> block, DeferredBlock<Block> baseBlock) {
        this.withExistingParent(block.getId().getPath(), mcLoc("block/fence_inventory"))
                .texture("texture", ResourceLocation.fromNamespaceAndPath(WungusMod.MOD_ID, "block/" + baseBlock.getId().getPath()));
    }

    public void buttonItem(DeferredBlock<Block> block, DeferredBlock<Block> baseBlock) {
        this.withExistingParent(block.getId().getPath(), mcLoc("block/button_inventory"))
                .texture("texture", ResourceLocation.fromNamespaceAndPath(WungusMod.MOD_ID, "block/" + baseBlock.getId().getPath()));
    }

    public void evenSimplerBlockItem(DeferredBlock<Block> block) {
        this.withExistingParent(WungusMod.MOD_ID + ":" + block.getId().getPath(),
                modLoc("block/" + block.getId().getPath()));
    }
}
