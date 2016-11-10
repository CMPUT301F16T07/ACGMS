package com.ualberta.cs.alfred;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarFragment;
import com.roughike.bottombar.OnTabSelectedListener;
import com.ualberta.cs.alfred.fragments.HomeFragment;
// import com.ualberta.cs.alfred.R;
import com.ualberta.cs.alfred.fragments.ListFragment;
import com.ualberta.cs.alfred.fragments.SettingsFragment;
import com.ualberta.cs.alfred.fragments.UserFragment;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

public class MenuActivity extends AppCompatActivity {
    private BottomBar bottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        bottomBar = BottomBar.attach(this, savedInstanceState);

        bottomBar.setFragmentItems(getSupportFragmentManager(), R.id.menu_fragment_container,
                new BottomBarFragment(HomeFragment.newInstance(), R.drawable.ic_home_white_24dp, "Home"),
                new BottomBarFragment(ListFragment.newInstance(), R.drawable.ic_view_list_white_24dp, "List"),
                new BottomBarFragment(UserFragment.newInstance(), R.drawable.ic_person_white_24dp, "User"),
                new BottomBarFragment(SettingsFragment.newInstance(), R.drawable.ic_settings_white_24dp, "Settings")
        );

//        bottomBar.mapColorForTab(0, "#3B494C");
//        bottomBar.mapColorForTab(1, "#00796B");
//        bottomBar.mapColorForTab(2, "#7B1FA2");
//        bottomBar.mapColorForTab(3, "#FF5252");
//
//        bottomBar.setOnItemSelectedListener(new OnTabSelectedListener() {
//            @Override
//            public void onItemSelected(int position) {
//                switch (position) {
//                    case 0:
//                        // Item 1 Selected
//                }
//            }
//        });

    }
}
