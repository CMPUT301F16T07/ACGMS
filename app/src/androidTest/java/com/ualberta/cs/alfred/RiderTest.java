package com.ualberta.cs.alfred;

import android.test.ActivityInstrumentationTestCase2;

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
        Rider rider = new Rider("Michael", "Cote", "mmcote", new Date(), "787398247", "mmcote@ualberta.ca", "34902845854");
        assertTrue(true);
    }

    public void testGetRider() {
        UserElasticSearchController.GetRider retrievedRider = new UserElasticSearchController.GetRider();
        retrievedRider.execute("mmcote");

        try {
            Rider rider = retrievedRider.get();
            assert(true);
            Rider jimbo = new Rider();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}

