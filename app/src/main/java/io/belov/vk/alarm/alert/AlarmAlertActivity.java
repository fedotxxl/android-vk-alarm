package io.belov.vk.alarm.alert;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.util.Date;

import javax.inject.Inject;

import at.grabner.circleprogress.CircleProgressView;
import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import io.belov.vk.alarm.R;
import io.belov.vk.alarm.alarm.Alarm;
import io.belov.vk.alarm.audio.PlayableSong;
import io.belov.vk.alarm.audio.PlayerFromQueue;
import io.belov.vk.alarm.audio.PlayerQueue;
import io.belov.vk.alarm.audio.PlayerUtils;
import io.belov.vk.alarm.audio.SingleSongProvider;
import io.belov.vk.alarm.audio.SongStartPlayingListener;
import io.belov.vk.alarm.preferences.PreferencesManager;
import io.belov.vk.alarm.ui.BaseActivity;
import io.belov.vk.alarm.user.UserAvatarStorage;
import io.belov.vk.alarm.user.UserManager;
import io.belov.vk.alarm.utils.AndroidUtils;
import io.belov.vk.alarm.utils.IoUtils;
import io.belov.vk.alarm.utils.TimeUtils;
import io.belov.vk.alarm.vk.VkRandomNextSongProvider;
import io.belov.vk.alarm.vk.VkSongManager;

/**
 * Created by fbelov on 19.10.15.
 */
public class AlarmAlertActivity extends BaseActivity {

    private static final String TAG = "AlarmAlertActivity";

    private AlarmAlert alarmAlert;
    private PlayerFromQueue player;
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
    @Inject
    PlayerQueue.Dependencies playerQueueDependencies;
    @Inject
    PlayerUtils playerUtils;
    @Inject
    UserAvatarStorage avatarStorage;

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
        player = initPlayer();

        Alarm.DisableComplexity disableComplexity = Alarm.DisableComplexity.myValueOf(alarmAlert.getDisableComplexity());

        progressValuePlus = getProgressValuePlus(disableComplexity);
        progressValueMinusTick = getProgressValueMinusTick(disableComplexity);

        updateTime();
        setupProfile();
        setupAvatar();
        setupThread();
        startAlarm();
    }

    private PlayerFromQueue initPlayer() {
        PlayerQueue.NextSongProvider nextSongProvider = getNextSongProvider();
        PlayerQueue queue = new PlayerQueue(playerQueueDependencies, nextSongProvider);

        return new PlayerFromQueue(playerUtils, queue, getSongStartPlayingListener());
    }

    private PlayerQueue.NextSongProvider getNextSongProvider() {
        if (alarmAlert.hasSong()) {
            return new SingleSongProvider(vkSongManager, userManager.getCurrentUserId(), alarmAlert.getSongId());
        } else {
            return new VkRandomNextSongProvider(vkSongManager);
        }
    }

    private SongStartPlayingListener getSongStartPlayingListener() {
        final Activity activity = this;

        return new SongStartPlayingListener() {
            @Override
            public void on(final PlayableSong song) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        songTitleTextView.setText(song.getTitle());
                        songTitleTextArtist.setText(song.getArtist());
                    }
                });
            }
        };
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

    private void setupAvatar() {
        try {
            File avatar = avatarStorage.get(UserAvatarStorage.CURRENT_USER_ID);

            if (!IoUtils.isEmpty(avatar)) {
                circleProfileImage.setImageBitmap(AndroidUtils.bitmapFromFile(avatar));
            }
        } catch (Exception e) {
            Log.e(TAG, "setupAvatar", e);
        }
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
        player.play();
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
