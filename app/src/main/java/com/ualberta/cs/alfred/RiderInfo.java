package com.ualberta.cs.alfred;


/**
 * The type Rider info. It stores specific info about a rider
 *
 * @author mmcote
 * @version 1.2
 */
public class RiderInfo {

    private String creditCardNumber;

    /**
     * Instantiates a new Rider.
     *
     * @param creditCardNumber the credit card number
     */
    public RiderInfo(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
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
}
