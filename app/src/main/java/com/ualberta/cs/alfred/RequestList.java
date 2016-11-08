package com.ualberta.cs.alfred;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class RequestList {

    protected List<Request> requestList;

    public RequestList() {
        this.requestList = new ArrayList<Request>();
    }

    public List<Request> getRequestList() {
        return requestList;
    }

    public Request getRequest(String requestID) {

        Request aRequest = null;
        for (Request r : requestList) {
            if (requestID.equals(r.getRequestID())) {
                aRequest = r;
                break;
            }
        }
        return aRequest;
    }

    public List<Request> getRequestOrdered() {

        Collections.sort(requestList, new Comparator<Request>() {
            public int compare(Request r1, Request r2) {
                return r1.getRequestDate().compareTo(r2.getRequestDate());
            }
        });

        return this.requestList;
    }

    public RequestList getSpecificRequestList(String requestStatus) {

        RequestList specificRequestList = new RequestList();

        if (requestList != null) {
            for (Request r : requestList) {
                if (r.getRequestStatus().equals(requestStatus)) {
                    specificRequestList.addRequest(r);
                }
            }
        }

        return specificRequestList;
    }

    public Boolean hasRequest(Request request) {
        return requestList.contains(request);
    }

    public void addRequest(Request request) {
        requestList.add(request);
    }


    public void deleteRequest(Request request) {
        requestList.remove(request);
    }

    public int getCount() {
        return requestList.size();
    }
}
