package com.ualberta.cs.alfred;

import android.test.ActivityInstrumentationTestCase2;

import java.util.List;



public class RequestListTest extends ActivityInstrumentationTestCase2 {

    public RequestListTest() {

        super(MainActivity.class);
    }

    public void testEmptyRequestList() {
        RequestList requestList = new RequestList();
        List<Request> aList = requestList.getRequestList();
        assertTrue("Request List is empty", aList.size() == 0);
    }

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
        String testReq1ID = req1.getRequestID();
        assertTrue("Request 1 is not the same", req1.equals(aList.getRequest(testReq1ID)));

        Request req1FromList = aList.getRequest(testReq1ID);
        String req1IDFromList = req1FromList.getRequestID();
        assertTrue("Request 1 from list not the same", req1.equals(aList.getRequest(req1IDFromList)));

        // Test request #2
        String testReq2ID = req2.getRequestID();
        assertTrue("Request 2 is not the same", req2.equals(aList.getRequest(testReq2ID)));

        Request req2FromList = aList.getRequest(testReq2ID);
        String req2IDFromList = req2FromList.getRequestID();
        assertTrue("Request 2 from list not the same", req2.equals(aList.getRequest(req2IDFromList)));
    }

    public void testGetSpecificRequestList() {

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

        RequestList testSpecReqList = aList.getSpecificRequestList("Pending");

        // Get every single pending request from the list and check its status
        for (int i = 0; i < testSpecReqList.getCount(); ++i) {
            int j = i + 1;
            Request r = testSpecReqList.getRequest("request" + j);
            String rStatus = r.getRequestStatus();
            assertTrue("Request status is not the same", rStatus.equals("Pending"));
        }


    }
}
