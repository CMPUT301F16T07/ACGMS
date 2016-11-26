package com.ualberta.cs.alfred;

import android.content.Context;
import android.location.*;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by carlcastello on 25/11/16.
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

    public static GeoCoder getInstance() {
        return instance;
    }

    public void geoSetArguments(Context context) {
        this.geoCoder = new Geocoder(context, Locale.getDefault());
    }

    public void setAddress(String address) {
        this.address = address;
    }

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

    public String getLatitudeString() {
        return latitudeString;
    }

    public String getLongitudeString() {
        return longitudeString;
    }

    public Double getLatitudeValue() {
        return latitudeValue;
    }

    public Double getLongitudeValue() {
        return longitudeValue;
    }
}
