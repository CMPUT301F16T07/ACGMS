package com.ualberta.cs.alfred.usecasetests;

import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.ualberta.cs.alfred.Address;
import com.ualberta.cs.alfred.AppSettings;
import com.ualberta.cs.alfred.Request;
import com.ualberta.cs.alfred.RequestESGetController;
import com.ualberta.cs.alfred.RiderInfo;
import com.ualberta.cs.alfred.User;
import com.ualberta.cs.alfred.UserESGetController;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.ExecutionException;

/**
 * Test cases for Status.
 *
 * @author ookmm
 */
@RunWith(AndroidJUnit4.class)
public class StatusTest {

    /**
     * US 02.01.01
     * As a rider or driver, I want to see the status of a request that I am involved in
     *
     * @throws Exception
     */
    @Test
    public void testSeeRequestStatus() throws Exception {
        /**
         * Step 1: Basic user info
         */
        String u1FirstName = "Stephen";
        String u1LastName = "Harper";
        String u1UserName = "sharper";

        // Create date
        GregorianCalendar gc = new GregorianCalendar(1962, Calendar.FEBRUARY, 17);
        Date u1DateOfBirth = gc.getTime();

        String u1PhoneNumber = "780-888-5555";
        String u1Email = "sharper@example.com";

        /**
         * Step 2: Create credit card
         */
        String creditCard = "2222333366662222";

        /**
         * Step 3: Create Rider info
         */
        RiderInfo riderInfo = new RiderInfo(creditCard);

        /**
         * Step 4: Check if DB has username already
         */
        UserESGetController.GetUserTask retrievedUser =
                new UserESGetController.GetUserTask();

        // Find user
        retrievedUser.execute(u1UserName);

        try {
            User user = retrievedUser.get();

            if (user == null) {
                /**
                 * Step 5: Create user
                 */
                User u1 = new User(u1FirstName, u1LastName, u1UserName, u1DateOfBirth, u1PhoneNumber,
                        u1Email, riderInfo);
            } else {
                Log.i("Error", "Try a different username");
                System.out.println("Try a different username");
            }
            assert (true);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        /**
         * Step 6: Get user ID with the initial username
         */
        String user2ID = null;

        UserESGetController.GetUserTask retrievedUserForUserID =
                new UserESGetController.GetUserTask();

        // Find user
        retrievedUserForUserID.execute(u1UserName);

        try {
            User user2 = retrievedUserForUserID.get();
            if (user2 != null) {
                user2ID = user2.getUserID();
            }
            assert (true);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        /**
         * Step 7: Make a request between two locations
         */
        String req1Status = AppSettings.REQUEST_PENDING;
        Address req1SrcAddr = new Address("University of Alberta", -113.52631859999997, 53.5232189);
        Address req1DestAddr = new Address("West Edmonton Mall", -113.62419060000002, 53.5225151);
        double req1Cost = 16.65;
        double req1Distance = 10.4;
        String req1RiderID = user2ID;

        Request req1 = new Request(req1Status, req1SrcAddr, req1DestAddr, req1Distance, req1Cost,
                req1RiderID);
        assert(true);

        /**
         * Step 8: Get requests by user with riderID
         */
        RequestESGetController.GetRequestTask retrievedRequest =
                new RequestESGetController.GetRequestTask();

        retrievedRequest.execute("riderID", user2ID);

        try {
            ArrayList<Request> requests = retrievedRequest.get();
            if (requests.size() != 0) {
                for (Request request : requests) {
                    System.out.println("====================");
                    System.out.println("Request ID is: " + request.getRequestID());
                    System.out.println("Request Status: " + request.getRequestStatus());
                    System.out.println("====================");

                    assert (true);
                }
            } else {
                Log.i("Error", "No request exists for this user");
                System.out.println("No request exists for this user");
            }
            assert (true);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

}
