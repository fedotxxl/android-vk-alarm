package io.belov.vk.alarm.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.PreferenceManager;

import io.belov.vk.alarm.audio.PlayerConnectionType;
import io.belov.vk.alarm.utils.FileDownloadPreferences;
import io.belov.vk.alarm.utils.to;

/**
 * Created by fbelov on 29.10.15.
 */
public class PreferencesManager {

    private SharedPreferences settings;
    private AppPreferences appPreferences;

    public PreferencesManager(Context context) {
        this.settings = initialize(context);
        this.appPreferences = read();
    }

    public AppPreferences get() {
        return appPreferences;
    }

    public AppPreferences read() {
        AppPreferences answer = new AppPreferences();

        answer.setMaxSongDurationInSeconds((settings.getInt("maxSongDurationInSeconds", AppPreferences.DEFAULT.getMaxSongDurationInSeconds())));
        answer.setPlayerBackupDelayInSeconds((settings.getInt("playerBackupDelayInSeconds", AppPreferences.DEFAULT.getPlayerBackupDelayInSeconds())));
        answer.setPlayerConnectionType((settings.getString("playerConnectionType", AppPreferences.DEFAULT.getPlayerConnectionType().getId())));

        return answer;
    }

    private SharedPreferences initialize(Context context) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);

        if (!settings.getBoolean("settingsInitialized", false)) {
            settings.edit()
                    .putInt("maxSongDurationInSeconds", AppPreferences.DEFAULT.getMaxSongDurationInSeconds())
                    .putInt("playerBackupDelayInSeconds", AppPreferences.DEFAULT.getPlayerBackupDelayInSeconds())
                    .putString("playerConnectionType", AppPreferences.DEFAULT.getPlayerConnectionType().getId())
                    .putBoolean("settingsInitialized", true)
                    .commit();
        }

        return settings;
    }

    private Uri getBackupSoundUri() {
        Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

        if (alert == null) {
            // alert is null, using backup
            alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            // I can't see this ever being null (as always have a default notification)
            // but just incase
            if (alert == null) {
                // alert backup is null, using 2nd backup
                alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            }
        }

        return alert;
    }

}
