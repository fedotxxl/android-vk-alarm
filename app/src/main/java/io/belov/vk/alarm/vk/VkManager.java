package io.belov.vk.alarm.vk;

import android.util.Log;

import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import org.json.JSONObject;

/**
 * Created by fbelov on 26.10.15.
 */
public class VkManager {

    private static final String TAG = "VkManager";

    public VKRequest getCurrentUserInfo(final VkUserInfoListener listener) {
        return getUserInfo(-1, listener);
    }

    public VKRequest getUserInfo(int userId, final VkUserInfoListener listener) {
        VKParameters params = VKParameters.from("fields", "photo_200, counters");

        if (userId > 0) {
            params.put("user_ids", userId);
        }

        VKRequest request = VKApi.users().get(params);

        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                try {
                    JSONObject jsonUser = response.json.getJSONArray("response").getJSONObject(0);

                    if (jsonUser != null) {
                        listener.on(new VkUser(jsonUser));
                    }
                } catch (Exception e) {
                    Log.e(TAG, "getUserInfo", e);
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
