package io.belov.vk.alarm.user;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import io.belov.vk.alarm.alarm.AlarmManager;
import io.belov.vk.alarm.bus.VkLoginEvent;
import io.belov.vk.alarm.bus.VkLogoutEvent;
import io.belov.vk.alarm.storage.SongsCacheI;
import io.belov.vk.alarm.utils.IoUtils;
import io.belov.vk.alarm.vk.VkManager;
import io.belov.vk.alarm.vk.VkUser;
import io.belov.vk.alarm.vk.VkUserInfoListener;

/**
 * Created by fbelov on 06.11.15.
 */
public class UserLoginLogoutProcessor {

    private AlarmManager alarmManager;
    private UserAvatarStorage avatarStorage;
    private SongsCacheI songsCache;
    private VkManager vkManager;
    private Bus bus;

    public UserLoginLogoutProcessor(Bus bus, AlarmManager alarmManager, UserAvatarStorage avatarStorage, SongsCacheI songsCache, VkManager vkManager) {
        this.alarmManager = alarmManager;
        this.avatarStorage = avatarStorage;
        this.songsCache = songsCache;
        this.vkManager = vkManager;
        this.bus = bus;
    }

    public void bootstrap() {
        bus.register(this);
    }

    @Subscribe
    public void on(VkLoginEvent e) {
        vkManager.getCurrentUserInfo(new VkUserInfoListener() {
            @Override
            public void on(VkUser user) {
                IoUtils.downloadAsync(user.getPhotoUrl(), avatarStorage.get(UserAvatarStorage.CURRENT_USER_ID));
            }
        });
    }

    @Subscribe
    public void on(VkLogoutEvent e) {
        alarmManager.removeAll();
        avatarStorage.clear();
        songsCache.clear();
    }

}
