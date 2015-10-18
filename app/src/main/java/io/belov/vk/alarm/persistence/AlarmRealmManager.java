package io.belov.vk.alarm.persistence;

import android.content.Context;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class AlarmRealmManager implements AlarmManager {

    private Realm mRealm;

    public AlarmRealmManager(Context context) {
//        RealmConfiguration config = new RealmConfiguration.Builder(context).build();
//        Realm.deleteRealm(config);
        mRealm = Realm.getInstance(context);
    }

    @Override
    public Alarm find(int id) {
        return mRealm.where(Alarm.class).equalTo("id", id).findAll().first();
    }

    @Override
    public List<Alarm> findAll() {
        RealmResults<Alarm> results = mRealm.where(Alarm.class).findAll();
        return results.subList(0, results.size());
    }

    @Override
    public void insert(Alarm data) {
        mRealm.beginTransaction();
        Alarm alarm = mRealm.createObject(Alarm.class);
        alarm.setId((int) mRealm.where(Alarm.class).maximumInt("id") + 1);

        alarm.setWhenHours(data.getWhenHours());
        alarm.setWhenMinutes(data.getWhenMinutes());
        alarm.setDisableComplexity(data.getDisableComplexity());
        alarm.setRepeat(data.getRepeat());
        alarm.setSnoozeInMinutes(data.getSnoozeInMinutes());
        alarm.setIsEnabled(data.isEnabled());
        alarm.setIsVibrate(data.isVibrate());
        alarm.setSongId(data.getSongId());
        alarm.setSongTitle(data.getSongTitle());
        alarm.setSongBandName(data.getSongBandName());

        mRealm.commitTransaction();
    }

    @Override
    public void update(Alarm alarm) {
        mRealm.beginTransaction();
        mRealm.copyToRealm(alarm);
        mRealm.commitTransaction();
    }

    @Override
    public void update(Alarm alarm, boolean isEnabled) {
        mRealm.beginTransaction();
        alarm.setIsEnabled(isEnabled);
        mRealm.commitTransaction();
    }

    @Override
    public void deleteCompleted() {
        mRealm.beginTransaction();
        RealmResults<Alarm> results = mRealm.where(Alarm.class).equalTo("completed", true).findAll();
        results.clear();
        mRealm.commitTransaction();
    }
}
