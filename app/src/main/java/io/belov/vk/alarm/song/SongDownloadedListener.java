package io.belov.vk.alarm.song;

import io.belov.vk.alarm.vk.VkSongWithFile;

/**
 * Created by fbelov on 30.10.15.
 */
public interface SongDownloadedListener {
    void on(VkSongWithFile songWithFile);
}
