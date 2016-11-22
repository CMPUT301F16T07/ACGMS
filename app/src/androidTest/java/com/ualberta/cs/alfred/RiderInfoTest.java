package com.ualberta.cs.alfred;


import android.test.ActivityInstrumentationTestCase2;

/**
 * Creates all the test for Rider info.
 */
public class RiderInfoTest extends ActivityInstrumentationTestCase2 {

    /**
     * Instantiates a new Rider info test.
     */
    public RiderInfoTest() {
        super(MainActivity.class);
    }

    /**
     * Test get credit card number.
     */
    public void testGetCreditCardNumber() {
        /**
         * Step 1: Create credit card
         */
        String creditCard = "5555333366662222";

        /**
         * Step 2: Create Rider info
         */
        RiderInfo riderInfo = new RiderInfo(creditCard);

        assertTrue("Credit card not equal", creditCard.equals(riderInfo.getCreditCardNumber()));

    }
}
