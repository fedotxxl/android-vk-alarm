package io.belov.vk.alarm.audio;

import android.media.MediaPlayer;

import io.belov.vk.alarm.song.Song;
import io.belov.vk.alarm.song.SongDownloadedListener;

/**
 * Created by fbelov on 30.10.15.
 */
public class PlayerFromQueue {

    private volatile MediaPlayer mp = null;
    private final PlayerQueue queue;
    private final SongPlayedListener songPlayedListener;

    public PlayerFromQueue(PlayerQueue queue, SongPlayedListener songPlayedListener) {
        this.queue = queue;
        this.songPlayedListener = songPlayedListener;
    }

    public void play() {
        stop();
        mp = initMediaPlayer();
        queue.getNextSongOr(getNextSongPlayAction(), getNextSongBackupAction());
    }

    private SongDownloadedListener getNextSongPlayAction() {
        return new SongDownloadedListener() {
            @Override
            public void on(Song song) {
                play(song);
            }
        };
    }

    private Runnable getNextSongBackupAction() {
        return new Runnable() {
            @Override
            public void run() {
                playNextSongOrBackup();
            }
        };
    }

    public void stop() {
        PlayerUtils.stop(mp);
        mp = null;
    }

    private void play(Song song) {
        PlayerUtils.playSong(mp, song);
        if (songPlayedListener != null) songPlayedListener.on(song);
    }

    private void playNextSongOrBackup() {
        play(queue.getNextSongOrBackup());
    }

    private MediaPlayer initMediaPlayer() {
        MediaPlayer answer = new MediaPlayer();

        answer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                playNextSongOrBackup();
            }
        });

        return answer;
    }
}
