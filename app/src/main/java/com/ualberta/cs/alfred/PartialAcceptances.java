package com.ualberta.cs.alfred;

/**
 * Created by Avery on 11/27/2016.
 *
 * Class which holds the necessary information for Drivers who accept a request while offline
 */

public class PartialAcceptances {
    private String from;
    private String userID;
    private String mode;
    private Request passedRequest;
    private String driverSelected;


    /**
     * Constructor for creating a PartialAcceptances class
     * @param from the from
     * @param userID the userID
     * @param mode the mode
     * @param passedRequest the passed request
     * @param driverSelected the selected driver
     */
    public PartialAcceptances(String from, String userID, String mode, Request passedRequest, String driverSelected){
            this.from = from;
            this.userID = userID;
            this.mode = mode;
            this.passedRequest = passedRequest;
            this.driverSelected = driverSelected;
        }

    /**
     * gets the from
     * @return from
     */
    public String getFrom() {
        return from;
    }

    /**
     * gets the userID
     * @return userID
     */
    public String getUserID() {
        return userID;
    }

    /**
     * gets the mode
     * @return mode
     */
    public String getMode() {
        return mode;
    }

    /**
     * gets the passed requests
     * @return passedRequest
     */
    public Request getPassedRequest() {
        return passedRequest;
    }


    /**
     * gets the selected driver
     * @return driverSelected
     */
    public String getDriverSelected() {
        return driverSelected;
    }
}
