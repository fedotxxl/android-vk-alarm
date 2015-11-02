package io.belov.vk.alarm.user;

import com.squareup.otto.Bus;
import com.vk.sdk.VKSdk;

import io.belov.vk.alarm.bus.VkLogoutEvent;
import io.belov.vk.alarm.vk.VkManager;
import io.belov.vk.alarm.vk.VkUser;
import io.belov.vk.alarm.vk.VkUserInfoListener;

/**
 * Created by fbelov on 26.10.15.
 */
public class UserManager {

    private UserDaoI dao;
    private VkManager vkManager;
    private Bus bus;

    public UserManager(UserDaoI dao, VkManager vkManager, Bus bus) {
        this.dao = dao;
        this.vkManager = vkManager;
        this.bus = bus;
    }

    public boolean logout() {
        VKSdk.logout();
        if (!VKSdk.isLoggedIn()) {
            dao.reset();
            bus.post(new VkLogoutEvent());

            return true;
        } else {
            return false;
        }
    }

    public void checkAndUpdateUserInfo() {
        vkManager.getCurrentUserInfo(new VkUserInfoListener() {
            @Override
            public void on(VkUser user) {
                save(user);
            }
        });
    }

    public int getCurrentUserId() {
        User user = dao.get();

        if (user == null) {
            return -1;
        } else {
            return user.getId();
        }
    }

    public int getCurrentUserSongsCount() {
        User user = dao.get();

        if (user == null) {
            return -1;
        } else {
            return user.getSongsCount();
        }
    }

    private void save(VkUser user) {
        dao.save(User.fromVkUser(user));
    }
}
