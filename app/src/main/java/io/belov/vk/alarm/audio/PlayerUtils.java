package io.belov.vk.alarm.audio;

import android.content.Context;
import android.media.MediaPlayer;

import java.io.IOException;

import io.belov.vk.alarm.vk.VkSongWithFile;

/**
 * Created by fbelov on 30.10.15.
 */
public class PlayerUtils {

    private static final String TAG = "PlayerUtils";

    private Context context;

    public PlayerUtils(Context context) {
        this.context = context;
    }

    public void stop(MediaPlayerWrapper mpw) {
        mpw.stop();
    }

    public void playSong(MediaPlayerWrapper mpw, PlayableSong song) {
        mpw.playSong(context, song);
    }

}
