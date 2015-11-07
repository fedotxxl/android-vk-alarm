package io.belov.vk.alarm.audio;

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
    private PreferencesManager preferencesManager;
    private PlayerBackupProvider playerBackupProvider;
    private SongDownloader songDownloader;
    private SongStorage songStorage;

    public PlayerQueue(Dependencies dependencies, NextSongProvider nextSongProvider) {
        this(dependencies.getPreferencesManager(), nextSongProvider, dependencies.playerBackupProvider, dependencies.songStorage, dependencies.songDownloader);
    }

    public PlayerQueue(PreferencesManager preferencesManager, NextSongProvider nextSongProvider, PlayerBackupProvider playerBackupProvider, SongStorage songStorage, SongDownloader songDownloader) {
        this.preferencesManager = preferencesManager;
        this.nextSongProvider = nextSongProvider;
        this.playerBackupProvider = playerBackupProvider;
        this.songStorage = songStorage;
        this.songDownloader = songDownloader;
    }

    public void moveToNextSong() {
        nextSongProvider.moveNext();
    }

    public SongWithInfo getCurrentSongOrBackup() {
        PlayableSong song;
        boolean isBackup;

        VkSongWithFile songWithFile = getCurrentSongWithFile();

        if (isSongFileExists(songWithFile)) {
            song = PlayableSong.from(songWithFile);
            isBackup = false;
        } else {
            song = playerBackupProvider.get();
            isBackup = true;
        }

        return new SongWithInfo(song, isBackup);
    }

    public void downloadCurrentSongOr(final SongDownloadedListener listener, final Runnable or) {
            final VkSong song = nextSongProvider.getCurrent();

            if (song == null) {
                or.run();
            } else {
                songDownloader.downloadAndCache(song, SongsCacheI.Importance.SMALL, listener, or, getFileDownloadPreferences(), getMaxDownloadDelayInMillis());
            }
    }

    public void scheduleToDownloadNextSongOrSkip() {
        VkSong nextSong = nextSongProvider.getNext();

        if (nextSong != null) {
            songDownloader.downloadAndCache(nextSong, SongsCacheI.Importance.SMALL, getFileDownloadPreferences());
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

    private FileDownloadPreferences getFileDownloadPreferences() {
        return preferencesManager.get().getPlayerFileDownloadPreferences();
    }

    private long getMaxDownloadDelayInMillis() {
        return preferencesManager.get().getPlayerBackupDelayInSeconds() * 1000;
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
        private PlayableSong song;
        private boolean isBackup;

        public SongWithInfo(PlayableSong song, boolean isBackup) {
            this.song = song;
            this.isBackup = isBackup;
        }

        public PlayableSong getSong() {
            return song;
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
