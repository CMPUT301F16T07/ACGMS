package com.ualberta.cs.alfred;


/**
 * Create the type Address.
 *
 * @author carlcastello on 07/11/16.
 * @version 1.1
 */
public class Address {

    private String location;
    private double latitude;
    private double longitude;

    /**
     * Instantiates a new Address.
     */
    public Address(){
        this.location = null;
        this.latitude = 0.0;
        this.longitude = 0.0;
    }

    /**
     * Instantiates a new Address.
     *
     * @param location  the location
     * @param latitude  the latitude
     * @param longitude the longitude
     */
    public Address(String location, double latitude, double longitude) {
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Gets location.
     *
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets location.
     *
     * @param location the location
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Gets latitude.
     *
     * @return the latitude
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Sets latitude.
     *
     * @param latitude the latitude
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * Gets longitude.
     *
     * @return the longitude
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Sets longitude.
     *
     * @param longitude the longitude
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}