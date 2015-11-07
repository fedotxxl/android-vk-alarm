package io.belov.vk.alarm;

import io.belov.vk.alarm.alarm.AlarmManager;
import io.belov.vk.alarm.alert.AlarmAlertActivity;
import io.belov.vk.alarm.bus.BusModule;
import io.belov.vk.alarm.persistence.PersistenceModule;
import io.belov.vk.alarm.preferences.PreferencesActivity;
import io.belov.vk.alarm.preferences.PreferencesManager;
import io.belov.vk.alarm.ui.AlarmEditActivity;
import io.belov.vk.alarm.ui.AlarmListActivity;

import javax.inject.Singleton;

import dagger.Component;
import io.belov.vk.alarm.ui.LoginActivity;
import io.belov.vk.alarm.user.UserLoginLogoutProcessor;

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
    void inject(AlarmAlertActivity activity);
    void inject(AlarmEditActivity activity);
    void inject(LoginActivity activity);
    void inject(PreferencesActivity.MyPreferenceFragment activity);
    AlarmManager provideAlarmManager();
    UserLoginLogoutProcessor provideUserLoginLogoutProcessor();
}
