package com.ualberta.cs.alfred;


import android.test.ActivityInstrumentationTestCase2;

/**
 * Creates all the tests for Driver info.
 *
 * @author ookmm
 * @version 1.0
 * @see DriverInfo
 */
public class DriverInfoTest extends ActivityInstrumentationTestCase2 {

    /**
     * Instantiates a new Driver info test.
     */
    public DriverInfoTest() {
        super(MainActivity.class);
    }

    /**
     * Test get licence number.
     */
    public void testGetLicenceNumber() {

        /**
         * Step 1: Create a driver licenceNumber
         */
        String licenceNumber = "AB0005";

        /**
         * Step 2: Create a vehicle
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
         * Create driverInfo
         */
        DriverInfo driverInfo = new DriverInfo(licenceNumber, vehicle);

        assertTrue("Licence number not equal", licenceNumber.equals(driverInfo.getLicenceNumber()));
    }

    /**
     * Test get driver rating.
     */
    public void testGetDriverRating() {
        /**
         * Step 1: Create a driver licenceNumber
         */
        String licenceNumber = "AB0005";

        /**
         * Step 2: Create a vehicle
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
         * Create driverInfo
         */
        DriverInfo driverInfo = new DriverInfo(licenceNumber, vehicle);

        assertTrue("Driver rating not equal to zero", driverInfo.getDriverRating().getRating() == 0);
    }

    /**
     * Test get vehicle.
     */
    public void testGetVehicle() {
        /**
         * Step 1: Create a driver licenceNumber
         */
        String licenceNumber = "AB0005";

        /**
         * Step 2: Create a vehicle
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
         * Create driverInfo
         */
        DriverInfo driverInfo = new DriverInfo(licenceNumber, vehicle);

        assertTrue("Vehicle not the same", vehicle.equals(driverInfo.getVehicle()));
    }
}
