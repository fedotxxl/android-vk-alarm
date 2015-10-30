package io.belov.vk.alarm.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fbelov on 30.10.15.
 */
public class FileDownloader {

    private Map<FileWithUrl, List<IoUtils.FileDownloadedListener>> downloadingFiles = new HashMap<>();

    public synchronized void downloadAsync(final String fileUrl, final File fileTo) {
        downloadAsync(fileUrl, fileTo, null);
    }

    public synchronized void downloadAsync(final String fileUrl, final File fileTo, final IoUtils.FileDownloadedListener listener) {
        final FileWithUrl fileWithUrl = new FileWithUrl(fileTo, fileUrl);

        if (isDownloading(fileWithUrl)) {
            addListener(fileWithUrl, listener);
        } else {
            onDownloadStart(fileWithUrl, listener);

            IoUtils.downloadAsync(fileUrl, fileTo, new IoUtils.FileDownloadedListener() {
                @Override
                public void on(String url, File file) {
                    onDownloadCompleted(fileWithUrl);
                }
            });
        }
    }

    private void addListener(FileWithUrl fileWithUrl, IoUtils.FileDownloadedListener listener) {
        if (listener != null) {
            downloadingFiles.get(fileWithUrl).add(listener);
        }
    }

    private boolean isDownloading(FileWithUrl fileWithUrl) {
        return downloadingFiles.containsKey(fileWithUrl);
    }

    private void onDownloadStart(FileWithUrl fileWithUrl, IoUtils.FileDownloadedListener listener) {
        List<IoUtils.FileDownloadedListener> listeners = new ArrayList<>();

        if (listener != null) {
            listeners.add(listener);
        }

        downloadingFiles.put(fileWithUrl, listeners);
    }

    private synchronized void onDownloadCompleted(FileWithUrl fileWithUrl) {
        List<IoUtils.FileDownloadedListener> listeners = downloadingFiles.remove(fileWithUrl);

        for (IoUtils.FileDownloadedListener listener : listeners) {
            listener.on(fileWithUrl.url, fileWithUrl.file);
        }
    }

    private static class FileWithUrl {

        private File file;
        private String url;

        public FileWithUrl(File file, String url) {
            this.file = file;
            this.url = url;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            FileWithUrl that = (FileWithUrl) o;

            if (!file.equals(that.file)) return false;
            return url.equals(that.url);

        }

        @Override
        public int hashCode() {
            int result = file.hashCode();
            result = 31 * result + url.hashCode();
            return result;
        }
    }
}
