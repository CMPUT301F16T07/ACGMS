package com.ualberta.cs.alfred;

/**
 * This class is used to create and initialize a driver that will be used to
 * represent a user in Driver Mode.
 *
 * @author mmcote
 * @version 1.0
 */
public class Driver extends User{
    private String licenceNumber;
    private String plateNumber;
    private RequestList bidList;
    private Rating driverRating;

    /**
     * Instantiates a new Driver.
     */
    public Driver() {
        super();
    }
    /**
     * Instantiates a new Driver.
     *
     * @param driverInfo driver's information
     * @param rider a rider class consisting of the driver's details
     *
     */
    public Driver(Rider rider, DriverInfo driverInfo) {
        super(rider.getFirstName(), rider.getLastName(), rider.getUserName(), rider.getDateOfBirth(), rider.getPhoneNumber(), rider.getEmail());
        this.licenceNumber = driverInfo.getLicenceNumber();
        this.plateNumber = driverInfo.getPlateNumber();
        this.driverRating = new Rating();
    }

    /**
     * gets the list of bidded rides.
     */
    public RequestList getBidList() {
        return bidList;
    }


    /**
     * Adds a ride bid to the bid offer
     *
     * @param bid the bidded amount
     */
    public void addBid(Request bid) {
        this.bidList.addRequest(bid);
    }


    /**
     * gets the license number.
     *
     * @return driver's license number
     */
    public String getLicenceNumber() {
        return licenceNumber;
    }


    /**
     * update license number
     */
    public void setLicenceNumber(String licenceNumber) {
        this.licenceNumber = licenceNumber;
    }

    /**
     * Get car plate number.
     *
     * @return returns the driver's plate number
     */
    public String getPlateNumber() {
        return plateNumber;
    }


    /**
     * update new calculated rating
     *
     * @param new_rating the new rating given to the driver
     */
    public void updateRating(float new_rating){
        this.driverRating.updateRating(new_rating);
    }

    /**
     * returns driver's rating
     *
     * @return returns the current rating
     */
    public float getRating(){
        return this.driverRating.getRating();
    }



    /**
     * Update plate number.
     *
     * @param plateNumber the driver's plate number to be updated
     */
    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }
}

