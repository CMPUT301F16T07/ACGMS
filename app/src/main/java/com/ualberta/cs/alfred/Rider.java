package com.ualberta.cs.alfred;

import java.util.Date;

/**
 * Created by mmcote on 2016-11-05.
 */

public class Rider extends User {

    private String creditCardNumber;
    private RequestList requests;

    public Rider() {
        super();
    }

    public Rider(String firstName, String lastName, String userName, Date dateOfBirth,
                 String phoneNumber, String email, String creditCardNumber) {

        super(firstName, lastName, userName, dateOfBirth, phoneNumber, email);
        this.creditCardNumber = creditCardNumber;
        this.save();
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    public RequestList getRequests() {
        return requests;
    }

    public void addRequest(Request newRequest) {
        this.requests.addRequest(newRequest);
    }

    private void save() {
        UserElasticSearchController.AddRider addRider = new UserElasticSearchController.AddRider();
        addRider.execute(this);
    }
}
