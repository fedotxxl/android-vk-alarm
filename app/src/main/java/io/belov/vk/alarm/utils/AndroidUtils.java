package io.belov.vk.alarm.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by fbelov on 29.10.15.
 */
public class AndroidUtils {

    public static ConnectionInfo getConnectionInfo(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connManager .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        return new ConnectionInfo(wifi.isConnected(), mobile.isConnected());
    }

    public static class ConnectionInfo {

        private boolean isWifi;
        private boolean isMobile;

        public ConnectionInfo(boolean isWifi, boolean isMobile) {
            this.isWifi = isWifi;
            this.isMobile = isMobile;
        }

        public boolean isWifi() {
            return isWifi;
        }

        public boolean isMobile() {
            return isMobile;
        }

        public boolean hasConnection() {
            return isMobile || isWifi;
        }
    }
}
