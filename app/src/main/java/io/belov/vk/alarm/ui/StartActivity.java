package io.belov.vk.alarm.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.vk.sdk.VKCallback;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;

import io.belov.vk.alarm.R;
import io.belov.vk.alarm.utils.ActivityUtils;

/**
 * Created by fbelov on 19.10.15.
 */
public class StartActivity extends FragmentActivity {

    private boolean isResumed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final Activity activity = this;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        VKSdk.wakeUpSession(this, new VKCallback<VKSdk.LoginState>() {
            @Override
            public void onResult(VKSdk.LoginState res) {
                if (isResumed) {
                    switch (res) {
                        case LoggedOut:
                            ActivityUtils.openLoginActivity(activity);
                            break;
                        case LoggedIn:
                            ActivityUtils.openAlarmsListActivityWithoutAnimation(activity);
                            break;
                        case Pending:
                            break;
                        case Unknown:
                            break;
                    }
                }
            }

            @Override
            public void onError(VKError error) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        isResumed = true;
        if (VKSdk.isLoggedIn()) {
            ActivityUtils.openAlarmsListActivityWithoutAnimation(this);
        } else {
            ActivityUtils.openLoginWithoutAnimation(this);
        }
    }

    @Override
    protected void onPause() {
        isResumed = false;
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
