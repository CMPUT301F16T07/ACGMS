package com.ualberta.cs.alfred;

/**
 * Created by mmcote on 2016-11-10.
 */

public class DriverInfo {
    private String userName;
    private String licenceNumber;
    private String plateNumber;

    public DriverInfo() {
    }

    public DriverInfo(String userName, String licenceNumber, String plateNumber) {
        this.userName = userName;
        this.licenceNumber = licenceNumber;
        this.plateNumber = plateNumber;
        this.save();
    }

    private void save() {
        UserElasticSearchController.AddDriverInfo addDriverInfo = new UserElasticSearchController.AddDriverInfo();
        addDriverInfo.execute(this);
    }

    public String getUserName() {
        return userName;
    }

    public String getLicenceNumber() {
        return licenceNumber;
    }

    public String getPlateNumber() {
        return plateNumber;
    }
}
