package com.ualberta.cs.alfred.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.ualberta.cs.alfred.BuildConfig;
import com.ualberta.cs.alfred.MenuActivity;
import com.ualberta.cs.alfred.R;
import com.ualberta.cs.alfred.Request;
import com.ualberta.cs.alfred.RequestESGetController;
import com.ualberta.cs.alfred.RequestList;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by carlcastello and shelleytian on 08/11/16.
 */

public class HomeFragment extends Fragment implements View.OnClickListener {

    //private MapView map;
    //private GeoPoint defaultLocation;
    private SharedPreferences preferences;

    //private GeoPoint startPoint;
    //private GeoPoint destinationPoint;
    //private Marker startMarker;
    //private Marker endMarker;
    //private IMapController mapController;

    //private ArrayList<GeoPoint> waypoints = new ArrayList<GeoPoint>();

    public static Integer requestedCount = new Integer(0);
    public static Integer pendingCount= new Integer(0);
    public static Integer acceptedCount= new Integer(0);

    private FragmentTransaction transaction;

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


        // disable the StrictMode policy in onCreate. Needed for routing
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        // This will modify initialize the counts for each list in the home screen
        if (preferences.getString("MODE", null).contentEquals("Driver Mode")) {
            // Modified getDriverRequestList
            // See getDriverRequestList
            requestedCount = getDriverListCount("Request");
            pendingCount = getDriverListCount("Pending");
            acceptedCount = getDriverListCount("Accepted");

        } else {
            // Modified getDriverRequestList
            // See getDriverRequestList
            requestedCount = getRiderListCount("Requested");
            pendingCount = getRiderListCount("Pending");
            acceptedCount = getRiderListCount("Accepted");
        }

        Button requestedButton = (Button) view.findViewById(R.id.button_requested);
        requestedButton.setBackgroundColor(0xfff08080);
        requestedButton.setText("REQUESTED\n"+Integer.toString(requestedCount));

        Button pendingButton = (Button) view.findViewById(R.id.button_pending);
        pendingButton.setBackgroundColor(0xfffffd00);
        pendingButton.setText("PENDING\n"+Integer.toString(pendingCount));

        Button acceptedButton = (Button) view.findViewById(R.id.button_accepted);
        acceptedButton.setBackgroundColor(0xff90ee90);
        acceptedButton.setText("ACCEPTED\n"+Integer.toString(acceptedCount));

        Button requestButton = (Button) view.findViewById(R.id.request_button);


        pendingButton.setOnClickListener(this);
        requestedButton.setOnClickListener(this);
        acceptedButton.setOnClickListener(this);
        requestButton.setOnClickListener(this);

        //important! set your user agent to prevent getting banned from the osm servers
        org.osmdroid.tileprovider.constants.OpenStreetMapTileProviderConstants.setUserAgentValue(BuildConfig.APPLICATION_ID);

        MapView map = (MapView) view.findViewById(R.id.map_view);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);

        GeoPoint defaultLocation = new GeoPoint(53.5444,-113.4909);
//        GeoPoint startPoint = new GeoPoint(53.5181319516847, -113.49131921322021);
//        GeoPoint destinationPoint = new GeoPoint(53.52798002388982, -113.52341989071044);

        IMapController mapController;
        mapController = map.getController();
        mapController.setZoom(11);
        mapController.setCenter(defaultLocation);

//        startMarker = new Marker(map);
//        startMarker.setPosition(startPoint);
//        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
//        startMarker.setSnippet("Start");
//        map.getOverlays().add(startMarker);

//        endMarker = new Marker(map);
//        endMarker.setPosition(destinationPoint);
//        endMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
//        endMarker.setSnippet("End");
//        map.getOverlays().add(endMarker);

//        RoadManager roadManager = new OSRMRoadManager(getActivity());
        //Set-up your start and end points:
        //ArrayList<GeoPoint> waypoints = new ArrayList<GeoPoint>();
//        waypoints.add(startPoint);
//        waypoints.add(destinationPoint);
        //retreive the road between those points
//        Road road = roadManager.getRoad(waypoints);
        //build a Polyline with the route shape:
//        Polyline roadOverlay = RoadManager.buildRoadOverlay(road);
        //Add this Polyline to the overlays of your map
//        map.getOverlays().add(roadOverlay);


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
                replaceFragmentwithoutStack(fragment);
                break;

            case R.id.button_requested:
                fragment = new ListFragment().newInstance(0);
                MenuActivity.bottomBar.selectTabAtPosition(1,true);
                replaceFragmentwithoutStack(fragment);
                break;

            case R.id.button_accepted:
                fragment = new ListFragment().newInstance(2);
                MenuActivity.bottomBar.selectTabAtPosition(1,true);
                replaceFragmentwithoutStack(fragment);
                break;
            case R.id.request_button:
                fragment = new RequestFragment().newInstance();
                replaceFragmentwithStack(fragment);
                break;
        }

    }

    private void replaceFragmentwithoutStack(Fragment fragment) {
        transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.menu_fragment_container, fragment);
        transaction.commit();
    }

    private void replaceFragmentwithStack(Fragment fragment) {
        transaction = getFragmentManager().beginTransaction();
        transaction.addToBackStack(null);
        transaction.replace(R.id.menu_fragment_container, fragment);
        transaction.commit();
    }


    /**
     * Modified getRiderRequestList
     * This function will take an argument with a type string
     * It'll then compare the argument into an appropriate type and return an appropriate list
     * @param argType
     * @return
     */
    private int getRiderListCount(String argType) {
        RequestESGetController.GetRequestTask getTask = new RequestESGetController.GetRequestTask();
        ArrayList<Request> returnList = null;

        try {
            getTask.execute("riderID", preferences.getString("USERNAME", null));
            returnList = (ArrayList<Request>) new RequestList(getTask.get()).getSpecificRequestList(argType);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return returnList.size();
    }

    /**
     * Modified getDriverRequestList
     * This function will take an argument with a type string
     * It'll then compare the argument into an appropriate type and return an appropriate list
     * @param argType
     * @return
     */
    private int getDriverListCount(String argType) {
        /* The request that should be retrieved are all requests that are currently with a requested status and those that
        are pending that do not include the driver on the bidlist of the request.
         */
        RequestESGetController.GetRequestTask getTask = new RequestESGetController.GetRequestTask();
        ArrayList<Request> returnList = null;

        try {
            if (argType.equals("Request")) {
                getTask.execute("requestStatus", "Requested");
                returnList = new RequestList(getTask.get()).returnArrayList();
            } else if (argType.equals("Pending")) {
                getTask.execute("requestStatus", "Pending");
                returnList = new RequestList(getTask.get()).getWithDriver(preferences.getString("USERNAME", null));

            } else if (argType.equals("Accepted")) {
                getTask.execute("requestStatus", "Accepted");
                returnList = new RequestList(getTask.get()).getWithDriver(preferences.getString("USERNAME", null));

            } else {
                returnList = new ArrayList<Request>();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return returnList.size();
    }
}
