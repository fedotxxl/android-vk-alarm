package io.belov.vk.alarm.audio;

import android.media.MediaPlayer;

import io.belov.vk.alarm.song.SongDownloadedListener;
import io.belov.vk.alarm.vk.VkSongWithFile;

/**
 * Created by fbelov on 30.10.15.
 */
public class PlayerFromQueue {

    private volatile MediaPlayer mp = null;
    private volatile boolean isBackupPlaying = false;

    private final PlayerUtils playerUtils;
    private final PlayerQueue queue;
    private final SongStartPlayingListener songStartPlayingListener;

    public PlayerFromQueue(PlayerUtils playerUtils, PlayerQueue queue, SongStartPlayingListener songStartPlayingListener) {
        this.playerUtils = playerUtils;
        this.queue = queue;
        this.songStartPlayingListener = songStartPlayingListener;
    }

    public void play() {
        stop();
        mp = initMediaPlayer();
        queue.downloadCurrentSongOr(getCurrentSongPlayAction(), getCurrentSongBackupAction());
    }

    private SongDownloadedListener getCurrentSongPlayAction() {
        return new SongDownloadedListener() {
            @Override
            public void on(VkSongWithFile song) {
                queue.scheduleToDownloadNextSongOrSkip();
                play(PlayableSong.from(song), false);
            }
        };
    }

    private Runnable getCurrentSongBackupAction() {
        return new Runnable() {
            @Override
            public void run() {
                playCurrentSongOrBackup();
            }
        };
    }

    public void stop() {
        playerUtils.stop(mp);
        mp = null;
    }

    private void play(PlayableSong song, boolean isBackup) {
        this.isBackupPlaying = isBackup;
        playerUtils.playSong(mp, song);
        if (songStartPlayingListener != null) songStartPlayingListener.on(song);
    }

    private void playCurrentSongOrBackup() {
        PlayerQueue.SongWithInfo songWithInfo = queue.getCurrentSongOrBackup();

        play(songWithInfo.getSong(), songWithInfo.isBackup());
    }

    private void moveToNextSong() {
        if (isBackupPlaying) {
            isBackupPlaying = false;
        } else {
            queue.moveToNextSong();
            queue.scheduleToDownloadNextSongOrSkip();
        }
    }

    private MediaPlayer initMediaPlayer() {
        MediaPlayer answer = new MediaPlayer();

        answer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                moveToNextSong();
                playCurrentSongOrBackup();
            }
        });

        return answer;
    }
}
