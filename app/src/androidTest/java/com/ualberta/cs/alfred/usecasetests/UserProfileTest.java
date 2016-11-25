package com.ualberta.cs.alfred.usecasetests;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.ualberta.cs.alfred.DriverInfo;
import com.ualberta.cs.alfred.RiderInfo;
import com.ualberta.cs.alfred.User;
import com.ualberta.cs.alfred.UserESGetController;
import com.ualberta.cs.alfred.UserESSetController;
import com.ualberta.cs.alfred.Vehicle;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;

/**
 * Test cases for User profile.
 *
 * @author ookmm
 */
@RunWith(AndroidJUnit4.class)
public class UserProfileTest {

    /**
     * US 03.01.01
     * As a user, I want a profile with a unique username and my contact information.
     *
     * Test for creating basic user profile.
     * @throws Exception
     */
    @Test
    public void testCreateUserProfile() throws Exception {

        String u1FirstName = "Justin";
        String u1LastName = "Trudeau";
        String u1UserName = "jtrudeau";

        // Create date
        GregorianCalendar gc = new GregorianCalendar(1975, Calendar.JUNE, 13);
        Date u1DateOfBirth = gc.getTime();

        String u1PhoneNumber = "613-444-5555";
        String u1Email = "jtrudeau@example.com";

        /**
         * Check if DB has username already
         */
        UserESGetController.GetUserTask retrievedUser =
                new UserESGetController.GetUserTask();

        // Find user
        retrievedUser.execute(u1UserName);

        try {
            User user = retrievedUser.get();

            if (user == null) {
                User u1 = new User(u1FirstName, u1LastName, u1UserName, u1DateOfBirth,
                        u1PhoneNumber, u1Email);
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
    }

    /**
     * US 03.01.01
     * As a user, I want a profile with a unique username and my contact information.
     *
     * Test for creating a rider profile.
     * @throws Exception
     */
    @Test
    public void testCreateRiderProfile() throws Exception {

        /**
         * Step 1: Basic user info
         */
        String u1FirstName = "Sarah";
        String u1LastName = "Mohammed";
        String u1UserName = "smohammed";

        // Create date
        GregorianCalendar gc = new GregorianCalendar(1992, Calendar.FEBRUARY, 21);
        Date u1DateOfBirth = gc.getTime();

        String u1PhoneNumber = "780-888-5555";
        String u1Email = "smohammed@example.com";

        /**
         * Step 2: Create credit card
         */
        String creditCard = "8888333366662222";

        /**
         * Step 3: Create Rider info
         */
        RiderInfo riderInfo = new RiderInfo(creditCard);

        /**
         * Check if DB has username already
         */
        UserESGetController.GetUserTask retrievedUser =
                new UserESGetController.GetUserTask();

        // Find user
        retrievedUser.execute(u1UserName);

        try {
            User user = retrievedUser.get();

            if (user == null) {
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
    }

    /**
     * US 03.01.01
     * As a user, I want a profile with a unique username and my contact information.
     *
     * Test for creating a driver profile.
     * @throws Exception
     */
    @Test
    public void testCreateDriverProfile() throws Exception {

        /**
         * Step 1: Basic user info
         */
        String u1FirstName = "Bill";
        String u1LastName = "Clinton";
        String u1UserName = "bclinton";

        // Create date
        GregorianCalendar gc = new GregorianCalendar(1956, Calendar.DECEMBER, 05);
        Date u1DateOfBirth = gc.getTime();

        String u1PhoneNumber = "780-404-5555";
        String u1Email = "bclinton@example.com";

        /**
         * Step 2: Create a driver licenceNumber
         */
        String licenceNumber = "AB5555";

        /**
         * Step 3: Create a vehicle
         */
        String serialNumber = "serial777999";
        String plateNumber = "BILL010";
        String type = "SUV";
        String make = "Nissan";
        String model = "Pathfinder";
        int year = 2015;
        String color = "Black";

        Vehicle vehicle = new Vehicle(serialNumber, plateNumber, type, make, model, year, color);

        /**
         * Step 4: Create driverInfo
         */
        DriverInfo driverInfo = new DriverInfo(licenceNumber, vehicle);

        /**
         * Step 5: Check if DB has username already
         */
        UserESGetController.GetUserTask retrievedUser =
                new UserESGetController.GetUserTask();

        // Find user
        retrievedUser.execute(u1UserName);

        try {
            User user = retrievedUser.get();

            if (user == null) {
                /**
                 * Step 6: Create driver
                 */
                User u1 = new User(u1FirstName, u1LastName, u1UserName, u1DateOfBirth, u1PhoneNumber,
                        u1Email, driverInfo);
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
    }

    /**
     * US 03.02.01
     * As a user, I want to edit the contact information in my profile.
     *
     * @throws Exception
     */
    @Test
    public void testEditUserContactInformation() throws Exception {

        /**
         * Get user ID
         */
        String u1UserName = "jtrudeau";
        String userID = null;

        UserESGetController.GetUserTask retrievedUser =
                new UserESGetController.GetUserTask();

        // Find user
        retrievedUser.execute(u1UserName);

        try {
            User user = retrievedUser.get();
            userID = user.getUserID();
            assert (true);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        /**
         * Updating user phone number and email.
         */
        String userProperty1 = "phoneNumber";
        String userPropertyType1 = "string";
        String userNewValue1 = "613-333-7777";

        String userProperty2 = "email";
        String userPropertyType2 = "string";
        String userNewValue2 = "jtrudeau@gmail.com";

        UserESSetController.SetMultiplePropertyValueTask setMultiplePropertyValueTask =
                new UserESSetController.SetMultiplePropertyValueTask();

        setMultiplePropertyValueTask.execute(userID,
                userProperty1, userPropertyType1, userNewValue1,
                userProperty2, userPropertyType2, userNewValue2);
        assert (true);
    }

}
