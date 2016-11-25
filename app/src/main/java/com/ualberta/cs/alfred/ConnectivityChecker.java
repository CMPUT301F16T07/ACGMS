package com.ualberta.cs.alfred;

/**
 * Created by Avery on 11/24/2016.
 */

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


/**
 *
 */

public class ConnectivityChecker {

    // Based on: https://goo.gl/oximGj
    // Author: Android Dev Docs
    // Retrieved on: November 13, 2016
    public static boolean isConnected( Context context ) {
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return  activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

}
