package com.ualberta.cs.alfred.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Pair;

import com.ualberta.cs.alfred.AppSettings;
import com.ualberta.cs.alfred.Request;
import com.ualberta.cs.alfred.RequestESGetController;
import com.ualberta.cs.alfred.RequestList;
import com.ualberta.cs.alfred.User;
import com.ualberta.cs.alfred.UserESGetController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


/**
<<<<<<< HEAD
* Accepted Fragment is a fragment class where all accepted listed is found.
*
* @author carlcastello on 09/11/16.
* @author averytan
* @author mmcote on 2016-11-17.
* @author shltien
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
<<<<<<< HEAD
     * Request flter command.
     * @param distance
     * @param coordinates
     * @param filter
     * @param userID
     * @param type
     * @return
=======
     * get filtered RequestList
     * @param distance the distance string being filtered
     * @param coordinates the coordinates string being filtered
     * @param filter the filter string
     * @param userID the userID string being filtered
     * @param type the type integer
     * @return RequestList of filtered requests
>>>>>>> 67f0a778a7f5645819700e7710aa85d0d5b04e54
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
                        "requestStatus", "string", AppSettings.REQUEST_REQUESTED,
                        "_all", "string", filter
                );
                retrievedPendingKeyword.execute(
                        "requestStatus", "string", AppSettings.REQUEST_PENDING,
                        "_all", "string", filter
                );
                requestList.mergeRequestList(retrievedRequestedKeyword.get());
                requestList.mergeRequestList(new RequestList(retrievedPendingKeyword.get()).removeDriver(userID));

            } else if (type == 1) {
                retrievedRequestedCoordinates.execute(
                        "requestStatus", "string", AppSettings.REQUEST_REQUESTED,
                        distance, coordinates
                );
                retrievedPendingCoordinates.execute(
                        "requestStatus", "string", AppSettings.REQUEST_PENDING,
                        distance, coordinates
                );
                requestList.mergeRequestList(retrievedRequestedCoordinates.get());
                requestList.mergeRequestList(new RequestList(retrievedPendingCoordinates.get()).removeDriver(userID));
            } else if (type == 2){
                RequestESGetController.GetRequestTask retrievedPending =
                        new RequestESGetController.GetRequestTask() ;
                RequestESGetController.GetRequestTask retrievedRequested=
                        new RequestESGetController.GetRequestTask();

                retrievedPending.execute(
                        "requestStatus", AppSettings.REQUEST_PENDING
                );
                retrievedRequested.execute(
                        "requestStatus", AppSettings.REQUEST_REQUESTED
                );
                requestList.mergeRequestList(retrievedRequested.get());
                requestList.mergeRequestList(new RequestList(retrievedPending.get()).removeDriver(userID));
                return new RequestList(requestList.sortByPrice());
            } else if (type == 3){
                RequestESGetController.GetRequestTask retrievedPending =
                        new RequestESGetController.GetRequestTask() ;
                RequestESGetController.GetRequestTask retrievedRequested=
                        new RequestESGetController.GetRequestTask();

                retrievedPending.execute(
                        "requestStatus", AppSettings.REQUEST_PENDING
                );
                retrievedRequested.execute(
                        "requestStatus", AppSettings.REQUEST_REQUESTED
                );
                requestList.mergeRequestList(retrievedRequested.get());
                requestList.mergeRequestList(new RequestList(retrievedPending.get()).removeDriver(userID));
                return new RequestList(requestList.sortByPricePerKM());
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
