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
 * This class is used to check whether a user a user exists and is able to log in
 * under the specified domain which they are requesting.
 *
 * @author mmcote
 * @version 1.0
 */
public class LoginController {
    private static JestDroidClient client;
    private String userName;
    private String requestedStatus;

    /**
     * Instantiates a new Login controller.
     *
     * @param userName    the user name
     * @param driverRider the driver rider
     */
    public LoginController(String userName, String driverRider) {
        BuildClient bC = new BuildClient();
        this.client = bC.getClient();
        this.userName = userName;
        this.requestedStatus = driverRider;
    }

    /**
     * Check rider exists.
     *
     * @return the boolean
     * @throws ExecutionException   the execution exception
     * @throws InterruptedException the interrupted exception
     */
    public Boolean checkRider() throws ExecutionException, InterruptedException {
        UserElasticSearchController.GetRider retrievedRider = new UserElasticSearchController.GetRider();
        Rider rider = retrievedRider.execute(this.userName).get();
        if (rider.getUserName() != null && rider.getUserName().contentEquals(this.userName)) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    /**
     * Check driver exists.
     *
     * @return the boolean
     * @throws ExecutionException   the execution exception
     * @throws InterruptedException the interrupted exception
     */
    public Boolean checkDriverInfo() throws ExecutionException, InterruptedException {
        UserElasticSearchController.GetDriverInfo retrievedDriverInfo = new UserElasticSearchController.GetDriverInfo();
        DriverInfo driverInfo = retrievedDriverInfo.execute(this.userName).get();
        if (driverInfo != null && driverInfo.getUserName().contentEquals(this.userName)) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}
