package io.belov.vk.alarm.bus;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class AlarmEvent {

    @IntDef({QUERY_INSERT, QUERY_UPDATE, QUERY_DELETE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Query {}

    public static final int QUERY_INSERT = 0;
    public static final int QUERY_UPDATE = 1;
    public static final int QUERY_DELETE = 2;

    private int query;

    public AlarmEvent(@Query int query) {
        this.query = query;
    }

    public int getQuery() {
        return query;
    }
}
