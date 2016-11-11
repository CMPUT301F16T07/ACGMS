package com.ualberta.cs.alfred;

import android.test.ActivityInstrumentationTestCase2;


/**
 * Creates the type Address test.
 *
 * @author carlcastello on 07/11/16.
 * @version 1.1
 * @see Address
 */
public class AddressTest extends ActivityInstrumentationTestCase2 {

    /**
     * Instantiates a new Address test.
     */
    public AddressTest() {

        super(MainActivity.class);
    }

    /**
     * Test get location.
     */
    public void testGetLocation() {
        // Initializing all necessary variable for testing.
        Address address;
        String location = "116 Knottwood Road NW";
        double latitude = 40.741895;
        double longtitude = -73.989308;
        address = new Address(location, latitude, longtitude);

        assertTrue("getLocation Failed!", location.equals(address.getLocation()));
    }

    /**
     * Test get latitude.
     */
    public void testGetLatitude() {
        // Initializing all necessary variable for testing.
        Address address;
        String location = "116 Knottwood Road NW";
        double latitude = 40.741895;
        double longitude = -73.989308;
        address = new Address(location, latitude, longitude);

        assertTrue("getLatitude Failed!", latitude == address.getLatitude());
    }

    /**
     * Test get longitude.
     */
    public void testGetLongitude(){
        // Initializing all necessary variable for testing.
        Address address;
        String location = "116 Knottwood Road NW";
        double latitude = 40.741895;
        double longitude = -73.989308;
        address = new Address(location, latitude, longitude);

        assertTrue("getLongitude Failed!", longitude == address.getLongitude());
    }

    /**
     * Test set location.
     */
    public void testSetLocation() {
        // Initializing all necessary variable for testing.
        Address address;
        String location1 = "116 Knottwood Road NW";
        String location2 = "7632 23 Ave NW";
        double latitude = 40.741895;
        double longitude = -73.989308;
        address = new Address(location1, latitude, longitude);

        address.setLocation(location2);

        assertFalse("setLocation Failed!", location1.equals(address.getLocation()));

        assertTrue("setLocation Failed!", location2.equals(address.getLocation()));
    }

    /**
     * Test set latitude.
     */
    public void testSetLatitude(){
        // Initializing all necessary variable for testing.
        Address address;
        String location1 = "116 Knottwood Road NW";
        double latitude1 = 40.741895;
        double latitude2 = 43.231312;
        double longitude = -73.989308;
        address = new Address(location1, latitude1, longitude);

        address.setLatitude(latitude2);

        assertFalse("setLatitude Failed!", latitude1 == address.getLatitude());

        assertTrue("setLatitude Failed!", latitude2 == address.getLatitude());
    }

    /**
     * Test set longitude.
     */
    public void testSetLongitude() {
        // Initializing all necessary variable for testing.
        Address address;
        String location1 = "116 Knottwood Road NW";
        double latitude = 40.741895;
        double longitude1 = -73.989308;
        double longitude2 = -77.201313;
        address = new Address(location1, latitude, longitude1);

        address.setLongitude(longitude1);

        assertFalse("setLongitude Failed!", longitude2 == address.getLongitude());

        assertTrue("setLongitude Failed!", longitude1 == address.getLongitude());
    }
}

