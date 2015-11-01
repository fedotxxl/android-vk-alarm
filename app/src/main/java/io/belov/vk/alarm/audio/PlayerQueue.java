package io.belov.vk.alarm.audio;

import io.belov.vk.alarm.preferences.PlayerPreferences;
import io.belov.vk.alarm.song.SongDownloadedListener;
import io.belov.vk.alarm.song.SongDownloader;
import io.belov.vk.alarm.song.SongStorage;
import io.belov.vk.alarm.storage.SongsCacheI;
import io.belov.vk.alarm.utils.FileDownloadPreferences;
import io.belov.vk.alarm.vk.VkSong;
import io.belov.vk.alarm.vk.VkSongWithFile;

/**
 * Created by fbelov on 30.10.15.
 */
public class PlayerQueue {

    private String TAG = "PlayerQueue";

    private NextSongProvider nextSongProvider;
    private PlayerBackupProvider playerBackupProvider;
    private SongDownloader songDownloader;
    private SongStorage songStorage;
    private FileDownloadPreferences fileDownloadPreferences;
    private long maxDownloadDelayInMillis;

    public PlayerQueue(PlayerPreferences playerPreferences, NextSongProvider nextSongProvider, PlayerBackupProvider playerBackupProvider, SongStorage songStorage, SongDownloader songDownloader) {
        this.maxDownloadDelayInMillis = playerPreferences.getBackupDelayInMillis();
        this.fileDownloadPreferences = playerPreferences.getFileDownloadPreferences();

        this.nextSongProvider = nextSongProvider;
        this.playerBackupProvider = playerBackupProvider;
        this.songStorage = songStorage;
        this.songDownloader = songDownloader;
    }

    public VkSongWithFile getNextSongOrBackup() {
        VkSongWithFile answer = getNextSongWithFile();

        if (!isSongFileExists(answer)) {
            answer = playerBackupProvider.get();
        }

        return answer;
    }

    public void downloadNextSongOr(final SongDownloadedListener listener, final Runnable or) {
            final VkSong song = nextSongProvider.next();

            if (song == null) {
                or.run();
            } else {
                SongDownloadedListener onDownloaded = new SongDownloadedListener() {
                    @Override
                    public void on(VkSongWithFile songWithFile) {
                        listener.on(songWithFile);

                        scheduleNextSong();
                    }
                };

                songDownloader.downloadAndCache(song, SongsCacheI.Importance.SMALL, onDownloaded, or, fileDownloadPreferences, maxDownloadDelayInMillis);
            }
    }

    private void scheduleNextSong() {
        VkSong nextSong = nextSongProvider.next();

        if (nextSong != null) {
            songDownloader.downloadAndCache(nextSong, SongsCacheI.Importance.SMALL, fileDownloadPreferences);
        }
    }

    private boolean isSongFileExists(VkSongWithFile vkSongWithFile) {
        return vkSongWithFile != null && vkSongWithFile.isFileExists();
    }

    private VkSongWithFile getNextSongWithFile() {
        VkSong nextSong = nextSongProvider.next();

        if (nextSong == null) {
            return null;
        } else {
            return songStorage.get(nextSong);
        }
    }

    interface NextSongProvider {

        VkSong next();

    }
}
