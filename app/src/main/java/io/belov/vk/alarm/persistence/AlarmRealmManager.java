package io.belov.vk.alarm.persistence;

import android.content.Context;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class AlarmRealmManager implements AlarmManager {

    private Realm mRealm;

    public AlarmRealmManager(Context context) {
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
    public void insert(String name, boolean completed) {
        mRealm.beginTransaction();
        Alarm alarm = mRealm.createObject(Alarm.class);
        alarm.setId((int)mRealm.where(Alarm.class).maximumInt("id") + 1);
        alarm.setName(name);
        alarm.setCompleted(completed);
        mRealm.commitTransaction();
    }

    @Override
    public void update(int id, String name, boolean completed) {
        mRealm.beginTransaction();
        Alarm alarm = find(id);
        alarm.setName(name);
        alarm.setCompleted(completed);
        mRealm.commitTransaction();
    }

    @Override
    public void update(Alarm alarm, String name) {
        mRealm.beginTransaction();
        alarm = mRealm.copyToRealm(alarm);
        alarm.setName(name);
        mRealm.commitTransaction();
    }

    @Override
    public void update(Alarm alarm, boolean completed) {
        mRealm.beginTransaction();
        alarm.setCompleted(completed);
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
