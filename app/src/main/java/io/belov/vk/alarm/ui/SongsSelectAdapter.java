package io.belov.vk.alarm.ui;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import io.belov.vk.alarm.R;
import io.belov.vk.alarm.vk.VkSong;

public class SongsSelectAdapter extends BaseAdapter {

    private List<VkSong> songs;
    private LayoutInflater mInflater;

    public SongsSelectAdapter(Activity activty, List<VkSong> songs) {
        this.mInflater = activty.getLayoutInflater();
        this.songs = songs;
    }

    @Override
    public int getCount() {
        return songs.size();
    }

    @Override
    public Object getItem(int arg0) {
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.dialog_songs_list_item, null);

            holder.textViewSong = (TextView) convertView.findViewById(R.id.selectSongTitle);
            holder.textViewArtist = (TextView) convertView.findViewById(R.id.selectSongArtist);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        VkSong song = songs.get(position);

        holder.textViewSong.setText(song.getTitle());
        holder.textViewArtist.setText(song.getArtist());

        return convertView;
    }

    private static class ViewHolder {
        TextView textViewSong;
        TextView textViewArtist;
    }
}