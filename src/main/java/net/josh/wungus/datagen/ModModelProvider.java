package net.josh.wungus.datagen;

import net.josh.wungus.WungusMod;
import net.josh.wungus.block.ModBlocks;
import net.josh.wungus.item.ModItems;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.data.models.model.TexturedModel;
import net.minecraft.core.Holder;
import net.minecraft.data.BlockFamily;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.template.ExtendedModelTemplateBuilder;

import java.util.Set;
import java.util.stream.Stream;

/**
 * Generates the block states, block models, item models and client item definitions (assets/wungus/items).
 * Blocks with hand made models and block states (statues, eggs, ...) live in src/main/resources and are skipped here,
 * their items still get a client item pointing at the hand made block model.
 */
public class ModModelProvider extends ModelProvider {
    // The vanilla wall templates, but with the "top" texture on their upward faces,
    // see src/main/resources/assets/wungus/models/block/template_wall_*_with_top.json
    private static final ModelTemplate WALL_POST_WITH_TOP = wallTemplateWithTop(ModelTemplates.WALL_POST, "template_wall_post_with_top");
    private static final ModelTemplate WALL_LOW_SIDE_WITH_TOP = wallTemplateWithTop(ModelTemplates.WALL_LOW_SIDE, "template_wall_side_with_top");
    private static final ModelTemplate WALL_TALL_SIDE_WITH_TOP = wallTemplateWithTop(ModelTemplates.WALL_TALL_SIDE, "template_wall_side_tall_with_top");
    private static final ModelTemplate WALL_INVENTORY_WITH_TOP = wallTemplateWithTop(ModelTemplates.WALL_INVENTORY, "wall_inventory_with_top");

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
        infestedLog(blockModels, ModBlocks.INFESTED_AILANTHUS_LOG.get(), ModBlocks.AILANTHUS_LOG.get());

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

        // Like vanilla stone, only the andaran stone gets a button and a pressure plate
        BlockFamily andaranStoneFamily = new BlockFamily.Builder(ModBlocks.ANDARAN_STONE.get())
                .stairs(ModBlocks.ANDARAN_STONE_STAIRS.get())
                .slab(ModBlocks.ANDARAN_STONE_SLAB.get())
                .wall(ModBlocks.ANDARAN_STONE_WALL.get())
                .button(ModBlocks.ANDARAN_STONE_BUTTON.get())
                .pressurePlate(ModBlocks.ANDARAN_STONE_PRESSURE_PLATE.get())
                .getFamily();
        blockModels.family(andaranStoneFamily.getBaseBlock()).generateFor(andaranStoneFamily);

        BlockFamily andaranCobblestoneFamily = new BlockFamily.Builder(ModBlocks.ANDARAN_COBBLESTONE.get())
                .stairs(ModBlocks.ANDARAN_COBBLESTONE_STAIRS.get())
                .slab(ModBlocks.ANDARAN_COBBLESTONE_SLAB.get())
                .wall(ModBlocks.ANDARAN_COBBLESTONE_WALL.get())
                .getFamily();
        blockModels.family(andaranCobblestoneFamily.getBaseBlock()).generateFor(andaranCobblestoneFamily);

        BlockFamily smoothAndaranStoneFamily = new BlockFamily.Builder(ModBlocks.SMOOTH_ANDARAN_STONE.get())
                .stairs(ModBlocks.SMOOTH_ANDARAN_STONE_STAIRS.get())
                .slab(ModBlocks.SMOOTH_ANDARAN_STONE_SLAB.get())
                .wall(ModBlocks.SMOOTH_ANDARAN_STONE_WALL.get())
                .getFamily();
        blockModels.family(smoothAndaranStoneFamily.getBaseBlock()).generateFor(smoothAndaranStoneFamily);

        andaranStoneBricks(blockModels);

        /* ITEMS */
        itemModels.generateFlatItem(ModItems.WUNGUS_SPAWN_EGG.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.LANTERNFLY_SPAWN_EGG.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.WUNGING_GIANT_SPAWN_EGG.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.WUNGUS_MILK.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.WUNGUS_HIDE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.WUNGUS_AMBROSIA.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.WUNGUS_BOOTS.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.WUNGUS_MASK.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.BBL.get(), ModelTemplates.FLAT_ITEM);
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

    // A log with its own side texture and the top texture of the normal log. All lanternfly counts look the same.
    private static void infestedLog(BlockModelGenerators blockModels, Block block, Block normalLog) {
        TextureMapping textures = TextureMapping.column(TextureMapping.getBlockTexture(block),
                TextureMapping.getBlockTexture(normalLog, "_top"));
        Identifier vertical = ModelTemplates.CUBE_COLUMN.create(block, textures, blockModels.modelOutput);
        Identifier horizontal = ModelTemplates.CUBE_COLUMN_HORIZONTAL.create(block, textures, blockModels.modelOutput);
        blockModels.blockStateOutput.accept(BlockModelGenerators.createRotatedPillarWithHorizontalVariant(block,
                BlockModelGenerators.plainVariant(vertical), BlockModelGenerators.plainVariant(horizontal)));
    }

    // The andaran stone bricks (and their stairs, slab and wall) have the alt brick texture
    // (andaran_stone_bricks_top) on their top faces and the normal brick texture everywhere else
    private static void andaranStoneBricks(BlockModelGenerators blockModels) {
        Block bricks = ModBlocks.ANDARAN_STONE_BRICKS.get();
        TextureMapping textures = new TextureMapping()
                .put(TextureSlot.SIDE, TextureMapping.getBlockTexture(bricks))
                .put(TextureSlot.BOTTOM, TextureMapping.getBlockTexture(bricks))
                .put(TextureSlot.TOP, TextureMapping.getBlockTexture(bricks, "_top"))
                .put(TextureSlot.WALL, TextureMapping.getBlockTexture(bricks));

        BlockFamily family = new BlockFamily.Builder(bricks)
                .stairs(ModBlocks.ANDARAN_STONE_BRICK_STAIRS.get())
                .slab(ModBlocks.ANDARAN_STONE_BRICK_SLAB.get())
                .getFamily();
        blockModels.new BlockFamilyProvider(textures).fullBlock(bricks, ModelTemplates.CUBE_BOTTOM_TOP).generateFor(family);

        // The vanilla wall templates only have one texture, so the wall uses copies of them with a top texture
        Block wall = ModBlocks.ANDARAN_STONE_BRICK_WALL.get();
        blockModels.blockStateOutput.accept(BlockModelGenerators.createWall(wall,
                BlockModelGenerators.plainVariant(WALL_POST_WITH_TOP.create(wall, textures, blockModels.modelOutput)),
                BlockModelGenerators.plainVariant(WALL_LOW_SIDE_WITH_TOP.create(wall, textures, blockModels.modelOutput)),
                BlockModelGenerators.plainVariant(WALL_TALL_SIDE_WITH_TOP.create(wall, textures, blockModels.modelOutput))));
        blockModels.registerSimpleItemModel(wall, WALL_INVENTORY_WITH_TOP.create(wall, textures, blockModels.modelOutput));
    }

    private static ModelTemplate wallTemplateWithTop(ModelTemplate vanillaTemplate, String parent) {
        return ExtendedModelTemplateBuilder.of(vanillaTemplate)
                .parent(Identifier.fromNamespaceAndPath(WungusMod.MOD_ID, "block/" + parent))
                .requiredTextureSlot(TextureSlot.TOP)
                .build();
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
