package io.belov.vk.alarm.user;

/**
 * Created by fbelov on 26.10.15.
 */
public interface UserDaoI {

    User get();
    void save(User user);
    void reset();

}
