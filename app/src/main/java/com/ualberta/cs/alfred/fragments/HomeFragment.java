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
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

/**
 * Created by carlcastello and shelleytian on 08/11/16.
 */

public class HomeFragment extends Fragment implements View.OnClickListener {

    MapView map;
    GeoPoint startPoint;
    GeoPoint destinationPoint;
    Marker startMarker;
    Marker endMarker;
    IMapController mapController;

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
        startPoint = new GeoPoint(53.5181319516847, -113.49131921322021);
        destinationPoint = new GeoPoint(53.52798002388982, -113.52341989071044);

        mapController = map.getController();
        mapController.setZoom(16);
        mapController.setCenter(startPoint);

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
