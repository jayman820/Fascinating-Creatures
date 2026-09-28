package net.josh.wungus.util;

import net.josh.wungus.WungusMod;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ModTags {
    public static class Items {
        public static final TagKey<Item> REPAIRS_WUNGUS_HIDE_ARMOR = createTag("repairs_wungus_hide_armor");
        public static final TagKey<Item> AILANTHUS_LOGS = createTag("ailanthus_logs");

        private static TagKey<Item> createTag(String name) {
            return ItemTags.create(Identifier.fromNamespaceAndPath(WungusMod.MOD_ID, name));
        }
    }
}
