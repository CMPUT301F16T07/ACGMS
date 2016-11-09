package com.ualberta.cs.alfred.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ualberta.cs.alfred.R;


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
    protected void onResume(){
        super.onResume();

        // This is where you check all input





        Button login_button = (Button) findViewById(R.id.main_button);
        login_button.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        // Launch MenuActivity where the buttom navbar is located.
                        Intent intent= new Intent(MainActivity.this, MenuActivity.class);
                        startActivity(intent);
                    }
                }
        );
    }
}
