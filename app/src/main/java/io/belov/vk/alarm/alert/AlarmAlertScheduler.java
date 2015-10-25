package io.belov.vk.alarm.alert;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.squareup.otto.Subscribe;

import io.belov.vk.alarm.alarm.Alarm;
import io.belov.vk.alarm.bus.AlarmDeletedEvent;
import io.belov.vk.alarm.bus.AlarmInsertedEvent;
import io.belov.vk.alarm.bus.AlarmUpdatedEvent;

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
        if (alarm.isEnabled()) {
            doSchedule(alarm);
        } else {
            unschedule(alarm.getId());
        }
    }

    public void unschedule(int id) {
        PendingIntent pendingIntent = getPendingIntent(id);

        alarmManager.cancel(pendingIntent);
    }

    private void doSchedule(Alarm alarm) {
        PendingIntent pendingIntent = getPendingIntent(alarm);

        if (alarm.isRepeating()) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, getAlarmTime(alarm), AlarmManager.INTERVAL_DAY, pendingIntent);
            //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY * 7, pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, getAlarmTime(alarm), pendingIntent);
        }
    }

    private PendingIntent getPendingIntent(Alarm alarm) {
        Intent myIntent = new Intent(context, AlarmAlertBroadcastReceiver.class);
        myIntent.putExtra("alarmAlert", new AlarmAlert(alarm));

        return PendingIntent.getBroadcast(context, alarm.getId(), myIntent, PendingIntent.FLAG_CANCEL_CURRENT);
    }

    private PendingIntent getPendingIntent(int id) {
        return PendingIntent.getBroadcast(context, id, new Intent(context, AlarmAlertBroadcastReceiver.class), PendingIntent.FLAG_CANCEL_CURRENT);
    }

    private long getAlarmTime(Alarm alarm) {
        return System.currentTimeMillis() + 1000*5;
    }

}
