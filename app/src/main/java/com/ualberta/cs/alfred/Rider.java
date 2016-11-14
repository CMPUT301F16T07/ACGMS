package com.ualberta.cs.alfred;

import java.util.Date;
import java.util.concurrent.ExecutionException;


/**
 * This class is used to create and initialize a rider to be used in
 * Rider Mode, this is a subclass of User, with it's opposite being
 * a Driver.
 *
 * @author mmcote
 * @version 1.2
 * @see User
 * @see Driver
 */

public class Rider extends User {

    private String creditCardNumber;
    private RequestList requests;

    /**
     * Instantiates a new null Rider.
     */
    public Rider() {
        super();
    }

    /**
     * Instantiates a new Rider.
     *
     * @param firstName        the first name
     * @param lastName         the last name
     * @param userName         the user name
     * @param dateOfBirth      the date of birth
     * @param phoneNumber      the phone number
     * @param email            the email
     * @param creditCardNumber the credit card number
     */
    public Rider(String firstName, String lastName, String userName, Date dateOfBirth,
                 String phoneNumber, String email, String creditCardNumber) {

        super(firstName, lastName, userName, dateOfBirth, phoneNumber, email);
        this.creditCardNumber = creditCardNumber;
        this.save();
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


    private void save() {
        UserElasticSearchController.AddUser<Rider> addRider = new UserElasticSearchController.AddUser<Rider>();
        addRider.execute(this);
        UserElasticSearchController.GetRider getRider = new UserElasticSearchController.GetRider();
        try {
            this.setUserID(getRider.execute(this.getUserName()).get().getUserID());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }


    /*
    private void save() {

        UserElasticSearchController.AddUser<Rider> addRider = new UserElasticSearchController.AddUser<Rider>();
        addRider.execute(this);
    }
    */

    /*
    private void save() {

        UserElasticSearchController.AddRiderTask addUserTask = new UserElasticSearchController.AddRiderTask();
        addUserTask.execute(this);
    }
    */
}
