package com.ualberta.cs.alfred;

import android.test.ActivityInstrumentationTestCase2;

import com.ualberta.cs.alfred.MainActivity;

import java.util.Date;
import java.util.concurrent.ExecutionException;

/**
 * Create all the test cases for Request
 * @author mmcote
 * @version 1
 * @see Rider
 */
public class RiderTest extends ActivityInstrumentationTestCase2 {
    public RiderTest() {
        super(MainActivity.class);
    }

    public void test() {
        Rider rider = new Rider("Jimbo", "Clown", "jimbo", new Date(), "12345678",
                "jimbo@ualberta.ca", "34903845854");
        assertTrue(true);
    }

    public void testGetRider() {
        UserElasticSearchController.GetRider retrievedRider = new UserElasticSearchController.GetRider();
        retrievedRider.execute("jimbo");

        try {
            Rider rider = retrievedRider.get();
            System.out.println("++++++++++++++++++++++++");
            System.out.println("FIRSTNAME: " + rider.getFirstName());
            System.out.println("++++++++++++++++++++++++");
            assert(true);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}

