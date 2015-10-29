package io.belov.vk.alarm.preferences;

import android.media.RingtoneManager;
import android.net.Uri;

/**
 * Created by fbelov on 29.10.15.
 */
public class PreferencesManager {

    public PlayerPreferences getPlayerPreferences() {
        return new PlayerPreferences(true, getBackupSoundUri(), 3000);
    }

    private Uri getBackupSoundUri() {
        Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

        if(alert == null){
            // alert is null, using backup
            alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            // I can't see this ever being null (as always have a default notification)
            // but just incase
            if(alert == null) {
                // alert backup is null, using 2nd backup
                alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            }
        }

        return alert;
    }

}
