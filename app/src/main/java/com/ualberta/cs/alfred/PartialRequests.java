package com.ualberta.cs.alfred;

/**
 * Created by Avery on 11/25/2016.
 *
 *
 * class that stores the details of requests made while connectivity is lost
 */

public class PartialRequests {
    private String mode;
    private String userID;
    private String status;
    private String sAddress;
    private String eAddress;
    private String x1;
    private String x2;
    private String y1;
    private String y2;

    /**
     * constructor to create a new instance of PartialRequests
     * @param mode mode, either AddressMODE or CoorMODE
     * @param userID the user id of the rider
     * @param status the status of the requests
     * @param sAddress the startign address
     * @param eAddress the end address
     * @param x1 the x coordinate of the starting address
     * @param x2 the y coordinate of the starting address
     * @param y1 the x coordinate of the destination address
     * @param y2 the y coordinate of the destination address
     */
    public PartialRequests(String mode, String userID, String status, String sAddress, String eAddress, String x1,String x2,String y1,String y2){
        this.mode = mode;
        this.userID = userID;
        this.status = status;
        this.sAddress = sAddress;
        this.eAddress = eAddress;
        this.x1=x1;
        this.x2 =x2;
        this.y1=y1;
        this.y2=y2;
    }

    /**
     * returns the mode
     * @return the mode
     */
    public String getMode() {
        return mode;
    }

    /**
     * returns user ID
     * @return user ID
     */
    public String getUserID() {
        return userID;
    }

    /**
     * returns status
     * @return status
     */
    public String getStatus() {
        return status;
    }

    /**
     * returns starting address
     * @return starting address
     */
    public String getsAddress() {
        return sAddress;
    }

    /**
     * returns destination address
     * @return destination address
     */
    public String geteAddress() {
        return eAddress;
    }

    /**
     * returns x coordinate of starting address
     * @return x coordinate of starting address
     */
    public String getX1() {
        return x1;
    }

    /**
     * returns y coordinate of starting address
     * @return y coordinate of starting address
     */
    public String getX2() {
        return x2;
    }

    /**
     * returns x coordinate of destination address
     * @return x coordinate of destination address
     */
    public String getY1() {
        return y1;
    }

    /**
     * returns y coordinate of destination address
     * @return y coordinate of destination address
     */
    public String getY2() {
        return y2;
    }
}
