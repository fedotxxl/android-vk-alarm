package io.belov.vk.alarm.vk;

import com.squareup.otto.Bus;
import com.vk.sdk.VKSdk;

import io.belov.vk.alarm.bus.VkLogoutEvent;

/**
 * Created by fbelov on 26.10.15.
 */
public class VkManager {

    private Bus bus;

    public VkManager(Bus bus) {
        this.bus = bus;
    }

    public boolean logout() {
        VKSdk.logout();
        if (!VKSdk.isLoggedIn()) {
            bus.post(new VkLogoutEvent());

            return true;
        } else {
            return false;
        }
    }
}
