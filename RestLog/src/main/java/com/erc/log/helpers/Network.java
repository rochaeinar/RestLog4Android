package com.erc.log.helpers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;

public class Network {
    public static boolean hasConnectivity(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        boolean hasConnection = netInfo != null && netInfo.isConnected();
        return hasConnection;
    }

    public static String connectivityString(Context context) {
        return hasConnectivity(context) ? "online" : "offline";
    }

    public static ConnectionType getConnectionType(Context context) {
        ConnectionType result = ConnectionType.NONE; // Returns connection type. 0: none; 1: mobile data; 2: wifi
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (cm != null) {
                NetworkCapabilities capabilities = cm.getNetworkCapabilities(cm.getActiveNetwork());
                if (capabilities != null) {
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        result = ConnectionType.WIFI;
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        result = ConnectionType.CELLULAR;
                    }
                }
            }
        } else {
            if (cm != null) {
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                if (activeNetwork != null) {
                    //noinspection deprecation
                    if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                        result = ConnectionType.WIFI;
                    } else //noinspection deprecation
                        if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                            result = ConnectionType.CELLULAR;
                        }
                }
            }
        }
        return result;
    }
}
