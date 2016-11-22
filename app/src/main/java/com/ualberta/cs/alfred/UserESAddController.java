package com.ualberta.cs.alfred;


import android.os.AsyncTask;
import android.util.Log;

import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import io.searchbox.params.Parameters;

/**
 * This controller holds the functionality to add and get users from
 * the elastic search server.
 *
 * @author mmcote
 * @version 1.0
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
}
