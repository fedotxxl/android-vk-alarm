package io.belov.vk.alarm.utils;

import android.app.Activity;
import android.content.Intent;

import io.belov.vk.alarm.persistence.Alarm;
import io.belov.vk.alarm.ui.AlarmEditActivity;
import io.belov.vk.alarm.ui.AlarmListActivity;
import io.belov.vk.alarm.ui.LoginActivity;

/**
 * Created by fbelov on 19.10.15.
 */
public class ActivityUtils {

    public static void openAlarmsListActivity(Activity activity) {
        activity.startActivity(new Intent(activity, AlarmListActivity.class));
    }

    public static void openAlarmsListActivityWithoutAnimation(Activity activity) {
        openAlarmsListActivity(activity);
        disableAnimation(activity);
    }

    public static void openLoginActivity(Activity activity) {
        activity.startActivity(new Intent(activity, LoginActivity.class));
    }

    public static void openLoginWithoutAnimation(Activity activity) {
        openLoginActivity(activity);
        disableAnimation(activity);
    }

    public static void openAlarm(Alarm alarm, Activity activity) {
        activity.startActivity(AlarmEditActivity.createIntent(activity, alarm.getId()));
    }

    private static void disableAnimation(Activity activity) {
        activity.overridePendingTransition(0, 0);
    }
}
