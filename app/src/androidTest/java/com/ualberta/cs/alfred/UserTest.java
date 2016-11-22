package com.ualberta.cs.alfred;

import android.test.ActivityInstrumentationTestCase2;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


/**
 * Creates the test cases for type User.
 *
 * @author ookmm
 * @version 1.1
 */
public class UserTest extends ActivityInstrumentationTestCase2 {

    /**
     * Instantiates a new User test.
     */
    public UserTest() {
        super(MainActivity.class);
    }

    /**
     * Test get first name.
     */
    public void testGetFirstName() {

        String u1FirstName = "Donald";
        String u1LastName = "Trump";
        String u1UserName = "dtrump";

        // Create date
        GregorianCalendar gc = new GregorianCalendar(2001, Calendar.JUNE, 13);
        Date u1DateOfBirth = gc.getTime();

        String u1PhoneNumber = "780-444-5555";
        String u1Email = "email@example.com";

        User u1 = new User(u1FirstName, u1LastName, u1UserName, u1DateOfBirth, u1PhoneNumber, u1Email);

        assertTrue("Firstname not the same", u1FirstName.equals(u1.getFirstName()));
    }

    /**
     * Test get last name.
     */
    public void testGetLastName() {

        String u1FirstName = "Donald";
        String u1LastName = "Trump";
        String u1UserName = "dtrump";

        // Create date
        GregorianCalendar gc = new GregorianCalendar(2001, Calendar.JUNE, 13);
        Date u1DateOfBirth = gc.getTime();

        String u1PhoneNumber = "780-444-5555";
        String u1Email = "email@example.com";

        User u1 = new User(u1FirstName, u1LastName, u1UserName, u1DateOfBirth, u1PhoneNumber, u1Email);

        assertTrue("Lastname not the same", u1LastName.equals(u1.getLastName()));
    }

    /**
     * Test get user name.
     */
    public void testGetUserName() {

        String u1FirstName = "Donald";
        String u1LastName = "Trump";
        String u1UserName = "dtrump";

        // Create date
        GregorianCalendar gc = new GregorianCalendar(2001, Calendar.JUNE, 13);
        Date u1DateOfBirth = gc.getTime();

        String u1PhoneNumber = "780-444-5555";
        String u1Email = "email@example.com";

        User u1 = new User(u1FirstName, u1LastName, u1UserName, u1DateOfBirth, u1PhoneNumber, u1Email);

        assertTrue("Username not the same", u1UserName.equals(u1.getUserName()));
    }

    /**
     * Test get date of birth.
     */
    public void testGetDateOfBirth() {

        String u1FirstName = "Donald";
        String u1LastName = "Trump";
        String u1UserName = "dtrump";

        // Create date
        GregorianCalendar gc = new GregorianCalendar(2001, Calendar.JUNE, 13);
        Date u1DateOfBirth = gc.getTime();

        String u1PhoneNumber = "780-444-5555";
        String u1Email = "email@example.com";

        User u1 = new User(u1FirstName, u1LastName, u1UserName, u1DateOfBirth, u1PhoneNumber, u1Email);

        assertTrue("Date of birth not the same", u1DateOfBirth.equals(u1.getDateOfBirth()));
    }

    /**
     * Test get phone number.
     */
    public void testGetPhoneNumber() {

        String u1FirstName = "Donald";
        String u1LastName = "Trump";
        String u1UserName = "dtrump";

        // Create date
        GregorianCalendar gc = new GregorianCalendar(2001, Calendar.JUNE, 13);
        Date u1DateOfBirth = gc.getTime();

        String u1PhoneNumber = "780-444-5555";
        String u1Email = "email@example.com";

        User u1 = new User(u1FirstName, u1LastName, u1UserName, u1DateOfBirth, u1PhoneNumber, u1Email);

        assertTrue("Phone number not the same", u1PhoneNumber.equals(u1.getPhoneNumber()));
    }

    /**
     * Test get email.
     */
    public void testGetEmail() {

        String u1FirstName = "Donald";
        String u1LastName = "Trump";
        String u1UserName = "dtrump";

        // Create date
        GregorianCalendar gc = new GregorianCalendar(2001, Calendar.JUNE, 13);
        Date u1DateOfBirth = gc.getTime();

        String u1PhoneNumber = "780-444-5555";
        String u1Email = "email@example.com";

        User u1 = new User(u1FirstName, u1LastName, u1UserName, u1DateOfBirth, u1PhoneNumber, u1Email);

        assertTrue("Email not the same", u1Email.equals(u1.getEmail()));
    }

    public void testIsRider() {

        String u1FirstName = "Donald";
        String u1LastName = "Trump";
        String u1UserName = "dtrump";

        // Create date
        GregorianCalendar gc = new GregorianCalendar(2001, Calendar.JUNE, 13);
        Date u1DateOfBirth = gc.getTime();

        String u1PhoneNumber = "780-444-5555";
        String u1Email = "email@example.com";

        User u1 = new User(u1FirstName, u1LastName, u1UserName, u1DateOfBirth, u1PhoneNumber, u1Email);

        //assertTrue("User is not a rider", u1.isDriver() == false);
    }

    public void testIsDriver() {

        /**
         * Step 1: Basic user info
         */
        String u1FirstName = "Donald";
        String u1LastName = "Trump";
        String u1UserName = "dtrump";

        // Create date
        GregorianCalendar gc = new GregorianCalendar(2001, Calendar.JUNE, 13);
        Date u1DateOfBirth = gc.getTime();

        String u1PhoneNumber = "780-444-5555";
        String u1Email = "email@example.com";

        /**
         * Step 2: Create a driver licenceNumber
         */
        String licenceNumber = "AB0005";

        /**
         * Create a vehicle
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
         * Step 3: Create driverInfo
         */
        DriverInfo driverInfo = new DriverInfo(licenceNumber, vehicle);

        /**
         * Step 4: Create user that is a driver
         */
        User driver = new User(u1FirstName, u1LastName, u1UserName, u1DateOfBirth, u1PhoneNumber,
                u1Email, driverInfo);

        //assertTrue("User is a driver", driver.isDriver() == true);
    }

}
