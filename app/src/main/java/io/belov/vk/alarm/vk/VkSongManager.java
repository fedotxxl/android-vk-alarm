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

    public VKRequest getByIdSync(int ownerId, int songId, final VkSongListener listener) {
        return getByIdSync(ownerId, songId, listener, null);
    }

    public VKRequest getByIdSync(int ownerId, int songId, final VkSongListener listener, final VkErrorListener errorListener) {
        VKRequest request = VKApi.audio().getById(VKParameters.from("audios", ownerId + "_" + songId));

        request.executeSyncWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                VkSong song = null;

                try {
                    JSONObject jsonSong = response.json.getJSONArray("response").getJSONObject(0);

                    if (jsonSong != null) {
                        song = new VkSong(jsonSong);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "getByIdSync", e);
                }

                if (song != null) {
                    listener.on(song);
                } else {
                    if (errorListener != null) errorListener.on();
                }
            }

            @Override
            public void onError(VKError error) {
                Log.e(TAG, error.toString());
                if (errorListener != null) errorListener.on();
            }
        });

        return request;
    }

    public VKRequest getAllSongs(final VkSongsListeners listener) {
        return getAllSongs(listener, null, false);
    }

    public VKRequest getAllSongs(final VkSongsListeners listener, final VkErrorListener errorListener, boolean sync) {
        VKRequest request = VKApi.audio().get();
        VKRequest.VKRequestListener requestListener = getAllSongsListener(listener, errorListener);

        if (sync) {
            request.executeSyncWithListener(requestListener);
        } else {
            request.executeWithListener(requestListener);
        }

        return request;
    }

    private VKRequest.VKRequestListener getAllSongsListener(final VkSongsListeners listener, final VkErrorListener errorListener) {
        return new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                boolean isSuccess = false;

                try {
                    JSONObject jsonResponse = response.json.getJSONObject("response");

                    if (jsonResponse != null) {
                        songsAllCache = getSongs(jsonResponse.getJSONArray("items"));
                        songsCount = jsonResponse.getInt("count");

                        listener.on(songsCount, songsAllCache);
                        isSuccess = true;
                    }
                } catch (Exception e) {
                    Log.e(TAG, "getAllSongs", e);
                }

                if (!isSuccess && errorListener != null) errorListener.on();
            }

            @Override
            public void onError(VKError error) {
                Log.e(TAG, error.toString());
                if (errorListener != null) errorListener.on();
            }
        };
    }

    public void getAllOrCachedSongsSync(VkSongsListeners listener) {
        getAllOrCachedSongs(listener, true);
    }

    public void getAllOrCachedSongs(VkSongsListeners listener) {
        getAllOrCachedSongs(listener, false);
    }

    private void getAllOrCachedSongs(VkSongsListeners listener, boolean sync) {
        if (songsCount < 0) {
            getAllSongs(listener, null, sync);
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
