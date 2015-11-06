package io.belov.vk.alarm.ui;

import android.support.v4.app.FragmentActivity;

import io.belov.vk.alarm.App;
import io.belov.vk.alarm.AppComponent;

/**
 * Created by fbelov on 19.10.15.
 */
public class BaseFragmentActivity extends FragmentActivity {

    protected AppComponent appComponent() {
        return ((App) getApplication()).getAppComponent();
    }

}
