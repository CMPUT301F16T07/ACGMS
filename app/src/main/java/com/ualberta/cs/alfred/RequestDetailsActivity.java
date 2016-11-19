package com.ualberta.cs.alfred;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.ualberta.cs.alfred.fragments.ListFragment;
import com.ualberta.cs.alfred.fragments.MapFragment;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


/**
 * activity for the request details
 */
public class RequestDetailsActivity extends AppCompatActivity {

    private DecimalFormat df = new DecimalFormat("0.00");

    //private Button cancelButton;
    private ListView biddingDriversListView;
    private ArrayAdapter<String> biddingDriversAdapter;

    private Button confirmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_details);

        Button cancelButton = (Button) findViewById(R.id.cancel_request_button);
        biddingDriversListView = (ListView) findViewById(R.id.biddingDriversListView);

        //pass in a request (need to uncomment mock request in MainActivity to test this)
        Intent intent = getIntent();
        final Request r = (Request) intent.getSerializableExtra("passedRequest");

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (preferences.getString("MODE", null).contentEquals("Rider Mode") &&
                (r.getRequestStatus().contentEquals("Pending") || r.getRequestStatus().contentEquals("Accepted"))) {
            // TODO: Implement a list of possible drivers for a pending request only for the rider to choose from
            ArrayList<String> fakeDrivers = new ArrayList<>();
            fakeDrivers.add("BILL");
            fakeDrivers.add("BOB");
//            biddingDriversAdapter = new ArrayAdapter<>(RequestDetails.this, R.layout.custom_row, r.getBiddingDrivers());
            biddingDriversAdapter = new ArrayAdapter<>(RequestDetailsActivity.this, R.layout.custom_row, fakeDrivers);
            biddingDriversListView.setAdapter(biddingDriversAdapter);

            // This will show all the possible drivers but we still need to be able to select a driver, and be able to view
            // his profile
        }

        //Mock request for testing
        /*Address start = new Address("loc1",53.5,-113.1);
        Address end = new Address("loc2", 53.0, -113.0);
        Request r = new Request("Accepted",start, end, 500.00, 23.09, "rider124");*/

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestESDeleteController.DeleteRequestTask deleteRequestTask =
                        new RequestESDeleteController.DeleteRequestTask();
                deleteRequestTask.execute(r.getRequestID());
                ListFragment.update(getApplicationContext());

                finish();
            }
        });


        showDetails(r);

        ArrayList<LatLng> edmonton = new ArrayList<>();
        edmonton.add(new LatLng(53.5444,-113.4904));
        MapFragment mapFragment = new MapFragment();
        mapFragment.putMarkers(edmonton);
        Fragment fragment = mapFragment.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.mapContainer, fragment);
        transaction.commit();


    }

    public void showDetails(Request request){
        TextView requestID = (TextView) findViewById(R.id.request_ID);
        TextView status = (TextView) findViewById(R.id.status);
        TextView estPrice = (TextView) findViewById(R.id.est_price);
        TextView distance = (TextView) findViewById(R.id.distance);
        TextView startLoc = (TextView) findViewById(R.id.start_loc);
        TextView endLoc = (TextView) findViewById(R.id.end_loc);

        // get and display request ID
        requestID.setText(request.getRequestID());
        //get and display request status
        status.setText(request.getRequestStatus());
        if (request.getRequestStatus().contentEquals("Accepted")){
            status.setTextColor(0xff008000);
        }
        else if (request.getRequestStatus().contentEquals("Pending")){
            status.setTextColor(0xffffd700);
        }
        else status.setTextColor(0xffff0000);
        //get and display estimated price
        estPrice.setText(Double.toString(request.getCost()));
        //get and display distance
        distance.setText(df.format(new Double(request.getDistance()))+" km");
        //get and display start & end
        startLoc.setText(request.getSourceAddress().getLocation());
        endLoc.setText(request.getDestinationAddress().getLocation());


    }
}