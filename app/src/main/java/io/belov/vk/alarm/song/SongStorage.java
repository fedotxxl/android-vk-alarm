package io.belov.vk.alarm.song;

import io.belov.vk.alarm.storage.SongsCacheI;
import io.belov.vk.alarm.vk.VkSong;
import io.belov.vk.alarm.vk.VkSongWithFile;

/**
 * Created by fbelov on 30.10.15.
 */
public class SongStorage {

    private SongsCacheI cache;

    public SongStorage(SongsCacheI cache) {
        this.cache = cache;
    }

    public VkSongWithFile get(VkSong song) {
        return new VkSongWithFile(song, cache.get(song.getId()).getFile());
    }

}
