package net.josh.wungus.datagen;

import net.josh.wungus.WungusMod;
import net.josh.wungus.item.ModItems;
import net.josh.wungus.loot.AddItemModifier;
import net.minecraft.data.PackOutput;
import net.minecraft.nbt.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.common.loot.LootTableIdCondition;

import javax.swing.*;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ModGlobalLootModifierProvider extends GlobalLootModifierProvider {

    public ModGlobalLootModifierProvider(PackOutput output) {
        super(output, WungusMod.MOD_ID);
    }

    @Override
    protected void start() {
        ItemStack writtenBook = new ItemStack(Items.WRITTEN_BOOK, 1);
        CompoundTag nbt = new CompoundTag();
        //List<String> page_list = Arrays.asList("{\"text\":\"Expedition Log, Day 1\\n\\nI was advised to start a log of my travels; I\\u2019d rather start when I had something noteworthy to write down, but here we are.\\n\\nNothing of interest to report. Currently I am headed toward a forest, where I will\"}", "[\"\",{\"text\":\"set up for a few days in search of the evidence for\\n\\n\"},{\"text\":\"Project Green Eyes\",\"obfuscated\":true},{\"text\":\"\\n \\nI do hope the grant money can be put to good use instead of a wasteful expedition across the world\\u2026\",\"color\":\"reset\"}]", "{\"text\":\"Expedition Log, Day 5\\n\\nAll seemed calm until this point. Last evening, outside of my tent, mixed among the usual cries of nightfall I heard a peculiar sound. A huff, then a mewl\\u2014or was it a trill?\\n\\nI could not fall asleep afterwards. It was not\"}", "{\"text\":\"even a frightening noise, but the curiosity it roused from me left me anxious. In the morning I searched around the parameters of my campsite, but to no avail, found nothing. The sounds of the night that riled me so suddenly? Perhaps nothing, too. There is \"}", "{\"text\":\"so much nothing this trip. It is about time for me to move on to another biome.\"}", "{\"text\":\"Expedition Log, Day 7\\n\\nQuickly I found a jungle, much more rich with wildlife. All the sounds blend together in the night, but still, I swear I hear that sound. That mewl. Or trill? Or was it a chirp? But there was a huff.\\n\\nBecause of the\"}", "{\"text\":\" foliage, it is difficult to decipher what could be a falling leaf or an inter-dimensional particle. I swear my eyes are playing tricks on me, especially through the static of rain. Last night, I swore I could see one big, unblinking green eye\\u2014the very same we are looking\"}", "{\"text\":\"for. But it must have been my imagination\\u2026\\n\\nStrange patches of dark soil and dead leaves have caught my interest. Almost as if they were some sort of animal nest. But which animal, \\u0020is my question. The remains of a shell are left behind, a faded blue. Maybe one of the\"}", "{\"text\":\"parrots has taken to living on the ground\\u2026\"}");
        //for (String page : page_list) {
         //   nbt.putString("pages", page);
        //}
        String pages = "['{\"text\":\"Expedition Log, Day 1\\\\n\\\\nI was advised to start a log of my travels; I\\\\u2019d rather start when I had something noteworthy to write down, but here we are.\\\\n\\\\nNothing of interest to report. Currently I am headed toward a forest, where I will\"}','[\"\",{\"text\":\"set up for a few days in search of the evidence for\\\\n\\\\n\"},{\"text\":\"Project Green Eyes\",\"obfuscated\":true},{\"text\":\"\\\\n \\\\nI do hope the grant money can be put to good use instead of a wasteful expedition across the world\\\\u2026\",\"color\":\"reset\"}]','{\"text\":\"Expedition Log, Day 5\\\\n\\\\nAll seemed calm until this point. Last evening, outside of my tent, mixed among the usual cries of nightfall I heard a peculiar sound. A huff, then a mewl\\\\u2014or was it a trill?\\\\n\\\\nI could not fall asleep afterwards. It was not\"}','{\"text\":\"even a frightening noise, but the curiosity it roused from me left me anxious. In the morning I searched around the parameters of my campsite, but to no avail, found nothing. The sounds of the night that riled me so suddenly? Perhaps nothing, too. There is \"}','{\"text\":\"so much nothing this trip. It is about time for me to move on to another biome.\"}','{\"text\":\"Expedition Log, Day 7\\\\n\\\\nQuickly I found a jungle, much more rich with wildlife. All the sounds blend together in the night, but still, I swear I hear that sound. That mewl. Or trill? Or was it a chirp? But there was a huff.\\\\n\\\\nBecause of the\"}','{\"text\":\" foliage, it is difficult to decipher what could be a falling leaf or an inter-dimensional particle. I swear my eyes are playing tricks on me, especially through the static of rain. Last night, I swore I could see one big, unblinking green eye\\\\u2014the very same we are looking\"}','{\"text\":\"for. But it must have been my imagination\\\\u2026\\\\n\\\\nStrange patches of dark soil and dead leaves have caught my interest. Almost as if they were some sort of animal nest. But which animal, \\\\u0020is my question. The remains of a shell are left behind, a faded blue. Maybe one of the\"}','{\"text\":\"parrots has taken to living on the ground\\\\u2026\"}']";
        String author = "Unknown";
        String nbtString = "{tag: \"{pages:[\\\"{\\\\\\\"text\\\\\\\":\\\\\\\"Expedition Log, Day 1\\\\\\\\n\\\\\\\\nI was advised to start a log of my travels; I’d rather start when I had something noteworthy to write down, but here we are.\\\\\\\\n\\\\\\\\nNothing of interest to report. Currently I am headed toward a forest, where I will\\\\\\\"}\\\", \\\"[{\\\\\\\"text\\\\\\\":\\\\\\\"set up for a few days in search of the evidence for\\\\\\\\n\\\\\\\\n\\\\\\\"},{\\\\\\\"text\\\\\\\":\\\\\\\"Project Green Eyes\\\\\\\",\\\\\\\"obfuscated\\\\\\\":true},{\\\\\\\"text\\\\\\\":\\\\\\\"\\\\\\\\n\\\\\\\\nI do hope the grant money can be put to good use instead of a wasteful expedition across the world…\\\\\\\",\\\\\\\"color\\\\\\\":\\\\\\\"reset\\\\\\\"}]\\\", \\\"{\\\\\\\"text\\\\\\\":\\\\\\\"Expedition Log, Day 5\\\\\\\\n\\\\\\\\nAll seemed calm until this point. Last evening, outside of my tent, mixed among the usual cries of nightfall I heard a peculiar sound. A huff, then a mewl—or was it a trill?\\\\\\\\n\\\\\\\\nI could not fall asleep afterwards. It was not\\\\\\\"}\\\", \\\"{\\\\\\\"text\\\\\\\":\\\\\\\"even a frightening noise, but the curiosity it roused from me left me anxious. In the morning I searched around the parameters of my campsite, but to no avail, found nothing. The sounds of the night that riled me so suddenly? Perhaps nothing, too. There is\\\\\\\"}\\\", \\\"{\\\\\\\"text\\\\\\\":\\\\\\\"so much nothing this trip. It is about time for me to move on to another biome.\\\\\\\"}\\\", \\\"{\\\\\\\"text\\\\\\\":\\\\\\\"Expedition Log, Day 7\\\\\\\\n\\\\\\\\nQuickly I found a jungle, much more rich with wildlife. All the sounds blend together in the night, but still, I swear I hear that sound. That mewl. Or trill? Or was it a chirp? But there was a huff.\\\\\\\\n\\\\\\\\nBecause of the\\\\\\\"}\\\", \\\"{\\\\\\\"text\\\\\\\":\\\\\\\"foliage, it is difficult to decipher what could be a falling leaf or an inter-dimensional particle. I swear my eyes are playing tricks on me, especially through the static of rain. Last night, I swore I could see one big, unblinking green eye—the very same we are looking\\\\\\\"}\\\", \\\"{\\\\\\\"text\\\\\\\":\\\\\\\"for. But it must have been my imagination…\\\\\\\\n\\\\\\\\nStrange patches of dark soil and dead leaves have caught my interest. Almost as if they were some sort of animal nest. But which animal, is my question. The remains of a shell are left behind, a faded blue. Maybe one of the\\\\\\\"}\\\", \\\"{\\\\\\\"text\\\\\\\":\\\\\\\"parrots has taken to living on the ground…\\\\\\\"}\\\"], title:\\\"Expedition Log\\\", author:\\\"Explorer\\\", resolved:1b}}";
        //nbt.putString("pages", pages);
        //nbt.putString("title", "Expedition Log");
        //nbt.putString("author", "Unknown");
        //nbt.putInt("generation", 3);
        nbt.putInt("count", 2);
        writtenBook.setTag(nbt);
        System.out.println("In ModGlobalLootProvider");
        System.out.println(nbt);

        add("wungus_journal_1", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(new ResourceLocation("chests/simple_dungeon")).build() },
                writtenBook.getItem(), nbtString));
        try {
            CompoundTag nbt2 = NbtUtils.snbtToStructure("{pages:['{\"text\":\"Expedition Log, Day 1\\\\n\\\\nI was advised to start a log of my travels; I\\\\u2019d rather start when I had something noteworthy to write down, but here we are.\\\\n\\\\nNothing of interest to report. Currently I am headed toward a forest, where I will\"}','[\"\",{\"text\":\"set up for a few days in search of the evidence for\\\\n\\\\n\"},{\"text\":\"Project Green Eyes\",\"obfuscated\":true},{\"text\":\"\\\\n \\\\nI do hope the grant money can be put to good use instead of a wasteful expedition across the world\\\\u2026\",\"color\":\"reset\"}]','{\"text\":\"Expedition Log, Day 5\\\\n\\\\nAll seemed calm until this point. Last evening, outside of my tent, mixed among the usual cries of nightfall I heard a peculiar sound. A huff, then a mewl\\\\u2014or was it a trill?\\\\n\\\\nI could not fall asleep afterwards. It was not\"}','{\"text\":\"even a frightening noise, but the curiosity it roused from me left me anxious. In the morning I searched around the parameters of my campsite, but to no avail, found nothing. The sounds of the night that riled me so suddenly? Perhaps nothing, too. There is \"}','{\"text\":\"so much nothing this trip. It is about time for me to move on to another biome.\"}','{\"text\":\"Expedition Log, Day 7\\\\n\\\\nQuickly I found a jungle, much more rich with wildlife. All the sounds blend together in the night, but still, I swear I hear that sound. That mewl. Or trill? Or was it a chirp? But there was a huff.\\\\n\\\\nBecause of the\"}','{\"text\":\" foliage, it is difficult to decipher what could be a falling leaf or an inter-dimensional particle. I swear my eyes are playing tricks on me, especially through the static of rain. Last night, I swore I could see one big, unblinking green eye\\\\u2014the very same we are looking\"}','{\"text\":\"for. But it must have been my imagination\\\\u2026\\\\n\\\\nStrange patches of dark soil and dead leaves have caught my interest. Almost as if they were some sort of animal nest. But which animal, \\\\u0020is my question. The remains of a shell are left behind, a faded blue. Maybe one of the\"}','{\"text\":\"parrots has taken to living on the ground\\\\u2026\"}']}");
            System.out.println(nbt2);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
