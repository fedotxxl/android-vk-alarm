package io.belov.vk.alarm.audio;

import android.content.Context;
import android.media.MediaPlayer;

import java.io.IOException;

/**
 * Created by fbelov on 06.11.15.
 */
public class MediaPlayerWrapper {

    private boolean initialized;
    private MediaPlayer mp;

    public MediaPlayerWrapper(MediaPlayer mp) {
        this.mp = mp;
    }

    public void stop() {
        if (initialized) {
            mp.stop();
            mp.reset();
        }
    }

    public void playSong(Context context, PlayableSong song) {
        try {
            stop();

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
                initialized = true;
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

}
