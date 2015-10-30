package io.belov.vk.alarm.audio;

import android.media.MediaPlayer;

import io.belov.vk.alarm.song.Song;
import io.belov.vk.alarm.song.SongDownloadedListener;
import io.belov.vk.alarm.song.SongDownloader;

/**
 * Created by fbelov on 30.10.15.
 */
public class PlayerFromUrl {

    private volatile MediaPlayer mp = null;

    private Song song;
    private SongDownloader songDownloader;

    public PlayerFromUrl(Song song, SongDownloader songDownloader) {
        this.song = song;
        this.songDownloader = songDownloader;
    }

    public void play() {
        songDownloader.download(song, new SongDownloadedListener() {
            @Override
            public void on(Song song) {
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
