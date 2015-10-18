package io.belov.vk.alarm.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import io.belov.vk.alarm.R;
import io.belov.vk.alarm.bus.AlarmEvent;
import io.belov.vk.alarm.persistence.Alarm;
import io.belov.vk.alarm.persistence.AlarmManager;
import io.belov.vk.alarm.utils.IntentUtils;
import com.squareup.otto.Bus;

import javax.inject.Inject;

import static io.belov.vk.alarm.Config.EXTRA_ID;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AlarmCreateActivity extends BaseActivity implements KeyEventEditText.KeyEventListener {

    public static final String TAG = AlarmCreateActivity.class.getSimpleName();
    private Alarm mAlarm;
    private MenuItem mDoneMenuItem;
    @Inject
    AlarmManager mAlarmManager;
    @Inject Bus mBus;

    @Bind(R.id.alarm_create_edittext) KeyEventEditText mEditText;

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
        if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            mEditText.setText(IntentUtils.getQueryParameter(intent, "text"));
        } else {
            int id = IntentUtils.getInt(intent, EXTRA_ID);
            if (id != 0) {
                mAlarm = mAlarmManager.find(id);
                mEditText.setText(mAlarm.getName());
                getSupportActionBar().setTitle(R.string.alarm_update);
            }
        }

        mEditText.setKeyEventListener(this);
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
            mAlarmManager.insert(mEditText.getText().toString(), false);
            mBus.post(new AlarmEvent(AlarmEvent.QUERY_INSERT));
        } else {
            mAlarmManager.update(mAlarm, mEditText.getText().toString());
            mBus.post(new AlarmEvent(AlarmEvent.QUERY_UPDATE));
        }
    }

    private boolean enableToSave() {
        return mEditText != null && !TextUtils.isEmpty(mEditText.getText().toString());
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

    private void finishAlarmCreateActivity() {
        finish();
        Intent intent = new Intent(this, AlarmListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }
}
