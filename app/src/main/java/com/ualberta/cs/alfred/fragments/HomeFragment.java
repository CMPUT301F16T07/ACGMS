package com.ualberta.cs.alfred.fragments;

import android.content.SharedPreferences;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.ualberta.cs.alfred.GeoCoder;
import com.ualberta.cs.alfred.MenuActivity;
import com.ualberta.cs.alfred.R;

import java.util.Locale;


/**
 * Accepted Fragment is a fragment class where all accepted listed is found.
 *
 * @author carlcastello
 * @author averytan
 * @author mmcote
 * @author shltien
 */
public class HomeFragment extends Fragment implements View.OnClickListener, OnMapReadyCallback {

    private String userMode;
    private MapView mapView;
    private GoogleMap googleMap;
    private static HomeFragment homeFragment = new HomeFragment();

    /**
     * Instantiates a new Home fragment.
     */
    public HomeFragment() {
        System.out.println();
    }

    /**
     * New instance home fragment.
     *
     * @return the home fragment
     */
    public static HomeFragment newInstance() {

        return homeFragment;
    }

    /**
     * All view functionalities are initialize.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);

        // Initialize a singleton of geoCoder
        GeoCoder geoCoder = GeoCoder.getInstance();
        geoCoder.geoSetArguments(getContext());

        // disable the StrictMode policy in onCreate. Needed for routing
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        // This will modify initialize the counts for each list in the home screen
        RequestFragmentsListController rFLC = new RequestFragmentsListController();
        rFLC.updateCounts(preferences.getString("MODE", null), getContext());

        userMode = preferences.getString("MODE", null);

        Button requestedButton = (Button) view.findViewById(R.id.button_requested);
        requestedButton.setText("Requested\n");

        Button pendingButton = (Button) view.findViewById(R.id.button_pending);
        pendingButton.setText("Pending\n");

        Button acceptedButton = (Button) view.findViewById(R.id.button_accepted);
        acceptedButton.setText("Accepted\n");

        Button requestButton = (Button) view.findViewById(R.id.request_button);

        pendingButton.setOnClickListener(this);
        requestedButton.setOnClickListener(this);
        acceptedButton.setOnClickListener(this);


        // Google map
        mapView = (MapView) view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();

        if (userMode.contentEquals("Driver Mode")) {
            requestButton.setText("Start Driving");
        }
        requestButton.setOnClickListener(this);



        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mapView.getMapAsync(this);
        return view;


    }

    /**
     * Buttons listener for buttons in the home fragment.
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        Fragment fragment;
        switch (v.getId()) {
            case R.id.button_pending:
                fragment = ListFragment.newInstance(1);
                MenuActivity.bottomBar.selectTabAtPosition(1,true);
                replaceFragmentwithoutStack(fragment);
                break;

            case R.id.button_requested:
                fragment = ListFragment.newInstance(0);
                MenuActivity.bottomBar.selectTabAtPosition(1,true);
                replaceFragmentwithoutStack(fragment);
                break;

            case R.id.button_accepted:
                fragment = ListFragment.newInstance(2);
                MenuActivity.bottomBar.selectTabAtPosition(1,true);
                replaceFragmentwithoutStack(fragment);
                break;
            case R.id.request_button:
                if (userMode.contentEquals("Rider Mode")) {
                    fragment = RequestFragment.newInstance();
                    replaceFragmentwithStack(fragment);
                } else {
                    fragment = ListFragment.newInstance(0);
                    MenuActivity.bottomBar.selectTabAtPosition(1,true);
                    replaceFragmentwithoutStack(fragment);
                }
                break;
        }

    }


    /**
     * Fragment replacing function without back stack
     * @param fragment
     */
    private void replaceFragmentwithoutStack(Fragment fragment) {
        FragmentTransaction transaction;
        transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.menu_fragment_container, fragment);
        transaction.commit();
    }

    /**
     * Fragment replacing function
     * @param fragment
     */
    private void replaceFragmentwithStack(Fragment fragment) {
        FragmentTransaction transaction;
        transaction = getFragmentManager().beginTransaction();
        transaction.addToBackStack(null);
        transaction.replace(R.id.menu_fragment_container, fragment);
        transaction.commit();
    }

    /**
     * this is where our map is created and initialize.
     * @param mMap
     */
    @Override
    public void onMapReady(GoogleMap mMap) {
        googleMap = mMap;
        LatLng defaultLocation = new LatLng(53.5444,-113.4904);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation,10));
        googleMap.getUiSettings().setZoomControlsEnabled(true);


        LatLng house = new LatLng(0,0);

        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        // List of points returned by the address
    }

    /**
     * onResume functions that manages the map
     */
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * onPause functions that manages the map
     */
    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * onDestroy functions that manages the map
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    /**
     * onLowMemory functions that manages the map
     */
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
