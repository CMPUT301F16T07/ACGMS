package com.ualberta.cs.alfred;

import android.test.ActivityInstrumentationTestCase2;


/**
 * Creates the type Address test.
 *
 * @author carlcastello on 07/11/16.
 * @version 1.2
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
        double longitude = -73.989308;
        double latitude = 40.741895;
        address = new Address(location, longitude, latitude);

        assertTrue("getLocation Failed!", location.equals(address.getLocation()));
    }

    /**
     * Test get latitude.
     */
    public void testGetLatitude() {
        // Initializing all necessary variable for testing.
        Address address;
        String location = "116 Knottwood Road NW";
        double longitude = -73.989308;
        double latitude = 40.741895;
        address = new Address(location, longitude, latitude);

        System.out.println("====== Lat =======");
        System.out.println(address.getLatitude());
        System.out.println("====== End =======");

        assertTrue("getLatitude Failed!", latitude == address.getLatitude());
    }

    /**
     * Test get longitude.
     */
    public void testGetLongitude(){
        // Initializing all necessary variable for testing.
        Address address;
        String location = "116 Knottwood Road NW";
        double longitude = -73.989308;
        double latitude = 40.741895;
        address = new Address(location, longitude, latitude);

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
        double longitude = -73.989308;
        double latitude = 40.741895;
        address = new Address(location1, longitude, latitude);

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
        double longitude = -73.989308;
        double latitude1 = 40.741895;
        double latitude2 = 43.231312;
        address = new Address(location1, longitude, latitude1);

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
        double longitude1 = -73.989308;
        double longitude2 = -77.201313;
        double latitude = 40.741895;
        address = new Address(location1, longitude1, latitude);

        address.setLongitude(longitude1);

        assertFalse("setLongitude Failed!", longitude2 == address.getLongitude());

        assertTrue("setLongitude Failed!", longitude1 == address.getLongitude());
    }
}

