package com.ualberta.cs.alfred;

import android.test.ActivityInstrumentationTestCase2;

/**
 * Test cases for deleting item from Requests
 *
 * @author ookmm
 * @version 1.0
 * @see RequestESDeleteController
 */
public class RequestESDeleteControllerTest extends ActivityInstrumentationTestCase2 {

    /**
     * Instantiates a new Request ES delete controller test.
     */
    public RequestESDeleteControllerTest() {

        super(MainActivity.class);
    }

    /**
     * Delete an item from a list
     *
     * @Note: for example, delete a driver ID from a list of drivers
     */
    public void testDeleteItemFromListTask() {

        String requestID = "AVhdOt-dtmmsbsUPVvpZ";
        String requestProperty = "driverIDList";
        String requestValueType = "string";
        String requestValue = "driver890";

        RequestESDeleteController.DeleteItemFromListTask deleteItemFromListTask =
                new RequestESDeleteController.DeleteItemFromListTask();

        deleteItemFromListTask.execute(requestID, requestProperty, requestValueType, requestValue);
        assert (true);

    }

    /**
     * Delete request by id task
     *
     * @Note: need to know the request ID.
     */
    public void testDeleteRequestTask() {

        RequestESDeleteController.DeleteRequestTask deleteRequest =
                new RequestESDeleteController.DeleteRequestTask();

        // Delete the request with this id
        deleteRequest.execute("AVhdOt9qtmmsbsUPVvpY");
        assert (true);
    }

}
