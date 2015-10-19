package io.belov.vk.alarm.alert;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.belov.vk.alarm.R;
import io.belov.vk.alarm.audio.Player;
import io.belov.vk.alarm.ui.BaseActivity;
import io.belov.vk.alarm.vk.VkSong;
import io.belov.vk.alarm.vk.VkSongListener;
import io.belov.vk.alarm.vk.VkSongManager;

/**
 * Created by fbelov on 19.10.15.
 */
public class AlarmAlertActivity extends BaseActivity {

    private AlarmAlert alarmAlert;
    private Player player;
    private boolean alarmActive;

    @Bind(R.id.alarm_alert_song_title)
    TextView songTitleTextView;

    @Inject
    VkSongManager vkSongManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appComponent().inject(this);

        final Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        setContentView(R.layout.activity_alarm_alert);
        ButterKnife.bind(this);

        Bundle bundle = this.getIntent().getExtras();
        alarmAlert = (AlarmAlert) bundle.getSerializable("alarmAlert");
        player = new Player();

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
        if (alarmAlert.isRandom()) {
            vkSongManager.getRandom(100, new VkSongListener() {
                @Override
                public void on(VkSong song) {
                    songTitleTextView.setText(song.getTitle());
                    player.play(song.getUrl());
                }
            });
        }
    }
}
