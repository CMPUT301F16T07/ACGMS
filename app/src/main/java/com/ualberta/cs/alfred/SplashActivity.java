package com.ualberta.cs.alfred;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * The type Splash activity.
 */
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent;

        UserCacheController userCacheController = new UserCacheController(this);
        UserCache userCache = userCacheController.userLoggedIn();

        /** if the userCache is empty (null), then go to the login screen where the user
         * can either login with there username or sign up for a new account
         */
        if (userCache == null) {
            intent = new Intent(this, MainActivity.class);
        } else {
            intent = new Intent(this, MainScreenActivity.class);
        }

        startActivity(intent);
        finish();
    }
}