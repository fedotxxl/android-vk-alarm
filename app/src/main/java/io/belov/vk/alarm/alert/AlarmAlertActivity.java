package io.belov.vk.alarm.alert;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.belov.vk.alarm.R;

/**
 * Created by fbelov on 19.10.15.
 */
public class AlarmAlertActivity extends Activity {

    private AlarmAlert alarmAlert;
    private boolean alarmActive;

    @Bind(R.id.alarm_alert_song_title)
    TextView songTitleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        setContentView(R.layout.activity_alarm_alert);
        ButterKnife.bind(this);

        Bundle bundle = this.getIntent().getExtras();
        alarmAlert = (AlarmAlert) bundle.getSerializable("alarmAlert");

        startAlarm();
    }

    @Override
    protected void onResume() {
        super.onResume();
        alarmActive = true;
    }

    @Override
    public void onBackPressed() {
        if (!alarmActive)
            super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        StaticWakeLock.lockOff(this);
    }

    private void startAlarm() {
        songTitleTextView.setText(alarmAlert.getSongTitle());
    }
}
