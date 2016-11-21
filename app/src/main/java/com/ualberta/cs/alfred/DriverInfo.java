package com.ualberta.cs.alfred;

/**
 * This class is used differentiate the explicit driver data that will be stored seperate from
 * the same user's rider data. As all users start as riders, it is only optional to become a
 * driver.
 *
 * @author mmcote
 * @version 1.0
 */
public class DriverInfo {
    private String userName;
    private String licenceNumber;
    private Rating driverRating;
    private Vehicle vehicle;

    /**
     * Instantiates a new null Driver info.
     */
    public DriverInfo() {
        this.userName = null;
        this.licenceNumber = null;
        this.driverRating = new Rating();
        this.vehicle = null;
    }

    /**
     * Instantiates a new Driver info.
     *
     * @param userName      the username
     * @param licenceNumber the licence number (on the users given license)
     */
    public DriverInfo(String userName, String licenceNumber,Vehicle vehicle) {
        this.userName = userName;
        this.licenceNumber = licenceNumber;
        this.driverRating = new Rating();
        this.vehicle = vehicle;
    }

    /**
     * Gets user name.
     *
     * @return the user name
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Gets licence number.
     *
     * @return the licence number
     */
    public String getLicenceNumber() {
        return licenceNumber;
    }

}
