package io.belov.vk.alarm.ui;

import android.app.Activity;

import io.belov.vk.alarm.App;
import io.belov.vk.alarm.AppComponent;

/**
 * Created by fbelov on 19.10.15.
 */
public class BaseActivity extends Activity {

    protected AppComponent appComponent() {
        return ((App) getApplication()).getAppComponent();
    }

}
