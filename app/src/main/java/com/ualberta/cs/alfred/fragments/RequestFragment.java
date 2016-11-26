package com.ualberta.cs.alfred.fragments;

import android.content.SharedPreferences;
import android.location.Geocoder;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.ualberta.cs.alfred.Address;
import com.ualberta.cs.alfred.ConnectivityChecker;
import com.ualberta.cs.alfred.GMapV2Direction;

import com.ualberta.cs.alfred.GeoCoder;
import com.ualberta.cs.alfred.MenuActivity;

import com.ualberta.cs.alfred.LocalDataManager;
import com.ualberta.cs.alfred.PartialRequests;

import com.ualberta.cs.alfred.R;
import com.ualberta.cs.alfred.Request;

import org.w3c.dom.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by carlcastello on 09/11/16.
 * This is the fragment for making a new request
 */

public class RequestFragment extends Fragment implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    // Variable for edit text view
    private EditText startInputOne;
    private EditText startInputTwo;
    private EditText endInputOne;
    private EditText endInputTwo;
    private ArrayList<PartialRequests> offlineRequestList;

    private int radioButtonID ;


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
        offlineRequestList = new ArrayList<PartialRequests>();
        LocalDataManager.savePartialRequests(offlineRequestList,getContext());

        startInputOne = (EditText) view.findViewById(R.id.start_input_1);
        startInputTwo = (EditText) view.findViewById(R.id.start_input_2);
        endInputOne = (EditText) view.findViewById(R.id.end_input_1);
        endInputTwo = (EditText) view.findViewById(R.id.end_input_2);
        radioButtonID = R.id.radioButtonAddress;

        RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(this);

        Button doneButton = (Button) view.findViewById(R.id.request_done_button);
        doneButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.request_done_button:
                //check if there is network connection, if so, run the request and if not, add it to the stored buffer.
                if(ConnectivityChecker.isConnected(getContext())){
                    onlineClick();
                }
                else{
                    String requestMode;
                    double x1 = 0;
                    double y1 = 0;
                    double x2 = 0;
                    double y2 = 0;

                    // Get user id from the user who requested a ride
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    String userID = preferences.getString("USERID", null);
                    // Initialize request status
                    String Status = "Requested";

                    String start = startInputOne.getText().toString() +", "+
                            startInputTwo.getText().toString();
                    String end = endInputOne.getText().toString() +", "+
                            endInputTwo.getText().toString();

                    // Check if input is null
                    if (start.matches("") || end.matches("")) {
                        // do nothing

                    } else {
                        //determine whether request was done using address or coordinates
                        if (radioButtonID == R.id.radioButtonAddress) {
                            requestMode = "AddressMODE"; //identifier to determine how entry will be inserted into an instance of PartialRequest
                            PartialRequests partialRequest = new PartialRequests(requestMode,userID,Status,start,end,"null","null","null","null");
                            offlineRequestList = LocalDataManager.loadPartialRequests(getContext()); //load partial request into the ArrayList buffer
                            offlineRequestList.add(partialRequest);
                            LocalDataManager.savePartialRequests(offlineRequestList, getContext());//save the new PartialRequest ArrayList buffer
                                }
                        else {
                            requestMode = "CoorMODE"; //identifier to determine how entry will be converted into instance of PartialRequest
                            String x1String = startInputOne.getText().toString();
                            String y1String = startInputTwo.getText().toString();
                            String x2String = endInputOne.getText().toString();
                            String y2String = endInputOne.getText().toString();
                            PartialRequests partialRequest = new PartialRequests(requestMode,userID,Status,"null","null",x1String,x2String,y1String,y2String);
                            //append our new request into a buffer of existing offline requests
                            offlineRequestList = LocalDataManager.loadPartialRequests(getContext());
                            offlineRequestList.add(partialRequest);
                            LocalDataManager.savePartialRequests(offlineRequestList,getContext());
                            
                        }
                    }

                }
        }

    }


    /**
     * this method processes an online requests, that is, a requests made while connectivity is present
     */
    public void onlineClick(){
        double x1 = 0;
        double y1 = 0;
        double x2 = 0;
        double y2 = 0;

        String start1 = startInputOne.getText().toString();
        String start2 = startInputTwo.getText().toString();
        String end1 = endInputOne.getText().toString();
        String end2 = endInputOne.getText().toString();

        // Get user id from the user who requested a ride
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String userID = preferences.getString("USERID", null);
        // Initialize request status
        String Status = "Requested";

        String start = startInputOne.getText().toString() +", "+
                startInputTwo.getText().toString();
        String end = endInputOne.getText().toString() +", "+
                endInputTwo.getText().toString();

        // Check if input is null

            if (start1.matches("") || start2.matches("") ||
                    end1.matches("") || end2.matches("")){
            // do nothing
        } else {
            if (radioButtonID == R.id.radioButtonAddress) {
                GeoCoder geoCoder = GeoCoder.getInstance();
                geoCoder.setAddress(start);
                geoCoder.calculateCoordinatesValue();

                x1 = geoCoder.getLatitudeValue();
                y1 = geoCoder.getLongitudeValue();

                geoCoder.setAddress(end);
                geoCoder.calculateCoordinatesValue();
                x2 = geoCoder.getLatitudeValue();
                y2 = geoCoder.getLongitudeValue();

                makeRequest(Status,userID,start,end,x1,y1,x2,y2);
            } else {
                try {
                    String x1String = startInputOne.getText().toString();
                    x1 = Double.parseDouble(x1String);
                    String y1String = startInputTwo.getText().toString();
                    y1 = Double.parseDouble(y1String);
                    String x2String = endInputOne.getText().toString();
                    x2 = Double.parseDouble(x2String);
                    String y2String = endInputOne.getText().toString();
                    y2 = Double.parseDouble(y2String);

                    makeRequest(Status,userID,start,end,x1,y1,x2,y2);
                } catch (NumberFormatException e) {
                    String errorMessage = "Invalid Coordinate/s";
                    Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
                }
            }

        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        View view = getView();
        TextView textView;
        EditText editText;
        switch(checkedId) {
            //LatLng(53.5444,-113.4904)
            case R.id.radioButtonAddress:
                textView = (TextView) view.findViewById(R.id.start_input1_text);
                textView.setText(R.string.address_text);
                textView = (TextView) view.findViewById(R.id.start_input2_text);
                textView.setText(R.string.city_text);
                textView = (TextView) view.findViewById(R.id.end_input1_text);
                textView.setText(R.string.address_text);
                textView = (TextView) view.findViewById(R.id.end_input2_text);
                textView.setText(R.string.city_text);

                editText = (EditText) view.findViewById(R.id.start_input_1);
                editText.setHint("1 Sir Winston Churchill SQ");
                editText = (EditText) view.findViewById(R.id.start_input_2);
                editText.setHint("Edmonton");

                editText = (EditText) view.findViewById(R.id.end_input_1);
                editText.setHint("1 Sir Winston Churchill SQ");
                editText = (EditText) view.findViewById(R.id.end_input_2);
                editText.setHint("Edmonton");
                radioButtonID =  R.id.radioButtonAddress;
                break;

            case R.id.radioButtonCoordinates:
                textView = (TextView) view.findViewById(R.id.start_input1_text);
                textView.setText(R.string.latitude_text);
                textView = (TextView) view.findViewById(R.id.start_input2_text);
                textView.setText(R.string.longitude_text);
                textView = (TextView) view.findViewById(R.id.end_input1_text);
                textView.setText(R.string.latitude_text);
                textView = (TextView) view.findViewById(R.id.end_input2_text);
                textView.setText(R.string.longitude_text);

                editText = (EditText) view.findViewById(R.id.start_input_1);
                editText.setHint("53.5444");
                editText = (EditText) view.findViewById(R.id.start_input_2);
                editText.setHint("-113.4904");

                editText = (EditText) view.findViewById(R.id.end_input_1);
                editText.setHint("53.5444");
                editText = (EditText) view.findViewById(R.id.end_input_2);
                editText.setHint("-113.4904");
                radioButtonID =  R.id.radioButtonAddress;
                break;

        }
    }

    private void makeRequest(String Status, String userID, String start, String end,
                             double x1,double y1,double x2,double y2){
        // Address is defined as
        // Address(String location, double longitude, double latitude)
        Address startPointAddress = new Address(start, y1, x1);
        Address endPointAddress = new Address(end, y2, x2);

        LatLng  startPoint = new LatLng(x1,y1);
        LatLng  endPoint = new LatLng(x2,y2);


        // Calculate Distance
        GMapV2Direction md = new GMapV2Direction();
        Document doc = md.getDocument(startPoint,endPoint,
               GMapV2Direction.MODE_DRIVING);

        double distance = md.getDistanceValue(doc) / 1000;


        // round to the nearest cent
        double cost = Math.round( (distance/2) * 100.0 ) / 100.0 ;




        if (startPointAddress == null) {
            Toast.makeText(getActivity(), "Invalid Start Point", Toast.LENGTH_SHORT).show();
        } else if (endPointAddress == null) {
            Toast.makeText(getActivity(), "Invalid End Point", Toast.LENGTH_SHORT).show();
        } else if (userID == null) {
            Toast.makeText(getActivity(), "Invalid User ID", Toast.LENGTH_SHORT).show();
        } else {
            Request request = new Request(Status, startPointAddress, endPointAddress, distance, cost, userID);
            // Notify save
            Toast.makeText(getActivity(),"Ride Requested",Toast.LENGTH_SHORT).show();
        }


        FragmentManager fm = getActivity().getSupportFragmentManager();
        for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
        // go to list
        MenuActivity.bottomBar.selectTabAtPosition(1,true);
    }

}
