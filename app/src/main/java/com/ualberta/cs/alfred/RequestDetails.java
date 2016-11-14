package com.ualberta.cs.alfred;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.renderscript.Double2;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Shelley on 2016-11-11.
 */
public class RequestDetails extends AppCompatActivity {

    private DecimalFormat df = new DecimalFormat("0.00");
    private GeoPoint startPoint;
    private GeoPoint destinationPoint;
    private GeoPoint midPoint;
    private MapView requestMap;
    private Marker startMarker;
    private Marker endMarker;
    private IMapController requestMapController;

    private Button cancelButton;
    private ListView biddingDriversListView;
    private ArrayAdapter<String> biddingDriversAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_details);

        cancelButton = (Button) findViewById(R.id.cancel_request_button);
        biddingDriversListView = (ListView) findViewById(R.id.biddingDriversListView);

        //pass in a request (need to uncomment mock request in MainActivity to test this)
        Intent intent = getIntent();
        final Request r = (Request) intent.getSerializableExtra("passedRequest");

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (preferences.getString("MODE", null).contentEquals("Rider Mode") &&
                (r.getRequestStatus().contentEquals("Pending") || r.getRequestStatus().contentEquals("Accepted"))) {
            ArrayList<String> fakeDrivers = new ArrayList<>();
            fakeDrivers.add("BILL");
            fakeDrivers.add("BOB");
//            biddingDriversAdapter = new ArrayAdapter<>(RequestDetails.this, R.layout.custom_row, r.getBiddingDrivers());
            biddingDriversAdapter = new ArrayAdapter<>(RequestDetails.this, R.layout.custom_row, fakeDrivers);
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
                RequestElasticSearchController.DeleteRequestTask deleteRequestTask =
                        new RequestElasticSearchController.DeleteRequestTask();
                deleteRequestTask.execute(r.getRequestID());
                try {
                    TimeUnit.MILLISECONDS.sleep(250);
                } catch (InterruptedException e) {
                    e.printStackTrace();
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
        startPoint = new GeoPoint(request.getSourceAddress().getLatitude(),
                request.getSourceAddress().getLongitude());
        destinationPoint = new GeoPoint(request.getDestinationAddress().getLatitude(),
                request.getDestinationAddress().getLongitude());

        //create map
        //important! set your user agent to prevent getting banned from the osm servers
        org.osmdroid.tileprovider.constants.OpenStreetMapTileProviderConstants.setUserAgentValue(BuildConfig.APPLICATION_ID);
        requestMap = (MapView) findViewById(R.id.request_map);
        requestMap.setTileSource(TileSourceFactory.MAPNIK);
        requestMap.setBuiltInZoomControls(true);
        requestMap.setMultiTouchControls(true);
        requestMapController = requestMap.getController();
        requestMapController.setZoom(8);

        //add markers on map
        double latMiddle = (startPoint.getLatitude()+destinationPoint.getLatitude())/2;
        double lonMiddle = (startPoint.getLongitude()+destinationPoint.getLongitude())/2;
        midPoint = new GeoPoint(latMiddle,lonMiddle);
        requestMapController.setCenter(midPoint);

        startMarker = new Marker(requestMap);
        startMarker.setPosition(startPoint);
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        startMarker.setSnippet("Start");
        requestMap.getOverlays().add(startMarker);

        endMarker = new Marker(requestMap);
        endMarker.setPosition(destinationPoint);
        endMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        endMarker.setSnippet("End");
        requestMap.getOverlays().add(endMarker);
    }
}