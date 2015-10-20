package io.belov.vk.alarm;

import io.belov.vk.alarm.persistence.Alarm;
import io.belov.vk.alarm.utils.StringUtils;

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

    public void setPropertiesFrom(Alarm data) {
        setPropertiesFrom(data.getId(), data);
    }

    public void setPropertiesFrom(int id, Alarm data) {
        alarm.setId(id);
        alarm.setWhenHours(data.getWhenHours());
        alarm.setWhenMinutes(data.getWhenMinutes());
        alarm.setLabel(data.getLabel());
        alarm.setDisableComplexity(data.getDisableComplexity());
        alarm.setRepeat(data.getRepeat());
        alarm.setSnoozeInMinutes(data.getSnoozeInMinutes());
        alarm.setIsEnabled(data.isEnabled());
        alarm.setIsVibrate(data.isVibrate());
        alarm.setSongId(data.getSongId());
        alarm.setSongTitle(data.getSongTitle());
        alarm.setSongBandName(data.getSongBandName());
    }

    public int getWhenInMinutes() {
        return alarm.getWhenHours()*60 + alarm.getWhenMinutes();
    }

    public boolean hasLabel() {
        return StringUtils.isEmpty(alarm.getLabel());
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

    public void setWhen(int hourOfDay, int minute) {
        alarm.setWhenHours(hourOfDay);
        alarm.setWhenMinutes(minute);
    }
}
