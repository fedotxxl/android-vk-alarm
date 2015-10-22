package io.belov.vk.alarm.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.squareup.otto.Bus;

import io.belov.vk.alarm.R;
import io.belov.vk.alarm.bus.AlarmItemOpenEvent;
import io.belov.vk.alarm.bus.AlarmToggleEnabledEvent;
import io.belov.vk.alarm.alarm.Alarm;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.belov.vk.alarm.utils.AlarmUtils;
import io.belov.vk.alarm.utils.Typefaces;

public class AlarmListAdapter extends ArrayAdapter<Alarm> implements ListAdapter {

    private Bus mBus;
    private Typeface iconFont;

    public AlarmListAdapter(Context context, List<Alarm> list, Bus mBus) {
        super(context, 0, list);

        this.mBus = mBus;
        this.iconFont = Typefaces.get(context, "alarm-icon-font.ttf");
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_alarm, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBus.post(new AlarmItemOpenEvent(position));
            }
        });

        Alarm alarm = getItem(position);
        boolean isEnabled = alarm.isEnabled();
        int color = getColor((isEnabled) ? R.color.alarmItemEnabled : R.color.alarmItemDisabled);

        setupUiWhen(viewHolder, alarm, color);
        setupUiRepeat(viewHolder, alarm, color);
        setupUiLabel(viewHolder, alarm, color);
        setupButtonIsEnabled(viewHolder, alarm, position);

        return convertView;
    }

    private void setupUiWhen(ViewHolder viewHolder, Alarm alarm, int color) {
        viewHolder.textViewWhen.setText(AlarmUtils.getWhenAsString(alarm));
        viewHolder.textViewWhen.setTextColor(color);
    }

    private void setupUiRepeat(ViewHolder viewHolder, Alarm alarm, int color) {
        viewHolder.textViewRepeat.setText(AlarmUtils.getRepeatAsString(this.getContext(), alarm));
        viewHolder.textViewRepeat.setTextColor(color);
    }

    private void setupUiLabel(ViewHolder viewHolder, Alarm alarm, int color) {
        int visibility = (alarm.hasLabel()) ? View.VISIBLE : View.GONE;

        viewHolder.textViewLabel.setText(alarm.getLabel());
        viewHolder.textViewLabel.setTextColor(color);
        viewHolder.textViewLabel.setVisibility(visibility);
        viewHolder.textViewSubSeparator.setTextColor(color);
        viewHolder.textViewSubSeparator.setVisibility(visibility);
    }

    private void setupButtonIsEnabled(ViewHolder viewHolder, Alarm alarm, final int position) {
        viewHolder.buttonIsEnabled.setTypeface(iconFont);
        viewHolder.buttonIsEnabled.setSelected(alarm.isEnabled());

        viewHolder.buttonIsEnabled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBus.post(new AlarmToggleEnabledEvent(position));
            }
        });
    }

    @ColorInt
    private int getColor(@ColorRes int id) {
        return getContext().getResources().getColor(id);
    }

    public static class ViewHolder {
        @Bind(R.id.item_alarm_when) TextView textViewWhen;
        @Bind(R.id.item_alarm_repeat) TextView textViewRepeat;
        @Bind(R.id.item_alarm_sub_separator) TextView textViewSubSeparator;
        @Bind(R.id.item_alarm_label) TextView textViewLabel;
        @Bind(R.id.item_alarm_is_enabled) Button buttonIsEnabled;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
