package io.belov.vk.alarm.alarm;

import java.util.Calendar;

import io.belov.vk.alarm.DayOfWeek;
import io.belov.vk.alarm.utils.CalendarUtils;
import io.belov.vk.alarm.utils.StringUtils;

public class Alarm {

    private int id;
    private int whenHours;
    private int whenMinutes;
    private int disableComplexity;
    private int repeat;
    private int snoozeInMinutes;
    private boolean isEnabled;
    private long enabledAt;
    private boolean isVibrate;
    private String label;

    private int songId;
    private String songTitle;
    private String songBandName;

    public Alarm() {
    }

    public Alarm(Alarm alarm) {
        this.id = alarm.getId();
        this.whenHours = alarm.getWhenHours();
        this.whenMinutes = alarm.getWhenMinutes();
        this.disableComplexity = alarm.getDisableComplexity();
        this.repeat = alarm.getRepeat();
        this.snoozeInMinutes = alarm.getSnoozeInMinutes();
        this.isEnabled = alarm.isEnabled();
        this.enabledAt = alarm.getEnabledAt();
        this.isVibrate = alarm.isVibrate();
        this.label = alarm.getLabel();
        this.songId = alarm.getSongId();
        this.songTitle = alarm.getSongTitle();
        this.songBandName = alarm.getSongBandName();
    }


    //    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public boolean isCompleted() {
//        return completed;
//    }
//
//    public void setCompleted(boolean completed) {
//        this.completed = completed;
//    }
//}

    public void setIsEnabled(int isEnabled) {
        setIsEnabled(isEnabled != 0);
    }

    public void setIsVibrate(int isVibrate) {
        this.isVibrate = (isVibrate != 0);
    }

    public void enable() {
        this.isEnabled = true;
        this.enabledAt = System.currentTimeMillis();
    }

    public void disable() {
        this.isEnabled = false;
        this.enabledAt = 0;
    }

    public void toggleEnabled() {
        if (isScheduled()) {
            disable();
        } else {
            enable();
        }
    }

    public boolean hasSong() {
        return songId != 0;
    }

    public int getWhenInMinutes() {
        return whenHours*60 + whenMinutes;
    }

    public boolean hasLabel() {
        return StringUtils.isNotEmpty(label);
    }

    public boolean isRepeatActive(Alarm.Repeat repeat) {
        return repeat.isSupportedBy(this.repeat);
    }

    public boolean isRepeatActive(DayOfWeek dayOfWeek) {
        return Repeat.getRepeatForDay(dayOfWeek).isSupportedBy(this.repeat);
    }

    public void toggleRepeat(Alarm.Repeat repeat) {
        int repeats = this.repeat;

        if (isRepeatActive(repeat)) {
            repeats -= repeat.getId();
        } else {
            repeats += repeat.getId();
        }

        this.repeat = repeats;
    }

    public void setWhen(int hourOfDay, int minute) {
        this.whenHours = hourOfDay;
        this.whenMinutes = minute;
    }

    public void setSong(int id, String title, String artist) {
        this.songId = id;
        this.songTitle = title;
        this.songBandName = artist;
    }

    public void setSongRandom() {
        songId = 0;
        songTitle = null;
        songBandName = null;
    }

    public boolean isOnce() {
        return !isRepeating();
    }

    public boolean isRepeating() {
        return repeat > 0;
    }

    public boolean isScheduled() {
        if (!isEnabled()) {
            return false;
        } else if (isRepeating()) {
            return true;
        } else {
            return isEnabledLessThanDayAgo();
        }
    }

    public enum DisableComplexity {
        EASY(1), MEDIUM(2), HARD(3);

        private int id;

        DisableComplexity(int id) {
            this.id = id;
        }

        public static DisableComplexity myValueOf(int id) {
            for (DisableComplexity disableComplexity : values()) {
                if (disableComplexity.id == id) return disableComplexity;
            }

            return null;
        }

        public int getId() {
            return id;
        }
    }

    public enum Repeat {
        MO(1), TU(2), WE(4), TH(8), FR(16), SA(32), SU(64);

        private int id;

        Repeat(Integer id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public boolean isSupportedBy(int repeats) {
            return ((repeats & id) != 0);
        }

        public static Repeat getRepeatForDay(DayOfWeek dayOfWeek) {
            return valueOf(dayOfWeek.toString());
        }
    }

    private boolean isEnabledLessThanDayAgo() {
        if (enabledAt <= 0) {
            return false;
        } else {
            Calendar now = Calendar.getInstance();
            Calendar enabledDate = CalendarUtils.getCalendarForMoment(enabledAt);
            Calendar nextPlayDate = CalendarUtils.getNextPlayDate(enabledDate, whenHours, whenMinutes);

            return now.before(nextPlayDate);
        }
    }

    //GET / SET

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWhenHours() {
        return whenHours;
    }

    public void setWhenHours(int whenHours) {
        this.whenHours = whenHours;
    }

    public int getWhenMinutes() {
        return whenMinutes;
    }

    public void setWhenMinutes(int whenMinutes) {
        this.whenMinutes = whenMinutes;
    }

    public int getDisableComplexity() {
        return disableComplexity;
    }

    public void setDisableComplexity(int disableComplexity) {
        this.disableComplexity = disableComplexity;
    }

    public int getRepeat() {
        return repeat;
    }

    public void setRepeat(int repeat) {
        this.repeat = repeat;
    }

    public int getSnoozeInMinutes() {
        return snoozeInMinutes;
    }

    public void setSnoozeInMinutes(int snoozeInMinutes) {
        this.snoozeInMinutes = snoozeInMinutes;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public long getEnabledAt() {
        return enabledAt;
    }

    public void setEnabledAt(long enabledAt) {
        this.enabledAt = enabledAt;
    }

    public boolean isVibrate() {
        return isVibrate;
    }

    public void setIsVibrate(boolean isVibrate) {
        this.isVibrate = isVibrate;
    }

    public int getSongId() {
        return songId;
    }

    public void setSongId(int songId) {
        this.songId = songId;
    }

    public String getSongTitle() {
        return songTitle;
    }

    public void setSongTitle(String songTitle) {
        this.songTitle = songTitle;
    }

    public String getSongBandName() {
        return songBandName;
    }

    public void setSongBandName(String songBandName) {
        this.songBandName = songBandName;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
