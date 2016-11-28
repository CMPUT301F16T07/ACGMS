package com.ualberta.cs.alfred;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Geocoder;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Avery on 11/25/2016.
 *
 * class that stores and manages 'offline' data that will be dealt with once connectivity is restored
 */
public class LocalDataManager{

    /**
     * Save a request list in a shared preference
     *
     * @param requestList the request list to be saved
     * @param mode the mode, either Driver or Rider
     * @param context the context to be passed in
     */
    public static void saveRRequestList(ArrayList<Request> requestList, String mode, Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(requestList);
        prefsEditor.putString("RequestList"+mode, json);
        prefsEditor.commit();


    }

    /**
     * Load an existing saved request list
     * @param mode the mode, either Driver or Rider
     * @param context the context to be passed in
     * @return the ArrayList of requests stored on the phone
     */
    public static ArrayList<Request> loadRRequestList(String mode, Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String json = preferences.getString("RequestList"+mode, null);
        ArrayList<Request> loadedRequestList = new Gson().fromJson(json, new TypeToken<List<Request>>(){}.getType());

        return loadedRequestList;
    }


    /**
     * Save a pending list in a shared preference
     *
     * @param requestList the pending list to be saved
     * @param mode the mode, either Driver or Rider
     * @param context the context to be passed in
     */
    public static void saveRPendingList(ArrayList<Request> requestList, String mode, Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(requestList);
        prefsEditor.putString("PendingList"+mode, json);
        prefsEditor.commit();

    }

    /**
     * Load an existing saved pending list
     * @param mode the mode, either Driver or Rider
     * @param context the context to be passed in
     * @return the ArrayList of pending requests stored on the phone
     */
    public static ArrayList<Request> loadRPendingList(String mode, Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String json = preferences.getString("PendingList"+mode, null);
        ArrayList<Request> loadedRequestList = new Gson().fromJson(json, new TypeToken<List<Request>>(){}.getType());

        return loadedRequestList;
    }



    /**
     * Save an accepted list in a shared preference
     *
     * @param requestList the accepted list to be saved
     * @param mode the mode, either Driver or Rider
     * @param context the context to be passed in
     */
    public static void saveRAcceptedList(ArrayList<Request> requestList, String mode, Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(requestList);
        prefsEditor.putString("AcceptedList"+mode, json);
        prefsEditor.commit();

    }

    /**
     * Load an existing saved accepted list
     * @param mode the mode, either Driver or Rider
     * @param context the context to be passed in
     * @return the ArrayList of accepted requests stored on the phone
     */
    public static ArrayList<Request> loadRAcceptedList(String mode, Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String json = preferences.getString("AcceptedList"+mode, null);
        ArrayList<Request> loadedRequestList = new Gson().fromJson(json, new TypeToken<List<Request>>(){}.getType());

        return loadedRequestList;
    }

    /**
     * Loads an ArrayList containing requests made while offline
     * @param context context to be passed in
     * @return an ArrayList containing PartialRequests that were made while offline
     */
    public static ArrayList<PartialRequests> loadPartialRequests(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String json = preferences.getString("PARTIALREQUESTS", null);
        ArrayList<PartialRequests> offlineRequests = new Gson().fromJson(json, new TypeToken<List<PartialRequests>>(){}.getType());

        return offlineRequests;
    }


    /**
     * Saves an ArrayList of requests that were made while offline into the Phone's memory
     * @param offlineList The ArrayList containing PartialRequests made while offline
     * @param context The context to be passed in
     */
    public static void savePartialRequests(ArrayList<PartialRequests> offlineList, Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(offlineList);
        prefsEditor.putString("PARTIALREQUESTS", json);
        prefsEditor.commit();
    }


    /**
     * load the list of offline Acceptances
     * @param context the context passed
     * @return the list of PartialAcceptances
     */
    public static ArrayList<PartialAcceptances> loadPartialAcceptances(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String json = preferences.getString("PARTIALACCEPTANCES", null);
        ArrayList<PartialAcceptances> offlineRequests = new Gson().fromJson(json, new TypeToken<List<PartialAcceptances>>(){}.getType());

        return offlineRequests;
    }


    /**
     * saves a list of PartialAcceptances on the local disk
     * @param offlineList the list of PartialAcceptances
     * @param context the context to the passed
     */
    public static void savePartialAcceptances(ArrayList<PartialAcceptances> offlineList, Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(offlineList);
        prefsEditor.putString("PARTIALACCEPTANCES", json);
        prefsEditor.commit();
    }


    /**
     * process the PartialAcceptances which were made by the driver while there was no connectivity
     * @param context
     */
    public static void executeOfflineAcceptances(Context context) {
        ArrayList<PartialAcceptances> offlineAcceptances = loadPartialAcceptances(context);
        for (int i = 0; i<offlineAcceptances.size();i++){
            PartialAcceptances curr = offlineAcceptances.get(i);
            String from = curr.getFrom();
            String userID = curr.getUserID();
            String mode = curr.getMode();
            Request passedRequest = curr.getPassedRequest();
            String driverSelected = curr.getDriverSelected();

            RequestESSetController.SetPropertyValueTask setPropertyValueTask =new RequestESSetController.SetPropertyValueTask();
            if (from.contentEquals("Pending")) {
                setPropertyValueTask.execute(passedRequest.getRequestID(), "requestStatus", "String", "Accepted");
            } else {
                setPropertyValueTask.execute(passedRequest.getRequestID(), "requestStatus", "String", "Pending");
            }
            if (mode.contentEquals("Driver Mode") && userID != null && !passedRequest.getDriverIDList().contains(userID)) {
                RequestESAddController.AddItemToListTask addItemToListTask1 =new RequestESAddController.AddItemToListTask();
                addItemToListTask1.execute(passedRequest.getRequestID(), "driverIDList", userID);
            }
            if (from.contentEquals("Pending") && mode.contentEquals("Rider Mode")) {
                RequestESSetController.SetPropertyValueTask setPropertyValueTask1 = new RequestESSetController.SetPropertyValueTask();
                setPropertyValueTask1.execute(passedRequest.getRequestID(), "driverID", "string", driverSelected);
            }
        }
        ArrayList<PartialAcceptances> offlineRequestList = new ArrayList<PartialAcceptances>();
        savePartialAcceptances(offlineRequestList,context);
    }


    /**
     * Process all requests made while offline
     * @param context the context to be passed in
     */
    public static void executeOfflineRequests(Context context) {
        ArrayList<PartialRequests> offlineRequests = loadPartialRequests(context);
        for (int i = 0; i < offlineRequests.size(); i++) {
            PartialRequests curr = offlineRequests.get(i);
            if(curr.getMode().equals("AddressMODE")){
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                // List of points returned by the address
                List<android.location.Address> startCoordinates;
                List<android.location.Address> endCoordinates;
                try {
                    startCoordinates = geocoder.getFromLocationName(curr.getsAddress(), 1);
                    endCoordinates = geocoder.getFromLocationName(curr.geteAddress(), 1);

                    int startCoordinatesSize = startCoordinates.size();
                    int endCoordinatesSize = endCoordinates.size();

                    // Check if both list are empty
                    if (startCoordinatesSize > 0 && endCoordinatesSize > 0) {
                        // get the coordinates of the first results for both address
                        double x1 = startCoordinates.get(0).getLatitude();
                        double y1 = startCoordinates.get(0).getLongitude();
                        double x2 = endCoordinates.get(0).getLatitude();
                        double y2 = endCoordinates.get(0).getLongitude();

                        makeRequest(curr.getStatus(),curr.getUserID(),curr.getsAddress(),curr.geteAddress(),x1,y1,x2,y2,context);
                    } else {
                        // Error messages
                        String errorMessage = "Unable to find start and/or destination Address";
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else {
                try {
                    String x1String = curr.getX1().toString();
                    double x1 = Double.parseDouble(x1String);
                    String y1String = curr.getY1().toString();
                    double y1 = Double.parseDouble(y1String);
                    String x2String = curr.getX2().toString();
                    double x2 = Double.parseDouble(x2String);
                    String y2String = curr.getY2().toString();
                    double y2 = Double.parseDouble(y2String);

                    makeRequest(curr.getStatus(),curr.getUserID(),curr.getsAddress(),curr.geteAddress(),x1,y1,x2,y2,context);
                } catch (NumberFormatException e) {
                    String errorMessage = "Invalid Coordinate/s";
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                }
            }

        }
        ArrayList<PartialRequests> offlineRequestList = new ArrayList<PartialRequests>();
        savePartialRequests(offlineRequestList,context);

    }


    /**
     * Processes and uploads the offline requests to the ela1 server
     * @param Status Status of the requests
     * @param userID User id that made the requests
     * @param start the start address
     * @param end the end address
     * @param x1 x coordinate of start address
     * @param y1 y coordinate of start address
     * @param x2 x coordinate of destination address
     * @param y2 y coordinate of destination address
     * @param context the context to be passed in
     */
    private static void makeRequest(String Status, String userID, String start, String end,
                             double x1,double y1,double x2,double y2, Context context){
        // Address is defined as
        // Address(String location, double longitude, double latitude)
        Address startPointAddress = new Address(start, y1, x1);
        Address endPointAddress = new Address(end, y2, x2);

        LatLng startPoint = new LatLng(x1,y1);
        LatLng  endPoint = new LatLng(x2,y2);


        // Calculate Distance
        GMapV2Direction md = new GMapV2Direction();
        Document doc = md.getDocument(startPoint,endPoint,
                GMapV2Direction.MODE_DRIVING);
        double distance = md.getDistanceValue(doc) / 1000;


        // round to the nearest cent
        double cost = Math.round( (distance/2) * 100.0 ) / 100.0 ;

        // Create an instance of a request and store into elastic search
        //    public Request(String requestStatus, Address sourceAddress, Address destinationAddress,
        //              double distance, double cost, String riderID)
        Request request = new Request(Status, startPointAddress, endPointAddress, distance, cost, userID);



        // Notify save
        Toast.makeText(context,"Ride Requested",Toast.LENGTH_SHORT).show();

    }


}
