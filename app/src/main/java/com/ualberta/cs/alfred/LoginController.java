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
    private RiderInfo riderInfo;
    private DriverInfo driverInfo;
    private String mode;

    /**
     * Instantiates a new Login controller.
     *
     * @param userName    the user name
     * @param driverRider the driver rider
     */
    public LoginController(String userName, String mode) {
        BuildClient bC = new BuildClient();
        this.client = bC.getClient();
        this.userName = userName;
        this.mode = mode;
    }


    private Boolean isExpected(String mode, User user) {
        if (user != null) {
            if ( (mode.contentEquals("Rider Mode") && user.getIsRider()) ||
                    (mode.contentEquals("Driver Mode") && user.getIsDriver()) ) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    /**
     * Check user exists.
     *
     * @return the boolean
     * @throws ExecutionException   the execution exception
     * @throws InterruptedException the interrupted exception
     */
    public Boolean checkUser() throws ExecutionException, InterruptedException {
        UserElasticSearchController.GetUserInfo retrievedUser = new UserElasticSearchController.GetUserInfo();
        User user = retrievedUser.execute(this.userName).get();
        if (user != null && this.isExpected(this.mode, user)) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    /**
     * Check if opposite mode exists.
     *
     * @return the boolean
     * @throws ExecutionException   the execution exception
     * @throws InterruptedException the interrupted exception
     */
    public Boolean checkOpposite() throws ExecutionException, InterruptedException {
        UserElasticSearchController.GetUserInfo retrievedUser = new UserElasticSearchController.GetUserInfo();
        User user = retrievedUser.execute(this.userName).get();
        String oppositeMode = "Driver Mode";
        if (this.mode.contentEquals(oppositeMode)) {
            oppositeMode = "Rider Mode";
        }
        if (user != null && this.isExpected(oppositeMode, user)) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}
