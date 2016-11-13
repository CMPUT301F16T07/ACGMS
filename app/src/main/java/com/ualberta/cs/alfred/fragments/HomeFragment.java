package com.ualberta.cs.alfred.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ualberta.cs.alfred.MenuActivity;
import com.ualberta.cs.alfred.BuildConfig;
import com.ualberta.cs.alfred.R;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polygon;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;

/**
 * Created by carlcastello and shelleytian on 08/11/16.
 */

public class HomeFragment extends Fragment implements View.OnClickListener {

    MapView map;
    GeoPoint defualtLocation;
    GeoPoint startPoint;
    GeoPoint destinationPoint;
    Marker startMarker;
    Marker endMarker;
    IMapController mapController;
    RoadManager roadManager;
    ArrayList<GeoPoint> waypoints = new ArrayList<GeoPoint>();

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


        Button pendingButton = (Button) view.findViewById(R.id.button_pending);
        Button requestedButton = (Button) view.findViewById(R.id.button_requested);
        Button acceptedButton = (Button) view.findViewById(R.id.button_accepted);

        Button requestButton = (Button) view.findViewById(R.id.request_button);

        pendingButton.setOnClickListener(this);
        requestedButton.setOnClickListener(this);
        acceptedButton.setOnClickListener(this);
        requestButton.setOnClickListener(this);

        //important! set your user agent to prevent getting banned from the osm servers
        org.osmdroid.tileprovider.constants.OpenStreetMapTileProviderConstants.setUserAgentValue(BuildConfig.APPLICATION_ID);

        map = (MapView) view.findViewById(R.id.map_view);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);

        // Set the initial view location to edmonton
        defualtLocation = new GeoPoint(53.5444,-113.4909);
        startPoint = new GeoPoint(53.5181319516847,-113.4913112921322021);
        destinationPoint = new GeoPoint(53.4599596,-113.37710959999998);

        // Road manager for routing
        roadManager = new OSRMRoadManager(getActivity());
        // Adding two points for routing
        waypoints.add(startPoint);
        waypoints.add(destinationPoint);
        // Routes between two waypoint
        Road road = roadManager.getRoad(waypoints);
        // A line between two waypoints
        Polyline roadOverlay = RoadManager.buildRoadOverlay(road);
        // Adding overlay to the map
        map.getOverlays().add(roadOverlay);


        mapController = map.getController();
        mapController.setZoom(11);
        mapController.setCenter(defualtLocation);

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


    @Override
    public void onClick(View v) {
        Fragment fragment = null;
        switch (v.getId()) {
            case R.id.button_pending:
                fragment = new ListFragment().newInstance(1);
                MenuActivity.bottomBar.selectTabAtPosition(1,true);
                replaceFragment(fragment);
                break;

            case R.id.button_requested:
                fragment = new ListFragment().newInstance(0);
                MenuActivity.bottomBar.selectTabAtPosition(1,true);
                replaceFragment(fragment);
                break;

            case R.id.button_accepted:
                fragment = new ListFragment().newInstance(2);
                MenuActivity.bottomBar.selectTabAtPosition(1,true);
                replaceFragment(fragment);
                break;
            case R.id.request_button:
                fragment = new RequestFragment().newInstance();
                replaceFragment(fragment);
                break;
        }

    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.menu_fragment_container, fragment);
        transaction.commit();
    }
}
