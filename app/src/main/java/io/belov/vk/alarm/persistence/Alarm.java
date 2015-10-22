package io.belov.vk.alarm.persistence;

public class Alarm {

    private int id;
    private int whenHours;
    private int whenMinutes;
    private int disableComplexity;
    private int repeat;
    private int snoozeInMinutes;
    private boolean isEnabled;
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
        this.isEnabled = (isEnabled != 0);
    }

    public void setIsVibrate(int isVibrate) {
        this.isVibrate = (isVibrate != 0);
    }

    public void toggleEnabled() {
        this.isEnabled = !isEnabled;
    }

    public boolean hasSong() {
        return songId != 0;
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
