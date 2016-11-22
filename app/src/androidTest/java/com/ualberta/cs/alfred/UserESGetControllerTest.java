package com.ualberta.cs.alfred;


import android.test.ActivityInstrumentationTestCase2;

import java.util.concurrent.ExecutionException;

public class UserESGetControllerTest extends ActivityInstrumentationTestCase2 {

    public UserESGetControllerTest() {
        super(MainActivity.class);
    }

    public void testGetUserByIdTask() {

        UserESGetController.GetUserByIdTask retrievedUser =
                new UserESGetController.GetUserByIdTask();

        // Find user
        retrievedUser.execute("AViNIZ0VdE2DZPCrf9nF");

        try {
            User user = retrievedUser.get();

            System.out.println("====================");
            System.out.println("User ID: " + user.getUserID());
            System.out.println("Username: " + user.getUserName());
            System.out.println("First name: " + user.getFirstName());
            System.out.println("Last name: " + user.getLastName());
            System.out.println("Phone: " + user.getPhoneNumber());
            System.out.println("Email: " + user.getEmail());
            System.out.println("Credit card: " + user.getRiderInfo().getCreditCardNumber());
            System.out.println("====================");
            assert (true);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
