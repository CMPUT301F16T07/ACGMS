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
import com.google.android.gms.maps.model.LatLng;
import com.ualberta.cs.alfred.Address;
import com.ualberta.cs.alfred.BuildConfig;
import com.ualberta.cs.alfred.MenuActivity;
import com.ualberta.cs.alfred.R;


import java.util.ArrayList;

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
    private Fragment fragment;
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



        fragment = MapFragment.newInstance();
        transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.mapContainer, fragment);
        transaction.commit();

        return view;


    }


    @Override
    public void onClick(View v) {
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
}
