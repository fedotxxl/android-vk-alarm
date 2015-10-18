package io.belov.vk.alarm.utils;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import io.belov.vk.alarm.R;
import io.belov.vk.alarm.persistence.Alarm;

/**
 * Created by fbelov on 18.10.15.
 */
public class AlarmUtils {

    public static String getWhenAsString(Alarm alarm) {
        return String.format("%02d:%02d", alarm.getWhenHours(), alarm.getWhenMinutes());
    }

    public static String getRepeatAsString(Context context, Alarm alarm) {
        int repeats = alarm.getRepeat();

        if (repeats <= 0) {
            return context.getString(R.string.repeat_once);
        } else {
            boolean hasRepeat = false;
            StringBuilder sb = new StringBuilder();

            for (Alarm.Repeat repeat : Alarm.Repeat.values()) {
                if (repeat.isSupportedBy(repeats)) {
                    if (hasRepeat) sb.append(" ");
                    sb.append(context.getString(context.getResources().getIdentifier("repeat_" + repeat.toString().toLowerCase(), "string", context.getApplicationInfo().packageName)));
                    hasRepeat = true;
                }
            }

            return sb.toString();
        }
    }
}
