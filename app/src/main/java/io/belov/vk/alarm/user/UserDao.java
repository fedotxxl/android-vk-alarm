package io.belov.vk.alarm.user;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import io.belov.vk.alarm.persistence.AlarmDatabase;

/**
 * Created by fbelov on 26.10.15.
 */
public class UserDao implements UserDaoI {

    private SQLiteDatabase db;

    public UserDao(Context context) {
        db = new AlarmDatabase(context).getWritableDatabase();
    }

    @Override
    public User get() {
        User answer = null;
        Cursor cursor = db.rawQuery("SELECT * FROM user", null);

        if (cursor != null && cursor.moveToFirst()) {
            answer = getUserFromCursor(cursor);

            cursor.close();
        }

        return answer;
    }

    @Override
    public void save(User user) {
        reset();
        insert(user);
    }

    @Override
    public void reset() {
        db.delete(AlarmDatabase.TABLE_USER, null, null);
    }

    private void insert(User user) {
        db.insert(AlarmDatabase.TABLE_USER, null, userToContentValues(user));
    }

    private User getUserFromCursor(Cursor cursor) {
        User user = new User();

        user.setId(cursor.getInt(cursor.getColumnIndex(AlarmDatabase.COL_USER_VK_ID)));
        user.setSongsCount(cursor.getInt(cursor.getColumnIndex(AlarmDatabase.COL_USER_VK_SONGS_COUNT)));

        return user;
    }

    private ContentValues userToContentValues(User user) {
        ContentValues values = new ContentValues();

        values.put(AlarmDatabase.COL_USER_VK_ID, user.getId());
        values.put(AlarmDatabase.COL_USER_VK_SONGS_COUNT, user.getSongsCount());

        return values;
    }
}
