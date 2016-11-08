package com.ualberta.cs.alfred;

/**
 * Created by carlcastello on 07/11/16.
 */

public class Address {
    private String Location;
    private double Latitude;
    private double Longitude;

    public Address(){
        this.Location = null;
        this.Latitude = 0;
        this.Longitude = 0;
    }

    // Constructor for testing
    public Address(String location,double latitude,double longitude){
        this.Location = location;
        this.Latitude = latitude;
        this.Longitude = longitude;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }
}