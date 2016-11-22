package com.ualberta.cs.alfred;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import io.searchbox.core.Update;
import io.searchbox.params.Parameters;

/**
 * This handles the setting of items to User
 *
 * @author ookmm
 * @version 1.0
 */
public class UserESSetController {

    /**
     * This task update the value of a property in ES.
     * If the property is an array of strings, it add one element to the array.
     * A valid user id is need to in order to update its properties.
     *
     * Note: need to provide a type. Only array, double, and date type.
     */
    public static class SetPropertyValueTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... update_parameters) {

            ESSettings.verifySettings();

            String userID = update_parameters[0];
            String userProperty = update_parameters[1];
            String userPropertyType = update_parameters[2];
            String userNewValue = update_parameters[3];

            String query = null;

            switch (userPropertyType.toLowerCase()) {

                case "array" :

                    query = String.format(
                            "{\n" +
                                    "\"script\" : {\n" +
                                    "\"inline\" : \"ctx._source.%s.add(params.newValue)\", \n" +
                                    "\"lang\" : \"painless\", \n" +
                                    "\"params\" : {\n" +
                                    "\"newValue\" : \"%s\"        \n" +
                                    "}\n" +
                                    "}\n" +
                                    "}",
                            userProperty, userNewValue
                    );
                    break;

                case "string" :
                case "date":
                    query = String.format(
                            "{\n" +
                                    "\"doc\" : {\n" +
                                    "\"%s\" : \"%s\"\n" +
                                    "}\n" +
                                    "}",
                            userProperty, userNewValue
                    );
                    break;

                case "double" :

                    double requestNewValueAsDouble = Double.parseDouble(userNewValue);
                    query = String.format(
                            "{\n" +
                                    "\"doc\" : {\n" +
                                    "\"%s\" : %s \n" +
                                    "}\n" +
                                    "}",
                            userProperty, requestNewValueAsDouble);
                    break;

                case "boolean" :

                    query = String.format(
                            "{\n" +
                                    "\"doc\" : {\n" +
                                    "\"%s\" : %s \n" +
                                    "}\n" +
                                    "}",
                            userProperty, userNewValue);
                    break;

                default:
                    throw new IllegalArgumentException("Provided an unsupported property type: " +
                            userProperty);
            }

            try {
                ESSettings.client.execute(new Update.Builder(query)
                        .index(ESSettings.INDEX_NAME)
                        .type(ESSettings.USER_TYPE_NAME)
                        .id(userID)
                        .setParameter(Parameters.REFRESH, true)
                        .build());
            } catch (IOException e) {
                Log.i("Error", "Something went wrong when we tried to communicate with " +
                        "the elasticsearch server!");
            }

            return null;
        }
    }

    /**
     * Set multiple property value at once.
     *
     * For example: Update user's firstname and lastname. See test cases for use.
     *
     * Note: This function is only non-nested properties.
     */
    public static class SetMultiplePropertyValueTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... update_parameters) {

            ESSettings.verifySettings();

            // The number of elements in "update_parameters"
            int pSize = update_parameters.length;

            String userID = update_parameters[0];

            StringBuilder stringBuilder = new StringBuilder();

            int j = 0;
            int k = 0;
            for (int i = 0; i < pSize - 2; ++i) {

                if (i + j + k + 2 < pSize) {
                    String key = update_parameters[i + j + k + 1];
                    String valueType = update_parameters[i + j + k + 2];
                    String value = update_parameters[i + j + k + 3];

                    String keyValue = null;

                    switch (valueType.toLowerCase()) {

                        case "string" :
                            keyValue = String.format("\"%s\" : \"%s\"", key, value);
                            stringBuilder.append(keyValue);
                            break;

                        case "double" :
                            double valueAsDouble = Double.parseDouble(value);
                            keyValue = String.format("\"%s\" : %s", key, valueAsDouble);
                            stringBuilder.append(keyValue);
                            break;

                        case "date" :
                            keyValue = String.format("\"%s\" : \"%s\"", key, value);
                            stringBuilder.append(keyValue);
                            break;

                        default:
                            throw new IllegalArgumentException("Provided an unsupported property type: " +
                                    valueType);

                    }

                    /**
                     * Don't add new line and comma on last element
                     */
                    if (i + j + k + 3 < pSize - 1) {
                        String comma = ",\n";
                        stringBuilder.append(comma);
                    }

                    ++j;
                    ++k;

                }
            }

            String query = String.format(
                    "{\n" +
                            "\"doc\" : {\n" +
                            "%s \n" +
                            "}\n" +
                            "}",
                    stringBuilder);

            System.out.println("=================");
            System.out.println(query);
            System.out.println("=================");

            try {
                ESSettings.client.execute(new Update.Builder(query)
                        .index(ESSettings.INDEX_NAME)
                        .type(ESSettings.USER_TYPE_NAME)
                        .id(userID)
                        .setParameter(Parameters.REFRESH, true)
                        .build());
            } catch (IOException e) {
                Log.i("Error", "Something went wrong when we tried to communicate with " +
                        "the elasticsearch server!");
            }

            return null;
        }
    }

    /**
     * Set singly nested object values in ES.
     * Example of singly nested object: driverInfo --> licenceNumber.
     *
     * Note: Need to provide the user ID.
     * How use: take a look at the test cases.
     */
    public static class SetNestedObjectPropertyValueTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... update_parameters) {

            ESSettings.verifySettings();

            // The number of elements in "update_parameters"
            int pSize = update_parameters.length;

            String userID = update_parameters[0];
            String userProperty = update_parameters[1];

            StringBuilder stringBuilder = new StringBuilder();

            int j = 0;
            int k = 0;
            for (int i = 0; i < pSize - 2; ++i) {

                if (i + j + k + 2 < pSize) {
                    String key = update_parameters[i + j + k + 2];
                    String valueType = update_parameters[i + j + k + 3];
                    String value = update_parameters[i + j + k + 4];

                    String keyValue = null;

                    switch (valueType.toLowerCase()) {

                        case "string" :
                            keyValue = String.format("\"%s\" : \"%s\"", key, value);
                            stringBuilder.append(keyValue);
                            break;

                        case "double" :
                            double valueAsDouble = Double.parseDouble(value);
                            keyValue = String.format("\"%s\" : %s", key, valueAsDouble);
                            stringBuilder.append(keyValue);
                            break;

                        case "int" :
                            double valueAsInt = Integer.parseInt(value);
                            keyValue = String.format("\"%s\" : %s", key, valueAsInt);
                            stringBuilder.append(keyValue);
                            break;

                        case "date" :
                            keyValue = String.format("\"%s\" : \"%s\"", key, value);
                            stringBuilder.append(keyValue);
                            break;

                        case "address" :
                        case "array" :
                            keyValue = String.format("\"%s\" : %s", key, value);
                            stringBuilder.append(keyValue);
                            break;

                        default:
                            throw new IllegalArgumentException("Provided an unsupported property type: " +
                                    valueType);

                    }

                    /**
                     * Don't add new line and comma on last element
                     */
                    if (i + j + k + 4 < pSize - 1) {
                        String comma = ",\n";
                        stringBuilder.append(comma);
                    }

                    ++j;
                    ++k;

                }
            }

            String query = String.format(
                    "{\n" +
                            "\"doc\" : {\n" +
                            "\"%s\" :  { \n" +
                            "%s \n" +
                            "}\n" +
                            "}\n" +
                            "}",
                    userProperty, stringBuilder);

            System.out.println("=================");
            System.out.println(query);
            System.out.println("=================");

            try {
                ESSettings.client.execute(new Update.Builder(query)
                        .index(ESSettings.INDEX_NAME)
                        .type(ESSettings.USER_TYPE_NAME)
                        .id(userID)
                        .setParameter(Parameters.REFRESH, true)
                        .build());
            } catch (IOException e) {
                Log.i("Error", "Something went wrong when we tried to communicate with " +
                        "the elasticsearch server!");
            }

            return null;
        }
    }

    /**
     * Set doubly nested object values in ES.
     * Example of doubly nested object: driverInfo --> vehicle --> color.
     *
     * Note: Need to provide the user ID.
     * How use: take a look at the test cases.
     */
    public static class SetDoublyNestedObjectPropertyValueTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... update_parameters) {

            ESSettings.verifySettings();

            // The number of elements in "update_parameters"
            int pSize = update_parameters.length;

            String userID = update_parameters[0];
            String userPropertyFirstLevel = update_parameters[1];
            String userPropertySecondLevel = update_parameters[2];

            StringBuilder stringBuilder = new StringBuilder();

            int j = 0;
            int k = 0;
            for (int i = 0; i < pSize - 2; ++i) {

                if (i + j + k + 3 < pSize) {
                    String key = update_parameters[i + j + k + 3];
                    String valueType = update_parameters[i + j + k + 4];
                    String value = update_parameters[i + j + k + 5];

                    String keyValue = null;

                    switch (valueType.toLowerCase()) {

                        case "string" :
                            keyValue = String.format("\"%s\" : \"%s\"", key, value);
                            stringBuilder.append(keyValue);
                            break;

                        case "double" :
                            double valueAsDouble = Double.parseDouble(value);
                            keyValue = String.format("\"%s\" : %s", key, valueAsDouble);
                            stringBuilder.append(keyValue);
                            break;

                        case "int" :
                            int valueAsInt = Integer.parseInt(value);
                            keyValue = String.format("\"%s\" : %s", key, valueAsInt);
                            stringBuilder.append(keyValue);
                            break;

                        case "date" :
                            keyValue = String.format("\"%s\" : \"%s\"", key, value);
                            stringBuilder.append(keyValue);
                            break;

                        default:
                            throw new IllegalArgumentException("Provided an unsupported property type: " +
                                    valueType);

                    }

                    /**
                     * Don't add new line and comma on last element
                     */
                    if (i + j + k + 5 < pSize - 1) {
                        String comma = ",\n";
                        stringBuilder.append(comma);
                    }

                    ++j;
                    ++k;

                }
            }

            String query = String.format(
                    "{\n" +
                            "\"doc\" : {\n" +
                            "\"%s\" :  { \n" +
                            "\"%s\" : { \n" +
                            "%s \n" +
                            "}\n" +
                            "}\n" +
                            "}\n" +
                            "}",
                    userPropertyFirstLevel, userPropertySecondLevel, stringBuilder);

            try {
                ESSettings.client.execute(new Update.Builder(query)
                        .index(ESSettings.INDEX_NAME)
                        .type(ESSettings.USER_TYPE_NAME)
                        .id(userID)
                        .setParameter(Parameters.REFRESH, true)
                        .build());
            } catch (IOException e) {
                Log.i("Error", "Something went wrong when we tried to communicate with " +
                        "the elasticsearch server!");
            }

            return null;
        }
    }
}
