package com.ualberta.cs.alfred;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Avery on 11/25/2016.
 */

public class LocalDataManager{
    public static void saveRRequestList(ArrayList<Request> requestList, String mode, Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(requestList);
        prefsEditor.putString("RequestList"+mode, json);
        prefsEditor.commit();
    }

    public static ArrayList<Request> loadRRequestList(String mode, Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String json = preferences.getString("RequestList"+mode, null);
        ArrayList<Request> loadedRequestList = new Gson().fromJson(json, new TypeToken<List<Request>>(){}.getType());

        return loadedRequestList;
    }



    public static void saveRPendingList(ArrayList<Request> requestList, String mode, Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(requestList);
        prefsEditor.putString("PendingList"+mode, json);
        prefsEditor.commit();

    }

    public static ArrayList<Request> loadRPendingList(String mode, Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String json = preferences.getString("PendingList"+mode, null);
        ArrayList<Request> loadedRequestList = new Gson().fromJson(json, new TypeToken<List<Request>>(){}.getType());

        return loadedRequestList;
    }

    public static void saveRAcceptedList(ArrayList<Request> requestList, String mode, Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(requestList);
        prefsEditor.putString("AcceptedList"+mode, json);
        prefsEditor.commit();

    }

    public static ArrayList<Request> loadRAcceptedList(String mode, Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String json = preferences.getString("AcceptedList"+mode, null);
        ArrayList<Request> loadedRequestList = new Gson().fromJson(json, new TypeToken<List<Request>>(){}.getType());

        return loadedRequestList;
    }


}
