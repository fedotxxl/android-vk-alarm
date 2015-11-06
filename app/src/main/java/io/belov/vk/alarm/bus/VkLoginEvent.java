package io.belov.vk.alarm.bus;

import com.vk.sdk.VKAccessToken;

/**
 * Created by fbelov on 06.11.15.
 */
public class VkLoginEvent {

    private VKAccessToken accessToken;

    public VkLoginEvent(VKAccessToken accessToken) {
        this.accessToken = accessToken;
    }

    public VKAccessToken getAccessToken() {
        return accessToken;
    }
}
