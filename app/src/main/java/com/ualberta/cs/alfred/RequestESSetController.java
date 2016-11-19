package com.ualberta.cs.alfred;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import io.searchbox.core.Update;
import io.searchbox.params.Parameters;

/**
 * This handles the setting of items to Requests
 *
 * @author ookmm
 * @version 1.0
 */
public class RequestESSetController {

    /**
     * This task update the value of a property in ES.
     * If the property is an array of strings, it add one element to the array.
     * A valid request id is need to in order to update its properties.
     *
     * Note: need to provide a type. Only array, double, and date type.
     */
    public static class SetPropertyValueTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... update_parameters) {

            ESSettings.verifySettings();

            String requestID = update_parameters[0];
            String requestProperty = update_parameters[1];
            String requestPropertyType = update_parameters[2];
            String requestNewValue = update_parameters[3];

            String query = null;

            switch (requestPropertyType.toLowerCase()) {

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
                            requestProperty, requestNewValue
                    );
                    break;

                case "string" :

                    query = String.format(
                            "{\n" +
                                    "\"doc\" : {\n" +
                                    "\"%s\" : \"%s\"\n" +
                                    "}\n" +
                                    "}",
                            requestProperty, requestNewValue
                    );
                    break;

                case "double" :

                    double requestNewValueAsDouble = Double.parseDouble(requestNewValue);
                    query = String.format(
                            "{\n" +
                                    "\"doc\" : {\n" +
                                    "\"%s\" : %s \n" +
                                    "}\n" +
                                    "}",
                            requestProperty, requestNewValueAsDouble);
                    break;

                case "date" :

                    query = String.format(
                            "{\n" +
                                    "\"doc\" : {\n" +
                                    "\"%s\" : \"%s\" \n" +
                                    "}\n" +
                                    "}",
                            requestProperty, requestNewValue);
                    break;

                default:
                    throw new IllegalArgumentException("Provided an unsupported property type: " +
                            requestProperty);
            }

            try {
                ESSettings.client.execute(new Update.Builder(query)
                        .index(ESSettings.INDEX_NAME)
                        .type(ESSettings.REQUEST_TYPE_NAME)
                        .id(requestID)
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
     * Add new item to property that has a type "array" in ES
     *
     * Note: need to supply the request ID, property name and the string item to be added
     */
    public static class SetNestedObjectPropertyValueTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... update_parameters) {

            ESSettings.verifySettings();

            // The number of elements in "update_parameters"
            int pSize = update_parameters.length;

            String requestID = update_parameters[0];
            String requestProperty = update_parameters[1];

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

                        case "date" :
                            keyValue = String.format("\"%s\" : \"%s\"", key, value);
                            stringBuilder.append(keyValue);
                            break;

                        case "address" :
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
                    requestProperty, stringBuilder);

            try {
                ESSettings.client.execute(new Update.Builder(query)
                        .index(ESSettings.INDEX_NAME)
                        .type(ESSettings.REQUEST_TYPE_NAME)
                        .id(requestID)
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
