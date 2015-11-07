package io.belov.vk.alarm.preferences;

import io.belov.vk.alarm.audio.PlayerConnectionType;
import io.belov.vk.alarm.utils.FileDownloadPreferences;

/**
 * Created by fbelov on 06.11.15.
 */
public class AppPreferences {

    public static final AppPreferences DEFAULT = new AppPreferences(0, 30, PlayerConnectionType.WIFI_ONLY);

    private int maxSongDurationInSeconds;
    private int playerBackupDelayInSeconds;
    private PlayerConnectionType playerConnectionType;

    public AppPreferences() {
    }

    public AppPreferences(int maxSongDurationInSeconds, int playerBackupDelayInSeconds, PlayerConnectionType playerConnectionType) {
        this.maxSongDurationInSeconds = maxSongDurationInSeconds;
        this.playerBackupDelayInSeconds = playerBackupDelayInSeconds;
        this.playerConnectionType = playerConnectionType;
    }

    public boolean isMaxSongDurationLimited() {
        return maxSongDurationInSeconds > 0;
    }

    public boolean isPlayerBackupDelaySet() {
        return playerBackupDelayInSeconds > 0;
    }

    public FileDownloadPreferences getPlayerFileDownloadPreferences() {
        return new FileDownloadPreferences(playerConnectionType);
    }

    public int getMaxSongDurationInSeconds() {
        return maxSongDurationInSeconds;
    }

    public int getPlayerBackupDelayInSeconds() {
        return playerBackupDelayInSeconds;
    }

    public PlayerConnectionType getPlayerConnectionType() {
        return playerConnectionType;
    }

    public AppPreferences setMaxSongDurationInSeconds(int maxSongDurationInSeconds) {
        this.maxSongDurationInSeconds = maxSongDurationInSeconds;

        return this;
    }

    public AppPreferences setPlayerBackupDelayInSeconds(int playerBackupDelayInSeconds) {
        this.playerBackupDelayInSeconds = playerBackupDelayInSeconds;

        return this;
    }

    public AppPreferences setPlayerConnectionType(String playerConnectionType) {
        return setPlayerConnectionType(PlayerConnectionType.myValueOf(playerConnectionType));
    }

    public AppPreferences setPlayerConnectionType(PlayerConnectionType playerConnectionType) {
        this.playerConnectionType = playerConnectionType;

        return this;
    }
}
