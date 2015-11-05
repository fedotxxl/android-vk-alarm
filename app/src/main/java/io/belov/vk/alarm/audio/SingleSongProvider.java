package io.belov.vk.alarm.audio;

import java.util.concurrent.atomic.AtomicReference;

import io.belov.vk.alarm.vk.VkSong;
import io.belov.vk.alarm.vk.VkSongListener;
import io.belov.vk.alarm.vk.VkSongManager;

/**
 * Created by fbelov on 02.11.15.
 */
public class SingleSongProvider implements PlayerQueue.NextSongProvider {

    private static final String TAG = "SingleSongProvider";

    private volatile VkSong song;

    private VkSongManager vkSongManager;
    private int ownerId;
    private int songId;

    public SingleSongProvider(VkSong song) {
        this.song = song;
    }

    public SingleSongProvider(VkSongManager vkSongManager, int ownerId, int songId) {
        this.vkSongManager = vkSongManager;
        this.ownerId = ownerId;
        this.songId = songId;
        moveNext();
    }

    @Override
    public VkSong getCurrent() {
        return song;
    }

    @Override
    public VkSong getNext() {
        return song;
    }

    @Override
    public void moveNext() {
        if (song == null && vkSongManager != null) {
            song = getSongFromVk();
        }
    }

    @Override
    public void onSongChange(SongChangeListener listener) {

    }

    private VkSong getSongFromVk() {
        final AtomicReference<VkSong> answer = new AtomicReference<>();

        vkSongManager.getByIdSync(ownerId, songId,
                new VkSongListener() {
                    @Override
                    public void on(VkSong song) {
                        answer.set(song);
                    }
                });

        return answer.get();
    }
}

