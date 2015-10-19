package io.belov.vk.alarm;

public class Config {

    public static final String PACKAGE = "io.belov.vk.alarm";
    public static final String PACKAGE_PREFIX = PACKAGE + ".";

    public static final String INTENT_EXTRA_PREFIX = PACKAGE_PREFIX + "extra.";

    public static final String EXTRA_ALARM_ID = INTENT_EXTRA_PREFIX + "id";
    public static final String EXTRA_ALARM_WHEN_HOURS = INTENT_EXTRA_PREFIX + "whenHours";
    public static final String EXTRA_ALARM_WHEN_MINUTES = INTENT_EXTRA_PREFIX + "whenMinutes";
}
