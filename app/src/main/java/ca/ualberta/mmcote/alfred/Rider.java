package ca.ualberta.mmcote.alfred;

/**
 * Created by mmcote on 2016-11-05.
 */

public class Rider extends User {
    private String creditCardNumber;
    private RequestList requests;

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    public RequestList getRequests() {
        return requests;
    }
}
