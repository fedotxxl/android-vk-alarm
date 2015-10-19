package io.belov.vk.alarm.vk;

import android.util.Log;

import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import org.json.JSONObject;

import io.belov.vk.alarm.utils.RandomUtils;

/**
 * Created by fbelov on 19.10.15.
 */
public class VkSongManager {

    private static final String TAG = "VkSongManager";

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


}
