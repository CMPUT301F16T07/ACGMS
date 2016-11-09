package com.ualberta.cs.alfred;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * The type Request list.
 */
public class RequestList {


    /**
     * The Request list.
     */
    protected List<Request> requestList;

    /**
     * Instantiates a new Request list.
     */
    public RequestList() {
        this.requestList = new ArrayList<Request>();
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
            if (requestID.equals(r.getRequestID())) {
                aRequest = r;
                break;
            }
        }
        return aRequest;
    }

    /**
     * Gets request ordered.
     *
     * @return the request ordered
     */
    public List<Request> getRequestOrdered() {

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
