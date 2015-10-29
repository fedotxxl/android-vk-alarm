package io.belov.vk.alarm.preferences;

import android.net.Uri;

/**
 * Created by fbelov on 29.10.15.
 */
public class PlayerPreferences {

    public static PlayerPreferences DEFAULT = new PlayerPreferences(true, null, 0);

    private boolean downloadByWifiOnly;
    private Uri backupUri;
    private int backupDelayInMillis;

    public PlayerPreferences(boolean downloadByWifiOnly, Uri backupUri, int backupDelayInMillis) {
        this.downloadByWifiOnly = downloadByWifiOnly;
        this.backupUri = backupUri;
        this.backupDelayInMillis = backupDelayInMillis;
    }

    public boolean isDownloadByWifiOnly() {
        return downloadByWifiOnly;
    }

    public int getBackupDelayInMillis() {
        return backupDelayInMillis;
    }

    public Uri getBackupUri() {
        return backupUri;
    }
}
