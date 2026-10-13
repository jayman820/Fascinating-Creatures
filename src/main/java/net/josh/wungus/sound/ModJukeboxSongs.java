package net.josh.wungus.sound;

import net.josh.wungus.WungusMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.JukeboxSong;

/**
 * The Wungus Game 2 music disc songs. The songs themselves (sound, length, description) are data,
 * see data/wungus/jukebox_song.
 */
public class ModJukeboxSongs {
    public static final ResourceKey<JukeboxSong> CONCERTO = createSong("concerto");
    public static final ResourceKey<JukeboxSong> MINGLE = createSong("mingle");
    public static final ResourceKey<JukeboxSong> MOON = createSong("moon");
    public static final ResourceKey<JukeboxSong> JUMP_ROPE = createSong("jump_rope");
    public static final ResourceKey<JukeboxSong> WUNGUS_RACING = createSong("wungus_racing");

    private static ResourceKey<JukeboxSong> createSong(String name) {
        return ResourceKey.create(Registries.JUKEBOX_SONG, Identifier.fromNamespaceAndPath(WungusMod.MOD_ID, name));
    }
}
