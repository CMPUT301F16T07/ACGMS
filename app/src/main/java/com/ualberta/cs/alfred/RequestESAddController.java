package com.ualberta.cs.alfred;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import io.searchbox.core.Update;
import io.searchbox.params.Parameters;

/**
 * Controller for for Adding Request items Elasticsearch
 * @author ookmm
 * @version 1.0
 */
public class  RequestESAddController {
    /**
     * The type Add request task.
     */
    public static class AddRequestTask extends AsyncTask<Request, Void, Void> {
        @Override
        // one or more Request given, can be an array of Requests without specifying an array
        protected Void doInBackground(Request... requests) {
            ESSettings.verifySettings();

            for (Request request : requests) {
                Index index = new Index.Builder(request)
                        .setParameter(Parameters.REFRESH, true)
                        .index(ESSettings.INDEX_NAME)
                        .type(ESSettings.REQUEST_TYPE_NAME).build();
                try {
                    DocumentResult result = ESSettings.client.execute(index);
                    if (!result.isSucceeded()) {
                        Log.i("Error", "Elastic search was not able to add the request.");
                    }
                } catch (Exception e) {
                    Log.i("Uh-Oh", "We failed to add a request to elastic search!");
                    e.printStackTrace();
                }
            }
            return null;
        }
    }

    /**
     * Add new item to property that has a type "array" in ES
     *
     * Note: need to supply the request ID, property name and the string item to be added.
     * Example: I want to add a driverID to a list of driverIDs.
     */
    public static class AddItemToListTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... update_parameters) {

            ESSettings.verifySettings();

            // The number of elements in "update_parameters"
            int pSize = update_parameters.length;

            String requestID = update_parameters[0];
            String requestProperty = update_parameters[1];
            String requestNewValue = update_parameters[2];

            String query = String.format(
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
