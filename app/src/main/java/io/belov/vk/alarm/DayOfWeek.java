package io.belov.vk.alarm;

import java.util.Calendar;

/**
 * Created by fbelov on 25.10.15.
 */
public enum DayOfWeek {

    MO(1, Calendar.MONDAY), TU(2, Calendar.TUESDAY), WE(3, Calendar.WEDNESDAY), TH(4, Calendar.THURSDAY), FR(5, Calendar.FRIDAY), SA(6, Calendar.SATURDAY), SU(7, Calendar.SUNDAY);

    private int id;
    private int calendarId;

    DayOfWeek(int id, int calendarId) {
        this.id = id;
        this.calendarId = calendarId;
    }

    public int getId() {
        return id;
    }

    public int getCalendarId() {
        return calendarId;
    }

}
