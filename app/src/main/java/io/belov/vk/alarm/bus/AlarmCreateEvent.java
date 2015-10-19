package io.belov.vk.alarm.bus;

/**
 * Created by fbelov on 20.10.15.
 */
public class AlarmCreateEvent {

    private int hourOfDay;
    private int minute;

    public AlarmCreateEvent(int hourOfDay, int minute) {
        this.hourOfDay = hourOfDay;
        this.minute = minute;
    }

    public int getHourOfDay() {
        return hourOfDay;
    }

    public int getMinute() {
        return minute;
    }
}
