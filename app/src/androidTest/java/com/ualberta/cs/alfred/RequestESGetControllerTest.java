package com.ualberta.cs.alfred;

import android.test.ActivityInstrumentationTestCase2;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Test cases for getting item from requests
 *
 * @author ookmm
 * @version 1.0
 * @see RequestESGetController
 */
public class RequestESGetControllerTest extends ActivityInstrumentationTestCase2 {

    public RequestESGetControllerTest() {

        super(MainActivity.class);
    }

    /**
     * Test get request task. Can be used to retrieve request
     *
     * @Note: need to provide the search parameters
     */
    public void testGetRequestTask() {

        RequestESGetController.GetRequestTask retrievedRequest =
                new RequestESGetController.GetRequestTask();

        // Find all requests where riderID is rider001
        retrievedRequest.execute("riderID", "rider001");

        try {
            ArrayList<Request> requests = retrievedRequest.get();
            for (Request request : requests) {
                System.out.println("====================");
                System.out.println("Request ID is: " + request.getRequestID());
                System.out.println("Request ID is: " + request.getRiderID());
                System.out.println("Request Distance: " + request.getDistance());
                System.out.println("Request Cost: " + request.getCost());
                System.out.println("Request Status: " + request.getRequestStatus());
                System.out.println("====================");

                assert (true);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
     * Test get request by id task
     *
     * @Note: need to know the request ID.
     */
    public void testGetRequestByIdTask() {

        RequestESGetController.GetRequestByIdTask retrievedRequest =
                new RequestESGetController.GetRequestByIdTask();

        // Find the request with this id
        retrievedRequest.execute("AVhUaYHOFLrhMuj9wTs4");

        try {
            Request request = retrievedRequest.get();

            System.out.println("====================");
            System.out.println("Request ID: " + request.getRequestID());
            System.out.println("Request Distance: " + request.getDistance());
            System.out.println("Request Cost: " + request.getCost());
            System.out.println("Request Status: " + request.getRequestStatus());

            System.out.println("====================");
            assert (true);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

}
