package io.belov.vk.alarm.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;

import io.belov.vk.alarm.R;

/**
 * Created by fbelov on 19.10.15.
 */
public class StartActivity extends FragmentActivity {

    private boolean isResumed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        VKSdk.wakeUpSession(this, new VKCallback<VKSdk.LoginState>() {
            @Override
            public void onResult(VKSdk.LoginState res) {
                if (isResumed) {
                    switch (res) {
                        case LoggedOut:
                            openLoginActivity();
                            break;
                        case LoggedIn:
                            openAlarmsListActivity();
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
            openAlarmsListActivity();
        } else {
            openLoginActivity();
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

    private void openAlarmsListActivity() {
        startActivity(new Intent(this, AlarmListActivity.class));
        overridePendingTransition(0, 0);
    }

    private void openLoginActivity() {
        startActivity(new Intent(this, LoginActivity.class));
        overridePendingTransition(0, 0);
    }

}
