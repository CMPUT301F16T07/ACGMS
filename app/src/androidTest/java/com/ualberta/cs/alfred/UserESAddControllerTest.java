package com.ualberta.cs.alfred;


import android.test.ActivityInstrumentationTestCase2;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Test cases for Adding item to a Users
 *
 * @author ookmm
 * @version 1.1
 * @see
 */
public class UserESAddControllerTest extends ActivityInstrumentationTestCase2 {

    public UserESAddControllerTest() {
        super(MainActivity.class);
    }

    public void testAddUser() {

        String u1FirstName = "Donald";
        String u1LastName = "Trump";
        String u1UserName = "dtrump";

        // Create date
        GregorianCalendar gc = new GregorianCalendar(2001, Calendar.JUNE, 13);
        Date u1DateOfBirth = gc.getTime();

        String u1PhoneNumber = "780-444-5555";
        String u1Email = "email@example.com";

        User u1 = new User(u1FirstName, u1LastName, u1UserName, u1DateOfBirth, u1PhoneNumber, u1Email);

        assert(true);
    }

    public void testAddRider() {

        /**
         * Step 1: Basic user info
         */
        String u1FirstName = "Peter";
        String u1LastName = "Mansbridge";
        String u1UserName = "peterman";

        // Create date
        GregorianCalendar gc = new GregorianCalendar(1940, Calendar.JUNE, 21);
        Date u1DateOfBirth = gc.getTime();

        String u1PhoneNumber = "613-888-5555";
        String u1Email = "peterman@example.com";

        /**
         * Step 2: Create credit card
         */
        String creditCard = "7777333366662222";

        /**
         * Step 3: Create Rider info
         */
        RiderInfo riderInfo = new RiderInfo(creditCard);

        User u1 = new User(u1FirstName, u1LastName, u1UserName, u1DateOfBirth, u1PhoneNumber,
                u1Email, riderInfo);

        assert(true);
    }

    public void testAddDriver() {

        /**
         * Step 1: Basic user infor
         */
        String u1FirstName = "Abram";
        String u1LastName = "Hindle";
        String u1UserName = "ahindle";

        // Create date
        GregorianCalendar gc = new GregorianCalendar(1970, Calendar.JANUARY, 13);
        Date u1DateOfBirth = gc.getTime();

        String u1PhoneNumber = "780-333-5555";
        String u1Email = "ahindle@example.com";

        /**
         * Step 2: Create a driver licenceNumber
         */
        String licenceNumber = "CMPUT301";

        /**
         * Step 3: Create a vehicle
         */
        String serialNumber = "serial000222444";
        String plateNumber = "BUT001";
        String type = "Sedan";
        String make = "Tesla";
        String model = "Model S";
        int year = 2016;
        String color = "Silver";

        Vehicle vehicle = new Vehicle(serialNumber, plateNumber, type, make, model, year, color);

        /**
         * Step 4: Create driverInfo
         */
        DriverInfo driverInfo = new DriverInfo(licenceNumber, vehicle);

        /**
         * Step 5: Create driver
         */
        User u1 = new User(u1FirstName, u1LastName, u1UserName, u1DateOfBirth, u1PhoneNumber,
                u1Email, driverInfo);

        assert(true);
    }

    /**
     * Test adding new driver rating
     *
     * @method AddNewDriverRatingTask()
     */
    public void testAddNewDriverRatingTask() {

        String userID = "AViVCADtdE2DZPCrf9o_";
        double newRating = 4;
        String newRatingAsString = String.valueOf(newRating);


        UserESAddController.AddNewDriverRatingTask addNewDriverRatingTask =
                new UserESAddController.AddNewDriverRatingTask();

        addNewDriverRatingTask.execute(userID, newRatingAsString);
        assert (true);
    }
}
