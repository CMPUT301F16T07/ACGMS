package com.ualberta.cs.alfred;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarBadge;
import com.roughike.bottombar.BottomBarFragment;
import com.ualberta.cs.alfred.fragments.HomeFragment;
import com.ualberta.cs.alfred.fragments.ListFragment;
import com.ualberta.cs.alfred.fragments.SettingsFragment;
import com.ualberta.cs.alfred.fragments.UserFragment;

public class MenuActivity extends AppCompatActivity {
    public static BottomBar bottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Bundle extras = getIntent().getExtras();

        bottomBar = BottomBar.attach(this, savedInstanceState);

        //set appropriate mappings between buttons on the list and fragment views
        bottomBar.setFragmentItems(getSupportFragmentManager(), R.id.menu_fragment_container,
                new BottomBarFragment(HomeFragment.newInstance(), R.drawable.ic_home_white_24dp, "Home"),
                new BottomBarFragment(ListFragment.newInstance(3), R.drawable.ic_view_list_white_24dp, "List"),
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
        BottomBarBadge unreadMessages = bottomBar.makeBadgeForTabAt(1, "#E91E63", 4);

        // Control the badge's visibility
        unreadMessages.show();
        //unreadMessages.hide();

        // Change the displayed count for this badge.
        //unreadMessages.setCount(4);

        // Change the show / hide animation duration.
        unreadMessages.setAnimationDuration(200);
    }

}
