package io.belov.vk.alarm.alert;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import io.belov.vk.alarm.persistence.Alarm;

/**
 * Created by fbelov on 19.10.15.
 */
public class AlarmAlertScheduler {

    private Context context;
    private AlarmManager alarmManager;

    public AlarmAlertScheduler(Context context) {
        this.context = context;
        this.alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    public void schedule(Alarm alarm) {
        Intent myIntent = new Intent(context, AlarmAlertBroadcastReceiver.class);
        myIntent.putExtra("alarmAlert", new AlarmAlert(alarm));

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, myIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        alarmManager.set(AlarmManager.RTC_WAKEUP, getAlarmTime(alarm), pendingIntent);
    }

    private long getAlarmTime(Alarm alarm) {
        return System.currentTimeMillis() + 1000*5;
    }

}
