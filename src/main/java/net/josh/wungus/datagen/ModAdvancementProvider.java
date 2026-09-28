package net.josh.wungus.datagen;

import net.josh.wungus.WungusMod;
import net.josh.wungus.block.ModBlocks;
import net.josh.wungus.entity.ModEntities;
import net.josh.wungus.item.ModItems;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.criterion.BredAnimalsTrigger;
import net.minecraft.advancements.criterion.ConsumeItemTrigger;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.advancements.criterion.KilledTrigger;
import net.minecraft.advancements.criterion.TameAnimalTrigger;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.advancements.AdvancementSubProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.Optional;
import java.util.function.Consumer;

public class ModAdvancementProvider implements AdvancementSubProvider {
    @Override
    public void generate(HolderLookup.Provider registries, Consumer<AdvancementHolder> saver) {
        HolderGetter<EntityType<?>> entityTypes = registries.lookupOrThrow(Registries.ENTITY_TYPE);
        HolderGetter<Item> items = registries.lookupOrThrow(Registries.ITEM);

        AdvancementHolder obtainWungusEgg = Advancement.Builder.advancement()
                .display(ModBlocks.WUNGUS_EGG.get(),
                        Component.literal("Legend of the Wungus"), Component.literal("Is this thing even alive?"),
                        Identifier.fromNamespaceAndPath(WungusMod.MOD_ID, "advancements/wungusicon"), AdvancementType.TASK,
                        true, true, false)
                .addCriterion("obtained_wungus_egg", InventoryChangeTrigger.TriggerInstance.hasItems(ModBlocks.WUNGUS_EGG.get()))
                .save(saver, name("wungus_egg_obtain"));

        AdvancementHolder hatchWungus = Advancement.Builder.advancement()
                .display(ModBlocks.WUNGUS_EGG.get(),
                        Component.literal("Wung at first sight"), Component.literal("It loves you!"),
                        null, AdvancementType.TASK,
                        true, true, true)
                .parent(obtainWungusEgg)
                .addCriterion("hatched_wungus_egg", TameAnimalTrigger.TriggerInstance.tamedAnimal(wungus(entityTypes)))
                .save(saver, name("hatch_wungus_egg"));

        AdvancementHolder breedWungus = Advancement.Builder.advancement()
                .display(ModBlocks.WUNGUS_EGG.get(),
                        Component.literal("Wung is in the air"), Component.literal("Look away..."),
                        null, AdvancementType.TASK,
                        true, true, true)
                .parent(hatchWungus)
                .addCriterion("breed_wungus", BredAnimalsTrigger.TriggerInstance.bredAnimals(
                        Optional.of(wungus(entityTypes).build()), Optional.of(wungus(entityTypes).build()), Optional.of(wungus(entityTypes).build())))
                .save(saver, name("breed_wungus"));

        AdvancementHolder milkWungus = Advancement.Builder.advancement()
                .display(Items.BUCKET,
                        Component.literal("Mmmm milk"), Component.literal("It's delicious!"),
                        null, AdvancementType.TASK,
                        true, true, true)
                .parent(obtainWungusEgg)
                .addCriterion("obtained_wungus_milk", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.WUNGUS_MILK.get()))
                .save(saver, name("wungus_milk_obtain"));

        AdvancementHolder drinkWungusMilk = Advancement.Builder.advancement()
                .display(ModItems.WUNGUS_MILK.get(),
                        Component.literal("Wung are we doing here?"), Component.literal("Why would you drink that...?"),
                        null, AdvancementType.TASK,
                        true, true, true)
                .addCriterion("drank_wungus_milk", ConsumeItemTrigger.TriggerInstance.usedItem(items, ModItems.WUNGUS_MILK.get()))
                .parent(milkWungus)
                .save(saver, name("wungus_milk_drink"));

        AdvancementHolder killWungus = Advancement.Builder.advancement()
                .display(ModItems.WUNGUS_HIDE.get(),
                        Component.literal("You monster"), Component.literal("How could you?"),
                        null, AdvancementType.TASK,
                        true, true, true)
                .parent(obtainWungusEgg)
                .addCriterion("killed_wungus", KilledTrigger.TriggerInstance.playerKilledEntity(wungus(entityTypes)))
                .save(saver, name("killed_wungus"));

        AdvancementHolder obtainWungusBoots = Advancement.Builder.advancement()
                .display(ModItems.WUNGUS_BOOTS.get(),
                        Component.literal("This feels illegal"), Component.literal("Is this ok?"),
                        null, AdvancementType.TASK,
                        true, true, true)
                .parent(killWungus)
                .addCriterion("obtained_wungus_boots", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.WUNGUS_BOOTS.get()))
                .save(saver, name("obtained_wungus_boots"));

        AdvancementHolder eatWungusFlesh = Advancement.Builder.advancement()
                .display(ModItems.COOKED_WUNGUS_FLESH.get(),
                        Component.literal("What the fuck?"), Component.literal("Where am I?"),
                        null, AdvancementType.TASK,
                        true, true, true)
                .parent(killWungus)
                .addCriterion("eat_wungus_flesh", ConsumeItemTrigger.TriggerInstance.usedItem(items, ModItems.COOKED_WUNGUS_FLESH.get()))
                .save(saver, name("eat_wungus_flesh"));
    }

    private static EntityPredicate.Builder wungus(HolderGetter<EntityType<?>> entityTypes) {
        return EntityPredicate.Builder.entity().of(entityTypes, ModEntities.WUNGUS.get());
    }

    private static String name(String path) {
        return WungusMod.MOD_ID + ":" + path;
    }
}
