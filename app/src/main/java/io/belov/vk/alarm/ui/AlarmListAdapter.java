package io.belov.vk.alarm.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.squareup.otto.Bus;

import io.belov.vk.alarm.R;
import io.belov.vk.alarm.bus.AlarmEvent;
import io.belov.vk.alarm.bus.AlarmToggleEnabledEvent;
import io.belov.vk.alarm.persistence.Alarm;

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

        Alarm alarm = getItem(position);
        boolean isEnabled = alarm.isEnabled();

        viewHolder.textViewWhen.setText(AlarmUtils.getWhenAsString(alarm));
        viewHolder.textViewWhen.setTextColor(getColor((isEnabled) ? R.color.alarmItemWhenEnabled : R.color.alarmItemWhenDisabled));
        viewHolder.textViewRepeat.setText(AlarmUtils.getRepeatAsString(this.getContext(), alarm));
        viewHolder.textViewRepeat.setTextColor(getColor((isEnabled) ? R.color.alarmItemRepeatEnabled : R.color.alarmItemRepeatDisabled));
        viewHolder.buttonIsEnabled.setTypeface(iconFont);
        viewHolder.buttonIsEnabled.setSelected(isEnabled);

        viewHolder.buttonIsEnabled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBus.post(new AlarmToggleEnabledEvent(position));
            }
        });

        return convertView;
    }

    @ColorInt
    private int getColor(@ColorRes int id) {
        return getContext().getResources().getColor(id);
    }

    public static class ViewHolder {
        @Bind(R.id.item_alarm_when) TextView textViewWhen;
        @Bind(R.id.item_alarm_repeat) TextView textViewRepeat;
        @Bind(R.id.item_alarm_is_enabled) Button buttonIsEnabled;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
