package io.belov.vk.alarm.audio;

import io.belov.vk.alarm.preferences.PlayerPreferences;
import io.belov.vk.alarm.preferences.PreferencesManager;
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

    public PlayerQueue(Dependencies dependencies, NextSongProvider nextSongProvider) {
        this(dependencies.getPreferencesManager().getPlayerPreferences(), nextSongProvider, dependencies.playerBackupProvider, dependencies.songStorage, dependencies.songDownloader);
    }

    public PlayerQueue(PlayerPreferences playerPreferences, NextSongProvider nextSongProvider, PlayerBackupProvider playerBackupProvider, SongStorage songStorage, SongDownloader songDownloader) {
        this.maxDownloadDelayInMillis = playerPreferences.getBackupDelayInMillis();
        this.fileDownloadPreferences = playerPreferences.getFileDownloadPreferences();

        this.nextSongProvider = nextSongProvider;
        this.playerBackupProvider = playerBackupProvider;
        this.songStorage = songStorage;
        this.songDownloader = songDownloader;
    }

    public void moveToNextSong() {
        nextSongProvider.moveNext();
    }

    public SongWithInfo getCurrentSongOrBackup() {
        boolean isBackup = false;
        VkSongWithFile songWithFile = getCurrentSongWithFile();

        if (!isSongFileExists(songWithFile)) {
            isBackup = true;
            songWithFile = playerBackupProvider.get();
        }

        return new SongWithInfo(songWithFile, isBackup);
    }

    public void downloadCurrentSongOr(final SongDownloadedListener listener, final Runnable or) {
            final VkSong song = nextSongProvider.getCurrent();

            if (song == null) {
                or.run();
            } else {
                songDownloader.downloadAndCache(song, SongsCacheI.Importance.SMALL, listener, or, fileDownloadPreferences, maxDownloadDelayInMillis);
            }
    }

    public void scheduleToDownloadNextSongOrSkip() {
        VkSong nextSong = nextSongProvider.getNext();

        if (nextSong != null) {
            songDownloader.downloadAndCache(nextSong, SongsCacheI.Importance.SMALL, fileDownloadPreferences);
        }
    }

    private boolean isSongFileExists(VkSongWithFile vkSongWithFile) {
        return vkSongWithFile != null && vkSongWithFile.isFileExists();
    }

    private VkSongWithFile getCurrentSongWithFile() {
        VkSong song = nextSongProvider.getCurrent();

        if (song == null) {
            return null;
        } else {
            return songStorage.get(song);
        }
    }

    public interface NextSongProvider {

        VkSong getCurrent();
        VkSong getNext();
        void moveNext();
        void onSongChange(SongChangeListener listener);

        interface SongChangeListener {

            void on(VkSong current, VkSong next);

        }
    }

    public static class SongWithInfo {
        private VkSongWithFile songWithFile;
        private boolean isBackup;

        public SongWithInfo(VkSongWithFile songWithFile, boolean isBackup) {
            this.songWithFile = songWithFile;
            this.isBackup = isBackup;
        }

        public VkSongWithFile getSongWithFile() {
            return songWithFile;
        }

        public boolean isBackup() {
            return isBackup;
        }
    }

    public static class Dependencies {
        private PreferencesManager preferencesManager;
        private PlayerBackupProvider playerBackupProvider;
        private SongDownloader songDownloader;
        private SongStorage songStorage;

        public Dependencies(PreferencesManager preferencesManager, PlayerBackupProvider playerBackupProvider, SongDownloader songDownloader, SongStorage songStorage) {
            this.preferencesManager = preferencesManager;
            this.playerBackupProvider = playerBackupProvider;
            this.songDownloader = songDownloader;
            this.songStorage = songStorage;
        }

        public PreferencesManager getPreferencesManager() {
            return preferencesManager;
        }

        public PlayerBackupProvider getPlayerBackupProvider() {
            return playerBackupProvider;
        }

        public SongDownloader getSongDownloader() {
            return songDownloader;
        }

        public SongStorage getSongStorage() {
            return songStorage;
        }
    }
}
