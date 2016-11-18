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

        // Find all requests where riderID is rider011
        retrievedRequest.execute("riderID", "rider011");

        try {
            ArrayList<Request> requests = retrievedRequest.get();
            for (Request request : requests) {
                System.out.println("====================");
                System.out.println("Request ID is: " + request.getRequestID());
                System.out.println("Rider ID is: " + request.getRiderID());
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

    /**
     * Test get request by multiple preferences task.
     *
     * @Note: need to provide the search parameters
     */
    public void testGetRequestByMultiplePreferencesTask() {

        RequestESGetController.GetRequestByMultiplePreferencesTask retrievedRequest =
                new RequestESGetController.GetRequestByMultiplePreferencesTask();
        /*
        // Find all requests where riderID is rider011 and requestStatus is Pending
        retrievedRequest.execute(
                "riderID", "string", "rider011",
                "requestStatus", "string", "Pending"
        );
        */

        /*
        // Find a request where id = AVhUaYHOFLrhMuj9wTs4
        retrievedRequest.execute(
                "_id", "string", "AVhUaYHOFLrhMuj9wTs4"
        );
        */

        /*
        // Find all requests where riderID is rider011
        retrievedRequest.execute("riderID", "string", "rider011");
        */

        // Get open requests by keyword
        String keyword = "South";
        retrievedRequest.execute(
                "requestStatus", "string", "Pending",
                "_all", "string", "South"
        );

        try {
            ArrayList<Request> requests = retrievedRequest.get();
            for (Request request : requests) {
                System.out.println("====================");
                System.out.println("Request ID is: " + request.getRequestID());
                System.out.println("Rider ID is: " + request.getRiderID());
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

}
