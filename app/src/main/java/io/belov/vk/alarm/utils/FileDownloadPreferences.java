package io.belov.vk.alarm.utils;

/**
 * Created by fbelov on 01.11.15.
 */
public class FileDownloadPreferences {

    public static final FileDownloadPreferences ALWAYS = new FileDownloadPreferences(false);

    private boolean downloadByWifiOnly;

    public FileDownloadPreferences(boolean downloadByWifiOnly) {
        this.downloadByWifiOnly = downloadByWifiOnly;
    }

    public boolean isDownloadByWifiOnly() {
        return downloadByWifiOnly;
    }
}
