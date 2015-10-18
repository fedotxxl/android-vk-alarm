package io.belov.vk.alarm;

import io.belov.vk.alarm.bus.BusModule;
import io.belov.vk.alarm.persistence.PersistenceModule;
import io.belov.vk.alarm.ui.AlarmCreateActivity;
import io.belov.vk.alarm.ui.AlarmListActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(
    modules = {
        AppModule.class,
        BusModule.class,
        PersistenceModule.class
    }
)
public interface AppComponent {
    void inject(AlarmListActivity activity);
    void inject(AlarmCreateActivity activity);
}
