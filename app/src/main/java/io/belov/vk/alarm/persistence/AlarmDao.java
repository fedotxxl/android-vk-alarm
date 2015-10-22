package io.belov.vk.alarm.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AlarmDao implements AlarmDaoI {

    private static final AlarmListComparator ALARM_LIST_COMPARATOR = new AlarmListComparator();

    private SQLiteDatabase db;

    public AlarmDao(Context context) {
        db = new AlarmDatabase(context).getWritableDatabase();
    }

    @Override
    public Alarm find(int id) {
        Alarm answer = null;
        Cursor cursor = db.rawQuery("SELECT * FROM alarms WHERE id = ?", new String[] { String.valueOf(id) });

        if (cursor != null && cursor.moveToFirst()) {
            answer = getAlarmFromCursor(cursor);

            cursor.close();
        }

        return answer;
    }

    @Override
    public List<Alarm> findAll() {
        List<Alarm> answer = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM alarms", null);

        if (cursor != null && cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                answer.add(getAlarmFromCursor(cursor));
            }

            cursor.close();
        }

        Collections.sort(answer, ALARM_LIST_COMPARATOR);

        return answer;
    }

    @Override
    public void insert(Alarm alarm) {
        db.insert(AlarmDatabase.TABLE_ALARMS, null, alarmToContentValues(alarm));
    }

    @Override
    public void update(Alarm alarm) {
        String whereClause = "id = ?";
        String[] whereArgs = { String.valueOf(alarm.getId()) };
        ContentValues values = alarmToContentValues(alarm);

        db.update(
                AlarmDatabase.TABLE_ALARMS,
                values,
                whereClause,
                whereArgs
        );
    }

    @Override
    public void delete(int id) {
        String whereClause = "id = ?";
        String[] whereArgs = { String.valueOf(id) };

        db.delete(AlarmDatabase.TABLE_ALARMS, whereClause, whereArgs);
    }

    private Alarm getAlarmFromCursor(Cursor cursor) {
        Alarm alarm = new Alarm();

        alarm.setId(cursor.getInt(cursor.getColumnIndex(AlarmDatabase.ID)));
        alarm.setWhenHours(cursor.getInt(cursor.getColumnIndex(AlarmDatabase.COL_WHEN_HOURS)));
        alarm.setWhenMinutes(cursor.getInt(cursor.getColumnIndex(AlarmDatabase.COL_DISABLE_COMPLEXITY)));
        alarm.setDisableComplexity(cursor.getInt(cursor.getColumnIndex(AlarmDatabase.ID)));
        alarm.setRepeat(cursor.getInt(cursor.getColumnIndex(AlarmDatabase.COL_REPEAT)));
        alarm.setSnoozeInMinutes(cursor.getInt(cursor.getColumnIndex(AlarmDatabase.COL_SNOOZE_IN_MINUTES)));
        alarm.setIsEnabled(cursor.getInt(cursor.getColumnIndex(AlarmDatabase.COL_ENABLED)));
        alarm.setIsVibrate(cursor.getInt(cursor.getColumnIndex(AlarmDatabase.COL_VIBRATE)));
        alarm.setLabel(cursor.getString(cursor.getColumnIndex(AlarmDatabase.COL_LABEL)));
        alarm.setSongId(cursor.getInt(cursor.getColumnIndex(AlarmDatabase.COL_SONG_ID)));
        alarm.setSongTitle(cursor.getString(cursor.getColumnIndex(AlarmDatabase.COL_SONG_TITLE)));
        alarm.setSongBandName(cursor.getString(cursor.getColumnIndex(AlarmDatabase.COL_SONG_ARTIST)));

        return alarm;
    }

    private ContentValues alarmToContentValues(Alarm alarm) {
        ContentValues values = new ContentValues();

        values.put(AlarmDatabase.COL_WHEN_HOURS, alarm.getWhenHours());
        values.put(AlarmDatabase.COL_WHEN_MINUTES, alarm.getWhenMinutes());
        values.put(AlarmDatabase.COL_DISABLE_COMPLEXITY, alarm.getDisableComplexity());
        values.put(AlarmDatabase.COL_REPEAT, alarm.getRepeat());
        values.put(AlarmDatabase.COL_SNOOZE_IN_MINUTES, alarm.getSnoozeInMinutes());
        values.put(AlarmDatabase.COL_ENABLED, alarm.isEnabled());
        values.put(AlarmDatabase.COL_VIBRATE, alarm.isVibrate());
        values.put(AlarmDatabase.COL_LABEL, alarm.getLabel());
        values.put(AlarmDatabase.COL_SONG_ID, alarm.getSongId());
        values.put(AlarmDatabase.COL_SONG_TITLE, alarm.getSongTitle());
        values.put(AlarmDatabase.COL_SONG_ARTIST, alarm.getSongBandName());

        return values;
    }

    private static class AlarmListComparator implements Comparator<Alarm> {

        @Override
        public int compare(Alarm lhs, Alarm rhs) {
            return lhs.getWhenInMinutes() - rhs.getWhenInMinutes();
        }

    }
}
