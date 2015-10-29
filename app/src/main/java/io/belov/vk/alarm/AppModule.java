package io.belov.vk.alarm;

import android.content.Context;

import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.belov.vk.alarm.alarm.AlarmManager;
import io.belov.vk.alarm.alert.AlarmAlertScheduler;
import io.belov.vk.alarm.persistence.AlarmDaoI;
import io.belov.vk.alarm.preferences.PreferencesManager;
import io.belov.vk.alarm.user.UserDao;
import io.belov.vk.alarm.user.UserDaoI;
import io.belov.vk.alarm.user.UserManager;
import io.belov.vk.alarm.vk.VkManager;
import io.belov.vk.alarm.vk.VkSongManager;

@Module
public class AppModule {

    private final App app;

    public AppModule(App app) {
        this.app = app;
    }

    @Provides @Singleton
    public Context provideContext() {
        return app;
    }

    @Provides @Singleton
    public AlarmAlertScheduler provideScheduler(Context context) {
        return new AlarmAlertScheduler(context);
    }

    @Provides @Singleton
    public VkSongManager provideVkSongManager() {
        return new VkSongManager();
    }

    @Provides @Singleton
    public AlarmManager provideAlarmManager(Bus bus, AlarmDaoI dao, AlarmAlertScheduler alertScheduler) {
        return new AlarmManager(bus, dao, alertScheduler);
    }

    @Provides @Singleton
    public VkManager provideVkManager() {
        return new VkManager();
    }

    @Provides @Singleton
    public UserDaoI provideUserDao(Context context) {
        return new UserDao(context);
    }

    @Provides @Singleton
    public UserManager provideUserManager(UserDaoI dao, VkManager vkManager, Bus bus) {
        return new UserManager(dao, vkManager, bus);
    }

    @Provides @Singleton
    public PreferencesManager providePreferencesManager() {
        return new PreferencesManager();
    }
}
