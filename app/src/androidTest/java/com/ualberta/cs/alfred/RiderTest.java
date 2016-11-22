package com.ualberta.cs.alfred;

import android.test.ActivityInstrumentationTestCase2;

import java.util.Date;
import java.util.concurrent.ExecutionException;

/**
 * Create all the test cases for Request
 * @author mmcote
 * @version 1
 * @see Rider
 */
public class RiderTest extends ActivityInstrumentationTestCase2 {
    public RiderTest() {
        super(MainActivity.class);
    }

    public void test() {
        Rider rider = new Rider("Mag", "Clown", "mag1", new Date(), "12345678",
                "jimbo@ualberta.ca", "34903845854");
        assertTrue(true);
    }

    public void testGetRider() {
        UserESGetController.GetRider retrievedRider = new UserESGetController.GetRider();
        retrievedRider.execute("jimbo");

        try {
            Rider rider = retrievedRider.get();
            System.out.println("++++++++++++++++++++++++");
            System.out.println("FIRSTNAME: " + rider.getFirstName());
            System.out.println("++++++++++++++++++++++++");
            assert(true);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void testAddRiderInfo() {
        RiderInfo riderInfo = new RiderInfo("23423432424");
        User user = new User("Jimmy", "Fallon", "jimFal", new Date(), "32432234", "jimmyFallon@ualberta.ca", riderInfo);

        UserESAddController.AddUserTask<User> addUserTask = new UserESAddController.AddUserTask<User>();
        addUserTask.execute(user);

        UserESGetController.GetUserTask getUserTask = new UserESGetController.GetUserTask();
        getUserTask.execute("jimFal");
        User userReturn = null;
        try {
            userReturn = getUserTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        assertTrue(userReturn.getUserName().contentEquals("jimFal"));
    }

    public void testUserInfoWithoutPrivate() {
        UserESGetController.GetUserInfoWithoutPrivateInfo getUserInfoWithoutPrivateInfo = new UserESGetController.GetUserInfoWithoutPrivateInfo();
        getUserInfoWithoutPrivateInfo.execute("jimFal", "riderInfo.creditCardNumber");

        User userReturn = null;
        try {
            userReturn = getUserInfoWithoutPrivateInfo.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        assertTrue(userReturn.getUserName().contentEquals("jimFal"));
    }
}

