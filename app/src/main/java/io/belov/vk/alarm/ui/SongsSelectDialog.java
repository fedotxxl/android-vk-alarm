package io.belov.vk.alarm.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.belov.vk.alarm.R;
import io.belov.vk.alarm.vk.VkSong;
import io.belov.vk.alarm.vk.VkSongManager;
import io.belov.vk.alarm.vk.VkSongsListeners;

/**
 * Created by fbelov on 21.10.15.
 */
public class SongsSelectDialog implements AdapterView.OnItemClickListener {

    private Activity context;
    private VkSongManager vkSongManager;

    private List<VkSong> songsDisplayed = new ArrayList<>();

    private AlertDialog dialog = null;
    private SongsSelectDialog.ViewHolder viewHolder;

    public SongsSelectDialog(VkSongManager vkSongManager, Activity context) {
        this.vkSongManager = vkSongManager;
        this.context = context;
    }

    public void open() {
        AlertDialog.Builder myDialog = new AlertDialog.Builder(context);
        View view = context.getLayoutInflater().inflate(R.layout.dialog_songs_list, null);

        viewHolder = new ViewHolder(view);

        updateLoadingSongsVisibility();

        myDialog.setView(view);

        viewHolder.listViewSongs.setOnItemClickListener(this);
        viewHolder.editTextFilter.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                refreshDisplayedSongs(viewHolder.listViewSongs, viewHolder.editTextFilter);
            }
        });
        myDialog.setNeutralButton("set random", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        myDialog.setNegativeButton("cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        updateLoadingSongsVisibility();

        vkSongManager.getAllOrCachedSongs(new VkSongsListeners() {
            @Override
            public void on(int count, List<VkSong> songs) {
                refreshDisplayedSongs(viewHolder.listViewSongs, viewHolder.editTextFilter);
                updateLoadingSongsVisibility();
            }
        });

        dialog = myDialog.show();
    }

    private void updateLoadingSongsVisibility() {
        boolean hasAllCachedSongs = vkSongManager.hasAllCachedSongs();

        viewHolder.selectSongItemsWithFilter.setVisibility(hasAllCachedSongs ? View.VISIBLE : View.GONE);
        viewHolder.selectSongLoading.setVisibility(!hasAllCachedSongs ? View.VISIBLE : View.GONE);
    }

    private void refreshDisplayedSongs(ListView listViewSongs, EditText editTextFilter) {
        String filter = editTextFilter.getText().toString().toLowerCase().trim();
        List<VkSong> songs = vkSongManager.getAllCachedSongs();

        songsDisplayed = getSongsToDisplay(songs, filter);

        listViewSongs.setAdapter(new SongsSelectAdapter(context, songsDisplayed));
    }

    private List<VkSong> getSongsToDisplay(List<VkSong> songs, String filter) {
        if (songs == null) {
            return new ArrayList<>();
        } else if (filter.length() < 3) {
            return songs;
        } else {
            List<VkSong> answer = new ArrayList<>();

            for (VkSong song : songs) {
                if (isFilterMatches(song.getTitle(), filter) || isFilterMatches(song.getArtist(), filter)) {
                    answer.add(song);
                }
            }

            return answer;
        }
    }

    private boolean isFilterMatches(String text, String filter) {
        return text != null && text.toLowerCase().contains(filter);
    }

    @Override
    public void onItemClick(AdapterView arg0, View arg1, int position, long arg3) {
        dialog.dismiss();

        VkSong song = songsDisplayed.get(position);

        Log.e("A", song.getTitle());
    }

    public static class ViewHolder {
        @Bind(R.id.selectSongItemsWithFilter)
        LinearLayout selectSongItemsWithFilter;
        @Bind(R.id.selectSongLoading)
        TextView selectSongLoading;
        @Bind(R.id.selectSongFilter)
        EditText editTextFilter;
        @Bind(R.id.selectSongItems)
        ListView listViewSongs;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
