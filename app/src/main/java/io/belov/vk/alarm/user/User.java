package io.belov.vk.alarm.user;

import io.belov.vk.alarm.vk.VkUser;

/**
 * Created by fbelov on 26.10.15.
 */
public class User {

    private int id; //vk id
    private int songsCount;

    public User() {
    }

    public User(int id, int songsCount) {
        this.id = id;
        this.songsCount = songsCount;
    }

    public static User fromVkUser(VkUser vkUser) {
        return new User(vkUser.getId(), vkUser.getAudiosCount());
    }

    //GETTERS / SETTERS
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSongsCount() {
        return songsCount;
    }

    public void setSongsCount(int songsCount) {
        this.songsCount = songsCount;
    }
}
