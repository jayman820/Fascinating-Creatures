package net.josh.wungus.loot;

import com.google.common.base.Suppliers;
import com.google.gson.*;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class AddItemModifier extends LootModifier {
    public static final Supplier<Codec<AddItemModifier>> CODEC = Suppliers.memoize(() -> RecordCodecBuilder.create(inst -> codecStart(inst).and(
                    inst.group(
                            ForgeRegistries.ITEMS.getCodec().fieldOf("item").forGetter(m -> m.item),
                            Codec.STRING.fieldOf("nbtString").forGetter(m -> m.nbtString)
                    )).apply(inst, AddItemModifier::new)
    ));
    private final Item item;
    private final String pages;
    private final String author;
    private final String nbtString;
    private final JsonElement parsedJson;

    public AddItemModifier(LootItemCondition[] conditionsIn, Item item, String nbtString) {
        super(conditionsIn);
        this.item = item;
        Gson gson = new Gson();
        //JsonElement parsedJson = JsonParser.parseString(pages);
        this.parsedJson = null;
        //String extractedJson = parsedJson.getAsJsonArray().get(0).getAsString();
        //System.out.println(extractedJson);
        this.pages = null;
        this.author = null;
        this.nbtString = nbtString;
    }

    public AddItemModifier(LootItemCondition[] conditionsIn, Item item) {
        super(conditionsIn);
        this.item = item;
        this.pages = null;
        this.author = null;
        this.parsedJson = null;
        this.nbtString = null;
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        for (LootItemCondition condition : this.conditions) {
            if(!condition.test(context)) {
                return generatedLoot;
            }
        }
        ItemStack new_item = new ItemStack(this.item);
        try {
            CompoundTag nbt = NbtUtils.snbtToStructure(this.nbtString);
            new_item.setTag(nbt);
            generatedLoot.add(new_item);
            System.out.println(nbtString);
            System.out.println(new_item.getTag());
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
        }
        /*
        if(this.pages != null && !this.pages.equalsIgnoreCase("")) {
            nbt.putString("pages", this.pages);
            nbt.putString("pages", this.pages);
            System.out.println(nbt);
            new_item.setTag(nbt);
        }
        if(this.author != null && !this.author.equalsIgnoreCase("")) {
            nbt.putString("author", this.author);
            new_item.setTag(nbt);
        }
        */

        return generatedLoot;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }
    /*
    private static class Serializer extends GlobalL<AddItemModifier> {
        @Override
        public AddItemModifier read(ResourceLocation location, JsonObject object, LootItemCondition[] conditions) {
            final String pages = GsonHelper.getAsString(object, "pages", "");
            final String item_name = GsonHelper.getAsString(object, "item", "");
            if (item_name.equals("")) throw new JsonParseException("An item name is necessary");
            CompoundTag nbt = new CompoundTag();
            nbt.putString("pages", pages);
            return new AddItemModifier(conditions, Items.WRITTEN_BOOK, pages);
        }

        @Override
        public JsonObject write(AddItemModifier instance) {
            final JsonObject obj = this.makeConditions(instance.conditions);
            obj.addProperty("pages", instance.pages);
            return obj;
        }
    }
     */
}
