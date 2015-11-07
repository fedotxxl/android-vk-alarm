package io.belov.vk.alarm.utils;

import io.belov.vk.alarm.audio.PlayerConnectionType;

/**
 * Created by fbelov on 01.11.15.
 */
public class FileDownloadPreferences {

    private PlayerConnectionType playerConnectionType;

    public FileDownloadPreferences(PlayerConnectionType playerConnectionType) {
        this.playerConnectionType = playerConnectionType;
    }

    public boolean isDownloadByWifiOnly() {
        return playerConnectionType == PlayerConnectionType.WIFI_ONLY;
    }
}
