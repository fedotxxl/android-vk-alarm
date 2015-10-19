package io.belov.vk.alarm.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import io.belov.vk.alarm.AlarmWrapper;
import io.belov.vk.alarm.R;
import io.belov.vk.alarm.bus.AlarmEvent;
import io.belov.vk.alarm.persistence.Alarm;
import io.belov.vk.alarm.persistence.AlarmManager;
import io.belov.vk.alarm.utils.AlarmUtils;
import io.belov.vk.alarm.utils.IntentUtils;

import com.codetroopers.betterpickers.timepicker.TimePickerBuilder;
import com.codetroopers.betterpickers.timepicker.TimePickerDialogFragment;
import com.squareup.otto.Bus;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import static io.belov.vk.alarm.Config.EXTRA_ALARM_ID;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AlarmEditActivity extends BaseAppCompatActivity implements KeyEventEditText.KeyEventListener {

    public static final String TAG = AlarmEditActivity.class.getSimpleName();
    private Alarm mAlarm;
    private AlarmWrapper alarmWrapper;
    private MenuItem mDoneMenuItem;

    @Inject
    AlarmManager mAlarmManager;
    @Inject Bus mBus;

    Map<Alarm.Repeat, Button> repeatButtonsByEnum;

    @Bind(R.id.alarm_edit_when)
    TextView whenTextView;

    @Bind(R.id.alarm_edit_label)
    EditText labelEditText;

    @Bind(R.id.alarm_edit_song_title)
    TextView songTitleTextView;

    @Bind(R.id.alarm_edit_song_artist)
    TextView songArtistTextView;

    @Bind(R.id.alarm_edit_disable_complexity_easy)
    Button complexityEasyButton;

    @Bind(R.id.alarm_edit_disable_complexity_medium)
    Button complexityMediumButton;

    @Bind(R.id.alarm_edit_disable_complexity_hard)
    Button complexityHardButton;

    @Bind(R.id.alarm_edit_repeat_mo)
    Button repeatMoButton;

    @Bind(R.id.alarm_edit_repeat_tu)
    Button repeatTuButton;

    @Bind(R.id.alarm_edit_repeat_we)
    Button repeatWeButton;

    @Bind(R.id.alarm_edit_repeat_th)
    Button repeatThButton;

    @Bind(R.id.alarm_edit_repeat_fr)
    Button repeatFrButton;

    @Bind(R.id.alarm_edit_repeat_sa)
    Button repeatSaButton;

    @Bind(R.id.alarm_edit_repeat_su)
    Button repeatSuButton;

    public static Intent createIntent(Context context, int id) {
        Intent intent = new Intent(context, AlarmEditActivity.class);
        intent.putExtra(EXTRA_ALARM_ID, id);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appComponent().inject(this);
        setContentView(R.layout.activity_alarm_edit);

        ButterKnife.bind(this);
        postBind();

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setTitle(R.string.alarm_create);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAlarm = new Alarm(mAlarmManager.find(IntentUtils.getInt(getIntent(), EXTRA_ALARM_ID)));
        alarmWrapper = new AlarmWrapper(mAlarm);

        setupListeners();
        setupUiFromAlarm();
    }

    private void postBind() {
        repeatButtonsByEnum = new HashMap<>();
        repeatButtonsByEnum.put(Alarm.Repeat.MO, repeatMoButton);
        repeatButtonsByEnum.put(Alarm.Repeat.TU, repeatTuButton);
        repeatButtonsByEnum.put(Alarm.Repeat.WE, repeatWeButton);
        repeatButtonsByEnum.put(Alarm.Repeat.TH, repeatThButton);
        repeatButtonsByEnum.put(Alarm.Repeat.FR, repeatFrButton);
        repeatButtonsByEnum.put(Alarm.Repeat.SA, repeatSaButton);
        repeatButtonsByEnum.put(Alarm.Repeat.SU, repeatSuButton);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.alarm_create, menu);
        mDoneMenuItem = menu.findItem(R.id.action_done);
        updateDoneMenuItem();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
                saveAlarm();
            case android.R.id.home:
                finishAlarmCreateActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onEnterPressed() {
        if (enableToSave()) {
            saveAlarm();
            finishAlarmCreateActivity();
        }
    }

    @Override
    public void onBackPressed() {
        finishAlarmCreateActivity();
    }

    @Override
    public void onTextChanged() {
        updateDoneMenuItem();
    }

    private void saveAlarm() {
        mAlarmManager.findAndUpdate(mAlarm);
        mBus.post(new AlarmEvent(AlarmEvent.QUERY_UPDATE));
    }

    private boolean enableToSave() {
        return true;
    }

    private void updateDoneMenuItem() {
        if (enableToSave()) {
            mDoneMenuItem.setEnabled(true);
            mDoneMenuItem.getIcon().setAlpha(255);
        } else {
            mDoneMenuItem.setEnabled(false);
            mDoneMenuItem.getIcon().setAlpha(127);
        }
    }

    private void setupListeners() {
        setupWhenListener();
        setupLabelListener();
        setupDisableComplexityListeners();
        setupRepeatListeners();
    }

    private void setupWhenListener() {
        whenTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimeFragment fragment = new TimeFragment(new TimePickerDialogFragment.TimePickerDialogHandler() {
                    @Override
                    public void onDialogTimeSet(int reference, int hourOfDay, int minute) {
                        alarmWrapper.setWhen(hourOfDay, minute);
                        setupUiWhen();
                    }
                });

                TimePickerBuilder dpb = new TimePickerBuilder()
                        .setTargetFragment(fragment)
                        .setFragmentManager(getSupportFragmentManager())
                        .setStyleResId(R.style.BetterPickersDialogFragment);
                dpb.show();
            }
        });
    }

    private void setupLabelListener() {
        labelEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                mAlarm.setLabel(s.toString());
            }
        });
    }

    private void setupDisableComplexityListeners() {
        complexityEasyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDisableComplexity(Alarm.DisableComplexity.EASY);
            }
        });
        complexityMediumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDisableComplexity(Alarm.DisableComplexity.MEDIUM);
            }
        });
        complexityHardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDisableComplexity(Alarm.DisableComplexity.HARD);
            }
        });
    }

    private void setupRepeatListeners() {
        for (Map.Entry<Alarm.Repeat, Button> e : repeatButtonsByEnum.entrySet()) {
            final Alarm.Repeat repeat = e.getKey();

            e.getValue().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alarmWrapper.toggleRepeat(repeat);
                    setupUiRepeat();
                }
            });
        }
    }

    private void setDisableComplexity(Alarm.DisableComplexity disableComplexity) {
        mAlarm.setDisableComplexity(disableComplexity.getId());

        setupUiDisableComplexity();
    }

    private void setupUiFromAlarm() {
        setupUiWhen();
        setupUiLabel();
        setupUiSong();
        setupUiDisableComplexity();
        setupUiRepeat();
    }

    private void setupUiWhen() {
        whenTextView.setText(AlarmUtils.getWhenAsString(mAlarm));
    }

    private void setupUiLabel() {
        labelEditText.setText(mAlarm.getLabel());
    }

    private void setupUiSong() {
        String songTitle;
        String songArtist;

        if (mAlarm.getSongId() == null) {
            songTitle = getString(R.string.alarm_edit_random);
            songArtist = null;
        } else {
            songTitle = mAlarm.getSongTitle();
            songArtist = mAlarm.getSongBandName();
        }

        songTitleTextView.setText(songTitle);

        if (songArtist == null) {
            songArtistTextView.setVisibility(View.GONE);
        } else {
            songArtistTextView.setVisibility(View.VISIBLE);
            songArtistTextView.setText(songArtist);
        }
    }

    private void setupUiDisableComplexity() {
        Alarm.DisableComplexity disableComplexity = Alarm.DisableComplexity.myValueOf(mAlarm.getDisableComplexity());
        Button[] buttons = new Button[] {complexityEasyButton, complexityMediumButton, complexityHardButton};
        Button activeButton = null;

        if (disableComplexity == Alarm.DisableComplexity.EASY) {
            activeButton = complexityEasyButton;
        } else if (disableComplexity == Alarm.DisableComplexity.MEDIUM) {
            activeButton = complexityMediumButton;
        } else if (disableComplexity == Alarm.DisableComplexity.HARD) {
            activeButton = complexityHardButton;
        }

        for (Button button : buttons) {
            button.setSelected(button == activeButton);
        }
    }

    private void setupUiRepeat() {
        for (Alarm.Repeat repeat : Alarm.Repeat.values()) {
            repeatButtonsByEnum.get(repeat).setSelected(alarmWrapper.isRepeatActive(repeat));
        }
    }

    private void finishAlarmCreateActivity() {
        finish();
        Intent intent = new Intent(this, AlarmListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }
}
