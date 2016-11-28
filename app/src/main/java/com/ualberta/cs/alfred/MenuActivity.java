package com.ualberta.cs.alfred;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.view.Menu;
import android.widget.EditText;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarBadge;
import com.roughike.bottombar.BottomBarFragment;
import com.ualberta.cs.alfred.fragments.AcceptedFragment;
import com.ualberta.cs.alfred.fragments.HomeFragment;
import com.ualberta.cs.alfred.fragments.ListFragment;
import com.ualberta.cs.alfred.fragments.SettingsFragment;
import com.ualberta.cs.alfred.fragments.UserFragment;

import java.util.GregorianCalendar;


/**
 * sets the bottom bar for app navigation
 *
 *
 */
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
                new BottomBarFragment(ListFragment.newInstance(0), R.drawable.ic_view_list_white_24dp, "List"),
                new BottomBarFragment(UserFragment.newInstance(0), R.drawable.ic_person_white_24dp, "User"),
                new BottomBarFragment(SettingsFragment.newInstance(), R.drawable.ic_history_white_24dp, "History")
        );

        // Define a time value of 5 seconds
        Long alertTime = new GregorianCalendar().getTimeInMillis();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MenuActivity.this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("awaitingPaymentOpen", false);
        editor.commit();
        // Define our intention of executing AlertReceiver
        Intent alertIntent = new Intent(this, AlertReciever.class);

        // Allows you to schedule for your application to do something at a later date
        // even if it is in he background or isn't active
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        PendingIntent alertPendingIntent = PendingIntent.getBroadcast(this, 1, alertIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        // set() schedules an alarm to trigger
        // Trigger for alertIntent to fire in 5 seconds
        // FLAG_UPDATE_CURRENT : Update the Intent if active
        alarmManager.setInexactRepeating(AlarmManager.RTC,
                SystemClock.elapsedRealtime() + 1000*5,
                1000*5, alertPendingIntent);

    }
}
