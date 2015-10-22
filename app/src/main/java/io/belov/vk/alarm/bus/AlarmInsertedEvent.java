package io.belov.vk.alarm.bus;

import io.belov.vk.alarm.alarm.Alarm;

/**
 * Created by fbelov on 22.10.15.
 */
public class AlarmInsertedEvent {

    private Alarm alarm;

    public AlarmInsertedEvent(Alarm alarm) {
        this.alarm = alarm;
    }

    public Alarm getAlarm() {
        return alarm;
    }
}
