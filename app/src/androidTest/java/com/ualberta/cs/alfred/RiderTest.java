package com.ualberta.cs.alfred;

import android.test.ActivityInstrumentationTestCase2;

import java.util.Date;

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
}

