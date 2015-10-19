package io.belov.vk.alarm.bus;

/**
 * Created by fbelov on 18.10.15.
 */
public class AlarmItemOpenEvent {

    private int position;

    public AlarmItemOpenEvent(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }
}
