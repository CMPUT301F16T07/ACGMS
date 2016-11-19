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

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.ualberta.cs.alfred.Address;
import com.ualberta.cs.alfred.BuildConfig;
import com.ualberta.cs.alfred.MenuActivity;
import com.ualberta.cs.alfred.R;


import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by carlcastello and shelleytian on 08/11/16.
 */

public class HomeFragment extends Fragment implements View.OnClickListener, OnMapReadyCallback {

    //private MapView map;
    //private GeoPoint defaultLocation;
    private SharedPreferences preferences;


    private Fragment fragment;
    private FragmentTransaction transaction;

    private MapView mapView;
    private GoogleMap googleMap;
    private LatLng defaultLocation = new LatLng(53.5444,-113.4904);

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
        RequestFragmentsListController rFLC = new RequestFragmentsListController();
        rFLC.updateCounts(preferences.getString("MODE", null), getContext());

        Button requestedButton = (Button) view.findViewById(R.id.button_requested);
        requestedButton.setBackgroundColor(0xfff08080);
        requestedButton.setText("Requested\n"+preferences.getString("Requested", "Error"));

        Button pendingButton = (Button) view.findViewById(R.id.button_pending);
        pendingButton.setBackgroundColor(0xfffffd00);
        pendingButton.setText("Pending\n"+preferences.getString("Pending", "Error"));

        Button acceptedButton = (Button) view.findViewById(R.id.button_accepted);
        acceptedButton.setBackgroundColor(0xff90ee90);
        acceptedButton.setText("Accepted\n"+preferences.getString("Accepted", "Error"));

        Button requestButton = (Button) view.findViewById(R.id.request_button);


        pendingButton.setOnClickListener(this);
        requestedButton.setOnClickListener(this);
        acceptedButton.setOnClickListener(this);
        requestButton.setOnClickListener(this);


        mapView = (MapView) view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mapView.getMapAsync(this);


//        fragment = MapFragment.newInstance();
//        transaction = getFragmentManager().beginTransaction();
//        transaction.replace(R.id.mapContainer, fragment);
//        transaction.commit();

        return view;


    }


    @Override
    public void onClick(View v) {
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
                fragment = RequestFragment.newInstance();
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

    @Override
    public void onMapReady(GoogleMap mMap) {
        googleMap = mMap;
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation,10));
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
