package com.ualberta.cs.alfred;

import android.content.Context;
import android.location.*;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author carlcastello
 */

public class GeoCoder {

    private static GeoCoder instance = new GeoCoder();

    private Geocoder geoCoder;
    private String address;
    private String latitudeString;
    private String longitudeString;
    private Double latitudeValue;
    private Double longitudeValue;

    private List<android.location.Address> addressCoordinates;

    private GeoCoder() {

    }

    /**
     * static GeoCoder
     * @return
     */
    public static GeoCoder getInstance() {
        return instance;
    }

    /**
     * set arguments of GeoCoder
     * @param context the context being passed in
     */
    public void geoSetArguments(Context context) {
        this.geoCoder = new Geocoder(context, Locale.CANADA);
    }

    /**
     * set address
     * @param address the address string being passed in
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * calculate coordinates
     */
    public void calculateCoordinatesString(){
        try {
            addressCoordinates = geoCoder.getFromLocationName(address, 1);
            if (addressCoordinates.size() > 0) {
                latitudeString = String.valueOf(addressCoordinates.get(0).getLatitude());
                longitudeString = String.valueOf(addressCoordinates.get(0).getLongitude());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * calculate coordinates value
     */
    public void calculateCoordinatesValue(){
        try {
            addressCoordinates = geoCoder.getFromLocationName(address, 1);
            if (addressCoordinates.size() > 0) {
                latitudeValue = addressCoordinates.get(0).getLatitude();
                longitudeValue = addressCoordinates.get(0).getLongitude();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * get latitude
     * @return the string of the latitude
     */
    public String getLatitudeString() {
        return latitudeString;
    }

    /**
     * get longitude
     * @return the string of the logitude
     */
    public String getLongitudeString() {
        return longitudeString;
    }

    /**
     * get latitude value
     * @return the latitude Double
     */
    public Double getLatitudeValue() {
        return latitudeValue;
    }

    /**
     * get longitude value
     * @return the longitude Double
     */
    public Double getLongitudeValue() {
        return longitudeValue;
    }
}
