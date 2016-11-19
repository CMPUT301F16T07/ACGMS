package com.ualberta.cs.alfred;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.ualberta.cs.alfred.fragments.ListFragment;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

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

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(RequestDetailsActivity.this);
        final String mode = preferences.getString("MODE", "None");
        Intent intent = getIntent();
        final String from = intent.getExtras().getString("FROM", "None");
        String next = "None";

        Button confirmButton = (Button) findViewById(R.id.accept_pending_button);
        final Button cancelButton = (Button) findViewById(R.id.cancel_request_button);
        biddingDriversListView = (ListView) findViewById(R.id.biddingDriversListView);

        if (from.contentEquals("Requested")) {
            // only the driver will be able to confirm anything in the first stage
            // the rider will only be able to cancel the request
            if (mode.contentEquals("Rider Mode")) {
                confirmButton.setVisibility(View.GONE);
            } else {
                cancelButton.setVisibility(View.GONE);
            }
            next = "Pending";
        } else if (from.contentEquals("Pending")) {
            // the cancel button needs to remain for both the driver and rider
            // since they both should be able to cancel a request or bid
            // but only the rider should be able to confirm a bid at this time
            if (mode.contentEquals("Driver Mode")) {
                confirmButton.setVisibility(View.GONE);
            }
            next = "Accepted";
        } else if (from.contentEquals("Accepted")) {
            // finally the trip is in progress and now the all both can do is
            // cancel the trip
            confirmButton.setVisibility(View.GONE);
        }

        final Request r = (Request) intent.getSerializableExtra("passedRequest");

        if (mode.contentEquals("Rider Mode") &&
                (r.getRequestStatus().contentEquals("Pending") ||
                        r.getRequestStatus().contentEquals("Accepted"))) {
            // TODO: Implement a list of possible drivers for a pending request only for the rider to choose from
            ArrayList<String> fakeDrivers = new ArrayList<>();
            fakeDrivers.add("BILL");
            fakeDrivers.add("BOB");
//            biddingDriversAdapter = new ArrayAdapter<>(RequestDetails.this, R.layout.custom_row, r.getBiddingDrivers());
            // This will show all the possible drivers but we still need to be able to select a driver, and be able to view
            // his profile
            biddingDriversAdapter = new ArrayAdapter<>(RequestDetailsActivity.this, R.layout.custom_row, fakeDrivers);
            biddingDriversListView.setAdapter(biddingDriversAdapter);
        }

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestESSetController.SetPropertyValueTask setPropertyValueTask =
                        new RequestESSetController.SetPropertyValueTask();
                if (from.contentEquals("Pending")) {
                    setPropertyValueTask.execute(r.getRequestID(), "requestStatus", "String", "Accepted");
                } else {
                    setPropertyValueTask.execute(r.getRequestID(), "requestStatus", "String", "Pending");
                }
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
                    RequestESDeleteController.DeleteItemFromListTask deleteItemFromListTask =
                            new RequestESDeleteController.DeleteItemFromListTask();
                    deleteItemFromListTask.execute(r.getRequestID(), "driverIDList", "String", preferences.getString("USERNAME", null));
                }
                finish();
            }
        });
        showDetails(r);
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

        //get coordinates of start and end
        GeoPoint startPoint = new GeoPoint(request.getSourceAddress().getLatitude(),
                request.getSourceAddress().getLongitude());
        GeoPoint destinationPoint = new GeoPoint(request.getDestinationAddress().getLatitude(),
                request.getDestinationAddress().getLongitude());

        //create map
        //important! set your user agent to prevent getting banned from the osm servers
        org.osmdroid.tileprovider.constants.OpenStreetMapTileProviderConstants.setUserAgentValue(BuildConfig.APPLICATION_ID);
        MapView requestMap = (MapView) findViewById(R.id.request_map);
        requestMap.setTileSource(TileSourceFactory.MAPNIK);
        requestMap.setBuiltInZoomControls(true);
        requestMap.setMultiTouchControls(true);
        IMapController requestMapController = requestMap.getController();
        requestMapController.setZoom(11);

        //add markers on map
        double latMiddle = (startPoint.getLatitude()+destinationPoint.getLatitude())/2;
        double lonMiddle = (startPoint.getLongitude()+destinationPoint.getLongitude())/2;
        GeoPoint midPoint = new GeoPoint(latMiddle,lonMiddle);
        requestMapController.setCenter(midPoint);

        Marker startMarker = new Marker(requestMap);
        startMarker.setPosition(startPoint);
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        startMarker.setSnippet("Start");
        requestMap.getOverlays().add(startMarker);

        Marker endMarker = new Marker(requestMap);
        endMarker.setPosition(destinationPoint);
        endMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        endMarker.setSnippet("End");
        requestMap.getOverlays().add(endMarker);

        // Route Overlay

        RoadManager roadManager = new OSRMRoadManager(this);
        //Set-up your start and end points:
        ArrayList<GeoPoint> waypoints = new ArrayList<GeoPoint>();
        waypoints.add(startPoint);
        waypoints.add(destinationPoint);
        //retreive the road between those points
        Road road = roadManager.getRoad(waypoints);
        //build a Polyline with the route shape:
        Polyline roadOverlay = RoadManager.buildRoadOverlay(road);
        //Add this Polyline to the overlays of your map
        requestMap.getOverlays().add(roadOverlay);

    }
}