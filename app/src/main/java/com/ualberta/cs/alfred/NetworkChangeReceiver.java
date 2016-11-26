package com.ualberta.cs.alfred;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

/**
 * Created by Avery on 11/24/2016.
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
            uploadRequest(context);

            Log.d("Network Available ", "Flag No 1");
        }
    }
    public void uploadRequest(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        String Status = preferences.getString("RSTATUS","null");
        Toast.makeText(context,"calling uploadRequest"+Status,Toast.LENGTH_SHORT).show();

        if (!Status.equals("null")){
            Gson sAddressGson = new Gson();
            String sAddressJson = preferences.getString("RSTARTADDRESS", null);
            Address startPointAddress = sAddressGson.fromJson(sAddressJson, Address.class);
            Gson eAddressGson = new Gson();
            String eAddressJson = preferences.getString("RSTARTADDRESS", null);
            Address endPointAddress = eAddressGson.fromJson(eAddressJson, Address.class);
            double distance = preferences.getLong("RDISTANCE", -1);
            double cost = preferences.getLong("RCOST", -1);
            String userID = preferences.getString("RUSERID", null);
            Request request = new Request(Status, startPointAddress, endPointAddress, distance, cost, userID);
       }


        editor.putString("RSTATUS", null);
        editor.putString("RSTARTADDRESS",null);
        editor.putString("RENDADDRESS", null);
        editor.putLong("RDISTANCE", -1);
        editor.putLong("RCOST", -1);
        editor.putString("RUSERID", null);
        editor.commit();
    }
}