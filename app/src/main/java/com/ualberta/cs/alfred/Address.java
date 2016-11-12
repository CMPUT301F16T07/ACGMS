package com.ualberta.cs.alfred;

import java.io.Serializable;

/**
 * Created by carlcastello on 07/11/16.
 */

public class Address implements Serializable{

    //code from https://www.youtube.com/watch?v=Gi46yco8OJg
    private static final long serialVersionUID = 1L;

    private String location;
    private double latitude;
    private double longitude;

    public Address(){
        this.location = null;
        this.latitude = 0;
        this.longitude = 0;
    }

    // Constructor for testing
    public Address(String location,double latitude,double longitude){
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        location = location;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        longitude = longitude;
    }
}