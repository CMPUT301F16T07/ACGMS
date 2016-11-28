package com.ualberta.cs.alfred;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.ualberta.cs.alfred.fragments.RequestFragmentsListController;

import org.w3c.dom.Document;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;


/**
 * activity for showing the request details
 * @author shelltian820 and mmcote and carlcastello
 *
 */
public class RequestDetailsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;
    private GoogleMap googleMap;
    private DecimalFormat df = new DecimalFormat("0.00");
    private Request passedRequest;
    private ListView biddingDriversListView;
    private ArrayAdapter<String> biddingDriversAdapter;
    private String driverSelected;
    private String mode;
    private String from;
    private ArrayList<String> driverIDs;
    private ArrayList<String> driverUsernameArray;
    private ArrayList<PartialAcceptances> partialAcceptancesList;
    private String driverID;
    private double newRating;
    private User driver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_details);

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(RequestDetailsActivity.this);
        final Intent intent = getIntent();

        // the mode and which request list this activity was called from will
        // decide what cancel and confirm will do
        mode = preferences.getString("MODE", "None");
        from = intent.getExtras().getString("FROM", "None");

        // the request passed from the list
        passedRequest = (Request) intent.getSerializableExtra("passedRequest");

        // which list the request will go to next
        String next = "None";

        // the driver elements should only show for the rider pending list and accepted for both
        // modes
        TextView requestingRider = (TextView) findViewById(R.id.requestingRider);
        UserESGetController.GetUserByIdTask getRiderByID = new UserESGetController.GetUserByIdTask();
        getRiderByID.execute(passedRequest.getRiderID());
        User user = null;
        try {
            user = getRiderByID.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        if (user != null) {
            requestingRider.setText(user.getUserName());
        } else {
            requestingRider.setText("Error retreiving username.");
        }

        TextView selectedDriverHeading = (TextView) findViewById(R.id.selectedDriverHeading);
        selectedDriverHeading.setVisibility(View.GONE);
        final TextView selectedDriver = (TextView) findViewById(R.id.selectedDriver);
        selectedDriver.setVisibility(View.GONE);

        // buttons that will be availible to requests, availibility of a request is dependent on
        // the list that it came from
        TableLayout tableLayout = (TableLayout) findViewById(R.id.tableView);
        Button rideCompleteButton = (Button) findViewById(R.id.ride_complete_button);
        rideCompleteButton.setVisibility(View.GONE);
        Button confirmButton = (Button) findViewById(R.id.accept_pending_button);
        final Button cancelButton = (Button) findViewById(R.id.cancel_request_button);
        Button rateDriverButton = (Button) findViewById(R.id.rate_driver_button);
        rateDriverButton.setVisibility(View.GONE);

        // the list of bidding drivers that will only show to the rider in the pending list
        biddingDriversListView = (ListView) findViewById(R.id.biddingDriversListView);
        TextView biddingDriversHeader = (TextView) findViewById(R.id.biddingDriversHeader);
        biddingDriversHeader.setVisibility(View.GONE);

        // if from requested list
        if (from.contentEquals("Requested")) {
            rideCompleteButton.setVisibility(View.GONE);
            // only the driver will be able to confirm anything in the first stage
            // the rider will only be able to cancel the request
            if (mode.contentEquals("Rider Mode")) {
                confirmButton.setVisibility(View.GONE);
                tableLayout.setColumnStretchable(0,true);
                tableLayout.setColumnCollapsed(1,true);
            } else {
                cancelButton.setVisibility(View.GONE);
                tableLayout.setColumnStretchable(1,true);
                tableLayout.setColumnCollapsed(0,true);
            }
            next = "Pending";
        // if from pending list
        } else if (from.contentEquals("Pending")) {
            // the cancel button needs to remain for both the driver and rider
            // since they both should be able to cancel a request or bid
            // but only the rider should be able to confirm a bid at this time
            if (mode.contentEquals("Driver Mode")) {
                confirmButton.setVisibility(View.GONE);
                tableLayout.setColumnStretchable(0,true);
                tableLayout.setColumnCollapsed(1,true);
            } else {
                tableLayout.setColumnStretchable(0,true);
                tableLayout.setColumnStretchable(1,true);
            }
            next = "Accepted";
        // if from accepted list
        } else if (from.contentEquals("Accepted")) {
            // finally the trip is in progress and now the all both can do is
            // cancel the trip
            confirmButton.setVisibility(View.GONE);
            tableLayout.setColumnStretchable(0,true);
            tableLayout.setColumnCollapsed(1,true);
            if (mode.contentEquals("Rider Mode")) {
                rideCompleteButton.setVisibility(View.VISIBLE);
            }
        } else if (from.contentEquals("Awaiting Payment") || from.contentEquals("Completed")) {
            confirmButton.setVisibility(View.GONE);
            cancelButton.setVisibility(View.GONE);
        }

        if ((mode.contentEquals("Rider Mode") && from.contentEquals("Pending")) ||
                from.contentEquals("Accepted") || from.contentEquals("Awaiting Payment") ||
                        from.contentEquals("Completed")) {
            selectedDriverHeading.setVisibility(View.VISIBLE);
            selectedDriver.setVisibility(View.VISIBLE);
        }

        if (mode.contentEquals("Rider Mode") && from.contentEquals("Pending")) {
            biddingDriversHeader.setVisibility(View.VISIBLE);
            driverIDs = passedRequest.getDriverIDList();
            RequestFragmentsListController rFLC = new RequestFragmentsListController();
            driverUsernameArray = rFLC.getCorrespondingDriverUsernames(driverIDs);
            biddingDriversAdapter = new ArrayAdapter<>(RequestDetailsActivity.this, R.layout.driver_row, driverUsernameArray);
            biddingDriversListView.setAdapter(biddingDriversAdapter);
            if (driverIDs.size() > 0) {
                driverSelected = driverIDs.get(0);
                selectedDriver.setText(driverUsernameArray.get(0));
            } else {
                driverSelected = "Error";
                selectedDriver.setText("");
            }
        } else if (from.contentEquals("Accepted") || from.contentEquals("Awaiting Payment") || from.contentEquals("Completed")) {
            if (from.contentEquals("Completed")){
                rateDriverButton.setVisibility(View.VISIBLE);
            }
            UserESGetController.GetUserByIdTask getUserByIdTask = new UserESGetController.GetUserByIdTask();
            getUserByIdTask.execute(passedRequest.getDriverID());
            User driver = null;
            try {
                user = getUserByIdTask.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            if (user != null) {
                selectedDriver.setText(user.getUserName());
            } else {
                selectedDriver.setText("Error retrieving selected driver.");
            }
        }

        biddingDriversListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RequestDetailsActivity.this);
                final String possibleDriverUsername = (String) biddingDriversListView.getItemAtPosition(position);
                final String possibleDriver = driverIDs.get(driverUsernameArray.indexOf(possibleDriverUsername));
                if (!driverSelected.contentEquals(possibleDriver)) {
                    builder.setTitle("Change selected driver?");
                    builder.setCancelable(Boolean.TRUE);
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            driverSelected = possibleDriver;
                            selectedDriver.setText(possibleDriverUsername);
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                } else {
                    builder.setTitle("Driver is already selected.");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                }
                builder.setNeutralButton("View Profile", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(RequestDetailsActivity.this, DriverDetailsActivity.class);
                        intent.putExtra("ID",possibleDriver);
                        startActivity(intent);
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userID = preferences.getString("USERID", null);

                //if there is connectivity, execute the processing of accepting a request, if not, store it in a list of
                //PartialAcceptances which is stored locally on the disk
                if (ConnectivityChecker.isConnected(RequestDetailsActivity.this)){
                    upgradeStatus(from);
                    if (mode.contentEquals("Driver Mode") && userID != null && !passedRequest.getDriverIDList().contains(userID)) {
                        RequestESAddController.AddItemToListTask addItemToListTask =
                                new RequestESAddController.AddItemToListTask();
                        addItemToListTask.execute(passedRequest.getRequestID(), "driverIDList", userID);
                    }
                    if (from.contentEquals("Pending") && mode.contentEquals("Rider Mode")) {
                        RequestESSetController.SetPropertyValueTask setPropertyValueTask = new RequestESSetController.SetPropertyValueTask();
                        setPropertyValueTask.execute(passedRequest.getRequestID(), "driverID", "string", driverSelected);
                    }
                    finish();
                }
                else{
                    partialAcceptancesList = LocalDataManager.loadPartialAcceptances(RequestDetailsActivity.this);
                    if (partialAcceptancesList.isEmpty()){
                        partialAcceptancesList = new ArrayList<PartialAcceptances>();
                    }
                    PartialAcceptances offlineAcceptances = new PartialAcceptances(from, userID, mode, passedRequest,driverSelected);
                    partialAcceptancesList.add(offlineAcceptances);
                    LocalDataManager.savePartialAcceptances(partialAcceptancesList,RequestDetailsActivity.this);
                }
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RequestDetailsActivity.this);
                builder.setCancelable(Boolean.TRUE);
                if (mode.contentEquals("Rider Mode")) {
                    if (!from.contentEquals("Requested")) {
                        builder.setTitle("Remove or Downgrade the selected request? " +
                                "(Downgrading the request will clear the list of bidding drivers.)");
                    } else {
                        builder.setTitle("Remove the selected request?");
                    }
                    builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            RequestESDeleteController.DeleteRequestTask deleteRequestTask =
                                    new RequestESDeleteController.DeleteRequestTask();
                            deleteRequestTask.execute(passedRequest.getRequestID());
                            finish();
                        }
                    });
                } else {
                    builder.setTitle("Remove your bid from the selected request?");
                }
                builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                if (!from.contentEquals("Requested")) {
                    builder.setPositiveButton("Downgrade", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (mode.contentEquals("Driver Mode")) {
                                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(RequestDetailsActivity.this);
                                RequestESDeleteController.DeleteItemFromListTask deleteItemFromListTask = new RequestESDeleteController.DeleteItemFromListTask();
                                deleteItemFromListTask.execute(passedRequest.getRequestID(), "driverIDList", "String", preferences.getString("USERID", null));
                                if (from.contentEquals("Accepted")) {
                                    UserESSetController.SetPropertyValueTask removeDriverID = new UserESSetController.SetPropertyValueTask();
                                    removeDriverID.execute(passedRequest.getRequestID(), "driverID", "String", "");
                                }
                            } else {
                                RequestESSetController.SetPropertyValueTask setPropertyValueTask =
                                        new RequestESSetController.SetPropertyValueTask();
                                if (from.contentEquals("Pending")) {
                                    setPropertyValueTask.execute(passedRequest.getRequestID(), "requestStatus", "String", "Requested");
                                    // TODO: Clear Driver List
                                } else {
                                    setPropertyValueTask.execute(passedRequest.getRequestID(), "requestStatus", "String", "Pending");
                                    UserESSetController.SetPropertyValueTask removeDriverID = new UserESSetController.SetPropertyValueTask();
                                    removeDriverID.execute(passedRequest.getRequestID(), "driverID", "String", "");
                                    // TODO: Clear Driver List
                                }
                            }
                            finish();
                        }
                    });
                }
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        rideCompleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RequestDetailsActivity.this);
                builder.setTitle("You arrived and are ready to pay?");
                builder.setMessage("Please confirm that you have arrived at your location.");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        RequestESSetController.SetPropertyValueTask setRequestedStatus =
                                new RequestESSetController.SetPropertyValueTask();
                        setRequestedStatus.execute(passedRequest.getRequestID(), "requestStatus", "string", "Awaiting Payment");
                        finish();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        rateDriverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get selected driver's ID
                driver = null;
                try {
                    //retrieving rider's information from elasticsearch
                    UserESGetController.GetUserTask getUser = new UserESGetController.GetUserTask();
                    driver = getUser.execute(driverSelected).get();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                if (driver != null){
                    driverID = driver.getUserID();
                }else{
                    Toast.makeText(RequestDetailsActivity.this,"Error retrieving selected driver!",Toast.LENGTH_LONG);
                }

                //code from http://stackoverflow.com/questions/4671428/how-can-i-add-a-third-button-to-an-android-alert-dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(RequestDetailsActivity.this);
                builder.setTitle("Rate Driver");

                builder.setItems(new CharSequence[]
                                {"1.0", "2.0", "3.0", "4.0", "5.0"},
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // The 'which' argument contains the index position
                                // of the selected item
                                switch (which) {
                                    case 0:
                                        newRating = 1.0;
                                        String newRatingAsString = String.valueOf(newRating);
                                        UserESAddController.AddNewDriverRatingTask addNewDriverRatingTask1 =
                                                new UserESAddController.AddNewDriverRatingTask();
                                        addNewDriverRatingTask1.execute(driverID, newRatingAsString);
                                        Toast.makeText(RequestDetailsActivity.this, "Rated 1.0", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 1:
                                        newRatingAsString = String.valueOf(newRating);
                                        UserESAddController.AddNewDriverRatingTask addNewDriverRatingTask2 =
                                                new UserESAddController.AddNewDriverRatingTask();
                                        addNewDriverRatingTask2.execute(driverID, newRatingAsString);
                                        Toast.makeText(RequestDetailsActivity.this, "Rated 2.0", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 2:
                                        newRating = 3.0;
                                        newRatingAsString = String.valueOf(newRating);
                                        UserESAddController.AddNewDriverRatingTask addNewDriverRatingTask3 =
                                                new UserESAddController.AddNewDriverRatingTask();
                                        addNewDriverRatingTask3.execute(driverID, newRatingAsString);
                                        Toast.makeText(RequestDetailsActivity.this, "Rated 3.0", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 3:
                                        newRating = 4.0;
                                        newRatingAsString = String.valueOf(newRating);
                                        UserESAddController.AddNewDriverRatingTask addNewDriverRatingTask4 =
                                                new UserESAddController.AddNewDriverRatingTask();
                                        addNewDriverRatingTask4.execute(driverID, newRatingAsString);
                                        Toast.makeText(RequestDetailsActivity.this, "Rated 4.0", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 4:
                                        newRating = 5.0;
                                        newRatingAsString = String.valueOf(newRating);
                                        UserESAddController.AddNewDriverRatingTask addNewDriverRatingTask5 =
                                                new UserESAddController.AddNewDriverRatingTask();
                                        addNewDriverRatingTask5.execute(driverID, newRatingAsString);
                                        Toast.makeText(RequestDetailsActivity.this, "Rated 5.0", Toast.LENGTH_SHORT).show();
                                        break;
                                }
                            }
                        });
                builder.create().show();

            }
        });

        showDetails(passedRequest);
        //load map only if cconnected to internet
        if (ConnectivityChecker.isConnected(RequestDetailsActivity.this)){
            mapView = (MapView) this.findViewById(R.id.mapView);
            mapView.onCreate(savedInstanceState);
            mapView.onResume();

            try {
                MapsInitializer.initialize(this.getApplicationContext());
            } catch (Exception e) {
                e.printStackTrace();
            }

            mapView.getMapAsync(this);
        }

    }

    /**
     * upgrades request status to different status
     * @param from current status
     */
    public void upgradeStatus(String from) {
        RequestESSetController.SetPropertyValueTask setPropertyValueTask =
                new RequestESSetController.SetPropertyValueTask();
        if (from.contentEquals("Pending")) {
            setPropertyValueTask.execute(passedRequest.getRequestID(), "requestStatus", "String", "Accepted");
        } else {
            setPropertyValueTask.execute(passedRequest.getRequestID(), "requestStatus", "String", "Pending");
        }
    }

    /**
     * show details in the RequestDetailsActivity
     * @param request the request that is passed in
     */
    public void showDetails(Request request){
        TextView status = (TextView) findViewById(R.id.status);
        TextView estPrice = (TextView) findViewById(R.id.est_price);
        TextView distance = (TextView) findViewById(R.id.distance);
        TextView startLoc = (TextView) findViewById(R.id.start_loc);
        TextView endLoc = (TextView) findViewById(R.id.end_loc);

        //get and display request status
        if (!from.contentEquals("Completed")) {
            status.setText(from);
            if (from.contentEquals("Accepted")){
                status.setBackgroundColor(getResources().getColor(R.color.lightGreen));
            }
            else if (from.contentEquals("Pending")){
                status.setBackgroundColor(getResources().getColor(R.color.lightYellow));            }
            else status.setBackgroundColor(getResources().getColor(R.color.lightRed));
        } else {
            String currentStatus = request.getRequestStatus();
            status.setText(currentStatus);
            if (request.getRequestStatus().contentEquals("Awaiting Payment")) {
                status.setTextColor(0xffff0032);
            } else {
                status.setTextColor(0xffff0065);
            }
        }

        //get and display estimated price
        estPrice.setText(Double.toString(request.getCost()));
        //get and display distance
        distance.setText(df.format(new Double(request.getDistance()))+" km");
        //get and display start & end
        startLoc.setText(request.getSourceAddress().getLocation());
        endLoc.setText(request.getDestinationAddress().getLocation());
    }

    /**
     * sets start and end markers and show the connecting route
     * @param mMap
     */
    @Override
    public void onMapReady(GoogleMap mMap) {

        googleMap = mMap;

        double x1 = passedRequest.getSourceAddress().getLatitude();
        double y1 = passedRequest.getSourceAddress().getLongitude();
        double x2 = passedRequest.getDestinationAddress().getLatitude();
        double y2 = passedRequest.getDestinationAddress().getLongitude();


        LatLng startPoint = new LatLng(x1,y1);
        LatLng endPoint = new LatLng(x2,y2);

        //http://stackoverflow.com/questions/6450449/how-to-set-a-dynamic-zoom-on-google-maps-v3
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(startPoint);
        builder.include(endPoint);
        LatLngBounds bound = builder.build();

        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bound,600,600,5));
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        //LatLng midPoint = calculateMidPoint(x1,y1,x2,y2);
        //googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(midPoint,10));

        Marker start = googleMap.addMarker(new MarkerOptions()
                .position(startPoint)
                .title("Start Point")
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_RED))
        );
        start.setTag(0);
        Marker destination = googleMap.addMarker(new MarkerOptions()
                .position(endPoint)
                .title("End Point")
                .icon(BitmapDescriptorFactory
                    .defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
        );
        destination.setTag(0);

        // http://stackoverflow.com/questions/14444228/android-how-to-draw-route-directions-google-maps-api-v2-from-current-location-t
        GMapV2Direction md = new GMapV2Direction();
        Document doc = md.getDocument(startPoint,endPoint,
                GMapV2Direction.MODE_DRIVING);

        ArrayList<LatLng> directionPoint = md.getDirection(doc);
        PolylineOptions rectLine = new PolylineOptions().width(10).color(
                Color.CYAN);

        for (int i = 0; i < directionPoint.size(); i++) {
            rectLine.add(directionPoint.get(i));
        }

        googleMap.addPolyline((rectLine));



    }

    /**
     * on resume activity
     */
    @Override
    public void onResume() {
        super.onResume();
        if (ConnectivityChecker.isConnected(RequestDetailsActivity.this)){
            mapView.onResume();
        }
    }

    /**
     * on pause activity
     */
    @Override
    public void onPause() {
        super.onPause();
        if (ConnectivityChecker.isConnected(RequestDetailsActivity.this)){
            mapView.onPause();
        }
    }

    /**
     * on destroy activity
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (ConnectivityChecker.isConnected(RequestDetailsActivity.this)){
            mapView.onDestroy();
        }
    }

    /**
     * on low memory activity
     */
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (ConnectivityChecker.isConnected(RequestDetailsActivity.this)){
            mapView.onLowMemory();
        }
    }

}