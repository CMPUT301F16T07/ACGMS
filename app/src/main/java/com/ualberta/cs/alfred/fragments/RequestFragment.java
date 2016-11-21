package com.ualberta.cs.alfred.fragments;

import android.content.SharedPreferences;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ualberta.cs.alfred.Address;
import com.ualberta.cs.alfred.MenuActivity;
import com.ualberta.cs.alfred.R;
import com.ualberta.cs.alfred.Request;
import com.ualberta.cs.alfred.RequestESAddController;
import com.ualberta.cs.alfred.RequestList;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by carlcastello on 09/11/16.
 * This is the fragment for making a new request
 */

public class RequestFragment extends Fragment implements View.OnClickListener {
    // Variable for edit text view
    private String Status;
    private EditText startAddress;
    private EditText startCity;
    private EditText endAddress;
    private EditText endCity;
    private String userName;



    public RequestFragment() {
    }

    public static RequestFragment newInstance() {
        Bundle args = new Bundle();
        RequestFragment requestFragment = new RequestFragment();
        requestFragment.setArguments(args);
        return requestFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_request,container,false);

        startAddress = (EditText) view.findViewById(R.id.start_address_input);
        startCity = (EditText) view.findViewById(R.id.start_city_input);
        endAddress = (EditText) view.findViewById(R.id.end_address_input);
        endCity = (EditText) view.findViewById(R.id.end_city_input);

        Button doneButton = (Button) view.findViewById(R.id.request_done_button);
        doneButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.request_done_button:
                // Initialize request status
                Status = "Requested";

                // Get user id from the user who requested a ride
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                userName = preferences.getString("USERNAME", null);


                String start = startAddress.getText().toString() +", "+
                        startCity.getText().toString();
                String end = endAddress.getText().toString() +", "+
                        endCity.getText().toString();

                // Check if input is null
                if (start.matches("") || end.matches("")) {
                    // do nothing
                } else {
                    Geocoder geocoder = new Geocoder(getContext(),Locale.getDefault());
                    // List of points returned by the address
                    List<android.location.Address> startCoordinates;
                    List<android.location.Address> endCoordinates;
                    try {
                        startCoordinates = geocoder.getFromLocationName(start,1);
                        endCoordinates = geocoder.getFromLocationName(end,1);

                        int startCoordinatesSize = startCoordinates.size();
                        int endCoordinatesSize = endCoordinates.size();

                        // Check if both list are empty
                        if (startCoordinatesSize > 0 && endCoordinatesSize > 0) {
                            // get the coordinates of the first results for both address
                            double x1 = startCoordinates.get(0).getLatitude();
                            double y1 = startCoordinates.get(0).getLongitude();
                            double x2 = endCoordinates.get(0).getLatitude();
                            double y2 = endCoordinates.get(0).getLongitude();

                            // Address is defined as
                            // Address(String location, double longitude, double latitude)
                            Address startPoint = new Address(start, y1, x1);
                            Address endPoint = new Address(end, y2, x2);

                            float[] results = new float[1];
                            Location.distanceBetween(x1,y1,x2,y2,results);

                            double distance = (double) results[0] / 1000;

                            // round to the nearest cent
                            double cost = Math.round( (distance/2) * 100.0 ) / 100.0 ;

                            //String string = Float.toString(results[0]);
                            //Toast.makeText(getActivity(),string,Toast.LENGTH_LONG).show();

                            // Create an instance of a request and store into elastic search
                            //    public Request(String requestStatus, Address sourceAddress, Address destinationAddress,
                            //              double distance, double cost, String riderID)
                            Request request = new Request(Status,startPoint,endPoint,distance,cost,userName);

                            // Notify save
                            Toast.makeText(getActivity(),"Ride Requested",Toast.LENGTH_SHORT).show();

                            // go to list
                            MenuActivity.bottomBar.selectTabAtPosition(1,true);

                        } else {
                            // Error messages
                            String errorMessage = "Unable to find start and destination Address";
                            if (startCoordinatesSize == 0) {
                                errorMessage = "Unable to find the start address";
                            }
                            if (endCoordinatesSize == 0) {
                                errorMessage = "Unable to find the destination address";
                            }
                            Toast.makeText(getActivity(),errorMessage,Toast.LENGTH_SHORT).show();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
        }
    }
}
