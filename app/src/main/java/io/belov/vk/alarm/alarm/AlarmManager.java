package io.belov.vk.alarm.alarm;

import com.squareup.otto.Bus;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.belov.vk.alarm.bus.AlarmDeletedEvent;
import io.belov.vk.alarm.bus.AlarmInsertedEvent;
import io.belov.vk.alarm.bus.AlarmUpdatedEvent;
import io.belov.vk.alarm.persistence.AlarmDaoI;

/**
 * Created by fbelov on 22.10.15.
 */
public class AlarmManager {

    private static final AlarmListComparator ALARM_LIST_COMPARATOR = new AlarmListComparator();

    private Bus bus;
    private AlarmDaoI dao;

    public AlarmManager(Bus bus, AlarmDaoI dao) {
        this.bus = bus;
        this.dao = dao;
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
        bus.post(new AlarmInsertedEvent(alarm));
    }

    public void update(Alarm alarm) {
        dao.update(alarm);
        bus.post(new AlarmUpdatedEvent(alarm));
    }

    public void delete(int id) {
        dao.delete(id);
        bus.post(new AlarmDeletedEvent(id));
    }

    private static class AlarmListComparator implements Comparator<Alarm> {

        @Override
        public int compare(Alarm lhs, Alarm rhs) {
            return lhs.getWhenInMinutes() - rhs.getWhenInMinutes();
        }

    }
}
