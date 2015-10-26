package io.belov.vk.alarm.vk;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by fbelov on 26.10.15.
 */
public class VkUser {

    private int id;
    private String photoUrl;
    private int audiosCount;

    public VkUser(JSONObject jsonSong) throws JSONException {
        fromJson(jsonSong);
    }

    private void fromJson(JSONObject jsonSong) throws JSONException {
        id = jsonSong.getInt("id");
        photoUrl = jsonSong.getString("photo_200");

        JSONObject counters = jsonSong.getJSONObject("counters");

        if (counters == null) {
            audiosCount = 0;
        } else {
            audiosCount = counters.getInt("audios");
        }
    }

    public int getId() {
        return id;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public int getAudiosCount() {
        return audiosCount;
    }
}
