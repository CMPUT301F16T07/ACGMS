package com.ualberta.cs.alfred;

import android.test.ActivityInstrumentationTestCase2;

import java.util.concurrent.ExecutionException;

/**
 * Test cases for setting item to a request
 *
 * @author ookmm
 * @version 1.2
 * @see UserESSetController
 */
public class UserESSetControllerTest extends ActivityInstrumentationTestCase2 {

    /**
     * Instantiates a new User es set controller test.
     */
    public UserESSetControllerTest() {
        super(MainActivity.class);
    }

    /**
     * Test set basic user info.
     */
    public void testSetBasicUserInfo () {

        /**
         * Updating user phone number
         */
        String userID = "AViJPA8adE2DZPCrf9ml";

        String userProperty = "phoneNumber";
        String userPropertyType = "string";
        String userNewValue = "8904445555";

        UserESSetController.SetPropertyValueTask setPropertyValueTask =
                new UserESSetController.SetPropertyValueTask();

        setPropertyValueTask.execute(userID, userProperty, userPropertyType, userNewValue);
        assert (true);

    }

    /**
     * Test set multiple basic user info.
     */
    public void testSetMultipleBasicUserInfo () {

        /**
         * Updating user phone number and last name.
         */
        String userID = "AViJPA8adE2DZPCrf9ml";

        String userProperty1 = "phoneNumber";
        String userPropertyType1 = "string";
        String userNewValue1 = "7809998899";

        String userProperty2 = "lastName";
        String userPropertyType2 = "string";
        String userNewValue2 = "Swift";

        UserESSetController.SetMultiplePropertyValueTask setMultiplePropertyValueTask =
                new UserESSetController.SetMultiplePropertyValueTask();

        setMultiplePropertyValueTask.execute(userID,
                userProperty1, userPropertyType1, userNewValue1,
                userProperty2, userPropertyType2, userNewValue2);
        assert (true);

    }

    /**
     * Test setting Driver Information
     *
     * @method SetNestedObjectPropertyValueTask()
     */
    public void testSetLicenceNumber() {

        String userID = "AViJPA8adE2DZPCrf9ml";
        String userProperty = "driverInfo";

        String nestedObject1Property = "licenceNumber";
        String nestedObject1ValueType = "string";
        String nestedObject1Value = "AB5000";

        UserESSetController.SetNestedObjectPropertyValueTask setNestedObjectPropertyValueTask =
                new UserESSetController.SetNestedObjectPropertyValueTask();

        setNestedObjectPropertyValueTask.execute(userID, userProperty,
                nestedObject1Property, nestedObject1ValueType, nestedObject1Value);
        assert (true);

    }

    /**
     * Test setting Vehicle Information
     *
     * @method SetDoublyNestedObjectPropertyValueTask()
     */
    public void testSetVehicleInfo() {

        /**
         * Update some vehicle information
         */
        String userID = "AViJPA8adE2DZPCrf9ml";

        String userPropertyFirstLevel = "driverInfo";
        String userPropertySecondLevel = "vehicle";

        String nestedObject1Property = "color";
        String nestedObject1ValueType = "string";
        String nestedObject1Value = "Red";

        String nestedObject2Property = "type";
        String nestedObject2ValueType = "string";
        String nestedObject2Value = "Sedan";

        UserESSetController.SetDoublyNestedObjectPropertyValueTask setDoubleNestedObject =
                new UserESSetController.SetDoublyNestedObjectPropertyValueTask();

        setDoubleNestedObject.execute(userID, userPropertyFirstLevel, userPropertySecondLevel,
                nestedObject1Property, nestedObject1ValueType, nestedObject1Value,
                nestedObject2Property, nestedObject2ValueType, nestedObject2Value);
        assert (true);
    }
}
