package io.belov.vk.alarm.ui;

import android.content.Context;
import android.preference.EditTextPreference;
import android.util.AttributeSet;

import io.belov.vk.alarm.utils.to;

/**
 * Created by fbelov on 07.11.15.
 */
public class IntEditTextPreference extends EditTextPreference {

    public IntEditTextPreference(Context context) {
        super(context);
    }

    public IntEditTextPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IntEditTextPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected String getPersistedString(String defaultReturnValue) {
        return String.valueOf(getPersistedInt(-1));
    }

    @Override
    protected boolean persistString(String value) {
        return persistInt(to.integer(value, 0));
    }

}
