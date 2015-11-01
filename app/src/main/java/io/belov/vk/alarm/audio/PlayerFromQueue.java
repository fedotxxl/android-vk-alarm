package io.belov.vk.alarm.audio;

import android.media.MediaPlayer;

import io.belov.vk.alarm.song.SongDownloadedListener;
import io.belov.vk.alarm.vk.VkSongWithFile;

/**
 * Created by fbelov on 30.10.15.
 */
public class PlayerFromQueue {

    private volatile MediaPlayer mp = null;
    private final PlayerQueue queue;
    private final SongStartPlayingListener songStartPlayingListener;

    public PlayerFromQueue(PlayerQueue queue, SongStartPlayingListener songStartPlayingListener) {
        this.queue = queue;
        this.songStartPlayingListener = songStartPlayingListener;
    }

    public void play() {
        stop();
        mp = initMediaPlayer();
        queue.downloadNextSongOr(getNextSongPlayAction(), getNextSongBackupAction());
    }

    private SongDownloadedListener getNextSongPlayAction() {
        return new SongDownloadedListener() {
            @Override
            public void on(VkSongWithFile song) {
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

    private void play(VkSongWithFile song) {
        PlayerUtils.playSong(mp, song);
        if (songStartPlayingListener != null) songStartPlayingListener.on(song);
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
