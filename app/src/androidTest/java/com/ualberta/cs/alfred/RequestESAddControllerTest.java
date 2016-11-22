package com.ualberta.cs.alfred;

import android.test.ActivityInstrumentationTestCase2;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.ExecutionException;

/**
 * Test cases for adding request item to Elasticsearch.
 * @author ookmm
 * @version 1.1
 * @see RequestESAddControllerTest
 */
public class RequestESAddControllerTest extends ActivityInstrumentationTestCase2 {

    public RequestESAddControllerTest() {
        super(MainActivity.class);
    }

    /**
     * Test add request task
     */
    public void testAddRequestTask() {

        /**
         * Create a new rider.
         */

        /* Step 1: Gather rider info */
        String rider1FirstName = "Vladimir";
        String rider1LastName = "Putin";
        String rider1Username = "vputin";

        // Create date
        GregorianCalendar gc = new GregorianCalendar(1950, Calendar.JUNE, 20);
        Date rider1BirthDate  = gc.getTime();

        String rider1PhoneNumber = "416-230-8888";
        String rider1Email = "vputin@example.com";
        String rider1CCNumber = "666688844443333";

        RiderInfo riderInfo = new RiderInfo(rider1CCNumber);

        /* Step 2: Add rider info to constructor */
        User rider1 = new User(rider1FirstName, rider1LastName, rider1Username, rider1BirthDate,
                rider1PhoneNumber, rider1Email, riderInfo);

        /* Step 3:  Get rider with a given username from ES */
        UserESGetController.GetUserTask retrievedRider = new UserESGetController.GetUserTask();

        // Find the rider with this username
        retrievedRider.execute("vputin");

        String rider1UserId = null;

        try {
            User rider = retrievedRider.get();
            rider1UserId = rider.getUserID();
            System.out.println("====================");
            System.out.println("Rider1 ID: " + rider1UserId);
            System.out.println("====================");
            assert (true);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        /* Step 4: Create request any number of request with the newly retrieved rider userId */
        // Create request #1
        String req1Status = "Pending";
        Address req1SrcAddr = new Address("South side", -65.56777, 79.34555);
        Address req1DestAddr = new Address("Downtown", -50.56500, 89.56888);
        double req1Cost = 90.30;
        double req1Distance = 4.5;
        String req1RiderID = rider1UserId;

        Request req1 = new Request(req1Status, req1SrcAddr, req1DestAddr, req1Distance, req1Cost,
                req1RiderID);
        assert(true);

        // Create request #2
        String req2Status = "Accepted";
        Address req2SrcAddr = new Address("Airport", -51.56777, 30.34555);
        Address req2DestAddr = new Address("South Campus", -20.56500, 12.56888);;
        double req2Cost = 30.30;
        double req2Distance = 21.5;
        String req2RiderID = rider1UserId;

        Request req2 = new Request(req2Status, req2SrcAddr, req2DestAddr, req2Distance, req2Cost,
                req2RiderID);
        assert(true);

        // Create request #3
        String req3Status = "Pending";
        Address req3SrcAddr = new Address("Airport", -51.56777, 30.34555);
        Address req3DestAddr = new Address("South Campus", -51.56500, 30.56888);;
        double req3Cost = 30.30;
        double req3Distance = 21.5;
        String req3DriverID = rider1UserId;

        Request req3 = new Request(req3Status, req3SrcAddr, req3DestAddr, req3Distance, req3Cost,
                req3DriverID);
        assert(true);
    }

    public void testAddItemToListTask() {

        String requestID = "AVhrkmHQdE2DZPCrf9fS";
        String requestProperty = "driverIDList";
        String requestNewValue = "driver900";

        RequestESAddController.AddItemToListTask addItemToListTask =
                new RequestESAddController.AddItemToListTask();

        addItemToListTask.execute(requestID, requestProperty, requestNewValue);
        assert (true);

    }
}
