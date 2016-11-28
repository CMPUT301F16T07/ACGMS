package com.ualberta.cs.alfred.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Pair;

import com.ualberta.cs.alfred.Request;
import com.ualberta.cs.alfred.RequestESGetController;
import com.ualberta.cs.alfred.RequestList;
import com.ualberta.cs.alfred.User;
import com.ualberta.cs.alfred.UserESGetController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Controller of lists that belong to the request fragments
 * @author mmcote
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

    /**
     * get filtered RequestList
     * @param distance the distance string being filtered
     * @param coordinates the coordinates string being filtered
     * @param filter the filter string
     * @param userID the userID string being filtered
     * @param type the type integer
     * @return RequestList of filtered requests
     */
    public RequestList getRequestFilter(String distance, String coordinates,String filter,String userID, int type){
        RequestList requestList = new RequestList();

        RequestESGetController.GetRequestByMultiplePreferencesTask retrievedRequestedKeyword =
                new RequestESGetController.GetRequestByMultiplePreferencesTask();
        RequestESGetController.GetRequestByMultiplePreferencesTask retrievedPendingKeyword =
                new RequestESGetController.GetRequestByMultiplePreferencesTask();
        RequestESGetController.GetRequestByLocationTask retrievedRequestedCoordinates =
                new RequestESGetController.GetRequestByLocationTask();
        RequestESGetController.GetRequestByLocationTask retrievedPendingCoordinates =
                new RequestESGetController.GetRequestByLocationTask();


        try {
            if (type == 0) {
                retrievedRequestedKeyword.execute(
                        "requestStatus", "string", "Requested",
                        "_all", "string", filter
                );
                retrievedPendingKeyword.execute(
                        "requestStatus", "string", "Pending",
                        "_all", "string", filter
                );
                requestList.mergeRequestList(retrievedRequestedKeyword.get());
                requestList.mergeRequestList(new RequestList(retrievedPendingKeyword.get()).removeDriver(userID));

            } else if (type == 1) {
                retrievedRequestedCoordinates.execute(
                        "requestStatus", "string", "Requested",
                        distance, coordinates
                );
                retrievedPendingCoordinates.execute(
                        "requestStatus", "string", "Pending",
                        distance, coordinates
                );
                requestList.mergeRequestList(retrievedRequestedCoordinates.get());
                requestList.mergeRequestList(new RequestList(retrievedPendingCoordinates.get()).removeDriver(userID));
            } else if (type == 2){
                RequestESGetController.GetRequestSortedByPriceTask retrievedPendingPrice =
                        new RequestESGetController.GetRequestSortedByPriceTask();
                RequestESGetController.GetRequestSortedByPriceTask retrievedRequestedPrice =
                        new RequestESGetController.GetRequestSortedByPriceTask();

                String orderBy = "desc";
                retrievedPendingPrice.execute(
                        "requestStatus", "string", "Pending",
                        orderBy
                );
                retrievedRequestedPrice.execute(
                        "requestStatus", "string", "Request",
                        orderBy
                );
                requestList.mergeRequestList(retrievedRequestedPrice.get());
                requestList.mergeRequestList(new RequestList(retrievedPendingPrice.get()).removeDriver(userID));
            } else if (type == 3){
                RequestESGetController.GetRequestSortedByPricePerKmTask retrievedPendingPriceKM =
                        new RequestESGetController.GetRequestSortedByPricePerKmTask();
                RequestESGetController.GetRequestSortedByPricePerKmTask retrievedRequestedPriceKM =
                        new RequestESGetController.GetRequestSortedByPricePerKmTask();

                String orderBy = "desc";
                retrievedPendingPriceKM.execute(
                        "requestStatus", "string", "Pending",
                        orderBy
                );
                retrievedRequestedPriceKM.execute(
                        "requestStatus", "string", "Request",
                        orderBy
                );
                requestList.mergeRequestList(retrievedRequestedPriceKM.get());
                requestList.mergeRequestList(new RequestList(retrievedPendingPriceKM.get()).removeDriver(userID));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return requestList;
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

    public ArrayList<String> getCorrespondingDriverUsernames(ArrayList<String> driverIDs) {
        ArrayList<String> driverUsernames = new ArrayList<>();
        for (String id : driverIDs) {
            UserESGetController.GetUserByIdTask getUserByIdTask = new UserESGetController.GetUserByIdTask();
            getUserByIdTask.execute(id);
            User user = null;
            try {
                user = getUserByIdTask.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
                return null;
            } catch (ExecutionException e) {
                e.printStackTrace();
                return null;
            }
            if (user != null) {
                driverUsernames.add(user.getUserName());
            }
        }
        return driverUsernames;
    }
}
