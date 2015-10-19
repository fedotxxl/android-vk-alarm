package io.belov.vk.alarm.persistence;

import java.util.List;

public interface AlarmManager {
    Alarm find(int id);
    List<Alarm> findAll();
    void insert(Alarm alarm);
    void update(Alarm alarm);
    void update(Alarm alarm, boolean enabled);
    void deleteCompleted();
}