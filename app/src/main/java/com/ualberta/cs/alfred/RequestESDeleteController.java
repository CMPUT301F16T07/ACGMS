package com.ualberta.cs.alfred;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import io.searchbox.core.Delete;
import io.searchbox.core.Update;

/**
 * This handles the deletion of items form Requests In Elasticsearch
 *
 * @author ookmm
 * @version 1.0
 */
public class RequestESDeleteController {

    /**
     * Delete an item from property that a type "array" in ES
     *
     * Note need to supply the request ID; the property name; the type of values inside the array,
     * example strings, doubles, dates, etc.; and the value to delete.
     * For example, I want to delete a driverID when a list that contains strings ot driverIDs.
     */
    public static class DeleteItemFromListTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... delete_parameters) {

            ESSettings.verifySettings();

            // The number of elements in "update_parameters"
            int pSize = delete_parameters.length;

            String requestID = delete_parameters[0];
            String requestProperty = delete_parameters[1];
            String requestValueType = delete_parameters[2];
            String requestValue = delete_parameters[3];

            String script = String.format("for (int i = 0; i < ctx._source.%s.length; ++i) { " +
                    "if (ctx._source.%s[i] == params.itemToDelete) {" +
                    "ctx._source.%s.remove(i);" +
                    "}" +
                    "}", requestProperty, requestProperty, requestProperty);

            String query = null;

            switch (requestValueType.toLowerCase()) {

                case "string" :
                    query = String.format(
                            "{\n" +
                                    "\"script\" : {\n" +
                                    "\"lang\" : \"painless\", \n" +
                                    "\"inline\" : \"%s\", \n" +
                                    "\"params\" : {\n" +
                                    "\"itemToDelete\" : \"%s\"        \n" +
                                    "}\n" +
                                    "}\n" +
                                    "}",
                            script, requestValue
                    );
                    break;

                case "double" :

                    query = String.format(
                            "{\n" +
                                    "\"script\" : {\n" +
                                    "\"lang\" : \"painless\", \n" +
                                    "\"inline\" : \"%s\", \n" +
                                    "\"params\" : {\n" +
                                    "\"itemToDelete\" : %s        \n" +
                                    "}\n" +
                                    "}\n" +
                                    "}",
                            script, requestValue
                    );
                    break;

                case "date" :

                    query = String.format(
                            "{\n" +
                                    "\"script\" : {\n" +
                                    "\"lang\" : \"painless\", \n" +
                                    "\"inline\" : \"%s\", \n" +
                                    "\"params\" : {\n" +
                                    "\"itemToDelete\" : \"%s\"        \n" +
                                    "}\n" +
                                    "}\n" +
                                    "}",
                            script, requestValue
                    );
                    break;

                default:

                    throw new IllegalArgumentException("Provided an unsupported property type: " +
                            requestValue);
            }

            System.out.println("=================");
            System.out.println(query);
            System.out.println("=================");


            try {
                ESSettings.client.execute(new Update.Builder(query)
                        .index(ESSettings.INDEX_NAME)
                        .type(ESSettings.REQUEST_TYPE_NAME)
                        .id(requestID)
                        .build());
            } catch (IOException e) {
                Log.i("Error", "Something went wrong when we tried to communicate with " +
                        "the elasticsearch server!");
            }

            return null;
        }
    }

    /**
     * Delete request by id task.
     */
    public static class DeleteRequestTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... search_parameters) {

            ESSettings.verifySettings();

            try {
                ESSettings.client.execute(new Delete.Builder(search_parameters[0])
                .index(ESSettings.INDEX_NAME)
                .type(ESSettings.REQUEST_TYPE_NAME)
                .build());
            } catch (IOException e) {
                Log.i("Error", "Something went wrong when we tried to communicate with " +
                        "the elasticsearch server!");
            }

            return null;
        }
    }
}
