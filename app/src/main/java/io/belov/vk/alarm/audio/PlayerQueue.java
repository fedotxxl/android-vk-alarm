package io.belov.vk.alarm.audio;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

import io.belov.vk.alarm.song.SongDownloadedListener;
import io.belov.vk.alarm.song.SongDownloader;
import io.belov.vk.alarm.song.SongStorage;
import io.belov.vk.alarm.utils.IoUtils;
import io.belov.vk.alarm.vk.VkSong;
import io.belov.vk.alarm.vk.VkSongWithFile;

/**
 * Created by fbelov on 30.10.15.
 */
public class PlayerQueue {

    private NextSongProvider nextSongProvider;
    private PlayerBackupProvider playerBackupProvider;
    private SongDownloader songDownloader;
    private SongStorage songStorage;
    private volatile VkSongWithFile nextSongWithFile;
    private long maxDownloadDelayInMillis;

    public VkSongWithFile getNextSongOrBackup() {
        VkSong nextSong = nextSongProvider.next();

        if (nextSong != null) {
            VkSongWithFile songWithFile = songStorage.get(nextSong);

            if (songWithFile.isFileExists()) {
                return songWithFile;
            }
        }

        return playerBackupProvider.get();
    }

    public void downloadNextSongOr(final SongDownloadedListener listener, final Runnable or) {
        if (nextSongWithFile != null) {
            listener.on(nextSongWithFile);
            nextSongWithFile = null;
        } else {
            final PlayerQueue that = this;
            final VkSong nextSong = nextSongProvider.next();

            if (nextSong == null) {
                or.run();
            } else {
                final VkSongWithFile songWithFile = songStorage.get(nextSong);

                if (songWithFile.isFileExists()) {
                    listener.on(songWithFile);
                } else {
                    final AtomicBoolean sync = new AtomicBoolean(false);

                    IoUtils.downloadAsync(songWithFile.getUrl(), songWithFile.getFile(), new IoUtils.FileDownloadedListener() {
                        @Override
                        public void on(String url, File file) {
                            if (!sync.getAndSet(true)) {
                                listener.on(songWithFile);
                            } else {
                                that.nextSongWithFile = songWithFile;
                            }

                            scheduleNextSong();
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
        //todo
    }

    interface NextSongProvider {

        VkSong next();

    }
}
