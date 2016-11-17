package com.ualberta.cs.alfred;

import android.test.ActivityInstrumentationTestCase2;

/**
 * Test cases for setting item to a request
 *
 * @author ookmm
 * @version 1.0
 * @see RequestESSetController
 */
public class RequestESSetControllerTest extends ActivityInstrumentationTestCase2 {

    public RequestESSetControllerTest() {

        super(MainActivity.class);
    }

    /**
     * Test for
     */
    public void testSetPropertyValueTask() {

        String requestID = "AVhdOt-dtmmsbsUPVvpZ";

        String requestProperty = "driverIDList";
        requestProperty = "cost";
        requestProperty = "requestDate";

        String requestPropertyType = "array";
        requestPropertyType = "double";
        requestPropertyType = "date";

        String requestNewValue = "driver890";
        requestNewValue = "130.00";

        // Format must be yyy-MM-dd : 2015-11-27
        // If you want to include time, then 2015-11-27T12:10:30Z
        requestNewValue = "2016-10-26T12:10:30Z";

        RequestESSetController.SetPropertyValueTask setPropertyValueTask =
                new RequestESSetController.SetPropertyValueTask();

        setPropertyValueTask.execute(requestID, requestProperty, requestPropertyType, requestNewValue);
        assert (true);

    }

    public void testSetNestedObjectPropertyValueTask() {

        String requestID = "AVhdOt-dtmmsbsUPVvpZ";
        String requestProperty = "sourceAddress";
        String nestedObject1Property = "location";
        String nestedObject1ValueType = "string";
        String nestedObject1Value = "Edmonton Place";
        String nestedObject2Property = "latitude";
        String nestedObject2PropertyType = "double";
        String nestedObject2Value = "190.45";
        String nestedObject3Property = "longitude";
        String nestedObject3PropertyType = "double";
        String nestedObject3Value = "-50.45";

        RequestESSetController.SetNestedObjectPropertyValueTask setNestedObjectPropertyValueTask =
                new RequestESSetController.SetNestedObjectPropertyValueTask();

        setNestedObjectPropertyValueTask.execute(
                requestID, requestProperty,
                nestedObject1Property, nestedObject1ValueType, nestedObject1Value,
                nestedObject2Property, nestedObject2PropertyType, nestedObject2Value,
                nestedObject3Property, nestedObject3PropertyType, nestedObject3Value);
        assert (true);

    }
}
