package com.ualberta.cs.alfred;


import java.io.Serializable;

/**
 * Address class which stores details of the address.
 *
 * @author carlcastello on 07/11/16.
 * @version 1.2
 */
public class Address implements Serializable{

    //code from https://www.youtube.com/watch?v=Gi46yco8OJg
    private static final long serialVersionUID = 1L;

    private String location;
    private double[] coordinates;

    /**
     * Instantiates a new Address.
     */
    public Address(){
        this.location = null;
        this.coordinates = new double[2];
        this.coordinates[0] = 0.0;
        this.coordinates[1] = 0.0;

    }

    /**
     * Instantiates a new Address.
     *
     * @param location  the location
     * @param latitude  the latitude
     * @param longitude the longitude
     */
    public Address(String location, double longitude, double latitude) {
        this.location = location;
        this.coordinates = new double[2];
        this.coordinates[0] = longitude;
        this.coordinates[1] = latitude;
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
     * Sets coordinates.
     *
     * @param longitude the longitude
     * @param latitude  the latitude
     */
    public void setCoordinates(double longitude, double latitude) {
        this.coordinates[0] = longitude;
        this.coordinates[1] = latitude;
    }


    /**
     * Get coordinates double [ ].
     *
     * @return the double [ ]
     */
    public double[] getCoordinates() {
        return this.coordinates;
    }

    /**
     * Gets longitude.
     *
     * @return the longitude
     */
    public double getLongitude() {
        return this.coordinates[0];
    }

    /**
     * Sets longitude.
     *
     * @param longitude the longitude
     */
    public void setLongitude(double longitude) {
        this.coordinates[0] = longitude;
    }


    /**
     * Gets latitude.
     *
     * @return the latitude
     */
    public double getLatitude() {
        return this.coordinates[1];
    }

    /**
     * Sets latitude.
     *
     * @param latitude the latitude
     */
    public void setLatitude(double latitude) {
        this.coordinates[1] = latitude;
    }



}