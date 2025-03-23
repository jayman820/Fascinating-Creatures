package net.josh.wungus.datagen;

import net.josh.wungus.WungusMod;
import net.josh.wungus.block.ModBlocks;
import net.josh.wungus.effect.ModEffects;
import net.josh.wungus.entity.ModEntities;
import net.josh.wungus.entity.custom.WungusEntity;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider;

import net.josh.wungus.item.ModItems;

import java.util.function.Consumer;

public class ModAdvancementProvider implements ForgeAdvancementProvider.AdvancementGenerator {
    @Override
    public void generate(HolderLookup.Provider registries, Consumer<Advancement> saver, ExistingFileHelper existingFileHelper) {
        Advancement obtainWungusEgg = Advancement.Builder.advancement()
                .display(new DisplayInfo(new ItemStack(ModBlocks.WUNGUS_EGG.get().asItem()),
                        Component.literal("Legend of the Wungus"), Component.literal("Is this thing even alive?"),
                        new ResourceLocation(WungusMod.MOD_ID, "textures/advancements/wungusicon.png"), FrameType.TASK,
                        true, true, false))
                .addCriterion("obtained_wungus_egg", InventoryChangeTrigger.TriggerInstance.hasItems(ModBlocks.WUNGUS_EGG.get().asItem()))
                .save(saver, new ResourceLocation(WungusMod.MOD_ID, "wungus_egg_obtain"), existingFileHelper);

        EntityPredicate.Builder wungus = new EntityPredicate.Builder();
        wungus.of(ModEntities.WUNGUS.get());
        Advancement hatchWungus = Advancement.Builder.advancement()
                .display(new DisplayInfo(new ItemStack(ModBlocks.WUNGUS_EGG.get().asItem()),
                        Component.literal("Wung at first sight"), Component.literal("It loves you!"),
                        null, FrameType.TASK,
                        true, true, true))
                .parent(obtainWungusEgg)
                .addCriterion("hatched_wungus_egg", TameAnimalTrigger.TriggerInstance.tamedAnimal(wungus.build()))
                .save(saver, new ResourceLocation(WungusMod.MOD_ID, "hatch_wungus_egg"), existingFileHelper);

        Advancement breedWungus = Advancement.Builder.advancement()
                .display(new DisplayInfo(new ItemStack(ModBlocks.WUNGUS_EGG.get().asItem()),
                        Component.literal("Wung is in the air"), Component.literal("Look away..."),
                        null, FrameType.TASK,
                        true, true, true))
                .parent(hatchWungus)
                .addCriterion("breed_wungus", BredAnimalsTrigger.TriggerInstance.bredAnimals(wungus.build(), wungus.build(), wungus.build()))
                .save(saver, new ResourceLocation(WungusMod.MOD_ID, "breed_wungus"), existingFileHelper);

        Advancement milkWungus = Advancement.Builder.advancement()
                .display(new DisplayInfo(new ItemStack(Items.BUCKET),
                        Component.literal("Mmmm milk"), Component.literal("It's delicious!"),
                        null, FrameType.TASK,
                        true, true, true))
                .parent(obtainWungusEgg)
                .addCriterion("obtained_wungus_milk", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.WUNGUS_MILK.get()))
                .save(saver, new ResourceLocation(WungusMod.MOD_ID, "wungus_milk_obtain"), existingFileHelper);

        Advancement drinkWungusMilk = Advancement.Builder.advancement()
                .display(new DisplayInfo(new ItemStack(ModItems.WUNGUS_MILK.get()),
                        Component.literal("Wung are we doing here?"), Component.literal("Why would you drink that...?"),
                        null, FrameType.TASK,
                        true, true, true))
                .addCriterion("drank_wungus_milk", ConsumeItemTrigger.TriggerInstance.usedItem(ModItems.WUNGUS_MILK.get()))
                .parent(milkWungus)
                .save(saver, new ResourceLocation(WungusMod.MOD_ID, "wungus_milk_drink"), existingFileHelper);

        Advancement killWungus = Advancement.Builder.advancement()
                .display(new DisplayInfo(new ItemStack(ModItems.WUNGUS_HIDE.get()),
                        Component.literal("You monster"), Component.literal("How could you?"),
                        null, FrameType.TASK,
                        true, true, true))
                .parent(obtainWungusEgg)
                .addCriterion("killed_wungus", KilledTrigger.TriggerInstance.playerKilledEntity(wungus.build()))
                .save(saver, new ResourceLocation(WungusMod.MOD_ID, "killed_wungus"), existingFileHelper);

        Advancement obtainWungusBoots = Advancement.Builder.advancement()
                .display(new DisplayInfo(new ItemStack(ModItems.WUNGUS_BOOTS.get()),
                        Component.literal("This feels illegal"), Component.literal("Is this ok?"),
                        null, FrameType.TASK,
                        true, true, true))
                .parent(killWungus)
                .addCriterion("obtained_wungus_boots", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.WUNGUS_BOOTS.get()))
                .save(saver, new ResourceLocation(WungusMod.MOD_ID, "obtained_wungus_boots"), existingFileHelper);

        Advancement eatWungusFlesh = Advancement.Builder.advancement()
                .display(new DisplayInfo(new ItemStack(ModItems.COOKED_WUNGUS_FLESH.get()),
                        Component.literal("What the fuck?"), Component.literal("Where am I?"),
                        null, FrameType.TASK,
                        true, true, true))
                .parent(killWungus)
                .addCriterion("eat_wungus_flesh", ConsumeItemTrigger.TriggerInstance.usedItem(ModItems.COOKED_WUNGUS_FLESH.get()))
                .save(saver, new ResourceLocation(WungusMod.MOD_ID, "eat_wungus_flesh"), existingFileHelper);
    }
}