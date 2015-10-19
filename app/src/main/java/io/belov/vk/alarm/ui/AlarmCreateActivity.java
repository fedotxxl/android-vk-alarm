package io.belov.vk.alarm.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import io.belov.vk.alarm.R;
import io.belov.vk.alarm.bus.AlarmEvent;
import io.belov.vk.alarm.persistence.Alarm;
import io.belov.vk.alarm.persistence.AlarmManager;
import io.belov.vk.alarm.utils.AlarmUtils;
import io.belov.vk.alarm.utils.IntentUtils;
import com.squareup.otto.Bus;

import java.util.Random;

import javax.inject.Inject;

import static io.belov.vk.alarm.Config.EXTRA_ID;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AlarmCreateActivity extends BaseAppCompatActivity implements KeyEventEditText.KeyEventListener {

    public static final String TAG = AlarmCreateActivity.class.getSimpleName();
    private Alarm mAlarm;
    private MenuItem mDoneMenuItem;

    @Inject
    AlarmManager mAlarmManager;
    @Inject Bus mBus;

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

    public static Intent createIntent(Context context) {
        return new Intent(context, AlarmCreateActivity.class);
    }

    public static Intent createIntent(Context context, int id) {
        Intent intent = new Intent(context, AlarmCreateActivity.class);
        intent.putExtra(EXTRA_ID, id);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appComponent().inject(this);
        setContentView(R.layout.activity_alarm_create);
        ButterKnife.bind(this);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setTitle(R.string.alarm_create);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        int id = IntentUtils.getInt(intent, EXTRA_ID);

        if (id != 0) {
            mAlarm = mAlarmManager.find(id);
        } else {
            mAlarm = new Alarm();
        }

        setupListeners();
        setupUiFromAlarm(mAlarm);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
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
        if (mAlarm == null) {
            mAlarmManager.insert(getAlarmToInsert());
            mBus.post(new AlarmEvent(AlarmEvent.QUERY_INSERT));
        } else {
            //, mEditText.getText().toString()
            mAlarmManager.update(mAlarm);
            mBus.post(new AlarmEvent(AlarmEvent.QUERY_UPDATE));
        }
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

    }

    private void setupUiFromAlarm(Alarm alarm) {
        whenTextView.setText(AlarmUtils.getWhenAsString(alarm));

        setupUiSong(alarm);
        setupUiDisableComplexity(alarm);
    }

    private void setupUiSong(Alarm alarm) {
        String songTitle;
        String songArtist;

        if (alarm.getSongId() == null) {
            songTitle = getString(R.string.alarm_edit_random);
            songArtist = null;
        } else {
            songTitle = alarm.getSongTitle();
            songArtist = alarm.getSongBandName();
        }

        songTitleTextView.setText(songTitle);

        if (songArtist == null) {
            songArtistTextView.setVisibility(View.GONE);
        } else {
            songArtistTextView.setVisibility(View.VISIBLE);
            songArtistTextView.setText(songArtist);
        }
    }

    private void setupUiDisableComplexity(Alarm alarm) {
        Alarm.DisableComplexity disableComplexity = Alarm.DisableComplexity.myValueOf(alarm.getDisableComplexity());
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

    private void finishAlarmCreateActivity() {
        finish();
        Intent intent = new Intent(this, AlarmListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    private Alarm getAlarmToInsert() {
        Random r = new Random();
        Alarm alarm = new Alarm();

        alarm.setIsEnabled(true);

        alarm.setWhenHours(r.nextInt(24));
        alarm.setWhenMinutes(r.nextInt(60));
        alarm.setDisableComplexity(r.nextInt(3));
        alarm.setRepeat(Alarm.Repeat.MO.getId() + Alarm.Repeat.WE.getId());
        alarm.setSnoozeInMinutes(5);
        alarm.setIsVibrate(r.nextBoolean());

        if (r.nextBoolean()) {
            alarm.setSongId("abc");
            alarm.setSongTitle("Bohemian Rhapsody");
            alarm.setSongBandName("The Queen");
        }

        return alarm;
    }
}
