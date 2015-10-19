package io.belov.vk.alarm.ui;

/**
 * Created by fbelov on 19.10.15.
 */
import android.support.v4.app.Fragment;

import com.codetroopers.betterpickers.timepicker.TimePickerDialogFragment;

/**
 * User: derek Date: 4/30/13 Time: 7:43 PM
 */
public class TimeFragment extends Fragment implements TimePickerDialogFragment.TimePickerDialogHandler {

    private TimePickerDialogFragment.TimePickerDialogHandler listener;

    public TimeFragment(TimePickerDialogFragment.TimePickerDialogHandler listener) {
        this.listener = listener;
    }

    @Override
    public void onDialogTimeSet(int reference, int hourOfDay, int minute) {
        listener.onDialogTimeSet(reference, hourOfDay, minute);
    }
}