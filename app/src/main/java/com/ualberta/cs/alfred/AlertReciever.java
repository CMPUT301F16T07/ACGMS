package com.ualberta.cs.alfred;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.NotificationCompat;

import com.ualberta.cs.alfred.fragments.RequestFragmentsListController;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by mmcote on 2016-11-22.
 */

public class AlertReciever extends BroadcastReceiver {
    private NotificationCompat.Builder notification;
    private static final int uniqueID = 00001;
    private static final RequestFragmentsListController rFLC = new RequestFragmentsListController();
    private static SharedPreferences preferences;
    // Called when a broadcast is made targeting this class
    @Override
    public void onReceive(Context context, Intent intent) {
        createNotification(context, "Times Up", "5 Seconds Has Passed", "Alert");
    }

    public void createNotification(Context context, String msg, String msgText, String msgAlert) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        notification = new NotificationCompat.Builder(context);
        notification.setAutoCancel(true);

        // Build the notification
        notification.setSmallIcon(R.drawable.ic_person_white_24dp);

        if (preferences.getString("MODE", "None").contentEquals("Rider Mode")) {
            Integer priorCount = Integer.valueOf(preferences.getString("Pending", null));
            RequestESGetController.GetRequestTask getRequestTask = new RequestESGetController.GetRequestTask();
            ArrayList<Request> returnList = null;
            getRequestTask.execute("riderID", preferences.getString("USERID", null));
            try {
                returnList = (ArrayList<Request>) new RequestList(getRequestTask.get()).getSpecificRequestList("Pending");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            if (returnList != null && priorCount < returnList.size()) {
                rFLC.updateCounts("Rider Mode", context);
                notification.setTicker("A driver accepted your request!");
                notification.setWhen(System.currentTimeMillis());
                notification.setContentTitle("Pending Request!");
                notification.setContentText("Please go to your pending request page to finalize your ride!");

                Intent intent = new Intent(context, MenuActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                notification.setContentIntent(pendingIntent);

                // Builds the notification and issues it
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
                notificationManager.notify(uniqueID, notification.build());
            }
        } else {
            RequestESGetController.GetRequestTask getAccepted = new RequestESGetController.GetRequestTask();
            ArrayList<Request> returnList = null;
            getAccepted.execute("requestStatus", "Accepted");
            try {
                returnList = new RequestList(getAccepted.get()).getWithDriver(preferences.getString("USERID", null));
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            Integer priorCount = Integer.valueOf(preferences.getString("Accepted", null));
            if (returnList != null && priorCount < returnList.size()) {
                rFLC.updateCounts("Driver Mode", context);
                notification.setTicker("The rider is ready to be picked up!");
                notification.setWhen(System.currentTimeMillis());
                notification.setContentTitle("Pick Up Request!");
                notification.setContentText("Please go pick up your rider!");

                Intent intent = new Intent(context, MenuActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                notification.setContentIntent(pendingIntent);

                // Builds the notification and issues it
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
                notificationManager.notify(uniqueID, notification.build());
            }
        }
        //Toast.makeText(context, "Hello", Toast.LENGTH_SHORT).show();
    }
}
