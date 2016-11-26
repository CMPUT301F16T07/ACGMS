package com.ualberta.cs.alfred;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Avery on 11/24/2016.
 *
 * Broadcast receiver that looks for a change in connectivity, that is, when are we reconnected to the internet
 */

public class NetworkChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {
        final ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        final android.net.NetworkInfo wifi = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        final android.net.NetworkInfo mobile = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (wifi.isAvailable() || mobile.isAvailable()) {
            Toast.makeText(context,"wifi available, uploading request to server",Toast.LENGTH_SHORT).show();
            //if network is connected again, execute everything in our offline request buffer
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            String mode =preferences.getString("MODE",null);
            if (mode.equals("Rider Mode")){
                LocalDataManager.executeOfflineRequests(context);
            }
            else if (mode.equals("Driver Mode")){
                //process accepting requests
            }


            Log.d("Network Available ", "Flag No 1");
        }
    }

}