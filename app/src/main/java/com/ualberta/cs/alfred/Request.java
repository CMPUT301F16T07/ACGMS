package com.ualberta.cs.alfred;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;

import io.searchbox.annotations.JestId;

/**
 * Holds information related to requests
 *
 * @author ookmm
 * @version 1.2
 */
public class Request implements Serializable{

    //code from https://www.youtube.com/watch?v=Gi46yco8OJg
    private static final long serialVersionUID = 1L;

    // Count for IDs
    //private int requestCount = 4;
    @JestId
    private String requestID;

    private String requestStatus;
    private Address sourceAddress;
    private Address destinationAddress;
    private double cost;
    private double distance;
    private String driverID;
    private ArrayList<String> driverIDList;
    private String riderID;
    private Date requestDate;
    private DecimalFormat df = new DecimalFormat("0.00");

    public Request() {

    }


    public Request(String requestStatus, Address sourceAddress, Address destinationAddress,
                   double distance, double cost, String riderID) {

        this.requestID = null;
        this.requestStatus = requestStatus;
        this.sourceAddress = sourceAddress;
        this.destinationAddress = destinationAddress;

        this.distance = distance;
        this.cost = cost;
        this.driverID = null;
        this.driverIDList = new ArrayList<>();
        this.riderID = riderID;
        this.requestDate = new Date();
        this.save();
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
     * Gets source address.
     *
     * @return the source address
     */
    public Address getSourceAddress() {
        return sourceAddress;
    }

    /**
     * Sets source address.
     *
     * @param sourceAddress the source address
     */
    public void setSourceAddress(Address sourceAddress) {
        this.sourceAddress = sourceAddress;
    }

    /**
     * Gets destination address.
     *
     * @return the destination address
     */
    public Address getDestinationAddress() {
        return destinationAddress;
    }

    /**
     * Sets destination address.
     *
     * @param destinationAddress the destination address
     */
    public void setDestinationAddress(Address destinationAddress) {
        this.destinationAddress = destinationAddress;
    }

    /**
     * Gets distance.
     *
     * @return the distance
     */
    public double getDistance() {
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
     * Gets cost.
     *
     * @return the cost
     */
    public double getCost() {
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

    public void addDriverIDToList(String newDriverID) {
        this.driverIDList.add(newDriverID);
    }

    public ArrayList<String> getDriverIDList() {
        return driverIDList;
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

    private void save() {

        RequestElasticSearchController.AddRequestTask addRequestsTask = new RequestElasticSearchController.AddRequestTask();
        addRequestsTask.execute(this);
    }

    @Override
    public String toString(){
        //return this.getRequestID()+"\n"+this.getSourceAddress().getLocation() +"-->"+
        //        this.getDestinationAddress().getLocation()+"\n"+Double.toString(this.getDistance());
        return this.getRequestID()+"\n"+this.getSourceAddress().getLocation() +"-->"+
                this.getDestinationAddress().getLocation()+"\n"+df.format(new Double(this.getDistance()));
    }
}
