package io.belov.vk.alarm;

import android.content.Context;

import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.belov.vk.alarm.alarm.AlarmManager;
import io.belov.vk.alarm.alert.AlarmAlertScheduler;
import io.belov.vk.alarm.persistence.AlarmDaoI;
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
}
