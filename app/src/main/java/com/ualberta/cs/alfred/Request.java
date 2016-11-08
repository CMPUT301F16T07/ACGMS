package com.ualberta.cs.alfred;

import java.util.Date;

/**
 * Creates the Request type
 * @author ookmm
 * @version 1
 */
public class Request {

    // Count for IDs
    private static Integer requestCount = 0;

    private String requestID;
    private String requestStatus;
    //private Address sourceAddress; // TODO: Uncomment this after Address Class implemented
    //private Address destinationAddress; // TODO: Uncomment this after Address Class implemented
    private Double cost;
    private Double distance;
    private String driverID;
    private String riderID;
    private Date requestDate;


    /**
     * Instantiates a new Request.
     *
     * @param requestStatus the request status
     * @param cost          the cost
     * @param distance      the distance
     * @param riderID       the rider id
     */
    public Request(String requestStatus, Double cost, Double distance, String riderID) {

        // Auto increment requestCount each time the constructor is called
        ++requestCount;

        this.requestID = requestCount.toString();
        this.requestStatus = requestStatus;
        this.cost = cost;
        this.distance = distance;
        this.driverID = null;
        this.riderID = riderID;
        this.requestDate = new Date();
    }

    /**
     * Gets request id.
     *
     * @return the request id
     */
    public String getRequestID() {
        return requestID;
    }

    /**
     * Sets request id.
     *
     * @param requestID the request id
     */
    public void setRequestID(String requestID) {
        this.requestID = requestID;
    }

    /**
     * Gets request status.
     *
     * @return the request status
     */
    public String getRequestStatus() {
        return requestStatus;
    }

    /**
     * Sets request status.
     *
     * @param requestStatus the request status
     */
    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }

    /**
     * Gets cost.
     *
     * @return the cost
     */
    public Double getCost() {
        return cost;
    }

    /**
     * Sets cost.
     *
     * @param cost the cost
     */
    public void setCost(Double cost) {
        this.cost = cost;
    }

    /**
     * Gets distance.
     *
     * @return the distance
     */
    public Double getDistance() {
        return distance;
    }

    /**
     * Sets distance.
     *
     * @param distance the distance
     */
    public void setDistance(Double distance) {
        this.distance = distance;
    }

    /**
     * Gets driver id.
     *
     * @return the driver id
     */
    public String getDriverID() {
        return driverID;
    }

    /**
     * Sets driver id.
     *
     * @param driverID the driver id
     */
    public void setDriverID(String driverID) {
        this.driverID = driverID;
    }

    /**
     * Gets rider id.
     *
     * @return the rider id
     */
    public String getRiderID() {
        return riderID;
    }

    /**
     * Sets rider id.
     *
     * @param riderID the rider id
     */
    public void setRiderID(String riderID) {
        this.riderID = riderID;
    }

    /**
     * Gets request date.
     *
     * @return the request date
     */
    public Date getRequestDate() {
        return requestDate;
    }

    /**
     * Sets request date.
     *
     * @param requestDate the request date
     */
    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

}
