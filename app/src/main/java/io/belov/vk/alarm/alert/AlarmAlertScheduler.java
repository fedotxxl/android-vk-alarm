package io.belov.vk.alarm.alert;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.belov.vk.alarm.DayOfWeek;
import io.belov.vk.alarm.alarm.Alarm;
import io.belov.vk.alarm.utils.CalendarUtils;

/**
 * Created by fbelov on 19.10.15.
 */
public class AlarmAlertScheduler {

    public static final long INTERVAL_WEEK = AlarmManager.INTERVAL_DAY * 7;

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

    public void unschedule(int alarmId) {
        doUnschedule(getId(alarmId));

        for (DayOfWeek dayOfWeek : DayOfWeek.values()) {
            doUnschedule(getId(alarmId, dayOfWeek));
        }
    }

    private void doSchedule(Alarm alarm) {
        //schedule / unschedule 8 alarms - 7 days + non repeating one with null day

        int alarmId = alarm.getId();

        List<DayOfWeek> daysToSchedule = new ArrayList<>();
        List<DayOfWeek> daysToUnschedule = new ArrayList<>();

        if (alarm.isRepeating()) {
            daysToUnschedule.add(null);

            for (DayOfWeek dayOfWeek : DayOfWeek.values()) {
                if (alarm.isRepeatActive(dayOfWeek)) {
                    daysToSchedule.add(dayOfWeek);
                } else {
                    daysToUnschedule.add(dayOfWeek);
                }
            }
        } else {
            daysToSchedule.add(null);
            Collections.addAll(daysToUnschedule, DayOfWeek.values());
        }

        for (DayOfWeek dayOfWeek : daysToSchedule) {
            if (dayOfWeek == null) {
                alarmManager.set(AlarmManager.RTC_WAKEUP, getAlarmTime(alarm, null), getPendingIntent(getId(alarmId), alarm));
            } else {
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, getAlarmTime(alarm, dayOfWeek), INTERVAL_WEEK, getPendingIntent(getId(alarmId, dayOfWeek), alarm));
            }
        }

        for (DayOfWeek dayOfWeek : daysToUnschedule) {
            doUnschedule(getId(alarmId, dayOfWeek));
        }
    }

    private long getAlarmTime(Alarm alarm, DayOfWeek dayOfWeek) {
        return CalendarUtils.getCalendarForMoment(dayOfWeek, alarm.getWhenHours(), alarm.getWhenMinutes()).getTimeInMillis();
    }

    private void doUnschedule(int pendingIntentId) {
        alarmManager.cancel(getPendingIntent(pendingIntentId));
    }

    private PendingIntent getPendingIntent(int id, Alarm alarm) {
        Intent myIntent = new Intent(context, AlarmAlertBroadcastReceiver.class);
        myIntent.putExtra("alarmAlert", new AlarmAlert(alarm));

        return PendingIntent.getBroadcast(context, id, myIntent, PendingIntent.FLAG_CANCEL_CURRENT);
    }

    private PendingIntent getPendingIntent(int id) {
        return PendingIntent.getBroadcast(context, id, new Intent(context, AlarmAlertBroadcastReceiver.class), PendingIntent.FLAG_CANCEL_CURRENT);
    }

    private int getId(int alarmId) {
        return getId(alarmId, null);
    }

    private int getId(int alarmId, DayOfWeek dayOfWeek) {
        if (dayOfWeek == null) { //once - no repeat
            return alarmId * 10;
        } else {
            return alarmId * 10 + dayOfWeek.getId();
        }
    }

}
