package com.ualberta.cs.alfred;

import java.util.Date;

/**
 * Created by mmcote on 2016-11-09.
 */
public class Driver extends User{
    private String licenceNumber;
    private String plateNumber;
    private RequestList bidList;

    public Driver() {
        super();
    }

    public Driver(Rider rider, DriverInfo driverInfo) {
        super(rider.getFirstName(), rider.getLastName(), rider.getUserName(), rider.getDateOfBirth(), rider.getPhoneNumber(), rider.getEmail());
        this.licenceNumber = driverInfo.getLicenceNumber();
        this.plateNumber = driverInfo.getPlateNumber();
    }

    public RequestList getBidList() {
        return bidList;
    }

    public void addBid(Request bid) {
        this.bidList.addRequest(bid);
    }

    public String getLicenceNumber() {
        return licenceNumber;
    }

    public void setLicenceNumber(String licenceNumber) {
        this.licenceNumber = licenceNumber;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }
}

