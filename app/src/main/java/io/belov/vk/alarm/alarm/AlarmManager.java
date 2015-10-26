package io.belov.vk.alarm.alarm;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.belov.vk.alarm.alert.AlarmAlertScheduler;
import io.belov.vk.alarm.bus.AlarmDeletedEvent;
import io.belov.vk.alarm.bus.AlarmInsertedEvent;
import io.belov.vk.alarm.bus.AlarmUpdatedEvent;
import io.belov.vk.alarm.bus.VkLogoutEvent;
import io.belov.vk.alarm.persistence.AlarmDaoI;

/**
 * Created by fbelov on 22.10.15.
 */
public class AlarmManager {

    private static final AlarmListComparator ALARM_LIST_COMPARATOR = new AlarmListComparator();

    private Bus bus;
    private AlarmDaoI dao;
    private AlarmAlertScheduler alertScheduler;

    public AlarmManager(Bus bus, AlarmDaoI dao, AlarmAlertScheduler alertScheduler) {
        this.bus = bus;
        this.dao = dao;
        this.alertScheduler = alertScheduler;

        bus.register(this);
    }

    public Alarm find(int id) {
        return dao.find(id);
    }

    public List<Alarm> findAll() {
        List<Alarm> answer = dao.findAll();

        Collections.sort(answer, ALARM_LIST_COMPARATOR);

        return answer;
    }

    public void insert(Alarm alarm) {
        dao.insert(alarm);
        alertScheduler.schedule(alarm);
        bus.post(new AlarmInsertedEvent(alarm));
    }

    public void update(Alarm alarm) {
        dao.update(alarm);
        alertScheduler.schedule(alarm);
        bus.post(new AlarmUpdatedEvent(alarm));
    }

    public void delete(int id) {
        dao.delete(id);
        alertScheduler.unschedule(id);
        bus.post(new AlarmDeletedEvent(id));
    }

    public void rescheduleAllAlarms() {
        List<Alarm> alarms = findAll();

        for (Alarm alarm : alarms) {
            alertScheduler.schedule(alarm);
        }
    }

    private void removeAll() {
        List<Alarm> alarms = findAll();

        for (Alarm alarm : alarms) {
            delete(alarm.getId());
        }
    }

    @Subscribe
    public void on(VkLogoutEvent e) {
        removeAll();
    }

    public boolean hasAlarms() {
        return dao.hasAlarms();
    }

    private static class AlarmListComparator implements Comparator<Alarm> {

        @Override
        public int compare(Alarm lhs, Alarm rhs) {
            return lhs.getWhenInMinutes() - rhs.getWhenInMinutes();
        }

    }
}
