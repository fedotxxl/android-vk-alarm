package io.belov.vk.alarm.audio;

import android.media.MediaPlayer;

import io.belov.vk.alarm.vk.VkSongWithFile;

/**
 * Created by fbelov on 30.10.15.
 */
public class PlayerUtils {

    public static void stop(MediaPlayer mp) {
        if (mp != null) {
            mp.stop();
            mp.release();
        }
    }

    public static void playSong(MediaPlayer mp, VkSongWithFile song) {
        //todo
    }

}
