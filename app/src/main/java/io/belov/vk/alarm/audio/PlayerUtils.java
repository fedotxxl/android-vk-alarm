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

    public void stop(MediaPlayer mp) {
        if (mp != null) {
            mp.stop();
            mp.release();
        }
    }

    public void playSong(MediaPlayer mp, PlayableSong song) {
        try {
            boolean hasDataSource = true;

            if (song.hasFile()) {
                mp.setDataSource(song.getFile().getAbsolutePath());
            } else if (song.hasUri()) {
                mp.setDataSource(context, song.getUri());
            } else {
                hasDataSource = false;
            }

            if (hasDataSource) {
                mp.prepare();
                mp.start();
            }

        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

}
