package io.belov.vk.alarm.alert;

import java.io.Serializable;

import io.belov.vk.alarm.alarm.Alarm;
import io.belov.vk.alarm.utils.AlarmUtils;

/**
 * Created by fbelov on 19.10.15.
 */
public class AlarmAlert implements Serializable {

    private static final long serialVersionUID = 8699489847426803789L;

    private int id;
    private int disableComplexity;
    private int snoozeInMinutes;
    private boolean isVibrate;

    private int songId;
    private String songTitle;
    private String songBandName;

    public AlarmAlert(Alarm alarm) {
        this.id = alarm.getId();
        this.disableComplexity = alarm.getDisableComplexity();
        this.snoozeInMinutes = alarm.getSnoozeInMinutes();
        this.isVibrate = alarm.isVibrate();
        this.songId = alarm.getSongId();
        this.songTitle = AlarmUtils.getWhenAsString(alarm); //todo
        this.songBandName = alarm.getSongBandName();
    }

    public boolean hasSong() {
        return songId > 0;
    }

    public boolean isRandom() {
        return !hasSong();
    }

    public int getId() {
        return id;
    }

    public int getDisableComplexity() {
        return disableComplexity;
    }

    public int getSnoozeInMinutes() {
        return snoozeInMinutes;
    }

    public boolean isVibrate() {
        return isVibrate;
    }

    public int getSongId() {
        return songId;
    }

    public String getSongTitle() {
        return songTitle;
    }

    public String getSongBandName() {
        return songBandName;
    }
}
