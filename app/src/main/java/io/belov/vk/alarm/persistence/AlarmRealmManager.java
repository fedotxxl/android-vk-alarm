package io.belov.vk.alarm.persistence;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.belov.vk.alarm.AlarmWrapper;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class AlarmRealmManager implements AlarmManager {

    private static final AlarmListComparator ALARM_LIST_COMPARATOR = new AlarmListComparator();

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
        List<Alarm> answer = new ArrayList<>(results);

        Collections.sort(answer, ALARM_LIST_COMPARATOR);

        return answer;
    }

    @Override
    public void insert(Alarm data) {
        mRealm.beginTransaction();
        Alarm alarm = mRealm.createObject(Alarm.class);
        AlarmWrapper wrapper = new AlarmWrapper(alarm);

        wrapper.setPropertiesFrom((int) mRealm.where(Alarm.class).maximumInt("id") + 1, data);

        mRealm.commitTransaction();
    }

    public void findAndUpdate(Alarm data) {
        Alarm alarm = find(data.getId());
        AlarmWrapper wrapper = new AlarmWrapper(alarm);

        mRealm.beginTransaction();

        wrapper.setPropertiesFrom(data);

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

    private static class AlarmListComparator implements Comparator<Alarm> {

        @Override
        public int compare(Alarm lhs, Alarm rhs) {
            return new AlarmWrapper(lhs).getWhenInMinutes() - new AlarmWrapper(rhs).getWhenInMinutes();
        }

    }
}
