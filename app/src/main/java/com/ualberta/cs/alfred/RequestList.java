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

    public Request getRequest(int i) {
        return requestList.get(i);
    }

    public List<Request> getRequest() {
        return requestList;
    }

    public List<Request> getRequestOrdered() {

        Collections.sort(requestList, new Comparator<Request>() {
            public int compare(Request r1, Request r2) {
                return r1.getRequestDate().compareTo(r2.getRequestDate());
            }
        });

        return this.requestList;
    }

    public List<Request> getSpecificRequestList(String requestStatus) {

        List specificRequestList = new ArrayList<Request>();

        if (requestList != null) {
            for (Request request : requestList) {
                if (request.getRequestStatus() == requestStatus) {
                    specificRequestList.add(request);
                } else {
                    return null;
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
