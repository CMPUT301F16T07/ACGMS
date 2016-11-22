package com.ualberta.cs.alfred;

/**
 * This class is used differentiate the explicit driver data that will be stored separate from
 * the same user's rider data. As all users start as riders, it is only optional to become a
 * driver.
 *
 * @author mmcote
 * @version 1.1
 */
public class DriverInfo {

    private String licenceNumber;
    private Rating driverRating;
    private Vehicle vehicle;

    /**
     * Instantiates a new Driver info.
     *
     * @param licenceNumber the licence number (on the users given license)
     * @param vehicle       the vehicle
     */
    public DriverInfo(String licenceNumber, Vehicle vehicle) {
        this.licenceNumber = licenceNumber;
        this.driverRating = new Rating();
        this.vehicle = vehicle;
    }

    /**
     * Gets licence number.
     *
     * @return the licence number
     */
    public String getLicenceNumber() {
        return licenceNumber;
    }

    /**
     * Sets licence number.
     *
     * @param licenceNumber the licence number
     */
    public void setLicenceNumber(String licenceNumber) {
        this.licenceNumber = licenceNumber;
    }

    /**
     * Gets driver rating.
     *
     * @return the driver rating
     */
    public Rating getDriverRating() {
        return driverRating;
    }

    /**
     * Sets driver rating.
     *
     * @param driverRating the driver rating
     */
    public void setDriverRating(Rating driverRating) {
        this.driverRating = driverRating;
    }

    /**
     * Gets vehicle.
     *
     * @return the vehicle
     */
    public Vehicle getVehicle() {
        return vehicle;
    }

    /**
     * Sets vehicle.
     *
     * @param vehicle the vehicle
     */
    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }
}
