package com.ualberta.cs.alfred.usecasetests;

import android.support.test.runner.AndroidJUnit4;

import com.ualberta.cs.alfred.AppSettings;
import com.ualberta.cs.alfred.Request;
import com.ualberta.cs.alfred.RequestESGetController;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Test cases for Searching.
 *
 * @author ookmm
 */
@RunWith(AndroidJUnit4.class)
public class SearchingTest {

    /**
     * US 04.01.01
     * As a driver, I want to browse and search for open requests by geo-location.
     *
     * @throws Exception
     */
    @Test
    public void testBrowseAndSearchByGeoLocation() throws Exception {
        RequestESGetController.GetRequestByLocationTask retrievedRequest =
                new RequestESGetController.GetRequestByLocationTask();


        // Get all requests within this distance
        String distance = "5km";

        double longitude = -113.5263186;
        String longitudeAsString = String.valueOf(longitude);

        double latitude = 53.5232189;
        String latitudeAsString = String.valueOf(latitude);
        String coordinates = String.format("[%s, %s]", longitudeAsString, latitudeAsString);

        // Get all requests within the specified distance
        retrievedRequest.execute(
                "requestStatus", "string", AppSettings.REQUEST_REQUESTED,
                distance, coordinates
        );

        try {
            ArrayList<Request> requests = retrievedRequest.get();
            for (Request request : requests) {
                System.out.println("====================");
                System.out.println("Request ID is: " + request.getRequestID());
                System.out.println("Rider ID is: " + request.getRiderID());
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
     * US 04.02.01
     * As a driver, I want to browse and search for open requests by keyword.
     *
     * @throws Exception
     */
    @Test
    public void testBrowseAndSearchByKeyword() throws Exception {
        RequestESGetController.GetRequestByMultiplePreferencesTask retrievedRequest =
                new RequestESGetController.GetRequestByMultiplePreferencesTask();

        // Get open requests by keyword
        String keyword = "Edmonton";
        retrievedRequest.execute(
                "requestStatus", "string", AppSettings.REQUEST_REQUESTED,
                "_all", "string", keyword
        );

        try {
            ArrayList<Request> requests = retrievedRequest.get();
            for (Request request : requests) {
                System.out.println("====================");
                System.out.println("Request ID is: " + request.getRequestID());
                System.out.println("Rider ID is: " + request.getRiderID());
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
}
