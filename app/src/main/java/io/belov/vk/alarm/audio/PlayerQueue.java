package io.belov.vk.alarm.audio;

import android.util.Log;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

import io.belov.vk.alarm.song.SongDownloadedListener;
import io.belov.vk.alarm.song.SongDownloader;
import io.belov.vk.alarm.song.SongStorage;
import io.belov.vk.alarm.utils.FileDownloader;
import io.belov.vk.alarm.utils.IoUtils;
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
    private FileDownloader fileDownloader;
    private volatile VkSongWithFile nextSongWithFile;
    private long maxDownloadDelayInMillis;

    public VkSongWithFile getNextSongOrBackup() {
        VkSongWithFile answer = null;

        if (isSongFileExists(nextSongWithFile)) {
            answer = nextSongWithFile;
            nextSongWithFile = null;
        } else {
            VkSongWithFile songWithFile = getNextSongWithFile();

            if (songWithFile != null) {
                if (songWithFile.isFileExists()) {
                    answer = songWithFile;
                }
            }
        }

        if (answer == null) {
            answer = playerBackupProvider.get();
        }

        return answer;
    }

    public void downloadNextSongOr(final SongDownloadedListener listener, final Runnable or) {
        if (isSongFileExists(nextSongWithFile)) {
            listener.on(nextSongWithFile);
            nextSongWithFile = null;
        } else {
            final PlayerQueue that = this;
            final VkSongWithFile songWithFile = getNextSongWithFile();

            if (songWithFile == null) {
                or.run();
            } else {
                if (songWithFile.isFileExists()) {
                    listener.on(songWithFile);
                } else {
                    final AtomicBoolean sync = new AtomicBoolean(false);

                    fileDownloader.downloadAsync(songWithFile.getUrl(), songWithFile.getFile(), new IoUtils.FileDownloadedListener() {
                        @Override
                        public void on(String url, File file) {
                            if (!sync.getAndSet(true)) {
                                listener.on(songWithFile);
                                scheduleNextSong();
                            } else {
                                that.nextSongWithFile = songWithFile;
                            }
                        }
                    });

                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            if (!sync.getAndSet(true)) {
                                or.run();
                            }
                        }
                    }, maxDownloadDelayInMillis);
                }
            }
        }
    }

    private void scheduleNextSong() {
        if (nextSongWithFile != null) {
            Log.e(TAG, "Next song is already scheduled");
            return;
        }

        VkSong nextSong = nextSongProvider.next();

        if (nextSong != null) {
            VkSongWithFile songWithFile = songStorage.get(nextSong);

            if (!songWithFile.isFileExists()) {
                fileDownloader.downloadAsync(songWithFile.getUrl(), songWithFile.getFile());
            }
        }
    }

    private boolean isSongFileExists(VkSongWithFile vkSongWithFile) {
        return vkSongWithFile != null && vkSongWithFile.isFileExists();
    }

    private VkSongWithFile getNextSongWithFile() {
        if (nextSongWithFile != null) {
            return nextSongWithFile;
        } else {
            VkSong nextSong =  nextSongProvider.next();

            if (nextSong == null) {
                return null;
            } else {
                return songStorage.get(nextSong);
            }
        }
    }

    interface NextSongProvider {

        VkSong next();

    }
}
