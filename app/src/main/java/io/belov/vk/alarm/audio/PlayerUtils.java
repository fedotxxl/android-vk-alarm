package io.belov.vk.alarm.audio;

import android.media.MediaPlayer;

import java.io.IOException;

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
        try {
            mp.setDataSource(song.getFile().getAbsolutePath());
            mp.prepare();
            mp.start();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

}
