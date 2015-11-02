package io.belov.vk.alarm.utils;

import junit.framework.TestCase;

import java.util.Calendar;

import io.belov.vk.alarm.DayOfWeek;

/**
 * Created by fbelov on 25.10.15.
 */
public class CalendarUtilsTest extends TestCase {

    public void testShouldReturnCorrectCalendarForDay() {
        Calendar c;
        Calendar now = Calendar.getInstance();
        int nowDayOfWeek = now.get(Calendar.DAY_OF_WEEK);
        int nowHours = now.get(Calendar.HOUR_OF_DAY);
        int nowMinutes = now.get(Calendar.MINUTE);
        int nowDayOfYear = now.get(Calendar.DAY_OF_YEAR);

        //then:
        c = CalendarUtils.getCalendarForMoment((DayOfWeek) null, nowHours, nowMinutes - 1);

        assertEquals(c.get(Calendar.DAY_OF_YEAR), nowDayOfYear + 1);
        assertEquals(c.get(Calendar.HOUR_OF_DAY), nowHours);
        assertEquals(c.get(Calendar.MINUTE), nowMinutes - 1);

        //then:
        c = CalendarUtils.getCalendarForMoment((DayOfWeek) null, nowHours, nowMinutes + 1);

        assertEquals(c.get(Calendar.DAY_OF_YEAR), nowDayOfYear);
        assertEquals(c.get(Calendar.HOUR_OF_DAY), nowHours);
        assertEquals(c.get(Calendar.MINUTE), nowMinutes + 1);

        //then:
        for (DayOfWeek dayOfWeek : DayOfWeek.values()) {
            int daysDifference = dayOfWeek.getCalendarId() - nowDayOfWeek;

            if (daysDifference == 0) {
                //this day
                c = CalendarUtils.getCalendarForMoment(dayOfWeek, nowHours, nowMinutes + 1);

                assertEquals(c.get(Calendar.DAY_OF_YEAR), nowDayOfYear);
                assertEquals(c.get(Calendar.HOUR_OF_DAY), nowHours);
                assertEquals(c.get(Calendar.MINUTE), nowMinutes + 1);

                c = CalendarUtils.getCalendarForMoment(dayOfWeek, nowHours, nowMinutes - 1);

                assertEquals(c.get(Calendar.DAY_OF_YEAR), nowDayOfYear + 7);
                assertEquals(c.get(Calendar.HOUR_OF_DAY), nowHours);
                assertEquals(c.get(Calendar.MINUTE), nowMinutes - 1);
            } else if (daysDifference < 0) {
                //past
                c = CalendarUtils.getCalendarForMoment(dayOfWeek, nowHours, nowMinutes + 1);

                assertEquals(c.get(Calendar.DAY_OF_YEAR), nowDayOfYear + daysDifference + 7);
                assertEquals(c.get(Calendar.HOUR_OF_DAY), nowHours);
                assertEquals(c.get(Calendar.MINUTE), nowMinutes + 1);
            } else {
                //future
                c = CalendarUtils.getCalendarForMoment(dayOfWeek, nowHours, nowMinutes - 1);

                assertEquals(c.get(Calendar.DAY_OF_YEAR), nowDayOfYear + daysDifference);
                assertEquals(c.get(Calendar.HOUR_OF_DAY), nowHours);
                assertEquals(c.get(Calendar.MINUTE), nowMinutes - 1);
            }
        }
    }

}