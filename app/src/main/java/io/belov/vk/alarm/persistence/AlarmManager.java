package io.belov.vk.alarm.persistence;

import java.util.List;

public interface AlarmManager {
    Alarm find(int id);
    List<Alarm> findAll();
    void insert(String name, boolean completed);
    void update(int id, String name, boolean completed);
    void update(Alarm alarm, String name);
    void update(Alarm alarm, boolean completed);
    void deleteCompleted();
}
