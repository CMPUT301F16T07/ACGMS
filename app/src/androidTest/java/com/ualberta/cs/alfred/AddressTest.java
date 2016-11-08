package com.ualberta.cs.alfred;

import junit.framework.TestCase;

/**
 * Created by carlcastello on 07/11/16.
 */

public class AddressTest extends TestCase {
    /**
     *  Test getLocation()
     */
    public void testGetLocation(){
        // Initializing all necessary variable for testing.
        Address address;
        String Location = "116 Knottwood Road NW";
        double Latitude = 40.741895;
        double Longtitude = -73.989308;
        address = new Address(Location,Latitude,Longtitude);

        // This will pass the test
        assertTrue("getLocation Failed!",Location.equals(address.getLocation()));
        // This will fail the test
        //assertFalse("getLocation Failed!",Location.equals(address.getLocation()));
    }

    /**
     *  Test getLatitude()
     */
    public void testGetLatitude(){
        // Initializing all necessary variable for testing.
        Address address;
        String Location = "116 Knottwood Road NW";
        double Latitude = 40.741895;
        double Longitude = -73.989308;
        address = new Address(Location,Latitude,Longitude);

        // This will pass the test
        assertTrue("getLatitude Failed!",Latitude == address.getLatitude());
        // This will fail the test
        //assertFalse("getLatitude Failed!",Location == address.getLatitude());
    }

    /**
     *  Test getLongitude()
     */
    public void testGetLongitude(){
        // Initializing all necessary variable for testing.
        Address address;
        String Location = "116 Knottwood Road NW";
        double Latitude = 40.741895;
        double Longitude = -73.989308;
        address = new Address(Location,Latitude,Longitude);

        // This will pass the test
        assertTrue("getLongitude Failed!",Longitude == address.getLongitude());
        // This will fail the test
        //assertFalse("getLongitude Failed!",Location == address.getLongitude());
    }

    /**
     *  Test setLocation()
     */
    public void testSetLocation(){
        // Initializing all necessary variable for testing.
        Address address;
        String Location1 = "116 Knottwood Road NW";
        String Location2 = "7632 23 Ave NW";
        double Latitude = 40.741895;
        double Longitude = -73.989308;
        address = new Address(Location1,Latitude,Longitude);

        address.setLocation(Location2);

        // This will pass the test
        assertFalse("setLocation Failed!",Location1.equals(address.getLocation()));
        // This will fail the test
        //assertTrue("setLocation Failed!",Location1.equals(address.getLocation()));
    }

    /**
     *  Test setLatitude()
     */
    public void testSetLatitude(){
        // Initializing all necessary variable for testing.
        Address address;
        String Location1 = "116 Knottwood Road NW";
        double Latitude1 = 40.741895;
        double Latitude2 = 43.231312;
        double Longitude = -73.989308;
        address = new Address(Location1,Latitude1,Longitude);

        address.setLatitude(Latitude2);

        // This will pass the test
        assertFalse("setLatitude Failed!",Latitude1 == address.getLatitude());
        // This will fail the test
        //assertTrue("setLatitude Pass",Latitude1 == address.getLatitude());
    }

    /**
     *  Test setLongitude()
     */
    public void testsetLongitude(){
        // Initializing all necessary variable for testing.
        Address address;
        String Location1 = "116 Knottwood Road NW";
        double Latitude = 40.741895;
        double Longitude1 = -73.989308;
        double Longitude2 = -77.201313;
        address = new Address(Location1,Latitude,Longitude1);

        address.setLongitude(Longitude1);

        // This will pass the test
        assertFalse("setLongitude Failed!",Longitude2 == address.getLongitude());
        // This will fail the test
        //assertTrue("Location not properly inserted",Location1.equals(address.getLocation()));
    }
}

