package com.ualberta.cs.alfred;

/**
 * Created by Avery on 11/18/2016.
 */

import android.test.ActivityInstrumentationTestCase2;


/**
 * Creates the test cases for type User.
 *
 * @author ookmm
 * @version 1.0
 */
public class RatingTest extends ActivityInstrumentationTestCase2 {

    /**
     * Instantiates a new User test.
     */
    public RatingTest() {
        super(MainActivity.class);
    }


    /**
     * Tests the functions of the Rating class
     *
     */
    public void testRating(){
        Rating rating0 = new Rating();
        rating0.addRating(5.00);
        assertTrue("rating should be 5, it's not", rating0.getRating()==5);


        rating0.addRating(1);
        assertTrue("rating should be ~3", (rating0.getRating()>=3) && (rating0.getRating()<=3.1));

        rating0.addRating(4.5);
        assertTrue("rating should be ~3.5, it's not", rating0.getRating()>3.4 && rating0.getRating()<3.6);

    }




}
