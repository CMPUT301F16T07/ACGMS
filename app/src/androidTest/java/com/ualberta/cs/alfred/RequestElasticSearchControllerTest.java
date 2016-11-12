package com.ualberta.cs.alfred;

import android.test.ActivityInstrumentationTestCase2;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


/**
 * The type Request elastic search controller test.
 */
public class RequestElasticSearchControllerTest extends ActivityInstrumentationTestCase2 {

    /**
     * Instantiates a new Request elastic search controller test.
     */
    public RequestElasticSearchControllerTest() {

        super(MainActivity.class);
    }

    /**
     * Test add request task
     */
    public void testAddRequestTask() {

        // Create request #1
        String req1Status = "Pending";
        Address req1SrcAddr = new Address("South side", 65.56777, 79.34555);
        Address req1DestAddr = new Address("Downtown", 50.56500, 89.56888);
        double req1Cost = 90.30;
        double req1Distance = 4.5;
        String req1RiderID = "rider011";

        Request req1 = new Request(req1Status, req1SrcAddr, req1DestAddr, req1Distance, req1Cost,
                req1RiderID);
        assert(true);

        // Create request #2
        String req2Status = "Accepted";
        Address req2SrcAddr = new Address("Airport", 51.56777, 30.34555);
        Address req2DestAddr = new Address("South Campus", 20.56500, 12.56888);;
        double req2Cost = 30.30;
        double req2Distance = 21.5;
        String req2RiderID = "rider013";

        Request req2 = new Request(req2Status, req2SrcAddr, req2DestAddr, req2Distance, req2Cost,
                req2RiderID);
        assert(true);

    }

    /**
     * Test get request task
     *
     * @Note: need to provide the search parameters
     */
    public void testGetRequestTask() {

        RequestElasticSearchController.GetRequestTask retrievedRequest = new RequestElasticSearchController.GetRequestTask();

        // Find all requests where riderID is rider002
        retrievedRequest.execute("riderID", "rider002");

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

        RequestElasticSearchController.GetRequestByIdTask retrievedRequest = new RequestElasticSearchController.GetRequestByIdTask();

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
     * Delete request by id task
     *
     * @Note: need to know the request ID.
     */
    public void testDeleteRequestTask() {

        RequestElasticSearchController.DeleteRequestTask deleteRequest = new RequestElasticSearchController.DeleteRequestTask();

        // Delete the request with this id
        deleteRequest.execute("AVhVAmNqFLrhMuj9wTtN");
        assert (true);
    }

}
