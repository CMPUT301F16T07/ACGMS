package com.ualberta.cs.alfred;

//import android.app.Fragment;
//import android.app.FragmentTransaction;
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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.ualberta.cs.alfred.fragments.UserViewFragment;

import org.w3c.dom.Document;

import java.text.DecimalFormat;
import java.util.ArrayList;


/**
 * activity for the request details
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

        // which list the request will go to next
        String next = "None";

        Button rideCompleteButton = (Button) findViewById(R.id.ride_complete_button);
        rideCompleteButton.setVisibility(View.GONE);
        Button confirmButton = (Button) findViewById(R.id.accept_pending_button);
        TableLayout tableLayout = (TableLayout) findViewById(R.id.tableView);
        final Button cancelButton = (Button) findViewById(R.id.cancel_request_button);
        biddingDriversListView = (ListView) findViewById(R.id.biddingDriversListView);

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
        } else if (from.contentEquals("Accepted")) {
            // finally the trip is in progress and now the all both can do is
            // cancel the trip
            confirmButton.setVisibility(View.GONE);
            tableLayout.setColumnStretchable(0,true);
            tableLayout.setColumnCollapsed(1,true);
            if (mode.contentEquals("Rider Mode")) {
                rideCompleteButton.setVisibility(View.VISIBLE);
            }
        }

        passedRequest = (Request) intent.getSerializableExtra("passedRequest");

        if (mode.contentEquals("Rider Mode") &&
                (passedRequest.getRequestStatus().contentEquals("Pending") ||
                        passedRequest.getRequestStatus().contentEquals("Accepted"))) {
            ArrayList<String> driverArray = passedRequest.getDriverIDList();
            biddingDriversAdapter = new ArrayAdapter<>(RequestDetailsActivity.this, R.layout.custom_row, driverArray);
            biddingDriversListView.setAdapter(biddingDriversAdapter);
            if (driverArray.size() > 0) {
                driverSelected = driverArray.get(0);
            } else {
                driverSelected = "Error";
            }
        }

        biddingDriversListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RequestDetailsActivity.this);
                final String possibleDriver = (String) biddingDriversListView.getItemAtPosition(position);
                if (!driverSelected.contentEquals(possibleDriver)) {
                    builder.setTitle("Change selected driver?");
                    builder.setCancelable(Boolean.TRUE);
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            driverSelected = possibleDriver;
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
                        // Todo open the driver profile
                        // pass username to fragment???
                        Fragment fragment = UserViewFragment.newInstance(1,possibleDriver);
                        MenuActivity.bottomBar.selectTabAtPosition(1,true);
                        replaceFragmentwithStack(fragment);

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upgradeStatus(from);
                String userID = preferences.getString("USERID", null);
                if (mode.contentEquals("Driver Mode") && userID != null && !passedRequest.getDriverIDList().contains(userID)) {
                    RequestESAddController.AddItemToListTask addItemToListTask =
                            new RequestESAddController.AddItemToListTask();
                    addItemToListTask.execute(passedRequest.getRequestID(), "driverIDList", preferences.getString("USERID", null));
                }
                finish();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RequestDetailsActivity.this);
                builder.setCancelable(Boolean.TRUE);
                if (mode.contentEquals("Rider Mode")) {
                    if (!from.contentEquals("Requested")) {
                        builder.setTitle("Delete or Downgrade the selected request? " +
                                "(Downgrading the request will clear the list of bidding drivers.)");
                    } else {
                        builder.setTitle("Delete the selected request?");
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
                                deleteItemFromListTask.execute(passedRequest.getRequestID(), "driverIDList", "String", preferences.getString("USERNAME", null));
                            } else {
                                //TODO: Clear the driverlist
                            }
                            if ((passedRequest.getDriverIDList().size() - 1) <= 0) {
                                RequestESSetController.SetPropertyValueTask setPropertyValueTask =
                                        new RequestESSetController.SetPropertyValueTask();
                                if (from.contentEquals("Pending")) {
                                    setPropertyValueTask.execute(passedRequest.getRequestID(), "requestStatus", "String", "Requested");
                                } else {
                                    setPropertyValueTask.execute(passedRequest.getRequestID(), "requestStatus", "String", "Pending");
                                }
                                finish();
                            }
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
                RequestESSetController.SetPropertyValueTask setPropertyValueTask =
                        new RequestESSetController.SetPropertyValueTask();
                setPropertyValueTask.execute(passedRequest.getRequestID(), "driverID", "String", driverSelected);
                setPropertyValueTask.execute(passedRequest.getRequestID(), "requestStatus", "String", "Completed");
                finish();
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

    public void upgradeStatus(String from) {
        RequestESSetController.SetPropertyValueTask setPropertyValueTask =
                new RequestESSetController.SetPropertyValueTask();
        if (from.contentEquals("Pending")) {
            setPropertyValueTask.execute(passedRequest.getRequestID(), "requestStatus", "String", "Accepted");
        } else {
            setPropertyValueTask.execute(passedRequest.getRequestID(), "requestStatus", "String", "Pending");
        }
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

    private void replaceFragmentwithStack(Fragment fragment) {
        FragmentTransaction transaction;
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.addToBackStack(null);
        transaction.replace(R.id.user_container, fragment); //in request details
        transaction.commit();
    }


    @Override
    public void onMapReady(GoogleMap mMap) {
        if (ConnectivityChecker.isConnected(RequestDetailsActivity.this)){
            googleMap = mMap;
            //googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation,10));

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

            Polyline polylin = googleMap.addPolyline(rectLine);
        }



    }

    @Override
    public void onResume() {
        super.onResume();
        if (ConnectivityChecker.isConnected(RequestDetailsActivity.this)){
            mapView.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (ConnectivityChecker.isConnected(RequestDetailsActivity.this)){
            mapView.onPause();
        }    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (ConnectivityChecker.isConnected(RequestDetailsActivity.this)){
            mapView.onDestroy();
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (ConnectivityChecker.isConnected(RequestDetailsActivity.this)){
            mapView.onLowMemory();
        }    }
}