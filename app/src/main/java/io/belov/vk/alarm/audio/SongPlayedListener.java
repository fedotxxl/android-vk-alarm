package io.belov.vk.alarm.audio;

import io.belov.vk.alarm.song.Song;

/**
 * Created by fbelov on 30.10.15.
 */
public interface SongPlayedListener {

    void on(Song song);

}
