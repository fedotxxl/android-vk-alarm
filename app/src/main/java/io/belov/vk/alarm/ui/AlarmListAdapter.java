package io.belov.vk.alarm.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import io.belov.vk.alarm.R;
import io.belov.vk.alarm.persistence.Alarm;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AlarmListAdapter extends ArrayAdapter<Alarm> implements ListAdapter {

    public AlarmListAdapter(Context context, List<Alarm> list) {
        super(context, 0, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_alarm, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        Alarm alarm = getItem(position);
        int textColor = alarm.isEnabled() ? R.color.myDisabledTextColor : R.color.myTextColor;

        viewHolder.textView.setText("asd");
        viewHolder.textView.setTextColor(getContext().getResources().getColor(textColor));

        return convertView;
    }

    public static class ViewHolder {
        @Bind(R.id.item_alarm_textview) TextView textView;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
