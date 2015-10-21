package io.belov.vk.alarm.vk;

import java.util.List;

/**
 * Created by fbelov on 21.10.15.
 */
public interface VkSongsListeners {

    void on(int count, List<VkSong> songs);

}
