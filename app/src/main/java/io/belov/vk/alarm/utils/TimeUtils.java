package io.belov.vk.alarm.utils;

/**
 * Created by fbelov on 24.10.15.
 */
public class TimeUtils {

    public static String getWhenAsString(int hours, int minutes) {
        return String.format("%02d:%02d", hours, minutes);
    }

}
