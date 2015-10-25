package io.belov.vk.alarm.ui;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.codetroopers.betterpickers.timepicker.TimePickerBuilder;
import com.codetroopers.betterpickers.timepicker.TimePickerDialogFragment;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.vk.sdk.VKSdk;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemLongClick;
import io.belov.vk.alarm.R;
import io.belov.vk.alarm.alarm.AlarmManager;
import io.belov.vk.alarm.bus.AlarmCreateEvent;
import io.belov.vk.alarm.bus.AlarmDeletedEvent;
import io.belov.vk.alarm.bus.AlarmInsertedEvent;
import io.belov.vk.alarm.bus.AlarmItemOpenEvent;
import io.belov.vk.alarm.bus.AlarmToggleEnabledEvent;
import io.belov.vk.alarm.alarm.Alarm;
import io.belov.vk.alarm.bus.AlarmUpdatedEvent;
import io.belov.vk.alarm.utils.ActivityUtils;
import io.belov.vk.alarm.utils.IntentUtils;

public class AlarmListActivity extends BaseAppCompatActivity {

    public static final String TAG = AlarmListActivity.class.getSimpleName();

    private AlarmListAdapter mAdapter;
    private List<Alarm> mList = new ArrayList<>();

    @Inject
    AlarmManager alarmManager;
    @Inject
    Bus mBus;

    @Bind(R.id.alarm_list_listview) ListView mListView;
    @Bind(R.id.alarm_list_empty_view) TextView mEmptyTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appComponent().inject(this);
        setContentView(R.layout.activity_alarm_list);
        ButterKnife.bind(this);

        mList.addAll(alarmManager.findAll());
        mAdapter = new AlarmListAdapter(this, mList, mBus);
        mListView.setAdapter(mAdapter);
        mListView.setEmptyView(mEmptyTextView);
    }

    @Override
    protected void onStart() {
        super.onStart();

        mBus.register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();

        mBus.unregister(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.alarm_list, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                createAlarm();
                break;
            case R.id.action_github:
                IntentUtils.openUri(this, "https://github.com/rakuishi/Alarm-Android/");
                break;
            case R.id.action_attributions:
                IntentUtils.openUri(this, "https://github.com/rakuishi/Alarm-Android/blob/master/ATTRIBUTIONS.md");
                break;
            case R.id.action_help:
                IntentUtils.openUri(this, "https://github.com/rakuishi/Alarm-Android/issues");
                break;
            case R.id.action_logout:
                doLogout();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @OnItemLongClick(R.id.alarm_list_listview)
    boolean onItemLongClick(int position, View view) {
        Alarm alarm = mAdapter.getItem(position);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Pair<View, String> pair =
                    new Pair<>(view.findViewById(R.id.item_alarm_when), getString(R.string.transition_name_alarm_name));
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, pair);

            startActivity(AlarmEditActivity.createIntent(this, alarm.getId()), options.toBundle());
        } else {
            startActivity(AlarmEditActivity.createIntent(this, alarm.getId()));
        }

        return true;
    }

    private void createAlarm() {
        TimeFragment fragment = new TimeFragment(new TimePickerDialogFragment.TimePickerDialogHandler() {
            @Override
            public void onDialogTimeSet(int reference, int hourOfDay, int minute) {
                mBus.post(new AlarmCreateEvent(hourOfDay, minute));
            }
        });

        TimePickerBuilder dpb = new TimePickerBuilder()
                .setTargetFragment(fragment)
                .setFragmentManager(getSupportFragmentManager())
                .setStyleResId(R.style.BetterPickersDialogFragment);
        dpb.show();
    }

    private void doLogout() {
        VKSdk.logout();
        if (!VKSdk.isLoggedIn()) {
            ActivityUtils.openLoginActivity(this);
        }
    }

    @Subscribe
    public void onAlarmEvent(AlarmInsertedEvent event) {
        onAlarmChanged();
    }

    @Subscribe
    public void onAlarmEvent(AlarmUpdatedEvent event) {
        onAlarmChanged();
    }

    @Subscribe
    public void onAlarmEvent(AlarmDeletedEvent event) {
        onAlarmChanged();
    }

    private void onAlarmChanged() {
        mList.clear();
        mList.addAll(alarmManager.findAll());
        mAdapter.notifyDataSetChanged();
    }

    @Subscribe
    public void onAlarmToggleEnabledEvent(AlarmToggleEnabledEvent event) {
        Alarm alarm = mAdapter.getItem(event.getPosition());

        alarm.toggleEnabled();

        alarmManager.update(alarm);
    }

    @Subscribe
    public void onAlarmCreateEvent(AlarmCreateEvent event) {
        Alarm alarm = new Alarm();
        alarm.enable();
        alarm.setIsVibrate(true);
        alarm.setDisableComplexity(Alarm.DisableComplexity.EASY.getId());
        alarm.setWhenHours(event.getHourOfDay());
        alarm.setWhenMinutes(event.getMinute());

        alarmManager.insert(alarm);
    }

    @Subscribe
    public void onAlarmOpenEvent(AlarmItemOpenEvent event) {
        ActivityUtils.openAlarm(mAdapter.getItem(event.getPosition()), this);
    }
}
