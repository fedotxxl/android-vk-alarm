package io.belov.vk.alarm.audio;

import android.media.MediaPlayer;
import android.util.Log;

import java.io.IOException;

/**
 * Created by fbelov on 19.10.15.
 */
public class Player {

    private static final String LOG_TAG = "Player";

    MediaPlayer mp = null;

    public void play(String url) {
        play(url, null);
    }

    public void play(final String url, final MediaPlayer.OnPreparedListener listener) {
        stop();

        MediaPlayer mp = new MediaPlayer();
        try {
            mp.setDataSource(url);
            mp.prepareAsync();
            mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();

                    Log.i(LOG_TAG, "Playing song by URL " + url);

                    if (listener != null) {
                        listener.onPrepared(mp);
                    }
                }
            });
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

    }

    public void stop() {
        if (mp != null) {
            mp.release();
            mp = null;
        }
    }

}
