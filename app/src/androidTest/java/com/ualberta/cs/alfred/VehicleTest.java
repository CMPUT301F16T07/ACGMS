package com.ualberta.cs.alfred;


import android.test.ActivityInstrumentationTestCase2;

/**
 * Creates all the test cases for Vehicle
 *
 * @author ookmm
 * @version 1.0
 * @see Vehicle
 */
public class VehicleTest extends ActivityInstrumentationTestCase2 {

    /**
     * Instantiates a new Vehicle test.
     */
    public VehicleTest() {
        super(MainActivity.class);
    }

    /**
     * Test get serial number.
     */
    public void testGetSerialNumber() {
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

        assertTrue("Serial number not equal", serialNumber.equals(vehicle.getSerialNumber()));
    }

    /**
     * Test get plate number.
     */
    public void testGetPlateNumber() {
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

        assertTrue("Plate number not equal", plateNumber.equals(vehicle.getPlateNumber()));
    }

    /**
     * Test get type.
     */
    public void testGetType() {
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

        assertTrue("Type not equal", type.equals(vehicle.getType()));
    }

    /**
     * Test get make.
     */
    public void testGetMake() {
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

        assertTrue("Make not equal", make.equals(vehicle.getMake()));
    }

    /**
     * Test get model.
     */
    public void testGetModel() {
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

        assertTrue("Make not equal", model.equals(vehicle.getModel()));
    }

    /**
     * Test get year.
     */
    public void testGetYear() {
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

        assertTrue("Make not equal", year == vehicle.getYear());
    }

    /**
     * Test get color.
     */
    public void testGetColor() {
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

        assertTrue("Make not equal", color.equals(vehicle.getColor()));
    }

}
