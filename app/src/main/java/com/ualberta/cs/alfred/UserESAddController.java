package com.ualberta.cs.alfred;


import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import io.searchbox.core.Update;
import io.searchbox.params.Parameters;

/**
 * This controller holds the functionality to add user and user items to ES.
 *
 * @author mmcote and ookmm
 * @version 1.1
 * @see User
 */
public class UserESAddController {
    /**
     * adds users to elasticsearch server
     *
     *
     */
    public static class AddUserTask<T> extends AsyncTask<T, Void, Void> {
        @Override
        // one or more objects (riders or drivers) given, can be an array of Riders without specifying an array
        protected Void doInBackground(T... objects) {
            ESSettings.verifySettings();


            String esType;

            for (T object : objects) {
                Index index = new Index.Builder(object)
                        .setParameter(Parameters.REFRESH, true)
                        .index(ESSettings.INDEX_NAME)
                        .type(ESSettings.USER_TYPE_NAME)
                        .build();

                try {
                    DocumentResult result = ESSettings.client.execute(index);
                    if (!result.isSucceeded()) {
                        Log.i("Error", "Elastic search was not able to add the "+ESSettings.USER_TYPE_NAME+".");
                    }
                } catch (Exception e) {
                    Log.i("Uh-Oh", "We failed to add a "+ESSettings.USER_TYPE_NAME+ "to elastic search!");
                    e.printStackTrace();
                }
            }
            return null;
        }
    }

    /**
     * Add new rating to driver in ES
     *
     * Note: need to supply the user ID.
     */
    public static class AddNewDriverRatingTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... update_parameters) {

            ESSettings.verifySettings();

            // The number of elements in "update_parameters"
            int pSize = update_parameters.length;

            String userID = update_parameters[0];
            String userRating = update_parameters[1];

            String query = String.format(
                    "{\n" +
                            "\"script\" : {\n" +
                            "\"inline\" : \"ctx._source.driverInfo.driverRating['count'] += 1; " +
                            "ctx._source.driverInfo.driverRating['total'] += params.newRating;\", \n" +
                            "\"lang\" : \"painless\", \n" +
                            "\"params\" : {\n" +
                            "\"newRating\" : %s        \n" +
                            "}\n" +
                            "}\n" +
                            "}",
                    userRating
            );

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
