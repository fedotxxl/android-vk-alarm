package io.belov.vk.alarm.audio;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.net.URI;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

import io.belov.vk.alarm.preferences.PlayerPreferences;
import io.belov.vk.alarm.utils.AndroidUtils;

/**
 * Created by fbelov on 19.10.15.
 */
public class Player {

    private static final String LOG_TAG = "Player";

    private final Context context;
    private volatile MediaPlayer mp = null;

    public Player(Context context) {
        this.context = context;
    }

    public void play(String url) {
        playWithBackup(url, PlayerPreferences.DEFAULT, null);
    }

    public void playWithBackup(String url, PlayerPreferences playerPreferences) {
        playWithBackup(url, playerPreferences, null);
    }


    public void playWithBackup(String url, PlayerPreferences playerPreferences, MediaPlayer.OnPreparedListener listener) {
        stop();

        mp = new MediaPlayer();

        if (canDownloadSound(playerPreferences)) {
            AtomicBoolean played = new AtomicBoolean(false);

            downloadAndPlayFromUrl(url, played, listener);
            scheduleBackupSound(playerPreferences, played, listener);
        } else {
            playBackupSound(playerPreferences, listener);
        }
    }

    private void downloadAndPlayFromUrl(final String url, final AtomicBoolean played, final MediaPlayer.OnPreparedListener listener) {
        try {
            mp.setDataSource(url);
            mp.prepareAsync();
            mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    if (!played.getAndSet(true)) {
                        mp.start();

                        Log.i(LOG_TAG, "Playing song by URL " + url);

                        if (listener != null) {
                            listener.onPrepared(mp);
                        }
                    }
                }
            });
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }

    private void scheduleBackupSound(final PlayerPreferences playerPreferences, final AtomicBoolean played, final MediaPlayer.OnPreparedListener listener) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    if (!played.getAndSet(true)) {
                        mp = new MediaPlayer();

                        playFromUri(playerPreferences.getBackupUri(), listener);
                    }
                } catch (IOException e) {
                    Log.e(LOG_TAG, "scheduleBackupSound failed");
                }
            }
        }, playerPreferences.getBackupDelayInMillis());
    }

    private void playBackupSound(PlayerPreferences playerPreferences, MediaPlayer.OnPreparedListener listener) {
        try {
            playFromUri(playerPreferences.getBackupUri(), listener);
        } catch (IOException e) {
            Log.e(LOG_TAG, "playBackupSound failed");
        }
    }

    private void playFromUri(@Nullable Uri uri, MediaPlayer.OnPreparedListener listener) throws IOException {
        if (uri == null) return;

        mp.setDataSource(context, uri);
        mp.prepare();
        mp.start();

        if (listener != null) {
            listener.onPrepared(mp);
        }
    }

    private boolean canDownloadSound(PlayerPreferences playerPreferences) {
        AndroidUtils.ConnectionInfo connectionInfo = AndroidUtils.getConnectionInfo(context);

        if (playerPreferences.isDownloadByWifiOnly()) {
            return connectionInfo.isWifi();
        } else {
            return connectionInfo.hasConnection();
        }
    }

    public void stop() {
        if (mp != null) {
            mp.stop();
            mp.release();
            mp = null;
        }
    }

}
