package io.belov.vk.alarm.ui;

import android.support.v7.app.AppCompatActivity;

import io.belov.vk.alarm.App;
import io.belov.vk.alarm.AppComponent;

public class BaseAppCompatActivity extends AppCompatActivity {

    protected AppComponent appComponent() {
        return ((App) getApplication()).getAppComponent();
    }
}
