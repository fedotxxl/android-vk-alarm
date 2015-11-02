package io.belov.vk.alarm.vk;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import io.belov.vk.alarm.audio.PlayerQueue;
import io.belov.vk.alarm.utils.RandomUtils;

/**
 * Created by fbelov on 02.11.15.
 */
public class VkRandomNextSongProvider implements PlayerQueue.NextSongProvider {

    private static final String TAG = "VkNextSongProvider";

    private VkSongManager vkSongManager;

    private volatile List<VkSong> allSongs = null;
    private List<SongChangeListener> onSongChangeListeners = new ArrayList<>();
    private VkSong currentSong = null;
    private VkSong nextSong = null;

    public VkRandomNextSongProvider(VkSongManager vkSongManager) {
        this.vkSongManager = vkSongManager;
    }

    @Override
    public VkSong getCurrent() {
        return currentSong;
    }

    @Override
    public VkSong getNext() {
        return nextSong;
    }

    @Override
    public void moveNext() {
        if (nextSong == null) {
            currentSong = getRandomSong();
        } else {
            currentSong = nextSong;
        }

        nextSong = getRandomSong();

        for (SongChangeListener listener : onSongChangeListeners) {
            listener.on(currentSong, nextSong);
        }
    }

    @Override
    public void onSongChange(SongChangeListener listener) {
        onSongChangeListeners.add(listener);
    }

    private List<VkSong> getAllSongs() {
        try {
            final CountDownLatch latch = new CountDownLatch(1);

            vkSongManager.getAllSongs(new VkSongsListeners() {
                @Override
                public void on(int count, List<VkSong> songs) {
                    allSongs = songs;
                    latch.countDown();
                }
            });

            latch.await();
        } catch (InterruptedException e) {
            Log.e(TAG, "getAllSongs");
        }

        return allSongs;
    }

    private VkSong getRandomSong() {
        List<VkSong> allSongs = getAllSongs();
        int songsCount = allSongs.size();

        return allSongs.get(RandomUtils.R.nextInt(songsCount));
    }
}
