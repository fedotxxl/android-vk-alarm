package io.belov.vk.alarm;

import io.belov.vk.alarm.persistence.Alarm;

/**
 * Created by fbelov on 19.10.15.
 */
public class AlarmWrapper {

    private Alarm alarm;

    public AlarmWrapper(Alarm alarm) {
        this.alarm = alarm;
    }

    public Alarm getAlarm() {
        return alarm;
    }

    public boolean isRepeatActive(Alarm.Repeat repeat) {
        return repeat.isSupportedBy(alarm.getRepeat());
    }

    public void toggleRepeat(Alarm.Repeat repeat) {
        int repeats = alarm.getRepeat();

        if (isRepeatActive(repeat)) {
            repeats -= repeat.getId();
        } else {
            repeats += repeat.getId();
        }

        alarm.setRepeat(repeats);
    }
}
