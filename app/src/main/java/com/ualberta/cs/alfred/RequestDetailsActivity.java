package com.ualberta.cs.alfred;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.ualberta.cs.alfred.fragments.ListFragment;


import org.w3c.dom.Document;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


/**
 * activity for the request details
 */
public class RequestDetailsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;
    private GoogleMap googleMap;
    private LatLng defaultLocation = new LatLng(53.5444,-113.4904);
    private DecimalFormat df = new DecimalFormat("0.00");
    private Request r;
    private ListView biddingDriversListView;
    private ArrayAdapter<String> biddingDriversAdapter;
    private String driverSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_details);

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(RequestDetailsActivity.this);
        final String mode = preferences.getString("MODE", "None");
        final Intent intent = getIntent();
        final String from = intent.getExtras().getString("FROM", "None");
        String next = "None";

        Button confirmButton = (Button) findViewById(R.id.accept_pending_button);
        TableLayout tableLayout = (TableLayout) findViewById(R.id.tableView);
        final Button cancelButton = (Button) findViewById(R.id.cancel_request_button);
        biddingDriversListView = (ListView) findViewById(R.id.biddingDriversListView);

        if (from.contentEquals("Requested")) {
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
        }

        r = (Request) intent.getSerializableExtra("passedRequest");

        if (mode.contentEquals("Rider Mode") &&
                (r.getRequestStatus().contentEquals("Pending") ||
                        r.getRequestStatus().contentEquals("Accepted"))) {
//            biddingDriversAdapter = new ArrayAdapter<>(RequestDetails.this, R.layout.custom_row, r.getBiddingDrivers());
            // This will show all the possible drivers but we still need to be able to select a driver, and be able to view
            // his profile
            ArrayList<String> driverArray = r.getDriverIDList();
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
                RequestESAddController.AddItemToListTask addItemToListTask =
                        new RequestESAddController.AddItemToListTask();
                addItemToListTask.execute(r.getRequestID(), "driverIDList", preferences.getString("USERNAME", null));
                finish();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mode.contentEquals("Rider Mode")) {
                    RequestESDeleteController.DeleteRequestTask deleteRequestTask =
                            new RequestESDeleteController.DeleteRequestTask();
                    deleteRequestTask.execute(r.getRequestID());
                } else {
                    downgradeStatus(from);
                    RequestESDeleteController.DeleteItemFromListTask deleteItemFromListTask = new RequestESDeleteController.DeleteItemFromListTask();
                    deleteItemFromListTask.execute(r.getRequestID(), "driverIDList", "String", preferences.getString("USERNAME", null));
                }
                finish();
            }
        });

        showDetails(r);

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

    public void upgradeStatus(String from) {
        RequestESSetController.SetPropertyValueTask setPropertyValueTask =
                new RequestESSetController.SetPropertyValueTask();
        if (from.contentEquals("Pending")) {
            setPropertyValueTask.execute(r.getRequestID(), "requestStatus", "String", "Accepted");
        } else {
            setPropertyValueTask.execute(r.getRequestID(), "requestStatus", "String", "Pending");
        }
    }

    public void downgradeStatus(String from) {
        RequestESSetController.SetPropertyValueTask setPropertyValueTask =
                new RequestESSetController.SetPropertyValueTask();
        if (from.contentEquals("Pending")) {
            setPropertyValueTask.execute(r.getRequestID(), "requestStatus", "String", "Requested");
        } else {
            setPropertyValueTask.execute(r.getRequestID(), "requestStatus", "String", "Pending");
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

    @Override
    public void onMapReady(GoogleMap mMap) {
        googleMap = mMap;
        //googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation,10));

        double x1 = r.getSourceAddress().getLatitude();
        double y1 = r.getSourceAddress().getLongitude();
        double x2 = r.getDestinationAddress().getLatitude();
        double y2 = r.getDestinationAddress().getLongitude();


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

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}