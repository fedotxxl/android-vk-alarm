package io.belov.vk.alarm.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by fbelov on 22.10.15.
 */
public class AlarmDatabase extends SQLiteOpenHelper {

    private static final String TAG = "AlarmDatabase";

    // If you change the database schema, you must increment the database version.
    private static final int DB_VERSION = 1;
    // DB Name, same is used to name the sqlite DB file
    private static final String DB_NAME = "vk_alarm_db";

    public static final String TABLE_ALARMS = "alarms";
    public static final String ID = "id";
    public static final String COL_WHEN_HOURS = "when_hours";
    public static final String COL_WHEN_MINUTES = "when_minutes";
    public static final String COL_DISABLE_COMPLEXITY = "disable_complexity";
    public static final String COL_REPEAT = "repeat";
    public static final String COL_SNOOZE_IN_MINUTES = "snooze";
    public static final String COL_ENABLED = "enabled";
    public static final String COL_VIBRATE = "vibrate";
    public static final String COL_LABEL = "label";
    public static final String COL_SONG_ID = "song_id";
    public static final String COL_SONG_TITLE = "song_title";
    public static final String COL_SONG_ARTIST = "song_artist";

    public static final String TABLE_USER = "user";
    public static final String COL_USER_VK_ID = "vk_id";
    public static final String COL_USER_VK_SONGS_COUNT = "vk_songs_count";


    private static final String CREATE_TABLE_ALARMS =
            "CREATE TABLE " + TABLE_ALARMS
                    + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COL_WHEN_HOURS + " INTEGER, "
                    + COL_WHEN_MINUTES + " INTEGER, "
                    + COL_DISABLE_COMPLEXITY + " INTEGER, "
                    + COL_REPEAT + " INTEGER, "
                    + COL_SNOOZE_IN_MINUTES + " INTEGER, "
                    + COL_ENABLED + " INTEGER, "
                    + COL_VIBRATE + " INTEGER, "
                    + COL_LABEL + " TEXT, "
                    + COL_SONG_ID + " INTEGER, "
                    + COL_SONG_TITLE + " TEXT, "
                    + COL_SONG_ARTIST + " TEXT);";


    public AlarmDatabase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_ALARMS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
