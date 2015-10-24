package io.belov.vk.alarm.alert;

import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Date;

import javax.inject.Inject;

import at.grabner.circleprogress.CircleProgressView;
import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import io.belov.vk.alarm.R;
import io.belov.vk.alarm.audio.Player;
import io.belov.vk.alarm.ui.BaseActivity;
import io.belov.vk.alarm.utils.RandomUtils;
import io.belov.vk.alarm.utils.TimeUtils;
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
    private int progressValue = 0;

    @Bind(R.id.alarm_alert_time)
    TextView timeTextView;
    @Bind(R.id.alarm_alert_song_title)
    TextView songTitleTextView;
    @Bind(R.id.alarm_alert_song_artist)
    TextView songTitleTextArtist;
    @Bind(R.id.alarm_alert_profile)
    RelativeLayout layoutProfile;
    @Bind(R.id.alarm_alert_profile_circle)
    CircleProgressView circleProgressView;
    @Bind(R.id.alarm_alert_profile_circle_contour)
    CircleProgressView circleContourProgressView;
    @Bind(R.id.alarm_alert_profile_image)
    CircleImageView circleProfileImage;

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

        setupTime();
        setupProfile();
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

    private void setupProfile() {
        @ColorInt int[] circleColors = {toColor(R.color.circleProgressC1), toColor(R.color.circleProgressC2)};

        circleProgressView.setValue(1);
        circleProgressView.setBarColor(circleColors);
        circleContourProgressView.setValue(360);
        circleContourProgressView.setBarColor(circleColors);

        layoutProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setProgressValue(RandomUtils.R.nextInt(360));
            }
        });
    }

    private void setupTime() {
        Date now = new Date();
        timeTextView.setText(TimeUtils.getWhenAsString(now.getHours(), now.getMinutes()));
    }

    private void setProgressValue(int progressValue) {
        circleProgressView.setValueAnimated(progressValue);
    }

    private void startAlarm() {
        if (alarmAlert.isRandom()) {
            vkSongManager.getRandom(100, new VkSongListener() {
                @Override
                public void on(VkSong song) {
                    songTitleTextView.setText(song.getTitle());
                    songTitleTextArtist.setText(song.getArtist());
                    //player.play(song.getUrl());
                }
            });
        }
    }

    @ColorInt
    private int toColor(@ColorRes int id) {
        return this.getResources().getColor(id);
    }
}
