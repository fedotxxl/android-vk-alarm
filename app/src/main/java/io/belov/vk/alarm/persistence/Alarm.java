package io.belov.vk.alarm.persistence;

import io.realm.RealmObject;

public class Alarm extends RealmObject {

    private int id;
    private int whenHours;
    private int whenMinutes;
    private int disableComplexity;
    private int repeat;
    private int snoozeInMinutes;
    private boolean isEnabled;
    private boolean isVibrate;

    private String songId;
    private String songTitle;
    private String songBandName;

    public Alarm() {
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

    public enum DisableComplexity {
        EASY(1), MEDIUM(2), HARD(3);

        private int id;

        DisableComplexity(int id) {
            this.id = id;
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

    public boolean isVibrate() {
        return isVibrate;
    }

    public void setIsVibrate(boolean isVibrate) {
        this.isVibrate = isVibrate;
    }

    public String getSongId() {
        return songId;
    }

    public void setSongId(String songId) {
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
}
