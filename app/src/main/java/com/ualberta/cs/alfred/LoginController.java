package com.ualberta.cs.alfred;

import android.os.AsyncTask;
import android.util.Log;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import java.util.concurrent.ExecutionException;

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
        BuildClient bC = new BuildClient();
        this.client = bC.getClient();
        this.userName = userName;
        this.requestedStatus = driverRider;
    }

    public Boolean checkRider() throws ExecutionException, InterruptedException {
        UserElasticSearchController.GetRider retrievedRider = new UserElasticSearchController.GetRider();
        Rider rider = retrievedRider.execute(this.userName).get();
        if (rider != null && rider.getUserName().contentEquals(this.userName)) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public Boolean checkDriverInfo() throws ExecutionException, InterruptedException {
        UserElasticSearchController.GetDriverInfo retrievedDriverInfo = new UserElasticSearchController.GetDriverInfo();
        DriverInfo driverInfo = (DriverInfo) retrievedDriverInfo.execute(this.userName).get();
        if (driverInfo.getUserName() != null && driverInfo.getUserName().contentEquals(this.userName)) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}
