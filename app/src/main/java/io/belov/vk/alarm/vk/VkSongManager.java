package io.belov.vk.alarm.vk;

import android.util.Log;

import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.belov.vk.alarm.utils.RandomUtils;

/**
 * Created by fbelov on 19.10.15.
 */
public class VkSongManager {

    private static final String TAG = "VkSongManager";

    private List<VkSong> songsAllCache = new ArrayList<>();
    private int songsCount = -1;

    public VKRequest getRandom(int maxOffset, final VkSongListener listener) {
        VKRequest request = VKApi.audio().get(VKParameters.from("offset", RandomUtils.R.nextInt(maxOffset), "count", 1));

        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                try {
                    JSONObject jsonSong = response.json.getJSONObject("response").getJSONArray("items").getJSONObject(0);

                    if (jsonSong != null) {
                        listener.on(new VkSong(jsonSong));
                    }
                } catch (Exception e) {
                    Log.e(TAG, "getRandom", e);
                }
            }

            @Override
            public void onError(VKError error) {
                Log.e(TAG, error.toString());
            }
        });

        return request;
    }

    public VKRequest getById(int ownerId, int songId, final VkSongListener listener) {
        VKRequest request = VKApi.audio().getById(VKParameters.from("audios", ownerId + "_" + songId));

        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                try {
                    JSONObject jsonSong = response.json.getJSONArray("response").getJSONObject(0);

                    if (jsonSong != null) {
                        listener.on(new VkSong(jsonSong));
                    }
                } catch (Exception e) {
                    Log.e(TAG, "getById", e);
                }
            }

            @Override
            public void onError(VKError error) {
                Log.e(TAG, error.toString());
            }
        });

        return request;
    }

    public VKRequest getAllSongs(final VkSongsListeners listener) {
        VKRequest request = VKApi.audio().get();

        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                try {
                    JSONObject jsonResponse = response.json.getJSONObject("response");

                    if (jsonResponse != null) {
                        songsAllCache = getSongs(jsonResponse.getJSONArray("items"));
                        songsCount = jsonResponse.getInt("count");

                        listener.on(songsCount, songsAllCache);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "getAllSongs", e);
                }
            }

            @Override
            public void onError(VKError error) {
                Log.e(TAG, error.toString());
            }
        });

        return request;
    }

    public void getAllOrCachedSongs(VkSongsListeners listener) {
        if (songsCount < 0) {
            getAllSongs(listener);
        } else {
            listener.on(songsCount, songsAllCache);
        }
    }

    public List<VkSong> getAllCachedSongs() {
        return songsAllCache;
    }

    public boolean hasAllCachedSongs() {
        return songsCount >= 0;
    }

    public int getAllCachedSongsCount() {
        return songsCount;
    }

    private List<VkSong> getSongs(JSONArray array) throws JSONException {
        List<VkSong> songs = new ArrayList<>(array.length());

        for (int i = 0; i < array.length(); i++) {
            songs.add(new VkSong((JSONObject) array.get(i)));
        }

        return songs;
    }
}
