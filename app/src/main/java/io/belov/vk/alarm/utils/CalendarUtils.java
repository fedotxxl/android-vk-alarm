package io.belov.vk.alarm.utils;

import java.util.Calendar;
import java.util.GregorianCalendar;

import io.belov.vk.alarm.DayOfWeek;

/**
 * Created by fbelov on 25.10.15.
 */
public class CalendarUtils {

    public static Calendar getCalendarForMoment(DayOfWeek dayOfWeek, int hours, int minutes) {
        Calendar date = Calendar.getInstance();

        date.set(Calendar.HOUR_OF_DAY, hours);
        date.set(Calendar.MINUTE, minutes);
        date.set(Calendar.SECOND, 0);

        if (dayOfWeek == null) {
            addDaysIfPast(date, 1);
        } else {
            addDaysIfPast(nextDayOfWeek(date, dayOfWeek), 7);
        }

        return date;
    }

    public static Calendar getCalendarForMoment(Calendar calendar, int hours, int minutes) {
        Calendar date = (Calendar) calendar.clone();

        date.set(Calendar.HOUR_OF_DAY, hours);
        date.set(Calendar.MINUTE, minutes);
        date.set(Calendar.SECOND, 0);

        return date;
    }

    public static Calendar getCalendarForMoment(long millis) {
        Calendar date = new GregorianCalendar();

        date.setTimeInMillis(millis);

        return date;
    }

    public static Calendar nextDayOfWeek(Calendar date, DayOfWeek dayOfWeek) {
        //http://stackoverflow.com/questions/3463756/is-there-a-good-way-to-get-the-date-of-the-coming-wednesday

        int diff = dayOfWeek.getCalendarId() - date.get(Calendar.DAY_OF_WEEK);
        if (diff < 0) {
            diff += 7;
        }
        date.add(Calendar.DAY_OF_MONTH, diff);

        return date;
    }

    public static Calendar addDaysIfPast(Calendar date, int daysToAdd) {
        return addDaysIfBefore(date, Calendar.getInstance(), daysToAdd);
    }

    public static Calendar addDaysIfBefore(Calendar date, Calendar dateBefore, int daysToAdd) {
        while (date.before(dateBefore)) {
            date.add(Calendar.DAY_OF_MONTH, daysToAdd);
        }

        return date;
    }

    public static Calendar getNextPlayDate(Calendar date, int hours, int minutes) {
        return addDaysIfBefore(getCalendarForMoment(date, hours, minutes), date, 1);
    }

}
