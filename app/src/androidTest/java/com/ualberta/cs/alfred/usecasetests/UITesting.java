package com.ualberta.cs.alfred.usecasetests;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.preference.PreferenceManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.support.test.uiautomator.Until;
import android.support.v7.view.ContextThemeWrapper;

import com.ualberta.cs.alfred.Address;
import com.ualberta.cs.alfred.DriverInfo;
import com.ualberta.cs.alfred.LocalDataManager;
import com.ualberta.cs.alfred.MainActivity;
import com.ualberta.cs.alfred.PartialAcceptances;
import com.ualberta.cs.alfred.PartialRequests;
import com.ualberta.cs.alfred.R;
import com.ualberta.cs.alfred.Request;
import com.ualberta.cs.alfred.RequestDetailsActivity;
import com.ualberta.cs.alfred.RequestESAddController;
import com.ualberta.cs.alfred.RequestESDeleteController;
import com.ualberta.cs.alfred.RequestESGetController;
import com.ualberta.cs.alfred.RequestESSetController;
import com.ualberta.cs.alfred.RequestList;
import com.ualberta.cs.alfred.RiderInfo;
import com.ualberta.cs.alfred.User;
import com.ualberta.cs.alfred.UserESGetController;
import com.ualberta.cs.alfred.UserESGetControllerTest;
import com.ualberta.cs.alfred.UserESSetController;
import com.ualberta.cs.alfred.Vehicle;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.Matchers.anything;


/**
 * Created by mmcote on 2016-11-27.
 */

@RunWith(AndroidJUnit4.class)
public class UITesting {
    private User user;
    final String STARTADDRESS = "10127 121 ST NW";
    final String STARTCITY = "Edmonton";
    final String ENDADDRESS = "116 St 85 Ave NW";
    final String ENDCITY = "Edmonton";

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    /* Constructor that will create the user for testing if not already availible
     */
    private void checkUser() {
        UserESGetController.GetUserTask userESGetController = new UserESGetController.GetUserTask();
        String username = "macarooniANDcheese";
        userESGetController.execute(username);
        try {
            user = userESGetController.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        if (user == null) {
            user = new User("Shida", "He", username, new Date(), "1-780-901-2233", "shidahe@ualberta.ca", new RiderInfo("34324242343423"),
                new DriverInfo("34324234", new Vehicle("2432434", "432434","Van", "Jeep", "Grand Cherokee", 2014, "Red")));
            UserESGetController.GetUserTask getUserTask = new UserESGetController.GetUserTask();
            getUserTask.execute(user.getUserName());
            try {
                user = getUserTask.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    /* Accessory function that will be used at the start of each test to log in the user as the
    specified mode.
     */
    private void login(String mode) {
        // login as a given rider
        onView(withId(R.id.username_input)).perform(typeText(user.getUserName()));
        if (mode.contentEquals("Driver Mode")) {
            onView(withId(R.id.mode1_button)).perform(click());
        } else {
            onView(withId(R.id.mode2_button)).perform(click());
        }
        onView(withId(R.id.main_button)).perform(click());
    }


    private void makeRequest() {
        // REQUEST BETWEEN TWO POINTS USING ADDRESSES
        // input start address
        onView(withId(R.id.start_input_1)).perform(typeText(STARTADDRESS));
        onView(withId(R.id.start_input_2)).perform(typeText(STARTCITY));
        // input end address
        onView(withId(R.id.end_input_1)).perform(typeText(ENDADDRESS));
        onView(withId(R.id.end_input_2)).perform(typeText(ENDCITY));
        // confirm request
        onView(withId(R.id.request_done_button)).perform(click());
    }

    // must be on request details page
    private void deleteRequest() {
        onView(withId(R.id.cancel_request_button)).perform(click());
        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        // Espresso code: login the user, navigate to contact sync and enable clicking on toggle
        // UIAutomator code: navigate back to my app under testm
        UiObject deleteButton = device.findObject(new UiSelector().textContains("DELETE"));
        if (!deleteButton.exists()) {
            throw new AssertionError("View with text <" + "DELETE" + "> not found!");
        }
        try {
            deleteButton.click();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }

    }

    // USER STORIES UI TESTS -----------------------------------------------------------------------
    /*
        US 01.01.01
        As a rider, I want to request rides between two locations.
    */
    @Test
    public void makeRequestBetweenTwoLocationsAsRider() {
        checkUser();
        login("Rider Mode");
        onView(withId(R.id.request_button)).perform(click());
        makeRequest();
        // This is a test of existance the click method will cause the test to fail if there is no
        // element in the list to click on and since we begin the test with zero elements then the
        // only way this will pass is if a request has been added to the list
        onData(anything()).inAdapterView(withId(R.id.requestedListView)).atPosition(0).perform(click());
        deleteRequest();
    }

    // USER STORIES UI TESTS -----------------------------------------------------------------------
    /*
        US 01.02.01
        As a rider, I want to see current requests I have open.
    */
    @Test
    public void testNavigateToOpenRequests() {
        checkUser();
        login("Rider Mode");
        onView(withId(R.id.request_button)).perform(click());
        makeRequest();
        onData(anything()).inAdapterView(withId(R.id.requestedListView)).atPosition(0).perform(click());
        // This test simply navigates to the open requests list page and checks if the newly made
        // request is there, this proves this is the open request page as we compare its status
        // to Requested
        onView(withId(R.id.status)).check(matches(withText("Requested")));
        deleteRequest();
    }

    /*
        US 01.03.01
        As a rider, I want to be notified if my request is accepted.
     */
    @Test
    public void testAcceptanceNotification() {
        checkUser();
        /* create a request that we can grab an ID off of to simulate a driver accepting a request
         to go to pending.
         */
        Address startAddress = new Address(STARTADDRESS, 54.0, 54.0);
        Address endAddress = new Address(ENDADDRESS, 55.0, 55.0);
        Request request = new Request("Requested", startAddress, endAddress, 34.0, 34.0, user.getUserID());
        RequestESGetController.GetRequestTask getRequestTask = new RequestESGetController.GetRequestTask();
        getRequestTask.execute("riderID", user.getUserID());

        try {
            request = getRequestTask.get().get(0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        if (request.getRequestID() == null) {
            assert(false);
        }

        RequestESSetController.SetPropertyValueTask setPropertyValueTask =
                new RequestESSetController.SetPropertyValueTask();
        login("Rider Mode");
        setPropertyValueTask.execute(request.getRequestID(), "requestStatus", "String", "Pending");
        try {
            TimeUnit.MILLISECONDS.sleep(6500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        onView(withId(R.id.request_button)).perform(click());
//        makeRequest();
        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        // Espresso code: login the user, navigate to contact sync and enable clicking on toggle
        // UIAutomator code: navigate back to my app under testm
        device.openNotification();
//        device.wait(Until.hasObject(By.pkg("com.android.systemui")), 100000);


        UiObject acceptedNotification = device.findObject(new UiSelector().textContains("Pending"));
        if (!acceptedNotification.exists()) {
            throw new AssertionError("The notification was not found!");
        }
        try {
            acceptedNotification.click();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }

        RequestESDeleteController.DeleteRequestTask deleteRequestTask =
                new RequestESDeleteController.DeleteRequestTask();
        deleteRequestTask.execute(request.getRequestID());
    }

    /*
        US 01.04.01
        As a rider, I want to cancel requests.
     */
    @Test
    public void testCancelRequests() {
        checkUser();
        Address startAddress = new Address(STARTADDRESS, 54.0, 54.0);
        Address endAddress = new Address(ENDADDRESS, 55.0, 55.0);
        Request requested = new Request("Requested", startAddress, endAddress, 34.0, 34.0, user.getUserID());
        Request pending = new Request("Pending", startAddress, endAddress, 34.0, 34.0, user.getUserID());
        Request accepted = new Request("Accepted", startAddress, endAddress, 34.0, 34.0, user.getUserID());

        login("Rider Mode");
        onView(withId(R.id.button_requested)).perform(click());
        onData(anything()).inAdapterView(withId(R.id.requestedListView)).atPosition(0).perform(click());
        deleteRequest();

        onView(withId(R.id.button_pending)).perform(click());
        onData(anything()).inAdapterView(withId(R.id.pendingListView)).atPosition(0).perform(click());
        deleteRequest();

        onView(withId(R.id.button_accepted)).perform(click());
        onData(anything()).inAdapterView(withId(R.id.acceptedListView)).atPosition(0).perform(click());
        deleteRequest();
    }

    /*
        US 01.05.01
        As a rider, I want to be able to phone or email the driver who accepted a request.
     */
    @Test
    public void testPhone() {
        checkUser();
        User driver1 = new User("Marc", "Anthony", "billy bob", new Date(), "1-780-901-2233", "marc@ualberta.ca",
                new DriverInfo("343242324334", new Vehicle("2433432434", "432434","Van", "Jeep", "Cherokee", 2014, "Blue")));

        UserESGetController.GetUserTask userESGetController1 = new UserESGetController.GetUserTask();
        userESGetController1.execute(driver1.getUserName());

        try {
            driver1 = userESGetController1.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        Address startAddress = new Address(STARTADDRESS, 54.0, 54.0);
        Address endAddress = new Address(ENDADDRESS, 55.0, 55.0);
        Request pending = new Request("Pending", startAddress, endAddress, 34.0, 34.0, user.getUserID());

        RequestESGetController.GetRequestTask getRequestTask = new RequestESGetController.GetRequestTask();
        getRequestTask.execute("riderID", user.getUserID());

        ArrayList<Request> requestList = null;
        RequestList requestListReal = null;
        try {
            requestList = getRequestTask.get();
            requestListReal = new RequestList(requestList);
            requestList = requestListReal.getSpecificRequestList("Pending");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        pending = requestList.get(0);
        RequestESAddController.AddItemToListTask addItemToListTask = new RequestESAddController.AddItemToListTask();
        addItemToListTask.execute(pending.getRequestID(), "driverIDList", driver1.getUserID());

        login("Rider Mode");
        onView(withId(R.id.button_pending)).perform(click());
        onData(anything()).inAdapterView(withId(R.id.pendingListView)).atPosition(0).perform(click());
        onData(anything()).inAdapterView(withId(R.id.biddingDriversListView)).atPosition(0).perform(click());

        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        UiObject viewButton = device.findObject(new UiSelector().textContains("VIEW"));
        if (!viewButton.exists()) {
            throw new AssertionError("View with text <" + "View Button" + "> not found!");
        }
        try {
            viewButton.click();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.call_driver_button)).perform(click());
        try {
            TimeUnit.MILLISECONDS.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertTrue(device.getCurrentPackageName().contentEquals("com.android.dialer"));
    }

    @Test
    public void testEmail() {
        checkUser();
        User driver1 = new User("Marc", "Anthony", "billy bob", new Date(), "1-780-901-2233", "marc@ualberta.ca",
                new DriverInfo("343242324334", new Vehicle("2433432434", "432434","Van", "Jeep", "Cherokee", 2014, "Blue")));

        UserESGetController.GetUserTask userESGetController1 = new UserESGetController.GetUserTask();
        userESGetController1.execute(driver1.getUserName());

        try {
            driver1 = userESGetController1.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        Address startAddress = new Address(STARTADDRESS, 54.0, 54.0);
        Address endAddress = new Address(ENDADDRESS, 55.0, 55.0);
        Request pending = new Request("Pending", startAddress, endAddress, 34.0, 34.0, user.getUserID());

        RequestESGetController.GetRequestTask getRequestTask = new RequestESGetController.GetRequestTask();
        getRequestTask.execute("riderID", user.getUserID());

        ArrayList<Request> requestList = null;
        RequestList requestListReal = null;
        try {
            requestList = getRequestTask.get();
            requestListReal = new RequestList(requestList);
            requestList = requestListReal.getSpecificRequestList("Pending");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        pending = requestList.get(0);
        RequestESAddController.AddItemToListTask addItemToListTask = new RequestESAddController.AddItemToListTask();
        addItemToListTask.execute(pending.getRequestID(), "driverIDList", driver1.getUserID());

        login("Rider Mode");
        onView(withId(R.id.button_pending)).perform(click());
        onData(anything()).inAdapterView(withId(R.id.pendingListView)).atPosition(0).perform(click());
        onData(anything()).inAdapterView(withId(R.id.biddingDriversListView)).atPosition(0).perform(click());

        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        UiObject viewButton = device.findObject(new UiSelector().textContains("VIEW"));
        if (!viewButton.exists()) {
            throw new AssertionError("View with text <" + "View Button" + "> not found!");
        }
        try {
            viewButton.click();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.email_driver_button)).perform(click());
        onView(withId(R.id.sendEmailButton)).perform(click());
        try {
            TimeUnit.MILLISECONDS.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String item = device.getCurrentPackageName();
        assertTrue(device.getCurrentPackageName().contentEquals("com.android.email"));
    }

    /*
        US 01.06.01
        As a rider, I want an estimate of a fair fare to offer to drivers.
    */
    @Test
    public void testGetEstimate() {
        checkUser();
        login("Rider Mode");
        onView(withId(R.id.request_button)).perform(click());
        makeRequest();
        onData(anything()).inAdapterView(withId(R.id.requestedListView)).atPosition(0).perform(click());
        onView(withId(R.id.est_price)).check(matches(isDisplayed()));
        deleteRequest();
    }

    /*
        US 01.07.01
        As a rider, I want to confirm the completion of a request and enable payment.
     */
    @Test
    public void testCompleteAndPay() {
        checkUser();
        Address startAddress = new Address(STARTADDRESS, 54.0, 54.0);
        Address endAddress = new Address(ENDADDRESS, 55.0, 55.0);
        Request accepted = new Request("Accepted", startAddress, endAddress, 34.0, 34.0, user.getUserID());
        login("Rider Mode");
        onView(withId(R.id.button_accepted)).perform(click());
        onData(anything()).inAdapterView(withId(R.id.acceptedListView)).atPosition(0).perform(click());
        onView(withId(R.id.ride_complete_button)).perform(click());
        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        UiObject deleteButton = device.findObject(new UiSelector().textContains("YES"));
        if (!deleteButton.exists()) {
            throw new AssertionError("View with text <" + "YES" + "> not found!");
        }
        try {
            deleteButton.click();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }
        // TODO: Go to completed requests and find the requests after the changes have been made to the button
    }

    /*
        US 01.08.01
        As a rider, I want to confirm a driver's acceptance. This allows us to choose from a list of acceptances if more than 1 driver accepts simultaneously.
    */
    @Test
    public void testConfirmDriversBid() {
        checkUser();
        login("Rider Mode");
        Address startAddress = new Address(STARTADDRESS, 54.0, 54.0);
        Address endAddress = new Address(ENDADDRESS, 55.0, 55.0);
        Request pending = new Request("Pending", startAddress, endAddress, 34.0, 34.0, user.getUserID());
        RequestESGetController.GetRequestTask getRequestTask = new RequestESGetController.GetRequestTask();
        getRequestTask.execute("riderID", user.getUserID());

        String requestID = null;
        ArrayList<Request> requestList = null;
        try {
            requestList = getRequestTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        requestID = requestList.get(0).getRequestID();
        RequestESAddController.AddItemToListTask addItemToListTask1 = new RequestESAddController.AddItemToListTask();
        RequestESAddController.AddItemToListTask addItemToListTask2 = new RequestESAddController.AddItemToListTask();

        User driver1 = new User("Marc", "Anthony", "marcAnthony", new Date(), "1-780-901-2233", "marc@ualberta.ca",
                new DriverInfo("343242324334", new Vehicle("2433432434", "432434","Van", "Jeep", "Cherokee", 2014, "Blue")));
        User driver2 = new User("Jacob", "Creek", "Jacob Creek", new Date(), "1-780-901-2333", "creek@ualberta.ca",
                new DriverInfo("d343242243", new Vehicle("324324", "43243434","Car", "Mercedes", "Smart Car", 2014, "Pink")));

        UserESGetController.GetUserTask userESGetController1 = new UserESGetController.GetUserTask();
        userESGetController1.execute(driver1.getUserName());

        UserESGetController.GetUserTask userESGetController2 = new UserESGetController.GetUserTask();
        userESGetController2.execute(driver2.getUserName());
        try {
            driver1 = userESGetController1.get();
            driver2 = userESGetController2.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        if (requestID != null) {
            addItemToListTask1.execute(requestID, "driverIDList", driver1.getUserID());
            addItemToListTask2.execute(requestID, "driverIDList", driver2.getUserID());
            onView(withId(R.id.button_pending)).perform(click());
            onData(anything()).inAdapterView(withId(R.id.pendingListView)).atPosition(0).perform(click());
            onData(anything()).inAdapterView(withId(R.id.biddingDriversListView)).atPosition(1).perform(click());

            UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
            UiObject acceptButton = device.findObject(new UiSelector().textContains("YES"));
            if (!acceptButton.exists()) {
                throw new AssertionError("View with text <" + "YES" + "> not found!");
            }
            try {
                acceptButton.click();
            } catch (UiObjectNotFoundException e) {
                e.printStackTrace();
            }
            onView(withId(R.id.accept_pending_button)).perform(click());
        }
        onView(withId(R.id.button_accepted)).perform(click());
        onData(anything()).inAdapterView(withId(R.id.acceptedListView)).atPosition(0).perform(click());
        deleteRequest();
    }

    /*
        US 1.09.01 (added 2016-11-14)
        As a rider, I should see a description of the driver's vehicle.
    */
    @Test
    public void testDriverVehicleData() {
        checkUser();
        login("Rider Mode");
        Address startAddress = new Address(STARTADDRESS, 54.0, 54.0);
        Address endAddress = new Address(ENDADDRESS, 55.0, 55.0);
        Request pending = new Request("Pending", startAddress, endAddress, 34.0, 34.0, user.getUserID());
        RequestESGetController.GetRequestTask getRequestTask = new RequestESGetController.GetRequestTask();
        getRequestTask.execute("riderID", user.getUserID());

        String requestID = null;
        ArrayList<Request> requestList = null;
        try {
            requestList = getRequestTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        requestID = requestList.get(0).getRequestID();
        RequestESAddController.AddItemToListTask addItemToListTask1 = new RequestESAddController.AddItemToListTask();
        RequestESAddController.AddItemToListTask addItemToListTask2 = new RequestESAddController.AddItemToListTask();

        String make = "Jeep";
        String model = "Cherokee";
        User driver1 = new User("Marc", "Anthony", "marcAnthony", new Date(), "1-780-901-2233", "marc@ualberta.ca",
                new DriverInfo("343242324334", new Vehicle("2433432434", "432434","Van", make, model, 2014, "Blue")));

        UserESGetController.GetUserTask userESGetController1 = new UserESGetController.GetUserTask();
        userESGetController1.execute(driver1.getUserName());

        try {
            driver1 = userESGetController1.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        if (requestID != null) {
            addItemToListTask1.execute(requestID, "driverIDList", driver1.getUserID());
            onView(withId(R.id.button_pending)).perform(click());
            onData(anything()).inAdapterView(withId(R.id.pendingListView)).atPosition(0).perform(click());
            onData(anything()).inAdapterView(withId(R.id.biddingDriversListView)).atPosition(0).perform(click());

            UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
            UiObject acceptButton = device.findObject(new UiSelector().textContains("View Profile"));
            if (!acceptButton.exists()) {
                throw new AssertionError("View with text <" + "View Profile" + "> not found!");
            }
            try {
                acceptButton.click();
            } catch (UiObjectNotFoundException e) {
                e.printStackTrace();
            }
            onView(withId(R.id.vehicle_make)).check(matches(withText(make)));
            onView(withId(R.id.vehicle_model)).check(matches(withText(model)));
        }
        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        device.pressBack();
        device.pressBack();
        onData(anything()).inAdapterView(withId(R.id.pendingListView)).atPosition(0).perform(click());
        deleteRequest();
    }

    /*
        US 05.01.01
        As a driver,  I want to accept a request I agree with and accept that offered payment upon
    */
    @Test
    public void testAcceptingRequest() {
        checkUser();
        login("Driver Mode");

        User rider1 = new User("Marc", "Anthony", "marcAnthony", new Date(), "1-780-901-2233", "marc@ualberta.ca",
                new RiderInfo("32423432432"));

        UserESGetController.GetUserTask userESGetController1 = new UserESGetController.GetUserTask();
        userESGetController1.execute(rider1.getUserName());

        try {
            rider1 = userESGetController1.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        Address startAddress = new Address(STARTADDRESS, 54.0, 54.0);
        Address endAddress = new Address(ENDADDRESS, 55.0, 55.0);

        Request pending = new Request("Requested", startAddress, endAddress, 34.0, 34.0, rider1.getUserID());
        onView(withId(R.id.button_requested)).perform(click());
        onData(anything()).inAdapterView(withId(R.id.requestedListView)).atPosition(0).perform(click());
        onView(withId(R.id.accept_pending_button)).perform(click());
        onView(withId(R.id.button_pending)).perform(click());
        onData(anything()).inAdapterView(withId(R.id.pendingListView)).atPosition(0).perform(click());
    }

    /*
        US 05.01.01
        As a driver,  I want to accept a request I agree with and accept that offered payment upon
    */
    @Test
    public void testAcceptingPaymentUponCompletion() {
        checkUser();

        String riderUsername = "jimmyFallon";
        UserESGetController.GetUserTask userESGetController = new UserESGetController.GetUserTask();
        userESGetController.execute(riderUsername);
        User rider = null;
        try {
            rider = userESGetController.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        if (rider == null) {
            rider = new User("Marc", "Anthony", riderUsername, new Date(), "1-780-901-2233", "marc@ualberta.ca",
                    new RiderInfo("32423432432"));
            userESGetController = new UserESGetController.GetUserTask();
            userESGetController.execute(riderUsername);
            try {
                rider = userESGetController.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        Address startAddress = new Address(STARTADDRESS, 54.0, 54.0);
        Address endAddress = new Address(ENDADDRESS, 55.0, 55.0);
        Request request = new Request("Accepted", startAddress, endAddress, 34.0, 34.0, rider.getUserID());

        RequestESGetController.GetRequestTask getRequestTask = new RequestESGetController.GetRequestTask();
        getRequestTask.execute("riderID", rider.getUserID());

        ArrayList<Request> requestList = null;
        RequestList requestListReal = null;
        try {
            requestList = getRequestTask.get();
            requestListReal = new RequestList(requestList);
            requestList = requestListReal.getSpecificRequestList("Accepted");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        request = requestList.get(0);

        RequestESSetController.SetPropertyValueTask setPropertyValueTask1 =
                new RequestESSetController.SetPropertyValueTask();
        RequestESSetController.SetPropertyValueTask setPropertyValueTask2 =
                new RequestESSetController.SetPropertyValueTask();
        login("Driver Mode");
        setPropertyValueTask1.execute(request.getRequestID(), "requestStatus", "String", "Awaiting Payment");
        setPropertyValueTask2.execute(request.getRequestID(), "driverID", "String", user.getUserID());

        try {
            TimeUnit.MILLISECONDS.sleep(6500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        UiObject agreeButton = device.findObject(new UiSelector().textContains("Ok"));
        if (!agreeButton.exists()) {
            throw new AssertionError("View with text <" + "View Profile" + "> not found!");
        }
        try {
            agreeButton.click();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }
    }

    /*
        US 05.02.01
        As a driver, I want to view a list of things I have accepted that are pending, each request with its description, and locations.
    */
    @Test
    public void testViewPendingListAndViewDescription() {
        checkUser();
        String riderUsername = "jimmyFallon";
        UserESGetController.GetUserTask userESGetController = new UserESGetController.GetUserTask();
        userESGetController.execute(riderUsername);
        User rider = null;
        try {
            rider = userESGetController.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        if (rider == null) {
            rider = new User("Marc", "Anthony", riderUsername, new Date(), "1-780-901-2233", "marc@ualberta.ca",
                    new RiderInfo("32423432432"));
            userESGetController = new UserESGetController.GetUserTask();
            userESGetController.execute(riderUsername);
            try {
                rider = userESGetController.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        Address startAddress = new Address(STARTADDRESS, 54.0, 54.0);
        Address endAddress = new Address(ENDADDRESS, 55.0, 55.0);
        Request request = new Request("Pending", startAddress, endAddress, 34.0, 34.0, rider.getUserID());

        RequestESGetController.GetRequestTask getRequestTask = new RequestESGetController.GetRequestTask();
        getRequestTask.execute("riderID", rider.getUserID());

        ArrayList<Request> requestList = null;
        RequestList requestListReal = null;
        try {
            requestList = getRequestTask.get();
            requestListReal = new RequestList(requestList);
            requestList = requestListReal.getSpecificRequestList("Pending");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        request = requestList.get(0);

        login("Driver Mode");

        onView(withId(R.id.button_pending)).perform(click());
        onData(anything()).inAdapterView(withId(R.id.pendingListView)).atPosition(0).perform(click());
//        onView(withId(R.id.status)).check(matches(withText("Pending")));

        RequestESDeleteController.DeleteRequestTask deleteRequestTask =
                new RequestESDeleteController.DeleteRequestTask();
        deleteRequestTask.execute(request.getRequestID());
    }

    /*
        US 05.03.01
        As a driver, I want to see if my acceptance was accepted.
    */
    @Test
    public void testViewAcceptedList() {
        checkUser();

        String riderUsername = "jimmyFallon";
        UserESGetController.GetUserTask userESGetController = new UserESGetController.GetUserTask();
        userESGetController.execute(riderUsername);
        User rider = null;
        try {
            rider = userESGetController.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        if (rider == null) {
            rider = new User("Marc", "Anthony", riderUsername, new Date(), "1-780-901-2233", "marc@ualberta.ca",
                    new RiderInfo("32423432432"));
            userESGetController = new UserESGetController.GetUserTask();
            userESGetController.execute(riderUsername);
            try {
                rider = userESGetController.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        Address startAddress = new Address(STARTADDRESS, 54.0, 54.0);
        Address endAddress = new Address(ENDADDRESS, 55.0, 55.0);
        Request request = new Request("Accepted", startAddress, endAddress, 34.0, 34.0, rider.getUserID());

        RequestESGetController.GetRequestTask getRequestTask = new RequestESGetController.GetRequestTask();
        getRequestTask.execute("riderID", rider.getUserID());

        ArrayList<Request> requestList = null;
        RequestList requestListReal = null;
        try {
            requestList = getRequestTask.get();
            requestListReal = new RequestList(requestList);
            requestList = requestListReal.getSpecificRequestList("Accepted");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        request = requestList.get(0);

        login("Driver Mode");

        RequestESSetController.SetPropertyValueTask setPropertyValueTask2 = new RequestESSetController.SetPropertyValueTask();
        setPropertyValueTask2.execute(request.getRequestID(), "driverID", "String", user.getUserID());
        RequestESAddController.AddItemToListTask addItemToListTask = new RequestESAddController.AddItemToListTask();
        addItemToListTask.execute(request.getRequestID(), "driverIDList", user.getUserID());

        onView(withId(R.id.button_accepted)).perform(click());
        onData(anything()).inAdapterView(withId(R.id.acceptedListView)).atPosition(0).perform(click());
        onView(withId(R.id.status)).check(matches(withText("Accepted")));

        RequestESDeleteController.DeleteRequestTask deleteRequestTask =
                new RequestESDeleteController.DeleteRequestTask();
        deleteRequestTask.execute(request.getRequestID());
    }

    /*
        US 05.04.01
        As a driver, I want to be notified if my ride offer was accepted.
    */
    @Test
    public void testDriverRequestNotification() {
        checkUser();

        String riderUsername = "jimmyFallon";

        UserESGetController.GetUserTask userESGetController = new UserESGetController.GetUserTask();
        userESGetController.execute(riderUsername);
        User rider = null;
        try {
            rider = userESGetController.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        if (rider == null) {
            rider = new User("Marc", "Anthony", riderUsername, new Date(), "1-780-901-2233", "marc@ualberta.ca",
                    new RiderInfo("32423432432"));
            userESGetController = new UserESGetController.GetUserTask();
            userESGetController.execute(riderUsername);
            try {
                rider = userESGetController.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        Address startAddress = new Address(STARTADDRESS, 54.0, 54.0);
        Address endAddress = new Address(ENDADDRESS, 55.0, 55.0);
        Request request = new Request("Pending", startAddress, endAddress, 34.0, 34.0, rider.getUserID());

        RequestESGetController.GetRequestTask getRequestTask = new RequestESGetController.GetRequestTask();
        getRequestTask.execute("riderID", rider.getUserID());

        ArrayList<Request> requestList = null;
        RequestList requestListReal = null;
        try {
            requestList = getRequestTask.get();
            requestListReal = new RequestList(requestList);
            requestList = requestListReal.getSpecificRequestList("Pending");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        request = requestList.get(0);
        RequestESAddController.AddItemToListTask addItemToListTask = new RequestESAddController.AddItemToListTask();
        addItemToListTask.execute(request.getRequestID(), "driverIDList", user.getUserID());

        login("Driver Mode");

        RequestESSetController.SetPropertyValueTask setPropertyValueTask = new RequestESSetController.SetPropertyValueTask();
        setPropertyValueTask.execute(request.getRequestID(), "requestStatus", "String", "Accepted");

        try {
            TimeUnit.MILLISECONDS.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        device.openNotification();
        try {
            TimeUnit.MILLISECONDS.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        UiObject acceptedNotification = device.findObject(new UiSelector().textContains("Pick"));
        if (!acceptedNotification.exists()) {
            throw new AssertionError("The notification was not found!");
        }
        try {
            acceptedNotification.click();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }

        RequestESDeleteController.DeleteRequestTask deleteRequestTask =
                new RequestESDeleteController.DeleteRequestTask();
        deleteRequestTask.execute(request.getRequestID());
    }

    /*
        US 08.01.01
        As an driver, I want to see requests that I already accepted while offline.
    */
    @Test
    public void testOfflineAcceptedRequests() {
        checkUser();

        String riderUsername = "jimmyFallon";

        UserESGetController.GetUserTask userESGetController = new UserESGetController.GetUserTask();
        userESGetController.execute(riderUsername);
        User rider = null;
        try {
            rider = userESGetController.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        if (rider == null) {
            rider = new User("Marc", "Anthony", riderUsername, new Date(), "1-780-901-2233", "marc@ualberta.ca",
                    new RiderInfo("32423432432"));
            userESGetController = new UserESGetController.GetUserTask();
            userESGetController.execute(riderUsername);
            try {
                rider = userESGetController.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        Address startAddress = new Address(STARTADDRESS, 54.0, 54.0);
        Address endAddress = new Address(ENDADDRESS, 55.0, 55.0);
        Request request = new Request("Pending", startAddress, endAddress, 34.0, 34.0, rider.getUserID());

        RequestESGetController.GetRequestTask getRequestTask = new RequestESGetController.GetRequestTask();
        getRequestTask.execute("riderID", rider.getUserID());

        ArrayList<Request> requestList = null;
        RequestList requestListReal = null;
        try {
            requestList = getRequestTask.get();
            requestListReal = new RequestList(requestList);
            requestList = requestListReal.getSpecificRequestList("Pending");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        request = requestList.get(0);
        RequestESAddController.AddItemToListTask addItemToListTask = new RequestESAddController.AddItemToListTask();
        addItemToListTask.execute(request.getRequestID(), "driverIDList", user.getUserID());

        login("Driver Mode");

        try {
            TimeUnit.MILLISECONDS.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(InstrumentationRegistry.getTargetContext());
        ArrayList<Request> pendingList;
        onView(withId(R.id.button_pending)).perform(click());

        try {
            TimeUnit.MILLISECONDS.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Context context = InstrumentationRegistry.getTargetContext();
        pendingList = LocalDataManager.loadRPendingList(preferences.getString("MODE", null), InstrumentationRegistry.getTargetContext());
        assertTrue(pendingList.size() > 0);
    }

    /*
        US 08.02.01
        As a rider, I want to see requests that I have made while offline.
    */
    @Test
    public void testSeeRequestsMadeOffline() {
        checkUser();
        login("Rider Mode");

        String riderUsername = "jimmyFallon";

        UserESGetController.GetUserTask userESGetController = new UserESGetController.GetUserTask();
        userESGetController.execute(riderUsername);
        User rider = null;
        try {
            rider = userESGetController.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        if (rider == null) {
            rider = new User("Marc", "Anthony", riderUsername, new Date(), "1-780-901-2233", "marc@ualberta.ca",
                    new RiderInfo("32423432432"));
            userESGetController = new UserESGetController.GetUserTask();
            userESGetController.execute(riderUsername);
            try {
                rider = userESGetController.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        Address startAddress = new Address(STARTADDRESS, 54.0, 54.0);
        Address endAddress = new Address(ENDADDRESS, 55.0, 55.0);
        Request request = new Request("Accepted", startAddress, endAddress, 34.0, 34.0, rider.getUserID());

        PartialRequests partialRequest = new PartialRequests("Rider Mode",rider.getUserID(),"Requested","null","null",Double.toString(2.3),Double.toString(2.3),Double.toString(2.3),Double.toString(2.3));
        ArrayList<PartialRequests> offlineRequestList = LocalDataManager.loadPartialRequests(InstrumentationRegistry.getTargetContext());
        offlineRequestList.add(partialRequest);
        LocalDataManager.savePartialRequests(offlineRequestList,InstrumentationRegistry.getTargetContext());
        ArrayList<PartialRequests> requestsToBeSent = LocalDataManager.loadPartialRequests(InstrumentationRegistry.getTargetContext());
        assertTrue(requestsToBeSent.size() > 0);

        onView(withId(R.id.button_requested)).perform(click());
        onData(anything()).inAdapterView(withId(R.id.requestedListView)).atPosition(0).perform(click());
    }

    /*
        US 08.03.01
        As a rider, I want to make requests that will be sent once I get connectivity again.
    */
    @Test
    public void testMakeRequestsOffline() {
        checkUser();
        login("Driver Mode");
        String riderUsername = "jimmyFallon";

        UserESGetController.GetUserTask userESGetController = new UserESGetController.GetUserTask();
        userESGetController.execute(riderUsername);
        User rider = null;
        try {
            rider = userESGetController.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        if (rider == null) {
            rider = new User("Marc", "Anthony", riderUsername, new Date(), "1-780-901-2233", "marc@ualberta.ca",
                    new RiderInfo("32423432432"));
            userESGetController = new UserESGetController.GetUserTask();
            userESGetController.execute(riderUsername);
            try {
                rider = userESGetController.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        Address startAddress = new Address(STARTADDRESS, 54.0, 54.0);
        Address endAddress = new Address(ENDADDRESS, 55.0, 55.0);
        Request request = new Request("Accepted", startAddress, endAddress, 34.0, 34.0, rider.getUserID());

        PartialRequests partialRequest = new PartialRequests("Rider Mode",rider.getUserID(),"Requested","null","null",Double.toString(2.3),Double.toString(2.3),Double.toString(2.3),Double.toString(2.3));
        ArrayList<PartialRequests> offlineRequestList = LocalDataManager.loadPartialRequests(InstrumentationRegistry.getTargetContext());
        offlineRequestList.add(partialRequest);
        LocalDataManager.savePartialRequests(offlineRequestList,InstrumentationRegistry.getTargetContext());
        ArrayList<PartialRequests> requestsToBeSent = LocalDataManager.loadPartialRequests(InstrumentationRegistry.getTargetContext());
        assertTrue(requestsToBeSent.size() > 0);
    }
    /*
        US 08.04.01
        As a driver, I want to accept requests that will be sent once I get connectivity again.
     */
    @Test
    public void testAcceptRequestOffline() {
        checkUser();
        login("Driver Mode");
        String riderUsername = "jimmyFallon";

        UserESGetController.GetUserTask userESGetController = new UserESGetController.GetUserTask();
        userESGetController.execute(riderUsername);
        User rider = null;
        try {
            rider = userESGetController.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        if (rider == null) {
            rider = new User("Marc", "Anthony", riderUsername, new Date(), "1-780-901-2233", "marc@ualberta.ca",
                    new RiderInfo("32423432432"));
            userESGetController = new UserESGetController.GetUserTask();
            userESGetController.execute(riderUsername);
            try {
                rider = userESGetController.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        Address startAddress = new Address(STARTADDRESS, 54.0, 54.0);
        Address endAddress = new Address(ENDADDRESS, 55.0, 55.0);
        Request request = new Request("Requested", startAddress, endAddress, 34.0, 34.0, rider.getUserID());


        ArrayList<PartialAcceptances> partialAcceptancesList = LocalDataManager.loadPartialAcceptances(InstrumentationRegistry.getTargetContext());
        if (partialAcceptancesList == null){
            partialAcceptancesList = new ArrayList<PartialAcceptances>();
        }

        PartialAcceptances offlineAcceptances = new PartialAcceptances("Requested", user.getUserID(), "Driver Mode", request, user.getUserID());
        partialAcceptancesList.add(offlineAcceptances);
        LocalDataManager.savePartialAcceptances(partialAcceptancesList,InstrumentationRegistry.getTargetContext());
        ArrayList<PartialAcceptances> requestsAcceptedOffline = LocalDataManager.loadPartialAcceptances(InstrumentationRegistry.getTargetContext());
        assertTrue(requestsAcceptedOffline.size() > 0);
    }

    /*
        US 10.01.01
        As a rider, I want to specify a start and end geo locations on a map for a request.
    */
    @Test
    public void testSpecifiyAStartEndGeoLocation() {
        checkUser();
        login("Rider Mode");
        onView(withId(R.id.request_button)).perform(click());
        makeRequest();
        onView(withId(R.id.button_requested));
        onData(anything()).inAdapterView(withId(R.id.requestedListView)).atPosition(0).perform(click());
        onView(withId(R.id.start_loc)).check(matches(withText(STARTADDRESS + ", Edmonton")));
        onView(withId(R.id.end_loc)).check(matches(withText(ENDADDRESS + ", Edmonton")));
    }

    /*
        US 10.02.01
        As a driver, I want to view start and end geo locations on a map for a request.
    */
    @Test
    public void testViewStartEndLocations() {
        checkUser();
        login("Rider Mode");
        onView(withId(R.id.request_button)).perform(click());
        makeRequest();
        onView(withId(R.id.button_requested));
        onData(anything()).inAdapterView(withId(R.id.requestedListView)).atPosition(0).perform(click());
        onView(withId(R.id.start_loc)).check(matches(withText(STARTADDRESS + ", Edmonton")));
        onView(withId(R.id.end_loc)).check(matches(withText(ENDADDRESS + ", Edmonton")));
    }
}
