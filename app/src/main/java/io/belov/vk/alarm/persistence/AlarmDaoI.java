package io.belov.vk.alarm.persistence;

import java.util.List;

import io.belov.vk.alarm.alarm.Alarm;

public interface AlarmDaoI {
    Alarm find(int id);
    List<Alarm> findAll();
    void insert(Alarm alarm);
    void update(Alarm alarm);
    void delete(int id);
}
