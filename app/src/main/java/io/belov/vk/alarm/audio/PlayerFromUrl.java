package io.belov.vk.alarm.audio;

import android.media.MediaPlayer;

import io.belov.vk.alarm.song.SongDownloadedListener;
import io.belov.vk.alarm.song.SongDownloader;
import io.belov.vk.alarm.storage.SongsCacheI;
import io.belov.vk.alarm.vk.VkSong;
import io.belov.vk.alarm.vk.VkSongWithFile;

/**
 * Created by fbelov on 30.10.15.
 */
public class PlayerFromUrl {

    private volatile MediaPlayer mp = null;

    private VkSong song;
    private SongsCacheI.Importance importance;
    private SongDownloader songDownloader;

    public PlayerFromUrl(VkSong song, SongsCacheI.Importance importance, SongDownloader songDownloader) {
        this.song = song;
        this.importance = importance;
        this.songDownloader = songDownloader;
    }

    public void play() {
        songDownloader.downloadAndCache(song, importance, new SongDownloadedListener() {
            @Override
            public void on(VkSongWithFile song) {
                stop();
                mp = new MediaPlayer();
                PlayerUtils.playSong(mp, song);
            }
        });
    }

    public void stop() {
        PlayerUtils.stop(mp);
        mp = null;
    }
}
