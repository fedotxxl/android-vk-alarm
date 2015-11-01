package io.belov.vk.alarm.preferences;

import android.net.Uri;

import io.belov.vk.alarm.utils.FileDownloadPreferences;

/**
 * Created by fbelov on 29.10.15.
 */
public class PlayerPreferences {

    public static PlayerPreferences DEFAULT = new PlayerPreferences(true, null, 0);

    private FileDownloadPreferences fileDownloadPreferences;
    private Uri backupUri;
    private int backupDelayInMillis;

    public PlayerPreferences(boolean downloadByWifiOnly, Uri backupUri, int backupDelayInMillis) {
        this.fileDownloadPreferences = new FileDownloadPreferences(downloadByWifiOnly);
        this.backupUri = backupUri;
        this.backupDelayInMillis = backupDelayInMillis;
    }

    public boolean isDownloadByWifiOnly() {
        return fileDownloadPreferences.isDownloadByWifiOnly();
    }

    public FileDownloadPreferences getFileDownloadPreferences() {
        return fileDownloadPreferences;
    }

    public int getBackupDelayInMillis() {
        return backupDelayInMillis;
    }

    public Uri getBackupUri() {
        return backupUri;
    }
}
