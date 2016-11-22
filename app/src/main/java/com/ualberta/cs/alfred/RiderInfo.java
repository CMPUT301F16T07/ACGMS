package com.ualberta.cs.alfred;

import java.util.Date;
import java.util.concurrent.ExecutionException;


/**
 * The type Rider info. It stores specific info about a rider
 *
 * @author mmcote
 * @version 1.1
 */
public class RiderInfo {

    private String creditCardNumber;
    private RequestList requests;

    /**
     * Instantiates a new Rider.
     *
     * @param creditCardNumber the credit card number
     */
    public RiderInfo(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
        this.requests = new RequestList();
    }

    /**
     * Gets credit card number.
     *
     * @return the credit card number
     */
    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    /**
     * Sets credit card number.
     *
     * @param creditCardNumber the credit card number
     */
    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    /**
     * Gets requests.
     *
     * @return the requests
     */
    public RequestList getRequests() {
        return requests;
    }

    /**
     * Add request.
     *
     * @param newRequest the new request
     */
    public void addRequest(Request newRequest) {
        this.requests.addRequest(newRequest);
    }
}
