package io.belov.vk.alarm.alert;

import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

import javax.inject.Inject;

import at.grabner.circleprogress.CircleProgressView;
import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import io.belov.vk.alarm.R;
import io.belov.vk.alarm.alarm.Alarm;
import io.belov.vk.alarm.audio.Player;
import io.belov.vk.alarm.preferences.PreferencesManager;
import io.belov.vk.alarm.ui.BaseActivity;
import io.belov.vk.alarm.user.UserManager;
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
    private int progressValuePlus;
    private int progressValueMinusTick;

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
    @Inject
    UserManager userManager;
    @Inject
    PreferencesManager preferencesManager;

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
        player = new Player(this);

        Alarm.DisableComplexity disableComplexity = Alarm.DisableComplexity.myValueOf(alarmAlert.getDisableComplexity());

        progressValuePlus = getProgressValuePlus(disableComplexity);
        progressValueMinusTick = getProgressValueMinusTick(disableComplexity);

        updateTime();
        setupProfile();
        setupThread();
        startAlarm();
    }

    private void setupThread() {
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    int i = 1;

                    while (!isInterrupted()) {
                        final boolean shouldUpdateTime = (i % 10 == 0);

                        Thread.sleep(200);

                        if (shouldUpdateTime) {
                            i = 1;
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (shouldUpdateTime) updateTime();
                                increaseProgressValue(progressValueMinusTick);
                            }
                        });

                        i++;
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        t.setDaemon(true);
        t.start();
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
                increaseProgressValue(progressValuePlus);
            }
        });
    }

    private void updateTime() {
        Date now = new Date();
        timeTextView.setText(TimeUtils.getWhenAsString(now.getHours(), now.getMinutes()));
    }

    private synchronized void increaseProgressValue(int progressValueIncrement) {
        int progressValueNew = Math.min(Math.max((progressValue + progressValueIncrement), 0), 360);
        int progressValueChange = progressValueNew - progressValue;

        progressValue = progressValueNew;

        if (progressValueChange != 0) {
            circleProgressView.setValueAnimated(progressValue, (progressValueChange > 0) ? Math.abs(progressValueChange) * 5 : 200);
        }

        if (progressValue >= 360) {
            stopActivity();
        }
    }

    private void startAlarm() {
        if (alarmAlert.isRandom()) {
            vkSongManager.getRandom(userManager.getCurrentUserSongsCount(), new VkSongListener() {
                @Override
                public void on(VkSong song) {
                    songTitleTextView.setText(song.getTitle());
                    songTitleTextArtist.setText(song.getArtist());
                    player.playWithBackup(song.getUrl(), preferencesManager.getPlayerPreferences());
                }
            });
        }
    }

    private void stopActivity() {
        player.stop();
        finishAffinity();
    }

    private int getProgressValuePlus(Alarm.DisableComplexity disableComplexity) {
        if (disableComplexity == Alarm.DisableComplexity.EASY) {
            return 60;
        } else if (disableComplexity == Alarm.DisableComplexity.MEDIUM) {
            return 30;
        } else {
            return 15;
        }
    }

    private int getProgressValueMinusTick(Alarm.DisableComplexity disableComplexity) {
        if (disableComplexity == Alarm.DisableComplexity.EASY) {
            return -5;
        } else if (disableComplexity == Alarm.DisableComplexity.MEDIUM) {
            return -10;
        } else {
            return -10;
        }
    }

    @ColorInt
    private int toColor(@ColorRes int id) {
        return this.getResources().getColor(id);
    }
}
