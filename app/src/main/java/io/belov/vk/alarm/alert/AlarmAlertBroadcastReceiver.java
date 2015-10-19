package io.belov.vk.alarm.alert;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by fbelov on 19.10.15.
 */
public class AlarmAlertBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        StaticWakeLock.lockOn(context);

        AlarmAlert alarmAlert = (AlarmAlert) intent.getExtras().getSerializable("alarmAlert");

        Intent mathAlarmAlertActivityIntent = new Intent(context, AlarmAlertActivity.class);
        mathAlarmAlertActivityIntent.putExtra("alarmAlert", alarmAlert);
        mathAlarmAlertActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(mathAlarmAlertActivityIntent);
    }

}
