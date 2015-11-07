package io.belov.vk.alarm.song;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

import io.belov.vk.alarm.storage.SongsCacheI;
import io.belov.vk.alarm.utils.AndroidUtils;
import io.belov.vk.alarm.utils.FileDownloadPreferences;
import io.belov.vk.alarm.utils.FileDownloader;
import io.belov.vk.alarm.utils.IoUtils;
import io.belov.vk.alarm.vk.VkSong;
import io.belov.vk.alarm.vk.VkSongWithFile;

/**
 * Created by fbelov on 30.10.15.
 */
public class SongDownloader {

    private static final String TAG = "SongDownloader";

    private Context context;
    private SongsCacheI cache;
    private FileDownloader fileDownloader;

    public SongDownloader(Context context, SongsCacheI cache, FileDownloader fileDownloader) {
        this.context = context;
        this.cache = cache;
        this.fileDownloader = fileDownloader;
    }

    public void downloadAndCache(VkSong song, SongsCacheI.Importance importance, FileDownloadPreferences fileDownloadPreferences) {
        downloadAndCache(song, importance, null, null, fileDownloadPreferences, -1);
    }

    public void downloadAndCache(VkSong song, final SongsCacheI.Importance importance, @Nullable final SongDownloadedListener songDownloadSuccessListener, @Nullable final Runnable songDownloadFailureListener, FileDownloadPreferences fileDownloadPreferences, long maxDownloadDelayInMillis) {
        final int songCacheKey = song.getId();
        final SongsCacheI.FileWithData fileWithData = cache.get(songCacheKey);
        final VkSongWithFile songWithFile = new VkSongWithFile(song, fileWithData.getFile());

        final Runnable onSuccess = new Runnable() {
            @Override
            public void run() {
                cache.touchAndSetImportance(songCacheKey, importance);

                if (songDownloadSuccessListener != null)
                    songDownloadSuccessListener.on(songWithFile);
            }
        };

        final Runnable onFailure = new Runnable() {
            @Override
            public void run() {
                if (songDownloadFailureListener != null) songDownloadFailureListener.run();
            }
        };

        if (songWithFile.isFileExists()) {
            onSuccess.run();
        } else if (!canDownload(songWithFile.getFile(), fileDownloadPreferences)) {
            onFailure.run();
        } else {
            try {
                final AtomicBoolean sync = new AtomicBoolean(false);

                fileDownloader.downloadAsync(songWithFile.getUrl(), songWithFile.getFile(), new IoUtils.FileDownloadedListener() {
                    @Override
                    public void on(String url, File file) {
                        sync.set(true);
                        onSuccess.run();
                    }
                });

                if (maxDownloadDelayInMillis > 0) {
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            if (!sync.get()) {
                                onFailure.run();
                            }
                        }
                    }, maxDownloadDelayInMillis);
                }
            } catch (Exception e) {
                Log.e(TAG, "Exception on downloading " + song.getUrl(), e);
                onFailure.run();
            }
        }
    }

    private boolean canDownload(File file, FileDownloadPreferences preferences) {
        AndroidUtils.ConnectionInfo connectionInfo = AndroidUtils.getConnectionInfo(context);

        if (preferences.isDownloadByWifiOnly()) {
            return connectionInfo.isWifi();
        } else {
            return connectionInfo.hasConnection();
        }
    }

}
