package com.ualberta.cs.alfred;

import android.os.AsyncTask;
import android.util.Log;

import io.searchbox.client.JestResult;
import io.searchbox.core.Get;



public class UserESGetController {

    /**
     * Gets User by id task.
     *
     * @return a user matching the id.
     */
    public static class GetUserByIdTask extends AsyncTask<String, Void, User> {
        @Override
        protected User doInBackground(String... search_parameters) {

            ESSettings.verifySettings();

            User user = null;

            Get get = new Get.Builder(ESSettings.INDEX_NAME, search_parameters[0])
                    .type(ESSettings.USER_TYPE_NAME)
                    .build();

            try {
                JestResult result = ESSettings.client.execute(get);
                if (result.isSucceeded()) {
                    user = result.getSourceAsObject(User.class);
                } else {
                    Log.i("Error", "The search query failed to find the user that matched.");
                }
            } catch (Exception e) {
                Log.i("Error", "Something went wrong when we tried to communicate with " +
                        "the elasticsearch server!");
            }

            return user;
        }
    }
}
