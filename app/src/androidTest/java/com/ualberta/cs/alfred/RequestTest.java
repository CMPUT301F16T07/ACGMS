package com.ualberta.cs.alfred;

import android.test.ActivityInstrumentationTestCase2;

import com.ualberta.cs.alfred.activities.MainActivity;

import java.util.Date;

/**
 * Create all the test cases for Request
 *
 * @author ookmm
 * @version 1.2
 * @see Request
 */
public class RequestTest extends ActivityInstrumentationTestCase2 {

    /**
     * Instantiates a new Request test.
     */
    public RequestTest() {
        super(MainActivity.class);
    }

    /**
     * Test getRequestID.
     */
    public void testRequestID() {
        /**
         * Create request #1
         */
        String req1Status = "Pending";
        Address req1SrcAddr = new Address("U of A", 65.56777, 79.34555);
        Address req1DestAddr = new Address("Downtown", 50.56500, 89.56888);;
        double req1Cost = 12.30;
        double req1Distance = 4.5;
        String req1RiderID = "rider001";

        Request req1 = new Request(req1Status, req1SrcAddr, req1DestAddr, req1Distance, req1Cost,
                req1RiderID);

        int temp = 1;
        String requestID = "request" + Integer.toString(temp);
        assertTrue("Request ID not the same", requestID.equals(req1.getRequestID()));
    }

    /**
     * Test getRequestStatus.
     */
    public void testGetRequestStatus () {
        /**
         * Create request #1
         */
        String req1Status = "Pending";
        Address req1SrcAddr = new Address("U of A", 65.56777, 79.34555);
        Address req1DestAddr = new Address("Downtown", 50.56500, 89.56888);;
        double req1Cost = 12.30;
        double req1Distance = 4.5;
        String req1RiderID = "rider001";

        Request req1 = new Request(req1Status, req1SrcAddr, req1DestAddr, req1Distance, req1Cost,
                req1RiderID);

        assertTrue("Request Status not equal", req1Status.equals(req1.getRequestStatus()));
    }

    /**
     * Test getSourceAddress
     */
    public void testGetSourceAddress() {
        /**
         * Create request #1
         */
        String req1Status = "Pending";
        Address req1SrcAddr = new Address("U of A", 65.56777, 79.34555);
        Address req1DestAddr = new Address("Downtown", 50.56500, 89.56888);;
        double req1Cost = 12.30;
        double req1Distance = 4.5;
        String req1RiderID = "rider001";

        Request req1 = new Request(req1Status, req1SrcAddr, req1DestAddr, req1Distance, req1Cost,
                req1RiderID);

        assertTrue("Source Address not the same", req1SrcAddr.equals(req1.getSourceAddress()));
    }


    /**
     * Test getDestinationAddress
     */
    public void testGetDestinationAddress() {
        /**
         * Create request #1
         */
        String req1Status = "Pending";
        Address req1SrcAddr = new Address("U of A", 65.56777, 79.34555);
        Address req1DestAddr = new Address("Downtown", 50.56500, 89.56888);;
        double req1Cost = 12.30;
        double req1Distance = 4.5;
        String req1RiderID = "rider001";

        Request req1 = new Request(req1Status, req1SrcAddr, req1DestAddr, req1Distance, req1Cost,
                req1RiderID);

        assertTrue("Source Address not the same", req1DestAddr.equals(req1.getDestinationAddress()));
    }

    /**
     * Test getDistance.
     */
    public void testGetDistance() {
        /**
         * Create request #1
         */
        String req1Status = "Pending";
        Address req1SrcAddr = new Address("U of A", 65.56777, 79.34555);
        Address req1DestAddr = new Address("Downtown", 50.56500, 89.56888);;
        double req1Cost = 12.30;
        double req1Distance = 4.5;
        String req1RiderID = "rider001";

        Request req1 = new Request(req1Status, req1SrcAddr, req1DestAddr, req1Distance, req1Cost,
                req1RiderID);

        assertTrue("Request cost not the same", req1Distance == req1.getDistance());
    }

    /**
     * Test getCost.
     */
    public void testGetCost() {
        /**
         * Create request #1
         */
        String req1Status = "Pending";
        Address req1SrcAddr = new Address("U of A", 65.56777, 79.34555);
        Address req1DestAddr = new Address("Downtown", 50.56500, 89.56888);;
        double req1Cost = 12.30;
        double req1Distance = 4.5;
        String req1RiderID = "rider001";

        Request req1 = new Request(req1Status, req1SrcAddr, req1DestAddr, req1Distance, req1Cost,
                req1RiderID);

        assertTrue("Request cost not the same", req1Cost == req1.getCost());
    }

    /**
     * Test getDriverID.
     */
    public void testGetDriverID() {
        /**
         * Create request #1
         */
        String req1Status = "Pending";
        Address req1SrcAddr = new Address("U of A", 65.56777, 79.34555);
        Address req1DestAddr = new Address("Downtown", 50.56500, 89.56888);;
        double req1Cost = 12.30;
        double req1Distance = 4.5;
        String req1RiderID = "rider001";

        Request req1 = new Request(req1Status, req1SrcAddr, req1DestAddr, req1Distance, req1Cost,
                req1RiderID);

        // Set Driver ID
        String driverID = "driver001";
        req1.setDriverID(driverID);
        assertTrue("Driver ID not the same", driverID.equals(req1.getDriverID()));
    }

    /**
     * Test getRiderID.
     */
    public void testGetRiderID() {
        /**
         * Create request #1
         */
        String req1Status = "Pending";
        Address req1SrcAddr = new Address("U of A", 65.56777, 79.34555);
        Address req1DestAddr = new Address("Downtown", 50.56500, 89.56888);;
        double req1Cost = 12.30;
        double req1Distance = 4.5;
        String req1RiderID = "rider001";

        Request req1 = new Request(req1Status, req1SrcAddr, req1DestAddr, req1Distance, req1Cost,
                req1RiderID);

        assertTrue("Driver ID not the same", req1RiderID.equals(req1.getRiderID()));
    }


    /**
     * Test getRequestDate.
     */
    public void testGetRequestDate() {
        /**
         * Create request #1
         */
        String req1Status = "Pending";
        Address req1SrcAddr = new Address("U of A", 65.56777, 79.34555);
        Address req1DestAddr = new Address("Downtown", 50.56500, 89.56888);;
        double req1Cost = 12.30;
        double req1Distance = 4.5;
        String req1RiderID = "rider001";

        Request req1 = new Request(req1Status, req1SrcAddr, req1DestAddr, req1Distance, req1Cost,
                req1RiderID);

        Date enteredDate = req1.getRequestDate();
        Date testDate = new Date();

        assertTrue("Year not the same", enteredDate.getYear() == testDate.getYear());
        assertTrue("Month not the same", enteredDate.getMonth() == testDate.getMonth());
        assertTrue("Day not the same", enteredDate.getDay() == testDate.getDay());
    }

}
