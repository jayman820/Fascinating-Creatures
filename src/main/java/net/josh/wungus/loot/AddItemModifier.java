package net.josh.wungus.loot;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;

/**
 * Adds a full item stack (including data components, e.g. a written book with pages) to the loot.
 * This replaces the old "nbtString" field: items no longer carry NBT in 1.21, the JSON looks like
 * {"item": {"id": "minecraft:written_book", "components": {"minecraft:written_book_content": {...}}}}
 */
public class AddItemModifier extends LootModifier {
    public static final MapCodec<AddItemModifier> CODEC = RecordCodecBuilder.mapCodec(inst -> codecStart(inst)
            .and(ItemStack.CODEC.fieldOf("item").forGetter(m -> m.stack))
            .apply(inst, AddItemModifier::new));
    private final ItemStack stack;

    public AddItemModifier(LootItemCondition[] conditionsIn, ItemStack stack) {
        super(conditionsIn);
        this.stack = stack;
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        // The conditions have already been checked by LootModifier#apply
        generatedLoot.add(this.stack.copy());
        return generatedLoot;
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }
}
