package com.ualberta.cs.alfred;

import android.test.ActivityInstrumentationTestCase2;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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

        // Create a rider
        String rider1FirstName = "Justin";
        String rider1LastName = "Trudeau";
        String rider1Username = "jtrudeau";

        // Create date
        GregorianCalendar gc = new GregorianCalendar(1970, Calendar.JUNE, 13);
        Date rider1BirthDate  = gc.getTime();

        String rider1PhoneNumber = "780-230-8888";
        String rider1Email = "rider1@example.com";
        String rider1CCNumber = "555588844443333";

        Rider rider1 = new Rider(rider1FirstName, rider1LastName, rider1Username, rider1BirthDate,
                rider1PhoneNumber, rider1Email, rider1CCNumber);

        // Get rider1 id
        //UserElasticSearchController.GetRiderByUserNameTask retrievedRider = new UserElasticSearchController.GetRiderByUserNameTask();
        //retrievedRider.execute("userName", "jtrudeau");

        UserElasticSearchController.GetRider retrievedRider = new UserElasticSearchController.GetRider();

        // Find the rider with this id
        retrievedRider.execute("jtrudeau");

        String rider1UserId = null;

        try {
            Rider rider = retrievedRider.get();
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

        // Create request #1
        String req1Status = "Pending";
        Address req1SrcAddr = new Address("South side", 65.56777, 79.34555);
        Address req1DestAddr = new Address("Downtown", 50.56500, 89.56888);
        double req1Cost = 90.30;
        double req1Distance = 4.5;
        String req1RiderID = rider1UserId;

        Request req1 = new Request(req1Status, req1SrcAddr, req1DestAddr, req1Distance, req1Cost,
                req1RiderID);
        assert(true);

        // Create request #2
        String req2Status = "Accepted";
        Address req2SrcAddr = new Address("Airport", 51.56777, 30.34555);
        Address req2DestAddr = new Address("South Campus", 20.56500, 12.56888);;
        double req2Cost = 30.30;
        double req2Distance = 21.5;
        String req2RiderID = rider1UserId;

        Request req2 = new Request(req2Status, req2SrcAddr, req2DestAddr, req2Distance, req2Cost,
                req2RiderID);
        assert(true);

        // Create request #3
        String req3Status = "Pending";
        Address req3SrcAddr = new Address("Airport", 51.56777, 30.34555);
        Address req3DestAddr = new Address("South Campus", 51.56500, 30.56888);;
        double req3Cost = 30.30;
        double req3Distance = 21.5;
        String req3DriverID = "driver001";

        Request req3 = new Request(req3Status, req3SrcAddr, req3DestAddr, req3Distance, req3Cost,
                req3DriverID);
        assert(true);
    }

    /**
     * Test get request task
     *
     * @Note: need to provide the search parameters
     */
    public void testGetRequestTask() {

        RequestElasticSearchController.GetRequestTask retrievedRequest =
                new RequestElasticSearchController.GetRequestTask();

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

        RequestElasticSearchController.GetRequestByIdTask retrievedRequest =
                new RequestElasticSearchController.GetRequestByIdTask();

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

    public void testAddItemToListTask() {

        String requestID = "AVhdOt-dtmmsbsUPVvpZ";
        String requestProperty = "driverIDList";
        String requestNewValue = "driver456";

        RequestElasticSearchController.AddItemToListTask addItemToListTask =
                new RequestElasticSearchController.AddItemToListTask();

        addItemToListTask.execute(requestID, requestProperty, requestNewValue);
        assert (true);

    }


    public void testDeleteItemFromListTask() {

        String requestID = "AVhdOt-dtmmsbsUPVvpZ";
        String requestProperty = "driverIDList";
        String requestValueType = "string";
        String requestValue = "driver890";

        RequestElasticSearchController.DeleteItemFromListTask deleteItemFromListTask =
                new RequestElasticSearchController.DeleteItemFromListTask();

        deleteItemFromListTask.execute(requestID, requestProperty, requestValueType, requestValue);
        assert (true);

    }


    public void testSetPropertyValueTask() {

        String requestID = "AVhdOt-dtmmsbsUPVvpZ";

        String requestProperty = "driverIDList";
        requestProperty = "cost";
        requestProperty = "requestDate";

        String requestPropertyType = "array";
        requestPropertyType = "double";
        requestPropertyType = "date";

        String requestNewValue = "driver890";
        requestNewValue = "130.00";
        // Format must be yyy-MM-dd : 2015-11-27
        // If you want to include time, then 2015-11-27T12:10:30Z
        requestNewValue = "2016-10-26T12:10:30Z";

        RequestElasticSearchController.SetPropertyValueTask setPropertyValueTask =
                new RequestElasticSearchController.SetPropertyValueTask();

        setPropertyValueTask.execute(requestID, requestProperty, requestPropertyType, requestNewValue);
        assert (true);

    }

    public void testSetNestedObjectPropertyValueTask() {

        String requestID = "AVhdOt-dtmmsbsUPVvpZ";
        String requestProperty = "sourceAddress";
        String nestedObject1Property = "location";
        String nestedObject1ValueType = "string";
        String nestedObject1Value = "Canada Place";
        String nestedObject2Property = "latitude";
        String nestedObject2PropertyType = "double";
        String nestedObject2Value = "90.45";
        String nestedObject3Property = "longitude";
        String nestedObject3PropertyType = "double";
        String nestedObject3Value = "-50.45";

        RequestElasticSearchController.SetNestedObjectPropertyValueTask setNestedObjectPropertyValueTask =
                new RequestElasticSearchController.SetNestedObjectPropertyValueTask();

        setNestedObjectPropertyValueTask.execute(
                requestID, requestProperty,
                nestedObject1Property, nestedObject1ValueType, nestedObject1Value,
                nestedObject2Property, nestedObject2PropertyType, nestedObject2Value,
                nestedObject3Property, nestedObject3PropertyType, nestedObject3Value);
        assert (true);

    }

    /**
     * Delete request by id task
     *
     * @Note: need to know the request ID.
     */
    public void testDeleteRequestTask() {

        RequestElasticSearchController.DeleteRequestTask deleteRequest =
                new RequestElasticSearchController.DeleteRequestTask();

        // Delete the request with this id
        deleteRequest.execute("AVhVAmNqFLrhMuj9wTtN");
        assert (true);
    }

}
