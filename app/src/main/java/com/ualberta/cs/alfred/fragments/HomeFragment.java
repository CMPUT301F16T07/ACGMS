package com.ualberta.cs.alfred.fragments;

import android.app.Activity;
import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ualberta.cs.alfred.BuildConfig;
import com.ualberta.cs.alfred.R;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.BoundingBoxE6;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

/**
 * Created by carlcastello and shelleytian on 08/11/16.
 * This is the home activity where the rider can see the summary of their requests,
 * see a map, and make requests.
 * @return the view
 */

public class HomeFragment extends Fragment {

    private MapView map;
    private GeoPoint startPoint;
    private GeoPoint destinationPoint;
    private GeoPoint midPoint;
    private Marker startMarker;
    private Marker endMarker;
    private IMapController mapController;


    public HomeFragment() {
    }

    public static HomeFragment newInstance() {
        Bundle args = new Bundle();
        HomeFragment homeFragment = new HomeFragment();
        homeFragment.setArguments(args);
        return homeFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);

        //important! set your user agent to prevent getting banned from the osm servers
        org.osmdroid.tileprovider.constants.OpenStreetMapTileProviderConstants.setUserAgentValue(BuildConfig.APPLICATION_ID);

        map = (MapView) view.findViewById(R.id.map_view);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);

        startPoint = new GeoPoint(53.0, -113.0); //Somewhere around Edmonton
        destinationPoint = new GeoPoint(54.0, -113.5);
        double latMiddle = (startPoint.getLatitude()+destinationPoint.getLatitude())/2;
        double lonMiddle = (startPoint.getLongitude()+destinationPoint.getLongitude())/2;
        midPoint = new GeoPoint(latMiddle,lonMiddle);

        mapController = map.getController();

        mapController.setCenter(midPoint);
        mapController.setZoom(8);

        //add markers (example)
        startMarker = new Marker(map);
        startMarker.setPosition(startPoint);
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        startMarker.setSnippet("Start");
        map.getOverlays().add(startMarker);

        endMarker = new Marker(map);
        endMarker.setPosition(destinationPoint);
        endMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        endMarker.setSnippet("End");
        map.getOverlays().add(endMarker);

        //save & display changes
        map.invalidate();



        return view;
    }
}
