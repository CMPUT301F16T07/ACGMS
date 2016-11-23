package com.ualberta.cs.alfred.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Pair;

import com.ualberta.cs.alfred.Request;
import com.ualberta.cs.alfred.RequestESGetController;
import com.ualberta.cs.alfred.RequestList;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by mmcote on 2016-11-17.
 */

public class RequestFragmentsListController {
    public RequestList getRequestList(List<Pair<String, String>> queries) {
        /* The request that should be retrieved are all requests that are currently with a requested status and those that
        are pending that do not include the driver on the bidlist of the request.
         */
        RequestList requestedList = new RequestList();
        try {
            for (Pair<String, String> query : queries) {
                RequestESGetController.GetRequestTask getRequested = new RequestESGetController.GetRequestTask();
                getRequested.execute(query.first, query.second);
                List<Request> requested = getRequested.get();
                requestedList.addMultipleRequest(requested);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return requestedList;
    }

    public void updateCounts(String mode, Context context) {
        if (mode.contentEquals("Rider Mode")) {
            getRiderListCount(context, "Requested");
            getRiderListCount(context, "Pending");
            getRiderListCount(context, "Accepted");
        } else {
            getDriverListCount(context, "Requested");
            getDriverListCount(context, "Pending");
            getDriverListCount(context, "Accepted");
        }
    }

    /**
     * Modified getRiderRequestList
     * This function will take an argument with a type string
     * It'll then compare the argument into an appropriate type and return an appropriate list
     * @param argType
     */
    private void getRiderListCount(Context context, String argType) {
        RequestESGetController.GetRequestTask getRequestTask = new RequestESGetController.GetRequestTask();
        ArrayList<Request> returnList = null;

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();

        try {
            getRequestTask.execute("riderID", preferences.getString("USERID", null));
            returnList = (ArrayList<Request>) new RequestList(getRequestTask.get()).getSpecificRequestList(argType);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        editor.putString(argType, Integer.toString(returnList.size()));
        editor.commit();
    }

    /**
     * Modified getDriverRequestList
     * This function will take an argument with a type string
     * It'll then compare the argument into an appropriate type and return an appropriate list
     * @param argType
     */
    private void getDriverListCount(Context context, String argType) {
        /* The request that should be retrieved are all requests that are currently with a requested status and those that
        are pending that do not include the driver on the bidlist of the request.
         */
        RequestESGetController.GetRequestTask getPending = new RequestESGetController.GetRequestTask();
        RequestESGetController.GetRequestTask getRequested = new RequestESGetController.GetRequestTask();
        RequestESGetController.GetRequestTask getAccepted = new RequestESGetController.GetRequestTask();
        ArrayList<Request> returnList = null;

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();

        try {
            if (argType.equals("Requested")) {
                getPending.execute("requestStatus", "Pending");
                returnList = new RequestList(getPending.get()).removeDriver(preferences.getString("USERID", null));
                getRequested.execute("requestStatus", "Requested");
                returnList.addAll(getRequested.get());
            } else if (argType.equals("Pending")) {
                getPending.execute("requestStatus", "Pending");
                returnList = new RequestList(getPending.get()).getWithDriver(preferences.getString("USERID", null));

            } else if (argType.equals("Accepted")) {
                getAccepted.execute("requestStatus", "Accepted");
                returnList = new RequestList(getAccepted.get()).getWithDriver(preferences.getString("USERID", null));

            } else {
                returnList = new ArrayList<Request>();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        editor.putString(argType, Integer.toString(returnList.size()));
        editor.commit();
    }
}
