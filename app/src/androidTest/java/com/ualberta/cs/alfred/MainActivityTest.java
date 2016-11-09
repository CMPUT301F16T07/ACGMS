package com.ualberta.cs.alfred;

import android.test.ActivityInstrumentationTestCase2;

import com.ualberta.cs.alfred.MainActivity;


/**
 * Created by mmcote on 2016-10-09.
 */
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

    public MainActivityTest() {
        super(MainActivity.class);
    }

    /*
    US 01.01.01

    Test whether a request was properly inputted into the system,
    the minor checks for if the function has been grabbed correctly,
    verifying the address will all be done in there own individual test.
     */
    public void testRequestRide() {

    }

    /*
    US 01.02.01

    This test will check if the inputted request has properly been
    updated to the android UI
     */
    public void testSeeARequest() {

    }

    /*
    US 01.03.01

    Test that a notification has been recieved by the rider by
    initiating a signal corresponding to a driver accepting
    there request.
     */
    public void testAcceptedRequest() {

    }

    /*
    US 01.04.01

    Test that the rider can cancel a request through the UI by initiating
    a canceled request, and checking if it's status has changed, and the
    request has been deleted
     */
    public void testCancelRequest() {

    }

    /*
    US 01.05.01

    Test whether the phone button to call the driver, initiates the
    phone application.
     */
    public void testPhoneDriver() {

    }

    /*
    US 01.05.01

    Test whether the email button to email the driver, initiates the
    email application.
     */
    public void testEmailDriver() {

    }

    /*
    US 01.06.01

    Test whether the given two addresses if the system can return the
    calculated price
     */
    public void testCalculatePrice() {

    }

    /*
    US 01.07.01

    Initiate a completion of a ride in progress and check whether the
    status of the trip has changed.
     */
    public void testConfirmCompletion() {

    }

    /*
    US 01.07.01

    Check whether when initiating the payment if we are able to grab the
    payment information of both the driver and the rider
     */
    public void testEnablePayment() {

    }

    /*
    US 01.08.01

    Initiate a acceptance of a accepted ride request from a driver
    check whether the status of the trip has changed.
     */
    public void testDriverAcceptance() {

    }

    /*
    US 02.01.01

    Test whether we are able to retrieve the status of a given
    known request
     */
    public void testGetStatus() {

    }

    /*
    US 03.01.01

    Test whether a username is unique when inputted, and if we are
    able to input the contact information of the user
     */
    public void testUserInfoInput() {

    }

    /*
    US 03.02.01

    Test if we are able to retrieve
    and change the contact information of the user
     */
    public void testChangeContactInfo() {

    }

    /*
    US 03.03.01

    Test if we are able to retrieve contact information based
    on the key of only a username. We should be able to show
    all the contact information needed to communicate.
     */
    public void testRetrieveContactInfo() {

    }

    /*
    US 04.01.01

    Test if we are able to retrieve a list of requests that
    correspond to a known area that we will try to retrieve
    using geo-location as the filter. This should return
    all request within the radius specified.
     */
    public void testGeoLocationSearchRequests() {

    }

    /*
    US 04.02.01

    Test if we are able to retrieve a list of requests that
    contain a specific keyword in the address. This should return
    all requests with the keyword regardless of distance away.
     */
    public void testKeyWordSearchRequests() {

    }

    /*
    US 05.01.01

    Test if the driver is able to place a bid on the given requests
    and to ensure that the payment is compatible between the two
    check if the offered payment is one of the accepted payments of
    the driver.
     */
    public void testDriverAcceptAndPayment() {

    }

    /*
    US 05.02.01

    Test if the driver is able to see a list of requests that he has
    already accepted and is just waiting for the final acceptance from
    the rider. Check if in each of the request there is a valid
    description, start, and end locations.
     */
    public void testPendingRequests() {

    }

    /*
    US 05.03.01

    Test if the driver can see a request change from pending to
    accepted after initiating a final acceptance from the rider.
     */
    public void testAcceptedRequests() {

    }

    /*
    US 05.04.01

    Test if the driver can see a notification with the corresponding
    request information that was accepted.
     */
    public void testAcceptedNotification() {

    }

    /*
    US 08.01.01

    Test whether the driver is able to see their pending requests
    after putting the phone into airplane mode programmatically.
     */
    public void testOfflinePendingRequests() {

    }

    /*
    US 08.02.01

    Test whether the rider is able to see their requests after
    putting the phone into airplane mode programmatically.
     */
    public void testOfflineRequests() {

    }

    /*
    US 08.03.01

    Test whether a requests will be added to the web server after
    reconnecting to the internet. This will be tested by making a
    request in airplane mode then seeing if once switched back to
    internet if the request has been added to server. Also test
    by notifying user once there request has been added.
     */
    public void testInputtingOfflineRequests() {

    }

    /*
    US 08.04.01

    Test whether a driver can accept a request while offline, by
    accepting the request in offline mode, then checking if the
    bid is placed on the server when the app connects again.
     */
    public void testAcceptingRequestOffline() {

    }

    /*
    US 10.01.01

    Test if second constructor is called to enter in geo locations
    for a request. The geocoder should be able to find a corresponding
    address. This will be tested by using known coordinates of known
    locations.
     */
    public void testGeoLocations() {

    }

    /*
    US 10.02.01 (added 2016-02-29)

    Test whether an overlay has been placed on top of the MAP UI to
    properly illustrate the path the driver will be taking of the
    start and end locations.
     */
    public void testViewGeoLocations() {

    }
}