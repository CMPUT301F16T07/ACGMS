package com.ualberta.cs.alfred.usecasetests;

import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.ualberta.cs.alfred.Address;
import com.ualberta.cs.alfred.AppSettings;
import com.ualberta.cs.alfred.DriverInfo;
import com.ualberta.cs.alfred.Request;
import com.ualberta.cs.alfred.RequestESDeleteController;
import com.ualberta.cs.alfred.RequestESGetController;
import com.ualberta.cs.alfred.RequestESSetController;
import com.ualberta.cs.alfred.RiderInfo;
import com.ualberta.cs.alfred.User;
import com.ualberta.cs.alfred.UserESAddController;
import com.ualberta.cs.alfred.UserESGetController;
import com.ualberta.cs.alfred.Vehicle;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.ExecutionException;


/**
 * Test cases for Requests.
 *
 * @author ookmm
 * @version 1.0
 */
@RunWith(AndroidJUnit4.class)
public class RequestsTest {

    /**
     * US 01.01.01
     * As a rider, I want to request rides between two locations.
     *
     * @throws Exception
     */
    @Test
    public void testRequestRide() throws Exception {
        /**
         * Step 1: Basic user info
         */
        String u1FirstName = "Stephen";
        String u1LastName = "Harper";
        String u1UserName = "sharper";

        // Create date
        GregorianCalendar gc = new GregorianCalendar(1962, Calendar.FEBRUARY, 17);
        Date u1DateOfBirth = gc.getTime();

        String u1PhoneNumber = "780-888-5555";
        String u1Email = "sharper@example.com";

        /**
         * Step 2: Create credit card
         */
        String creditCard = "2222333366662222";

        /**
         * Step 3: Create Rider info
         */
        RiderInfo riderInfo = new RiderInfo(creditCard);

        /**
         * Step 4: Check if DB has username already
         */
        UserESGetController.GetUserTask retrievedUser =
                new UserESGetController.GetUserTask();

        // Find user
        retrievedUser.execute(u1UserName);

        try {
            User user = retrievedUser.get();

            if (user == null) {
                /**
                 * Step 5: Create user
                 */
                User u1 = new User(u1FirstName, u1LastName, u1UserName, u1DateOfBirth, u1PhoneNumber,
                        u1Email, riderInfo);
            } else {
                Log.i("Error", "Try a different username");
                System.out.println("Try a different username");
            }
            assert (true);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        /**
         * Step 6: Get user ID with the initial username
         */
        String user2ID = null;

        UserESGetController.GetUserTask retrievedUserForUserID =
                new UserESGetController.GetUserTask();

        // Find user
        retrievedUserForUserID.execute(u1UserName);

        try {
            User user2 = retrievedUserForUserID.get();
            if (user2 != null) {
                user2ID = user2.getUserID();
            }
            assert (true);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        /**
         * Step 7: Make a request between two locations
         */
        String req1Status = AppSettings.REQUEST_PENDING;
        Address req1SrcAddr = new Address("University of Alberta", -113.52631859999997, 53.5232189);
        Address req1DestAddr = new Address("West Edmonton Mall", -113.62419060000002, 53.5225151);
        double req1Cost = 16.65;
        double req1Distance = 10.4;
        String req1RiderID = user2ID;

        Request req1 = new Request(req1Status, req1SrcAddr, req1DestAddr, req1Distance, req1Cost,
                req1RiderID);
        assert(true);
    }

    /**
     * US 01.02.01
     * As a rider, I want to see current requests I have open.
     *
     * @throws Exception
     */
    @Test
    public void testSeeCurrentOpenRequests() throws Exception {
        /**
         * Step 1: Get user ID with username
         */
        String u1UserName = "mmcote";
        String userID = null;

        UserESGetController.GetUserTask retrievedUserForUserID =
                new UserESGetController.GetUserTask();

        // Find user
        retrievedUserForUserID.execute(u1UserName);

        try {
            User user = retrievedUserForUserID.get();
            if (user != null) {
                userID = user.getUserID();
            }
            assert (true);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        /**
         * Step 2: Get requests with by userID
         */
        RequestESGetController.GetRequestByMultiplePreferencesTask retrievedRequest =
                new RequestESGetController.GetRequestByMultiplePreferencesTask();

        retrievedRequest.execute(
                "riderID", "string", userID,
                "requestStatus", "string", AppSettings.REQUEST_PENDING
        );

        try {
            ArrayList<Request> requests = retrievedRequest.get();
            for (Request request : requests) {
                System.out.println("====================");
                System.out.println("Request ID is: " + request.getRequestID());
                System.out.println("Request Status: " + request.getRequestStatus());
                System.out.println("Request Starting Location is: " +
                        request.getSourceAddress().getLocation());
                System.out.println("Request Destination Location is: " +
                        request.getDestinationAddress().getLocation());
                System.out.println("Request Distance: " + request.getDistance());
                System.out.println("Request Cost: " + request.getCost());
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
     * US 01.03.01
     * As a rider, I want to be notified if my request is accepted.
     *
     */
    // TODO: Micheal's


    /**
     * US 01.04.01
     * As a rider, I want to cancel requests.
     *
     * @throws Exception
     */
    @Test
    public void testCancelRequest() throws Exception {
        /**
         * Step 1: Basic user info
         */
        String u1FirstName = "Stephen";
        String u1LastName = "Harper";
        String u1UserName = "sharper";

        // Create date
        GregorianCalendar gc = new GregorianCalendar(1962, Calendar.FEBRUARY, 17);
        Date u1DateOfBirth = gc.getTime();

        String u1PhoneNumber = "780-888-5555";
        String u1Email = "sharper@example.com";

        /**
         * Step 2: Create credit card
         */
        String creditCard = "2222333366662222";

        /**
         * Step 3: Create Rider info
         */
        RiderInfo riderInfo = new RiderInfo(creditCard);

        /**
         * Step 4: Check if DB has username already
         */
        UserESGetController.GetUserTask retrievedUser =
                new UserESGetController.GetUserTask();

        // Find user
        retrievedUser.execute(u1UserName);

        try {
            User user = retrievedUser.get();

            if (user == null) {
                /**
                 * Step 5: Create user
                 */
                User u1 = new User(u1FirstName, u1LastName, u1UserName, u1DateOfBirth, u1PhoneNumber,
                        u1Email, riderInfo);
            } else {
                Log.i("Error", "Try a different username");
                System.out.println("Try a different username");
            }
            assert (true);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        /**
         * Step 6: Get user ID with the initial username
         */
        String user2ID = null;

        UserESGetController.GetUserTask retrievedUserForUserID =
                new UserESGetController.GetUserTask();

        // Find user
        retrievedUserForUserID.execute(u1UserName);

        try {
            User user2 = retrievedUserForUserID.get();
            if (user2 != null) {
                user2ID = user2.getUserID();
            }
            assert (true);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        /**
         * Step 7: Make a request between two locations
         */
        String req1Status = AppSettings.REQUEST_PENDING;
        Address req1SrcAddr = new Address("University of Alberta", -113.52631859999997, 53.5232189);
        Address req1DestAddr = new Address("West Edmonton Mall", -113.62419060000002, 53.5225151);
        double req1Cost = 16.65;
        double req1Distance = 10.4;
        String req1RiderID = user2ID;

        Request req1 = new Request(req1Status, req1SrcAddr, req1DestAddr, req1Distance, req1Cost,
                req1RiderID);
        assert(true);

        /**
         * Step 8: Get requests by user with riderID
         */
        RequestESGetController.GetRequestTask retrievedRequest =
                new RequestESGetController.GetRequestTask();

        retrievedRequest.execute("riderID", user2ID);

        // Get the ID of the request to cancel.
        // In this case, any request would do. We have chosen the first request that comes back
        // from Elasticsearch.
        Request requestToCancel = null;
        String requestToCancelID = "";

        try {
            ArrayList<Request> requests = retrievedRequest.get();
            if (requests.size() != 0) {
                requestToCancel = requests.get(0);
                requestToCancelID = requestToCancel.getRequestID();
            } else {
                Log.i("Error", "No request exists for this user");
                System.out.println("No request exists for this user");
            }
            assert (true);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        /**
         * Step 8: Cancel request
         */
        if (!(requestToCancelID.isEmpty())) {
            RequestESDeleteController.DeleteRequestTask deleteRequest =
                    new RequestESDeleteController.DeleteRequestTask();

            // Delete the request with this id
            deleteRequest.execute(requestToCancelID);
            assert (true);
        } else {
            Log.i("Error", "Request ID is empty");
            System.out.println("Request ID is empty");
        }

    }

    /**
     * US 01.05.01
     * As a rider, I want to be able to phone or email the driver who accepted a request.
     */
    // TODO: Micheal's

    /**
     * US 01.06.01
     * As a rider, I want an estimate of a fair fare to offer to drivers.
     *
     * @throws Exception
     */
    @Test
    public void testGetFareEstimate() throws Exception {
        /**
         * Step 1: Basic user info
         */
        String u1FirstName = "Stephen";
        String u1LastName = "Harper";
        String u1UserName = "sharper";

        // Create date
        GregorianCalendar gc = new GregorianCalendar(1962, Calendar.FEBRUARY, 17);
        Date u1DateOfBirth = gc.getTime();

        String u1PhoneNumber = "780-888-5555";
        String u1Email = "sharper@example.com";

        /**
         * Step 2: Create credit card
         */
        String creditCard = "2222333366662222";

        /**
         * Step 3: Create Rider info
         */
        RiderInfo riderInfo = new RiderInfo(creditCard);

        /**
         * Step 4: Check if DB has username already
         */
        UserESGetController.GetUserTask retrievedUser =
                new UserESGetController.GetUserTask();

        // Find user
        retrievedUser.execute(u1UserName);

        try {
            User user = retrievedUser.get();

            if (user == null) {
                /**
                 * Step 5: Create user
                 */
                User u1 = new User(u1FirstName, u1LastName, u1UserName, u1DateOfBirth, u1PhoneNumber,
                        u1Email, riderInfo);
            } else {
                Log.i("Error", "Try a different username");
                System.out.println("Try a different username");
            }
            assert (true);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        /**
         * Step 6: Get user ID with the initial username
         */
        String user2ID = null;

        UserESGetController.GetUserTask retrievedUserForUserID =
                new UserESGetController.GetUserTask();

        // Find user
        retrievedUserForUserID.execute(u1UserName);

        try {
            User user2 = retrievedUserForUserID.get();
            if (user2 != null) {
                user2ID = user2.getUserID();
            }
            assert (true);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        /**
         * Step 7: Make a request between two locations
         */
        String req1Status = AppSettings.REQUEST_PENDING;
        Address req1SrcAddr = new Address("University of Alberta", -113.52631859999997, 53.5232189);
        Address req1DestAddr = new Address("West Edmonton Mall", -113.62419060000002, 53.5225151);
        double req1Cost = 16.65;
        double req1Distance = 10.4;
        String req1RiderID = user2ID;

        Request req1 = new Request(req1Status, req1SrcAddr, req1DestAddr, req1Distance, req1Cost,
                req1RiderID);
        assert(true);

        /**
         * Step 8: Get requests by user with riderID
         */
        RequestESGetController.GetRequestTask retrievedRequest =
                new RequestESGetController.GetRequestTask();

        retrievedRequest.execute("riderID", user2ID);

        // Get the ID of the request to get fare estimate.
        // In this case, any request would do. We have chosen the first request that comes back
        // from Elasticsearch.

        try {
            ArrayList<Request> requests = retrievedRequest.get();
            if (requests.size() != 0) {
                Request requestToGetFareEstimate = requests.get(0);
                System.out.println("==================================");
                System.out.println("Fare estimate is: " + requestToGetFareEstimate.getCost());
                System.out.println("==================================");
            } else {
                Log.i("Error", "No request exists for this user");
                System.out.println("No request exists for this user");
            }
            assert (true);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
     * US 01.07.01
     * As a rider, I want to confirm the completion of a request and enable payment.
     */
    // TODO: Micheal's

    /**
     * US 01.08.01
     * As a rider, I want to confirm a driver's acceptance.
     * This allows us to choose from a list of acceptances if more than 1 driver
     * accepts simultaneously.
     */
    // TODO: Micheal's

    /**
     * US 1.09.01 (added 2016-11-14)
     * As a rider, I should see a description of the driver's vehicle.
     *
     * @throws Exception
     */
    @Test
    public void testSeeVehicleDescription() throws Exception {
        /**
         * Step 1: Basic driver info
         */
        String driverFirstName = "Eddie";
        String driverLastName = "Santos";
        String driverUserName = "esantos";

        // Create date
        GregorianCalendar gcDriver = new GregorianCalendar(1988, Calendar.JANUARY, 13);
        Date driverDateOfBirth = gcDriver.getTime();

        String driverPhoneNumber = "780-333-5555";
        String driverEmail = "esantos@example.com";

        /**
         * Step 2: Create a driver licenceNumber
         */
        String licenceNumber = "ABB005";

        /**
         * Step 3: Create a vehicle
         */
        String serialNumber = "serial990088";
        String plateNumber = "HELLO2";
        String type = "Truck";
        String make = "Toyota";
        String model = "Tacoma";
        int year = 2016;
        String color = "Blue";

        Vehicle vehicle = new Vehicle(serialNumber, plateNumber, type, make, model, year, color);

        /**
         * Step 4: Create driverInfo
         */
        DriverInfo driverInfo = new DriverInfo(licenceNumber, vehicle);

        /**
         * Step 5: Create driver if doesn't exist
         */
        UserESGetController.GetUserTask retrievedDriver =
                new UserESGetController.GetUserTask();

        // Find driver
        retrievedDriver.execute(driverUserName);

        try {
            User user = retrievedDriver.get();

            if (user == null) {
                // Create driver
                User driver = new User(driverFirstName, driverLastName, driverUserName,
                        driverDateOfBirth, driverPhoneNumber, driverEmail, driverInfo);
            } else {
                Log.i("Error", "User already registered. Try a different username");
                System.out.println("User already registered. Try a different username");
            }
            assert (true);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        /**
         * Step 6: Get driver user ID with the initial username
         */
        String driverID = null;

        UserESGetController.GetUserTask retrievedDriverForUserID =
                new UserESGetController.GetUserTask();

        // Find driver
        retrievedDriverForUserID.execute(driverUserName);

        User driver2 = null;

        try {
            driver2 = retrievedDriverForUserID.get();
            if (driver2 != null) {
                driverID = driver2.getUserID();
            }
            assert (true);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        /**
         * Step 7: Basic rider info
         */
        String u1FirstName = "Stephen";
        String u1LastName = "Harper";
        String u1UserName = "sharper";

        // Create date
        GregorianCalendar gc = new GregorianCalendar(1962, Calendar.FEBRUARY, 17);
        Date u1DateOfBirth = gc.getTime();

        String u1PhoneNumber = "780-888-5555";
        String u1Email = "sharper@example.com";

        /**
         * Step 8: Create credit card
         */
        String creditCard = "2222333366662222";

        /**
         * Step 9: Create Rider info
         */
        RiderInfo riderInfo = new RiderInfo(creditCard);

        /**
         * Step 10: Create rider if doesn't exist
         */
        UserESGetController.GetUserTask retrievedUser =
                new UserESGetController.GetUserTask();

        // Find user
        retrievedUser.execute(u1UserName);

        try {
            User user = retrievedUser.get();

            if (user == null) {
                // Create user
                User u1 = new User(u1FirstName, u1LastName, u1UserName, u1DateOfBirth, u1PhoneNumber,
                        u1Email, riderInfo);
            } else {
                Log.i("Error", "Try a different username");
                System.out.println("Try a different username");
            }
            assert (true);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        /**
         * Step 11: Get user ID with the initial username
         */
        String user2ID = null;

        UserESGetController.GetUserTask retrievedUserForUserID =
                new UserESGetController.GetUserTask();

        // Find user
        retrievedUserForUserID.execute(u1UserName);

        try {
            User user2 = retrievedUserForUserID.get();
            if (user2 != null) {
                user2ID = user2.getUserID();
            }
            assert (true);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        /**
         * Step 12: Make a request between two locations
         */
        String req1Status = AppSettings.REQUEST_PENDING;
        Address req1SrcAddr = new Address("1978 99 St NW, Edmonton", -113.483609, 53.44708019999999);
        Address req1DestAddr = new Address("11211 142 St NW, Edmonton", -113.5640156, 53.5612133);
        double req1Cost = 19.56;
        double req1Distance = 20.2;
        String req1RiderID = user2ID;

        Request req1 = new Request(req1Status, req1SrcAddr, req1DestAddr, req1Distance, req1Cost,
                req1RiderID);
        assert(true);

        /**
         * Step 13: Get requests by user with riderID
         */
        RequestESGetController.GetRequestTask retrievedRequest =
                new RequestESGetController.GetRequestTask();

        retrievedRequest.execute("riderID", user2ID);

        // Get the ID of the request to add to driver's list.
        // In this case, any request would do. We have chosen the first request that comes back
        // from Elasticsearch.
        Request riderRequest = null;
        String riderRequestID = "";

        try {
            ArrayList<Request> requests = retrievedRequest.get();
            if (requests.size() != 0) {
                riderRequest = requests.get(0);
                riderRequestID = riderRequest.getRequestID();
            } else {
                Log.i("Error", "No request exists for this user");
                System.out.println("No request exists for this user");
            }
            assert (true);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        /**
         * Step 14: Add driver to driverIDList
         */
        if (!(riderRequestID.isEmpty())) {
            String requestProperty = "driverIDList";
            String requestPropertyType = "array";
            String requestNewValue = driverID;

            RequestESSetController.SetPropertyValueTask setPropertyValueTask =
                    new RequestESSetController.SetPropertyValueTask();

            setPropertyValueTask.execute(riderRequestID, requestProperty, requestPropertyType,
                    requestNewValue);
            assert (true);
        } else {
            Log.i("Error", "Request ID is empty");
            System.out.println("Request ID is empty");
        }

        /**
         * Step 15: Get list of driver who accepted my request
         */
        RequestESGetController.GetRequestTask retrievedRequest2 =
                new RequestESGetController.GetRequestTask();

        retrievedRequest2.execute("riderID", user2ID);

        // Get the ID of the request to add to driver's list.
        // In this case, any request would do. We have chosen the first request that comes back
        // from Elasticsearch.
        Request riderRequest2 = null;
        String riderRequestID2 = "";
        ArrayList<String> myDriverIDList = null;

        try {
            ArrayList<Request> requests2 = retrievedRequest2.get();
            if (requests2.size() != 0) {
                riderRequest2 = requests2.get(0);
                riderRequestID2 = riderRequest2.getRequestID();
                myDriverIDList = riderRequest2.getDriverIDList();
            } else {
                Log.i("Error", "No request exists for this user");
                System.out.println("No request exists for this user");
            }
            assert (true);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        /**
         * Step 16: See driver's vehicle description
         */
        if (driver2.getIsDriver()) {
            System.out.println("=============== VEHICLE INFO =================");
            System.out.println("Vehicle Make: " + driver2.getDriverInfo().getVehicle().getMake());
            System.out.println("Vehicle Model: " + driver2.getDriverInfo().getVehicle().getModel());
            System.out.println("Vehicle Model: " + driver2.getDriverInfo().getVehicle().getColor());
            System.out.println("Vehicle Model: " + driver2.getDriverInfo().getVehicle().getYear());
            System.out.println("Vehicle type: " + driver2.getDriverInfo().getVehicle().getType());
            System.out.println("=========== END OF VEHICLE INFO ==============");
        }

    }

    /**
     * US 1.10.01 (added 2016-11-14)
     * As a rider, I want to see some summary rating of the drivers who accepted my offers.
     *
     * @throws Exception
     */
    @Test
    public void testSeeDriverSummaryRating() throws Exception {
        /**
         * Step 1: Basic driver info
         */
        String driverFirstName = "Eddie";
        String driverLastName = "Santos";
        String driverUserName = "esantos";

        // Create date
        GregorianCalendar gcDriver = new GregorianCalendar(1988, Calendar.JANUARY, 13);
        Date driverDateOfBirth = gcDriver.getTime();

        String driverPhoneNumber = "780-333-5555";
        String driverEmail = "esantos@example.com";

        /**
         * Step 2: Create a driver licenceNumber
         */
        String licenceNumber = "ABB005";

        /**
         * Step 3: Create a vehicle
         */
        String serialNumber = "serial990088";
        String plateNumber = "HELLO2";
        String type = "Truck";
        String make = "Toyota";
        String model = "Tacoma";
        int year = 2016;
        String color = "Blue";

        Vehicle vehicle = new Vehicle(serialNumber, plateNumber, type, make, model, year, color);

        /**
         * Step 4: Create driverInfo
         */
        DriverInfo driverInfo = new DriverInfo(licenceNumber, vehicle);

        /**
         * Step 5: Create driver if doesn't exist
         */
        UserESGetController.GetUserTask retrievedDriver =
                new UserESGetController.GetUserTask();

        // Find driver
        retrievedDriver.execute(driverUserName);

        try {
            User user = retrievedDriver.get();

            if (user == null) {
                // Create driver
                User driver = new User(driverFirstName, driverLastName, driverUserName,
                        driverDateOfBirth, driverPhoneNumber, driverEmail, driverInfo);
            } else {
                Log.i("Error", "User already registered. Try a different username");
                System.out.println("User already registered. Try a different username");
            }
            assert (true);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        /**
         * Step 6: Get driver user ID with the initial username
         */
        String driverID = null;

        UserESGetController.GetUserTask retrievedDriverForUserID =
                new UserESGetController.GetUserTask();

        // Find driver
        retrievedDriverForUserID.execute(driverUserName);

        User driver2 = null;

        try {
            driver2 = retrievedDriverForUserID.get();
            if (driver2 != null) {
                driverID = driver2.getUserID();
            }
            assert (true);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        /**
         * Step 7: Basic rider info
         */
        String u1FirstName = "Stephen";
        String u1LastName = "Harper";
        String u1UserName = "sharper";

        // Create date
        GregorianCalendar gc = new GregorianCalendar(1962, Calendar.FEBRUARY, 17);
        Date u1DateOfBirth = gc.getTime();

        String u1PhoneNumber = "780-888-5555";
        String u1Email = "sharper@example.com";

        /**
         * Step 8: Create credit card
         */
        String creditCard = "2222333366662222";

        /**
         * Step 9: Create Rider info
         */
        RiderInfo riderInfo = new RiderInfo(creditCard);

        /**
         * Step 10: Create rider if doesn't exist
         */
        UserESGetController.GetUserTask retrievedUser =
                new UserESGetController.GetUserTask();

        // Find user
        retrievedUser.execute(u1UserName);

        try {
            User user = retrievedUser.get();

            if (user == null) {
                // Create user
                User u1 = new User(u1FirstName, u1LastName, u1UserName, u1DateOfBirth, u1PhoneNumber,
                        u1Email, riderInfo);
            } else {
                Log.i("Error", "Try a different username");
                System.out.println("Try a different username");
            }
            assert (true);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        /**
         * Step 11: Get user ID with the initial username
         */
        String user2ID = null;

        UserESGetController.GetUserTask retrievedUserForUserID =
                new UserESGetController.GetUserTask();

        // Find user
        retrievedUserForUserID.execute(u1UserName);

        try {
            User user2 = retrievedUserForUserID.get();
            if (user2 != null) {
                user2ID = user2.getUserID();
            }
            assert (true);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        /**
         * Step 12: Make a request between two locations
         */
        String req1Status = AppSettings.REQUEST_PENDING;
        Address req1SrcAddr = new Address("1978 99 St NW, Edmonton", -113.483609, 53.44708019999999);
        Address req1DestAddr = new Address("11211 142 St NW, Edmonton", -113.5640156, 53.5612133);
        double req1Cost = 19.56;
        double req1Distance = 20.2;
        String req1RiderID = user2ID;

        Request req1 = new Request(req1Status, req1SrcAddr, req1DestAddr, req1Distance, req1Cost,
                req1RiderID);
        assert(true);

        /**
         * Step 13: Get requests by user with riderID
         */
        RequestESGetController.GetRequestTask retrievedRequest =
                new RequestESGetController.GetRequestTask();

        retrievedRequest.execute("riderID", user2ID);

        // Get the ID of the request to add to driver's list.
        // In this case, any request would do. We have chosen the first request that comes back
        // from Elasticsearch.
        Request riderRequest = null;
        String riderRequestID = "";

        try {
            ArrayList<Request> requests = retrievedRequest.get();
            if (requests.size() != 0) {
                riderRequest = requests.get(0);
                riderRequestID = riderRequest.getRequestID();
            } else {
                Log.i("Error", "No request exists for this user");
                System.out.println("No request exists for this user");
            }
            assert (true);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        /**
         * Step 14: Add driver to driverIDList
         */
        if (!(riderRequestID.isEmpty())) {
            String requestProperty = "driverIDList";
            String requestPropertyType = "array";
            String requestNewValue = driverID;

            RequestESSetController.SetPropertyValueTask setPropertyValueTask =
                    new RequestESSetController.SetPropertyValueTask();

            setPropertyValueTask.execute(riderRequestID, requestProperty, requestPropertyType,
                    requestNewValue);
            assert (true);
        } else {
            Log.i("Error", "Request ID is empty");
            System.out.println("Request ID is empty");
        }

        /**
         * Step 15: Get list of driver who accepted my request
         */
        RequestESGetController.GetRequestTask retrievedRequest2 =
                new RequestESGetController.GetRequestTask();

        retrievedRequest2.execute("riderID", user2ID);

        // Get the ID of the request to add to driver's list.
        // In this case, any request would do. We have chosen the first request that comes back
        // from Elasticsearch.
        Request riderRequest2 = null;
        String riderRequestID2 = "";
        ArrayList<String> myDriverIDList = null;

        try {
            ArrayList<Request> requests2 = retrievedRequest2.get();
            if (requests2.size() != 0) {
                riderRequest2 = requests2.get(0);
                riderRequestID2 = riderRequest2.getRequestID();
                myDriverIDList = riderRequest2.getDriverIDList();
            } else {
                Log.i("Error", "No request exists for this user");
                System.out.println("No request exists for this user");
            }
            assert (true);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        /**
         * Step 16: Get every ride inside the driverIDList
         */
        for (int i = 0; i < myDriverIDList.size(); ++i) {
            UserESGetController.GetUserByIdTask retrievedDriverFromList =
                    new UserESGetController.GetUserByIdTask();

            // Find driver
            retrievedDriverFromList.execute(myDriverIDList.get(i));

            try {
                User driver = retrievedDriverFromList.get();

                System.out.println("====================");
                System.out.println("Username: " + driver.getUserName());
                System.out.println("First name: " + driver.getFirstName());
                System.out.println("Last name: " + driver.getLastName());
                System.out.println("Rating: " + driver.getDriverInfo().getDriverRating().getRating());
                System.out.println("====================");
                assert (true);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * US 1.11.01 (added 2016-11-14)
     * As a rider, I want to rate a driver for his/her service (1-5).
     *
     * @throws Exception
     */
    @Test
    public void testRateDriver() throws Exception {
        /**
         * Step 1: Basic driver info
         */
        String driverFirstName = "Eddie";
        String driverLastName = "Santos";
        String driverUserName = "esantos";

        // Create date
        GregorianCalendar gcDriver = new GregorianCalendar(1988, Calendar.JANUARY, 13);
        Date driverDateOfBirth = gcDriver.getTime();

        String driverPhoneNumber = "780-333-5555";
        String driverEmail = "esantos@example.com";

        /**
         * Step 2: Create a driver licenceNumber
         */
        String licenceNumber = "ABB005";

        /**
         * Step 3: Create a vehicle
         */
        String serialNumber = "serial990088";
        String plateNumber = "HELLO2";
        String type = "Truck";
        String make = "Toyota";
        String model = "Tacoma";
        int year = 2016;
        String color = "Blue";

        Vehicle vehicle = new Vehicle(serialNumber, plateNumber, type, make, model, year, color);

        /**
         * Step 4: Create driverInfo
         */
        DriverInfo driverInfo = new DriverInfo(licenceNumber, vehicle);

        /**
         * Step 5: Create driver if doesn't exist
         */
        UserESGetController.GetUserTask retrievedDriver =
                new UserESGetController.GetUserTask();

        // Find driver
        retrievedDriver.execute(driverUserName);

        try {
            User user = retrievedDriver.get();

            if (user == null) {
                // Create driver
                User driver = new User(driverFirstName, driverLastName, driverUserName,
                        driverDateOfBirth, driverPhoneNumber, driverEmail, driverInfo);
            } else {
                Log.i("Error", "User already registered. Try a different username");
                System.out.println("User already registered. Try a different username");
            }
            assert (true);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        /**
         * Step 6: Get driver user ID with the initial username
         */
        String driverID = null;

        UserESGetController.GetUserTask retrievedDriverForUserID =
                new UserESGetController.GetUserTask();

        // Find driver
        retrievedDriverForUserID.execute(driverUserName);

        User driver2 = null;

        try {
            driver2 = retrievedDriverForUserID.get();
            if (driver2 != null) {
                driverID = driver2.getUserID();
            }
            assert (true);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        /**
         * Step 7: Basic rider info
         */
        String u1FirstName = "Stephen";
        String u1LastName = "Harper";
        String u1UserName = "sharper";

        // Create date
        GregorianCalendar gc = new GregorianCalendar(1962, Calendar.FEBRUARY, 17);
        Date u1DateOfBirth = gc.getTime();

        String u1PhoneNumber = "780-888-5555";
        String u1Email = "sharper@example.com";

        /**
         * Step 8: Create credit card
         */
        String creditCard = "2222333366662222";

        /**
         * Step 9: Create Rider info
         */
        RiderInfo riderInfo = new RiderInfo(creditCard);

        /**
         * Step 10: Create rider if doesn't exist
         */
        UserESGetController.GetUserTask retrievedUser =
                new UserESGetController.GetUserTask();

        // Find user
        retrievedUser.execute(u1UserName);

        try {
            User user = retrievedUser.get();

            if (user == null) {
                // Create user
                User u1 = new User(u1FirstName, u1LastName, u1UserName, u1DateOfBirth, u1PhoneNumber,
                        u1Email, riderInfo);
            } else {
                Log.i("Error", "Try a different username");
                System.out.println("Try a different username");
            }
            assert (true);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        /**
         * Step 11: Get user ID with the initial username
         */
        String user2ID = null;

        UserESGetController.GetUserTask retrievedUserForUserID =
                new UserESGetController.GetUserTask();

        // Find user
        retrievedUserForUserID.execute(u1UserName);

        try {
            User user2 = retrievedUserForUserID.get();
            if (user2 != null) {
                user2ID = user2.getUserID();
            }
            assert (true);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        /**
         * Step 12: Rate driver
         */
        double newRating = 4;
        String newRatingAsString = String.valueOf(newRating);


        UserESAddController.AddNewDriverRatingTask addNewDriverRatingTask =
                new UserESAddController.AddNewDriverRatingTask();

        addNewDriverRatingTask.execute(driverID, newRatingAsString);
        assert (true);
    }

}
