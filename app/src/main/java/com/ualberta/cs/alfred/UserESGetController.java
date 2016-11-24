package com.ualberta.cs.alfred;

import android.os.AsyncTask;
import android.util.Log;

import java.util.List;
import java.util.Map;

import io.searchbox.client.JestResult;
import io.searchbox.core.Get;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

/**
 * This controller holds the functionality to get users from
 * the elastic search server.
 *
 * @author mmcote and ookmm
 * @version 1.1
 * @see User
 */
public class UserESGetController {

    /**
     * This class is used to run an AsyncTask in the background to get a rider from the
     * elastic search server.
     */
    public static class GetUserTask extends AsyncTask<String, Void, User> {
        @Override
        protected User doInBackground(String... search_parameters) {

            ESSettings.verifySettings();

            User user = null;

            String query = "{\n" +
                    "    \"query\": {\n" +
                    "        \"match\" : {\n" +
                    "            \"userName\" : \n" +
                    "                \""+search_parameters[0]+"\"\n" +
                    "            }\n" +
                    "    }\n" +
                    "}";

            Search search = new Search.Builder(query)
                    .addIndex(ESSettings.INDEX_NAME)
                    .addType(ESSettings.USER_TYPE_NAME)
                    .build();

            try {
                SearchResult result = ESSettings.client.execute(search);
                if (result.isSucceeded()) {
                    user = result.getSourceAsObject(User.class);
                }
                else {
                    Log.i("Error", "The search query failed to find the rider that matched.");
                }
            }
            catch (Exception e) {
                Log.i("Error", "Something went wrong when we tried to communicate with " +
                        "the elasticsearch server!");
            }
            return user;
        }
    }


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
