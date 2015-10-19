package io.belov.vk.alarm.alert;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by fbelov on 19.10.15.
 */
public class AlarmAlertBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            //todo enable alarms on reboot
        }
    }

}
