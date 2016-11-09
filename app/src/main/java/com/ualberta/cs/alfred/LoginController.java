package com.ualberta.cs.alfred;

import android.util.Log;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

/**
 * Created by mmcote on 2016-11-09.
 */

public class LoginController {
    private static JestDroidClient client;
    private String userName;
    private String requestedStatus;

    public LoginController(String userName, String driverRider) {
        this.client = new BuildClient().getClient();
        this.userName = userName;
        this.requestedStatus = driverRider;
    }

    public boolean check() {
        if (requestedStatus.contentEquals("Driver")) {
            return checkDrivers();
        } else {
            return checkRiders();
        }
    }

    public String createQuery() {
        return "{\n" +
                "    \"query\": {\n" +
                "        \"match\" : {\n" +
                "            \"userName\" : \n" +
                "                \"" + this.userName + "\"\n" +
                "            }\n" +
                "    }\n" +
                "}";
    }

    private boolean checkRiders() {
        Rider rider = new Rider();
        Search search = new Search.Builder(createQuery())
                .addIndex("riderlist")
                .addType("rider")
                .build();

        try {
            SearchResult result = client.execute(search);
            if (result.isSucceeded()) {
                rider = result.getSourceAsObject(Rider.class);
                if (rider.getUserName().equals(userName)) {
                    return true;
                }
            } else {
                Log.i("Error", "The search query failed to find the rider that matched.");
                return false;
            }
        } catch (Exception e) {
            Log.i("Error", "Something went wrong when we tried to communicate with the elasticsearch server!");
        }
        return false;
    }

    private boolean checkDrivers() {
        Driver driver = new Driver();
        Search search = new Search.Builder(createQuery())
                .addIndex("driverlist")
                .addType("driver")
                .build();

        try {
            SearchResult result = client.execute(search);
            if (result.isSucceeded()) {
                driver = result.getSourceAsObject(Driver.class);
                if (driver.getUserName().equals(userName)) {
                    return true;
                }
            } else {
                Log.i("Error", "The search query failed to find the driver that matched.");
                return false;
            }
        } catch (Exception e) {
            Log.i("Error", "Something went wrong when we tried to communicate with the elasticsearch server!");
        }
        return false;
    }

}
