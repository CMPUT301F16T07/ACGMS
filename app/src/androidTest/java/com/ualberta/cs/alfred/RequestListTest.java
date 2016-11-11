package com.ualberta.cs.alfred;

import android.test.ActivityInstrumentationTestCase2;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * Creates all the test cases for type RequestList.
 *
 * @author ookmm
 * @version 1.2
 * @see RequestList
 */
public class RequestListTest extends ActivityInstrumentationTestCase2 {

    /**
     * Instantiates a new Request list test.
     */
    public RequestListTest() {

        super(MainActivity.class);
    }

    /**
     * Test empty request list.
     */
    public void testEmptyRequestList() {
        RequestList requestList = new RequestList();
        List<Request> aList = requestList.getRequestList();
        assertTrue("Request List is empty", aList.size() == 0);
    }

    /**
     * Test get request.
     */
    public void testGetRequest() {

        RequestList aList = new RequestList();

        // Test list before adding any element
        assertTrue("Request List has 0 requests", aList.getCount() == 0);


        // Create request #1
        String req1Status = "Pending";
        Address req1SrcAddr = new Address("U of A", 65.56777, 79.34555);
        Address req1DestAddr = new Address("Downtown", 50.56500, 89.56888);;
        double req1Cost = 12.30;
        double req1Distance = 4.5;
        String req1RiderID = "rider001";

        Request req1 = new Request(req1Status, req1SrcAddr, req1DestAddr, req1Distance, req1Cost,
                req1RiderID);

        aList.addRequest(req1);

        // Create request #2
        String req2Status = "Accepted";
        Address req2SrcAddr = new Address("West Ed", 51.56777, 30.34555);
        Address req2DestAddr = new Address("South Campus", 20.56500, 12.56888);;
        double req2Cost = 30.30;
        double req2Distance = 21.5;
        String req2RiderID = "rider001";

        Request req2 = new Request(req2Status, req2SrcAddr, req2DestAddr, req2Distance, req2Cost,
                req2RiderID);

        aList.addRequest(req2);

        // Test list after adding two elements
        assertTrue("Request List has 2 requests", aList.getCount() == 2);

        // Test request #1
        Long testReq1ID = req1.getRequestID();
        assertTrue("Request 1 is not the same", req1.equals(aList.getRequest(testReq1ID)));

        Request req1FromList = aList.getRequest(testReq1ID);
        Long req1IDFromList = req1FromList.getRequestID();
        assertTrue("Request 1 from list not the same", req1.equals(aList.getRequest(req1IDFromList)));

        // Test request #2
        Long testReq2ID = req2.getRequestID();
        assertTrue("Request 2 is not the same", req2.equals(aList.getRequest(testReq2ID)));

        Request req2FromList = aList.getRequest(testReq2ID);
        Long req2IDFromList = req2FromList.getRequestID();
        assertTrue("Request 2 from list not the same", req2.equals(aList.getRequest(req2IDFromList)));
    }

    /**
     * Test get request ordered list.
     */
    public void testGetRequestOrderedList() {

        RequestList aList = new RequestList();

        // Create request #1
        String req1Status = "Pending";
        Address req1SrcAddr = new Address("U of A", 65.56777, 79.34555);
        Address req1DestAddr = new Address("Downtown", 50.56500, 89.56888);;
        double req1Cost = 12.30;
        double req1Distance = 4.5;
        String req1RiderID = "rider001";

        Request req1 = new Request(req1Status, req1SrcAddr, req1DestAddr, req1Distance, req1Cost,
                req1RiderID);

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Create request #2
        String req2Status = "Accepted";
        Address req2SrcAddr = new Address("West Ed", 51.56777, 30.34555);
        Address req2DestAddr = new Address("South Campus", 20.56500, 12.56888);;
        double req2Cost = 30.30;
        double req2Distance = 21.5;
        String req2RiderID = "rider001";

        Request req2 = new Request(req2Status, req2SrcAddr, req2DestAddr, req2Distance, req2Cost,
                req2RiderID);

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Create request #3
        String req3Status = "Completed";
        Address req3SrcAddr = new Address("City Center", 60.56779, 10.34522);
        Address req3DestAddr = new Address("Whyte Ave", 30.56500, 21.56999);
        double req3Cost = 12.33;
        double req3Distance = 11.43;
        String req3RiderID = "rider001";

        Request req3 = new Request(req3Status, req3SrcAddr, req3DestAddr, req3Distance, req3Cost,
                req2RiderID);

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        aList.addRequest(req1);
        aList.addRequest(req3);
        aList.addRequest(req2);

        List<Request> orderedReqList = aList.getRequestOrderedList();

        assertTrue("Not the same", orderedReqList.get(0).equals(req1));
        assertTrue("Not the same", orderedReqList.get(1).equals(req2));
        assertTrue("Not the same", orderedReqList.get(2).equals(req3));

    }

    /**
     * Test get specific request list.
     */
    public void testGetSpecificRequestList() {

        RequestList aList = new RequestList();

        // Create request #1
        String req1Status = "Pending";
        Address req1SrcAddr = new Address("U of A", 65.56777, 79.34555);
        Address req1DestAddr = new Address("Downtown", 50.56500, 89.56888);;
        double req1Cost = 12.30;
        double req1Distance = 4.5;
        String req1RiderID = "rider001";

        Request req1 = new Request(req1Status, req1SrcAddr, req1DestAddr, req1Distance, req1Cost,
                req1RiderID);

        aList.addRequest(req1);

        // Create request #2
        String req2Status = "Accepted";
        Address req2SrcAddr = new Address("West Ed", 51.56777, 30.34555);
        Address req2DestAddr = new Address("South Campus", 20.56500, 12.56888);;
        double req2Cost = 30.30;
        double req2Distance = 21.5;
        String req2RiderID = "rider001";

        Request req2 = new Request(req2Status, req2SrcAddr, req2DestAddr, req2Distance, req2Cost,
                req2RiderID);

        aList.addRequest(req2);

        // Create a request with Pending requests only
        List<Request> testSpecReqList1 = aList.getSpecificRequestList("Pending");

        // Get every single pending request from the list and check its status
        for (Request r : testSpecReqList1) {
            String rStatus = r.getRequestStatus();
            assertTrue("Request status is not the same", rStatus.equals("Pending"));
        }

        // Create a request with Accepted requests only
        List<Request> testSpecReqList2 = aList.getSpecificRequestList("Accepted");

        // Get every single accepted request from the list and check its status
        for (Request r : testSpecReqList2) {
            String rStatus = r.getRequestStatus();
            assertTrue("Request status is not the same", rStatus.equals("Accepted"));
        }
    }

    /**
     * Test has request.
     */
    public void testHasRequest() {

        RequestList aList = new RequestList();

        // Create request #1
        String req1Status = "Pending";
        Address req1SrcAddr = new Address("U of A", 65.56777, 79.34555);
        Address req1DestAddr = new Address("Downtown", 50.56500, 89.56888);;
        double req1Cost = 12.30;
        double req1Distance = 4.5;
        String req1RiderID = "rider001";

        Request req1 = new Request(req1Status, req1SrcAddr, req1DestAddr, req1Distance, req1Cost,
                req1RiderID);

        aList.addRequest(req1);

        assertTrue(aList.hasRequest(req1));
    }

    /**
     * Test delete request.
     */
    public void testDeleteRequest() {

        RequestList aList = new RequestList();

        // Create request #1
        String req1Status = "Pending";
        Address req1SrcAddr = new Address("U of A", 65.56777, 79.34555);
        Address req1DestAddr = new Address("Downtown", 50.56500, 89.56888);;
        double req1Cost = 12.30;
        double req1Distance = 4.5;
        String req1RiderID = "rider001";

        Request req1 = new Request(req1Status, req1SrcAddr, req1DestAddr, req1Distance, req1Cost,
                req1RiderID);

        aList.addRequest(req1);

        // Create request #2
        String req2Status = "Accepted";
        Address req2SrcAddr = new Address("West Ed", 51.56777, 30.34555);
        Address req2DestAddr = new Address("South Campus", 20.56500, 12.56888);;
        double req2Cost = 30.30;
        double req2Distance = 21.5;
        String req2RiderID = "rider001";

        Request req2 = new Request(req2Status, req2SrcAddr, req2DestAddr, req2Distance, req2Cost,
                req2RiderID);

        aList.addRequest(req2);

        // Test list after adding two elements
        assertTrue("Request List has 2 requests", aList.getCount() == 2);

        aList.deleteRequest(req1);

        // Check if list has only 1 element after delete req1
        assertTrue("Request List has 2 requests", aList.getCount() == 1);

        // Check if the element left is req2
        Long testReq2ID = req2.getRequestID();
        assertTrue("Request 2 is not the same", req2.equals(aList.getRequest(testReq2ID)));

        Request req2FromList = aList.getRequest(testReq2ID);
        Long req2IDFromList = req2FromList.getRequestID();
        assertTrue("Request 2 from list not the same", req2.equals(aList.getRequest(req2IDFromList)));
    }
}
