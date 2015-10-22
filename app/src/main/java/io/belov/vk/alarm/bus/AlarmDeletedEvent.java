package io.belov.vk.alarm.bus;

/**
 * Created by fbelov on 22.10.15.
 */
public class AlarmDeletedEvent {
    private int id;

    public AlarmDeletedEvent(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
