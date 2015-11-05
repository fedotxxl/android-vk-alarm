package io.belov.vk.alarm.audio;

import io.belov.vk.alarm.vk.VkSong;
import io.belov.vk.alarm.vk.VkSongWithFile;

/**
 * Created by fbelov on 30.10.15.
 */
public class PlayerBackupProvider {

    VkSongWithFile get() {
        return new VkSongWithFile(new VkSong(-1 ,-1, "backup", "backup", -1, null), null); //todo
    }

}
