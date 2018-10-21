package com.example.milja.movieapp.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtils {
    public static boolean hasInternetAccess(Activity context, boolean ignoreWiFiOnly) {
        boolean hasInternet = false;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (cm != null) {
            networkInfo = cm.getActiveNetworkInfo();
        }
        if (networkInfo != null) {
            hasInternet = networkInfo.isConnectedOrConnecting();
            if (hasInternet && !ignoreWiFiOnly) {
                // check is WIFI
                hasInternet = networkInfo.getType() == ConnectivityManager.TYPE_WIFI;
            }
        }

        return hasInternet;
    }
}
