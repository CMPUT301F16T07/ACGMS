package com.ualberta.cs.alfred;

import java.util.ArrayList;

/**
 * Created by mmcote on 2016-11-05.
 */

public class RequestList {
    private ArrayList<com.ualberta.cs.alfred.Request> requests;

    public RequestList() {
        this.requests = new ArrayList<com.ualberta.cs.alfred.Request>();
    }

    public RequestList(ArrayList<com.ualberta.cs.alfred.Request> requests) {
        this.requests = requests;
    }

    public void addRequest(com.ualberta.cs.alfred.Request requestGiven) {
        this.requests.add(requestGiven);
    }
}
