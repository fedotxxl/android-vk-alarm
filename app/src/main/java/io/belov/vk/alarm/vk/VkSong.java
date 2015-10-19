package io.belov.vk.alarm.vk;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by fbelov on 19.10.15.
 */
public class VkSong {

    private int id;
    private int ownerId;
    private String artist;
    private String title;
    private long date;
    private String url;

    public VkSong(JSONObject jsonSong) throws JSONException {
        fromJson(jsonSong);
    }

    private void fromJson(JSONObject jsonSong) throws JSONException {
        id = jsonSong.getInt("id");
        ownerId = jsonSong.getInt("owner_id");
        artist = jsonSong.getString("artist");
        title = jsonSong.getString("title");
        date = jsonSong.getLong("date");
        url = jsonSong.getString("url");
    }

    public int getId() {
        return id;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public String getArtist() {
        return artist;
    }

    public String getTitle() {
        return title;
    }

    public long getDate() {
        return date;
    }

    public String getUrl() {
        return url;
    }
}
