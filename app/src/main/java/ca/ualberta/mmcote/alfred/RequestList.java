package ca.ualberta.mmcote.alfred;

import java.util.ArrayList;

/**
 * Created by mmcote on 2016-11-05.
 */

public class RequestList {
    private ArrayList<Request> requests;

    public RequestList() {
        this.requests = new ArrayList<Request>();
    }

    public RequestList(ArrayList<Request> requests) {
        this.requests = requests;
    }

    public void addRequest(Request requestGiven) {
        this.requests.add(requestGiven);
    }
}
