package io.belov.vk.alarm.ui;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v4.util.Pair;
import android.widget.Toast;

import io.belov.vk.alarm.R;
import io.belov.vk.alarm.alert.AlarmAlertScheduler;
import io.belov.vk.alarm.bus.AlarmEvent;
import io.belov.vk.alarm.bus.AlarmItemOpenEvent;
import io.belov.vk.alarm.bus.AlarmToggleEnabledEvent;
import io.belov.vk.alarm.persistence.Alarm;
import io.belov.vk.alarm.persistence.AlarmManager;
import io.belov.vk.alarm.utils.ActivityUtils;
import io.belov.vk.alarm.utils.IntentUtils;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.vk.sdk.VKSdk;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import butterknife.OnItemLongClick;

public class AlarmListActivity extends BaseAppCompatActivity {

    public static final String TAG = AlarmListActivity.class.getSimpleName();
    private AlarmListAdapter mAdapter;
    private List<Alarm> mList = new ArrayList<>();

    @Inject
    AlarmManager mAlarmManager;
    @Inject
    Bus mBus;
    @Inject
    AlarmAlertScheduler alertScheduler;

    @Bind(R.id.alarm_list_listview) ListView mListView;
    @Bind(R.id.alarm_list_empty_view) TextView mEmptyTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appComponent().inject(this);
        setContentView(R.layout.activity_alarm_list);
        ButterKnife.bind(this);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        mBus.register(this);

        mList.addAll(mAlarmManager.findAll());
        mAdapter = new AlarmListAdapter(this, mList, mBus);
        mListView.setAdapter(mAdapter);
        mListView.setEmptyView(mEmptyTextView);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
                createAlaram();
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

            startActivity(AlarmCreateActivity.createIntent(this, alarm.getId()), options.toBundle());
        } else {
            startActivity(AlarmCreateActivity.createIntent(this, alarm.getId()));
        }

        return true;
    }

    private void createAlaram() {
        startActivity(AlarmCreateActivity.createIntent(this));
    }

    private void doLogout() {
        VKSdk.logout();
        if (!VKSdk.isLoggedIn()) {
            ActivityUtils.openLoginActivity(this);
        }
    }

    @Subscribe
    public void onAlarmEvent(AlarmEvent event) {
        mList.clear();
        mList.addAll(mAlarmManager.findAll());
        mAdapter.notifyDataSetChanged();
    }

    @Subscribe
    public void onAlarmToggleEnabledEvent(AlarmToggleEnabledEvent event) {

        alertScheduler.schedule(mAdapter.getItem(event.getPosition()));
        Toast.makeText(this, "Scheduled!", Toast.LENGTH_LONG).show();


        Alarm alarm = mAdapter.getItem(event.getPosition());
        mAlarmManager.update(alarm, !alarm.isEnabled());
        mBus.post(new AlarmEvent(AlarmEvent.QUERY_UPDATE));
    }

    @Subscribe
    public void onAlarmOpenEvent(AlarmItemOpenEvent event) {
        ActivityUtils.openAlarm(mAdapter.getItem(event.getPosition()), this);
    }
}
