package com.ualberta.cs.alfred.fragments;

import android.content.SharedPreferences;
import android.graphics.Color;
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
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ualberta.cs.alfred.MenuActivity;
import com.ualberta.cs.alfred.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by carlcastello and shelleytian on 08/11/16.
 */

public class HomeFragment extends Fragment implements View.OnClickListener, OnMapReadyCallback {

    private MapView mapView;
    private GoogleMap googleMap;

    public HomeFragment() {
        System.out.println();
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

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        // This will modify initialize the counts for each list in the home screen
        RequestFragmentsListController rFLC = new RequestFragmentsListController();
        rFLC.updateCounts(preferences.getString("MODE", null), getContext());

        String userMode = preferences.getString("MODE", null);

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

        if (userMode.contentEquals("Rider Mode")) {
            requestButton.setOnClickListener(this);
        } else {
            requestButton.clearFocus();
            requestButton.setText("Driver Mode");
            requestButton.setBackgroundColor(Color.WHITE);
        }


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
                fragment = RequestFragment.newInstance();
                replaceFragmentwithStack(fragment);
                break;
        }

    }


    private void replaceFragmentwithoutStack(Fragment fragment) {
        FragmentTransaction transaction;
        transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.menu_fragment_container, fragment);
        transaction.commit();
    }

    private void replaceFragmentwithStack(Fragment fragment) {
        FragmentTransaction transaction;
        transaction = getFragmentManager().beginTransaction();
        transaction.addToBackStack(null);
        transaction.replace(R.id.menu_fragment_container, fragment);
        transaction.commit();
    }

    @Override
    public void onMapReady(GoogleMap mMap) {
        googleMap = mMap;
        LatLng defaultLocation = new LatLng(53.5444,-113.4904);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation,10));


        LatLng house = new LatLng(0,0);

        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        // List of points returned by the address
        String start = "2369 29a Ave NW Edmonton";
        List<android.location.Address> startCoordinates;
        try {
            startCoordinates = geocoder.getFromLocationName(start,1);

            int startCoordinatesSize = startCoordinates.size();

            // Check if both list are empty
            if (startCoordinatesSize > 0) {
                // get the coordinates of the first results for both address
                double x1 = startCoordinates.get(0).getLatitude();
                double y1 = startCoordinates.get(0).getLongitude();
                house = new LatLng(x1,y1);

            } else {
                // Error messages
                String errorMessage = "Unable to find start and destination Address";
                if (startCoordinatesSize == 0) {
                    errorMessage = "Unable to find the start address";
                }
                Toast.makeText(getActivity(),errorMessage,Toast.LENGTH_SHORT).show();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        Marker mhouse = googleMap.addMarker(new MarkerOptions()
                .position(house)
        );
        //mhouse.setTag(0);

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
