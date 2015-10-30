package io.belov.vk.alarm.song;

import java.io.File;

import io.belov.vk.alarm.storage.SongsCacheI;
import io.belov.vk.alarm.utils.IoUtils;
import io.belov.vk.alarm.vk.VkSong;
import io.belov.vk.alarm.vk.VkSongWithFile;

/**
 * Created by fbelov on 30.10.15.
 */
public class SongDownloader {

    private SongsCacheI cache;

    public SongDownloader(SongsCacheI cache) {
        this.cache = cache;
    }

    public void downloadAndCache(VkSong song, final SongsCacheI.Importance importance, final SongDownloadedListener songDownloadedListener) {
        final int songCacheKey = song.getId();
        final SongsCacheI.FileWithData fileWithData = cache.get(songCacheKey);
        final VkSongWithFile songWithFile = new VkSongWithFile(song, fileWithData.getFile());

        if (songWithFile.isFileExists()) {
            cache.touchAndSetImportance(songCacheKey, importance);
            
            songDownloadedListener.on(songWithFile);
        } else {
            downloadAnd(songWithFile, new SongDownloadedListener() {
                @Override
                public void on(VkSongWithFile song) {
                    cache.touchAndSetImportance(songCacheKey, importance);
                    songDownloadedListener.on(songWithFile);
                }
            });
        }
    }

    private void downloadAnd(final VkSongWithFile songWithFile, final SongDownloadedListener songDownloadedListener) {
        IoUtils.downloadAsync(songWithFile.getUrl(), songWithFile.getFile(), new IoUtils.FileDownloadedListener() {
            @Override
            public void on(String url, File file) {
                if (file != null && file.exists()) {
                    songDownloadedListener.on(songWithFile);
                }
            }
        });
    }
}
