package com.ualberta.cs.alfred;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * Creates the type RequestList.
 *
 * @author ookmm
 * @version 1.2
 */
public class RequestList {

    /**
     * The Request list.
     */
    protected List<Request> requestList;

    public ArrayList<Request> returnArrayList() {
        return (ArrayList<Request>) requestList;
    };

    /**
     * Instantiates a new Request list.
     */
    public RequestList() {
        this.requestList = new ArrayList<Request>();
    }

    /**
     * Instantiates a new Request list (Assignment Operator)
     */
    public RequestList(ArrayList<Request> requestList) {
        this.requestList = requestList;
    }
    /**
     * Gets request list.
     *
     * @return the request list
     */
    public List<Request> getRequestList() {
        return requestList;
    }

    /**
     * Gets request.
     *
     * @param requestID the request id
     * @return the request
     */
    public Request getRequest(String requestID) {

        Request aRequest = null;
        for (Request r : requestList) {
            if (requestID == r.getRequestID()) {
                aRequest = r;
                break;
            }
        }
        return aRequest;
    }

    /**
     * Gets request list ordered by date.
     *
     * @return the request ordered list
     */
    public List<Request> getRequestOrderedList() {

        Collections.sort(requestList, new Comparator<Request>() {
            public int compare(Request r1, Request r2) {
                return r1.getRequestDate().compareTo(r2.getRequestDate());
            }
        });

        return this.requestList;
    }

    /**
     * Gets specific request list.
     *
     * @param requestStatus the request status
     * @return the specific request list
     */
    public List<Request> getSpecificRequestList(String requestStatus) {

        List<Request> specificRequestList = new ArrayList<Request>();

        if (requestList != null) {
            for (Request r : requestList) {
                if (r.getRequestStatus().equals(requestStatus)) {
                    specificRequestList.add(r);
                }
            }
        }

        return specificRequestList;
    }

    public RequestList removeDriver(String userName) {
        for (Request request : requestList) {
            if (request.getBiddingDrivers().contains(userName)) {
                requestList.remove(request);
            }
        }
        return new RequestList((ArrayList<Request>) requestList);
    }

    public RequestList getWithDriver(String userName) {
        for (Request request : requestList) {
            if (!request.getBiddingDrivers().contains(userName)) {
                requestList.remove(request);
            }
        }
        return new RequestList((ArrayList<Request>) requestList);
    }


    /**
     * Has request boolean.
     *
     * @param request the request
     * @return the boolean
     */
    public Boolean hasRequest(Request request) {
        return requestList.contains(request);
    }

    /**
     * Add request.
     *
     * @param request the request
     */
    public void addRequest(Request request) {
        requestList.add(request);
    }

    public void addMultipleRequest(List<Request> requests) {
        requestList.addAll(requests);
    }


    /**
     * Delete request.
     *
     * @param request the request
     */
    public void deleteRequest(Request request) {
        requestList.remove(request);
    }

    /**
     * Gets count.
     *
     * @return the count
     */
    public int getCount() {
        return requestList.size();
    }
}
