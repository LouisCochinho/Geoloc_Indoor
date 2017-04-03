package com.imag.air.geoloc_indoor.services;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by louis on 02/03/2017.
 */

// Manage Network connection

public final class NetworkService {

    public static void setWifiEnabled(Context context, boolean enabled){
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        wifi.setWifiEnabled(enabled);
    }

    /**
     * Check if the network is available on the device
     */
    public static boolean networkChecking(Context context) {
        try {
            ConnectivityManager nInfo = (ConnectivityManager) context.getSystemService(
                    Context.CONNECTIVITY_SERVICE);
            nInfo.getActiveNetworkInfo().isConnectedOrConnecting();
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(
                    Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            return netInfo != null && netInfo.isConnectedOrConnecting();
        } catch (Exception e) {
            return false;
        }
    }

    // TODO : public static void setNetworkEnabled(Context context; boolean enabled)
}
