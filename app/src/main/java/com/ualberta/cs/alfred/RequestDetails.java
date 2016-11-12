package com.ualberta.cs.alfred;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Double2;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

/**
 * Created by Shelley on 2016-11-11.
 */
public class RequestDetails extends AppCompatActivity {

    private GeoPoint startPoint;
    private GeoPoint destinationPoint;
    private GeoPoint midPoint;
    private MapView requestMap;
    private Marker startMarker;
    private Marker endMarker;
    private IMapController requestMapController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_details);

        TextView requestID = (TextView) findViewById(R.id.request_ID);
        TextView estPrice = (TextView) findViewById(R.id.est_price);
        TextView distance = (TextView) findViewById(R.id.distance);
        TextView startLoc = (TextView) findViewById(R.id.start_loc);
        TextView endLoc = (TextView) findViewById(R.id.end_loc);

        //pass in a request
        Intent intent = getIntent();
        Request request = (Request) intent.getSerializableExtra("habit");

        //get and display request ID
        requestID.setText(request.getRequestID());
        //get and display estimated price
        estPrice.setText(Double.toString(request.getCost()));
        //get and display distance
        distance.setText(Double.toString(request.getDistance()));
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
