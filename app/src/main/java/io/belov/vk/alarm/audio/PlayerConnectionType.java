package io.belov.vk.alarm.audio;

import io.belov.vk.alarm.R;

/**
 * Created by fbelov on 07.11.15.
 */
public enum PlayerConnectionType {
    WIFI_ONLY("wifi_only", R.string.pref_download_connection_wifi_only), ANY("any", R.string.pref_download_connection_any);

    private String id;
    private int translationId;

    PlayerConnectionType(String id, int translationId) {
        this.id = id;
        this.translationId = translationId;
    }

    public String getId() {
        return id;
    }

    public int getTranslationId() {
        return translationId;
    }

    public static PlayerConnectionType myValueOf(String id) {
        for (PlayerConnectionType type : values()) {
            if (type.id.equals(id)) return type;
        }

        return null;
    }
}
