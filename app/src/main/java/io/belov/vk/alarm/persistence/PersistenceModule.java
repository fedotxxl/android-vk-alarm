package io.belov.vk.alarm.persistence;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class PersistenceModule {

    @Provides @Singleton
    public AlarmDaoI provideAlarmManager(Context context) {
        return new AlarmDao(context);
    }
}
