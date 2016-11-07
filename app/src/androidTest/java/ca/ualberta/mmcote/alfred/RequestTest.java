package ca.ualberta.mmcote.alfred;

import android.provider.Settings;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import java.util.Date;

/**
 * Create all the test cases for Request
 * @author ookmm
 * @version 1
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
        Double req1Cost = 12.30;
        Double req1Distance = 4.5;
        String req1RiderID = "rider001";

        Request req1 = new Request(req1Status, req1Cost, req1Distance, req1RiderID);

        Integer temp = 1;
        assertTrue("Request ID not the same", temp.toString().equals(req1.getRequestID()));
    }


    /**
     * Test getRequestStatus.
     */
    public void testGetRequestStatus () {
        /**
         * Create request #1
         */
        String req1Status = "Pending";
        Double req1Cost = 12.30;
        Double req1Distance = 4.5;
        String req1RiderID = "rider001";

        Request req1 = new Request(req1Status, req1Cost, req1Distance, req1RiderID);

        assertTrue("Request Status not equal", req1Status.equals(req1.getRequestStatus()));
    }


    /**
     * Test getCost.
     */
    public void testGetCost() {

        /**
         * Create request #1
         */
        String req1Status = "Pending";
        Double req1Cost = 12.30;
        Double req1Distance = 4.5;
        String req1RiderID = "rider001";

        Request req1 = new Request(req1Status, req1Cost, req1Distance, req1RiderID);
        assertTrue("Request cost not the same", req1Cost.equals(req1.getCost()));
    }


    /**
     * Test getCost.
     */
    public void testGetDistance() {

        /**
         * Create request #1
         */
        String req1Status = "Pending";
        Double req1Cost = 12.30;
        Double req1Distance = 4.5;
        String req1RiderID = "rider001";

        Request req1 = new Request(req1Status, req1Cost, req1Distance, req1RiderID);
        assertTrue("Request distance not the same", req1Distance.equals(req1.getDistance()));
    }


    /**
     * Test getDriverID.
     */
    public void testGetDriverID() {
        /**
         * Create request #1
         */
        String req1Status = "Pending";
        Double req1Cost = 12.30;
        Double req1Distance = 4.5;
        String req1RiderID = "rider001";

        Request req1 = new Request(req1Status, req1Cost, req1Distance, req1RiderID);

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
        Double req1Cost = 12.30;
        Double req1Distance = 4.5;
        String req1RiderID = "rider001";

        Request req1 = new Request(req1Status, req1Cost, req1Distance, req1RiderID);

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
        Double req1Cost = 12.30;
        Double req1Distance = 4.5;
        String req1RiderID = "rider001";

        Request req1 = new Request(req1Status, req1Cost, req1Distance, req1RiderID);

        Date enteredDate = req1.getRequestDate();
        Date testDate = new Date();

        assertTrue("Year not the same", enteredDate.getYear() == testDate.getYear());
        assertTrue("Month not the same", enteredDate.getMonth() == testDate.getMonth());
        assertTrue("Day not the same", enteredDate.getDay() == testDate.getDay());
    }

}
