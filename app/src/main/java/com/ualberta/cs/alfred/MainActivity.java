package com.ualberta.cs.alfred;


import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.Serializable;

/**
 *
 * MainActivity Class
 * This is where the app starts
 * and it prompts user to log-in
 *
 * Created by carlcastello on 08/11/16.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // This is where you check all input


        Button login_button = (Button) findViewById(R.id.main_button);
        login_button.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        // Launch MenuActivity where the bottom navbar is located.
                        Intent intent = new Intent(MainActivity.this, RequestDetails.class);

                        //Mock request for testing RequestDetails class
                       /* Address start = new Address("loc1",53.5,-113.1);
                        Address end = new Address("loc2", 53.0, -113.0);
                        Request r = new Request("Accepted",start, end, 500.00, 23.09, "rider124");
                        intent.putExtra("passedRequest",r);*/

                        startActivity(intent);
                    }
                }
        );


    }
}
