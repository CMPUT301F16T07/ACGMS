package com.ualberta.cs.alfred;

import android.test.ActivityInstrumentationTestCase2;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Test cases for getting item from requests
 *
 * @author ookmm
 * @version 1.4
 * @see RequestESGetController
 */
public class RequestESGetControllerTest extends ActivityInstrumentationTestCase2 {

    /**
     * Instantiates a new Request es get controller test.
     */
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
                System.out.println("Request ID is: " + request.getRiderID());
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
        retrievedRequest.execute("AVh6kX-AdE2DZPCrf9kW");

        try {
            Request request = retrievedRequest.get();

            System.out.println("====================");
            System.out.println("Request ID: " + request.getRequestID());
            System.out.println("Request Distance: " + request.getDistance());
            System.out.println("Request Cost: " + request.getCost());
            System.out.println("Request Status: " + request.getRequestStatus());
            System.out.println("Request Source Address Location: " + request.getSourceAddress().getLocation());
            System.out.println("Request Destination Address Location: " + request.getDestinationAddress().getLocation());

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
        String keyword = "116";
        retrievedRequest.execute(
                "requestStatus", "string", "Pending",
                "_all", "string", keyword
        );

        /*
        // Get all requests
        retrievedRequest.execute(
                "match_all", "all", "{}"
        );
        */

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
     * Test get request by multiple preferences task sorted by price (cost).
     *
     * @Note: need to provide the search parameters, and if sorted by either asc or desc.
     */
    public void testGetRequestSortedByPriceTask() {

        RequestESGetController.GetRequestSortedByPriceTask retrievedRequest =
                new RequestESGetController.GetRequestSortedByPriceTask();

        /**
         * Find all requests with status "Pending" where rider is rider011
         * sorted by price in descending order.
         */
        /*
        String orderBy = "desc";
        retrievedRequest.execute(
                "requestStatus", "string", "Pending",
                "riderID", "string", "rider011",
                orderBy
        );
        */

        // Get all requests sorted by by price in descending order.
        String orderBy = "desc";
        retrievedRequest.execute(
                "match_all", "all", "{}", orderBy
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

    /**
     * Test get request by multiple preferences task sorted by price per kilometer.
     *
     * @Note: need to provide the search parameters, and if sorted by either asc or desc.
     */
    public void testGetRequestSortedByPricePerKmTask() {

        RequestESGetController.GetRequestSortedByPricePerKmTask retrievedRequest =
                new RequestESGetController.GetRequestSortedByPricePerKmTask();

        /**
         * Find all requests with status "Pending" where rider is rider011
         * sorted by price in descending order.
         */
        /*
        String orderBy = "desc";
        retrievedRequest.execute(
                "requestStatus", "string", "Pending",
                "riderID", "string", "rider011",
                orderBy
        );
        */

        // Get all requests sorted by by price in descending order.
        String orderBy = "desc";
        retrievedRequest.execute(
                "match_all", "all", "{}", orderBy
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

    /**
     * Test get request by multiple preferences close to a certain location.
     */
    public void testGetRequestByLocationTask() {

        RequestESGetController.GetRequestByLocationTask retrievedRequest =
                new RequestESGetController.GetRequestByLocationTask();


        // Get all requests within this distance
        String distance = "20000km";

        double longitude = -113.530152;
        String longitudeAsString = String.valueOf(longitude);

        double latitude = 53.5416253;
        String latitudeAsString = String.valueOf(latitude);
        String coordinates = String.format("[%s, %s]", longitudeAsString, latitudeAsString);

        /*
        // Find all Pending requests where Rider ID is AViOEboRdE2DZPCrf9nT within 20000km
        retrievedRequest.execute(
                "riderID", "string", "AViOEboRdE2DZPCrf9nT",
                "requestStatus", "string", "Accepted",
                distance, coordinates
        );
        */


        // Get all requests within 20000km distance
        retrievedRequest.execute(
                "match_all", "all", "{}",
                distance, coordinates
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
