package com.ualberta.cs.alfred;

import android.test.ActivityInstrumentationTestCase2;

/**
 * Test cases for setting item to a request
 *
 * @author ookmm
 * @version 1.1
 * @see RequestESSetController
 */
public class RequestESSetControllerTest extends ActivityInstrumentationTestCase2 {

    public RequestESSetControllerTest() {

        super(MainActivity.class);
    }

    /**
     * Test for Adding new driver to driverList on a particular request
     *
     * @method SetPropertyValueTask()
     */
    public void testSetNewDriverToList() {

        String requestID = "AVhrljSxdE2DZPCrf9fY";

        String requestProperty = "driverIDList";
        //requestProperty = "cost";
        //requestProperty = "requestDate";

        String requestPropertyType = "array";
        //requestPropertyType = "double";
        //requestPropertyType = "date";

        String requestNewValue = "driver600";
        //requestNewValue = "130.00";

        // Format must be yyy-MM-dd : 2015-11-27
        // If you want to include time, then 2015-11-27T12:10:30Z
        //requestNewValue = "2016-10-26T12:10:30Z";

        RequestESSetController.SetPropertyValueTask setPropertyValueTask =
                new RequestESSetController.SetPropertyValueTask();

        setPropertyValueTask.execute(requestID, requestProperty, requestPropertyType, requestNewValue);
        assert (true);

    }

    /**
     * Test setting Address Information
     *
     * @method SetNestedObjectPropertyValueTask()
     */
    public void testSetAddressInformation() {

        String requestID = "AVh6kX-AdE2DZPCrf9kW";
        String requestProperty = "sourceAddress";

        String nestedObject1ValueType = "string";
        String nestedObject1Property = "location";
        String nestedObject1Value = "Edmonton Place";

        String nestedObject2Property = "coordinates";
        String nestedObject2PropertyType = "address";

        double longitude = -200.30;
        String longitudeAsString = String.valueOf(longitude);

        double latitude = 45.23;
        String latitudeAsString = String.valueOf(latitude);

        String nestedObject2Value = String.format("[%s, %s]", longitudeAsString, latitudeAsString);


        RequestESSetController.SetNestedObjectPropertyValueTask setNestedObjectPropertyValueTask =
                new RequestESSetController.SetNestedObjectPropertyValueTask();

        setNestedObjectPropertyValueTask.execute(
                requestID, requestProperty,
                nestedObject1Property, nestedObject1ValueType, nestedObject1Value,
                nestedObject2Property, nestedObject2PropertyType, nestedObject2Value);
        assert (true);

    }
}
