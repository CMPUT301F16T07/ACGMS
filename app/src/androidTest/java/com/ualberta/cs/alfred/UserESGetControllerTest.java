package com.ualberta.cs.alfred;


import android.test.ActivityInstrumentationTestCase2;

import java.util.concurrent.ExecutionException;


/**
 * Test cases for getting item from User
 *
 * @author ookmm
 * @version 1.1
 * @see UserESGetController
 */
public class UserESGetControllerTest extends ActivityInstrumentationTestCase2 {

    /**
     * Instantiates a new User es get controller test.
     */
    public UserESGetControllerTest() {
        super(MainActivity.class);
    }

    /**
     * Test get user by id task.
     */
    public void testGetUserByIdTask() {

        UserESGetController.GetUserByIdTask retrievedUser =
                new UserESGetController.GetUserByIdTask();

        // Find user
        retrievedUser.execute("AViOOOZodE2DZPCrf9nn");

        try {
            User user = retrievedUser.get();

            System.out.println("====================");
            System.out.println("User ID: " + user.getUserID());
            System.out.println("Username: " + user.getUserName());
            System.out.println("First name: " + user.getFirstName());
            System.out.println("Last name: " + user.getLastName());
            System.out.println("Phone: " + user.getPhoneNumber());
            System.out.println("Email: " + user.getEmail());
            if (user.getIsRider()) {
                System.out.println("Credit card: " + user.getRiderInfo().getCreditCardNumber());
            } else if (user.getIsDriver()) {
                System.out.println("Driver licence number: " + user.getDriverInfo().getLicenceNumber());
            }
            System.out.println("====================");
            assert (true);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
