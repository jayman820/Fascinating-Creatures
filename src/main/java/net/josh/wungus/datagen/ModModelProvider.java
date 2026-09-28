package net.josh.wungus.datagen;

import net.josh.wungus.WungusMod;
import net.josh.wungus.block.ModBlocks;
import net.josh.wungus.item.ModItems;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TexturedModel;
import net.minecraft.core.Holder;
import net.minecraft.data.BlockFamily;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.Set;
import java.util.stream.Stream;

/**
 * Generates the block states, block models, item models and client item definitions (assets/wungus/items).
 * Blocks with hand made models and block states (statues, eggs, ...) live in src/main/resources and are skipped here,
 * their items still get a client item pointing at the hand made block model.
 */
public class ModModelProvider extends ModelProvider {
    public ModModelProvider(PackOutput output) {
        super(output, WungusMod.MOD_ID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        /* BLOCKS */
        blockModels.woodProvider(ModBlocks.AILANTHUS_LOG.get()).logWithHorizontal(ModBlocks.AILANTHUS_LOG.get())
                .wood(ModBlocks.AILANTHUS_WOOD.get());
        blockModels.woodProvider(ModBlocks.STRIPPED_AILANTHUS_LOG.get()).logWithHorizontal(ModBlocks.STRIPPED_AILANTHUS_LOG.get())
                .wood(ModBlocks.STRIPPED_AILANTHUS_WOOD.get());

        BlockFamily ailanthusFamily = new BlockFamily.Builder(ModBlocks.AILANTHUS_PLANKS.get())
                .button(ModBlocks.AILANTHUS_BUTTON.get())
                .fence(ModBlocks.AILANTHUS_FENCE.get())
                .fenceGate(ModBlocks.AILANTHUS_FENCE_GATE.get())
                .pressurePlate(ModBlocks.AILANTHUS_PRESSURE_PLATE.get())
                .sign(ModBlocks.AILANTHUS_SIGN.get(), ModBlocks.AILANTHUS_WALL_SIGN.get())
                .slab(ModBlocks.AILANTHUS_SLAB.get())
                .stairs(ModBlocks.AILANTHUS_STAIRS.get())
                .door(ModBlocks.AILANTHUS_DOOR.get())
                .trapdoor(ModBlocks.AILANTHUS_TRAPDOOR.get())
                .getFamily();
        blockModels.family(ailanthusFamily.getBaseBlock()).generateFor(ailanthusFamily);

        blockModels.createHangingSign(ModBlocks.AILANTHUS_PLANKS.get(),
                ModBlocks.AILANTHUS_HANGING_SIGN.get(), ModBlocks.AILANTHUS_WALL_HANGING_SIGN.get());

        blockModels.createTrivialBlock(ModBlocks.AILANTHUS_LEAVES.get(), TexturedModel.LEAVES);
        blockModels.createTrivialBlock(ModBlocks.AILANTHUS_LEAVES_2.get(), TexturedModel.LEAVES);

        blockModels.createCrossBlockWithDefaultItem(ModBlocks.AILANTHUS_SAPLING.get(), BlockModelGenerators.PlantType.NOT_TINTED);

        /* ITEMS */
        itemModels.generateFlatItem(ModItems.WUNGUS_SPAWN_EGG.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.WUNGUS_MILK.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.WUNGUS_HIDE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.WUNGUS_AMBROSIA.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.WUNGUS_BOOTS.get(), ModelTemplates.FLAT_ITEM);
        // The mask and the bbl have no item texture of their own yet and use the boots texture
        flatItemWithTexture(itemModels, ModItems.WUNGUS_MASK.get(), ModItems.WUNGUS_BOOTS.get());
        flatItemWithTexture(itemModels, ModItems.BBL.get(), ModItems.WUNGUS_BOOTS.get());
        itemModels.generateFlatItem(ModItems.RAW_WUNGUS_FLESH.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.COOKED_WUNGUS_FLESH.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.WUNGUS_SHAWARMA.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.SANTONIO_CASHEW.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.HEALTH_STEROID.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.SPEED_STEROID.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.JUMP_STEROID.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.PRATTLING_WUNGUS_1.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.PRATTLING_WUNGUS_2.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.PRATTLING_WUNGUS_3.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.PRATTLING_WUNGUS_4.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModBlocks.WUNGUS_EGG.get().asItem(), ModelTemplates.FLAT_ITEM);

        // The remaining block items (statues, hedge, bbl table, andaran dirt, ...) automatically get
        // a client item pointing at their block model, see ModelProvider.ItemInfoCollector#finalizeAndValidate
    }

    private static void flatItemWithTexture(ItemModelGenerators itemModels, Item item, Item textureItem) {
        itemModels.itemModelOutput.accept(item, ItemModelUtils.plainModel(
                ModelTemplates.FLAT_ITEM.create(item, TextureMapping.layer0(textureItem), itemModels.modelOutput)));
    }

    // Blocks whose block states and models are hand made, see src/main/resources/assets/wungus/blockstates
    private static Set<Block> handMadeBlocks() {
        return Set.of(ModBlocks.ANDARAN_DIRT.get(), ModBlocks.ANDARAN_GRASS_BLOCK.get(), ModBlocks.BBL_TABLE.get(),
                ModBlocks.WUNGUS_STATUE.get(), ModBlocks.STONE_STATUE.get(), ModBlocks.GOLD_STATUE.get(),
                ModBlocks.GLOWSTONE_STATUE.get(), ModBlocks.WUNGUS_EGG.get(), ModBlocks.WUNGUS_HEDGE.get());
    }

    @Override
    protected Stream<? extends Holder<Block>> getKnownBlocks() {
        Set<Block> handMade = handMadeBlocks();
        return super.getKnownBlocks().filter(holder -> !handMade.contains(holder.value()));
    }
}
