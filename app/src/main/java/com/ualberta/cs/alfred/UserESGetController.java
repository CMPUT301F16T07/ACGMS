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
 * @author mmcote
 * @version 1.0
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
                Log.i("Error", "Something went wrong when we tried to communicate with the elasticsearch server!");
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

    /**
     * This class is used to run an AsyncTask in the background to get a rider from the
     * elastic search server.
     */
    public static class GetUserInfoWithoutPrivateInfo extends AsyncTask<String, Void, User> {
        @Override
        protected User doInBackground(String... search_parameters) {

            ESSettings.verifySettings();

            User rider = null;
            String exclusionString = "";
            for (int i = 1; i < search_parameters.length; ++i) {
                if (i != search_parameters.length - 1) {
                    exclusionString += "\""+search_parameters[i]+"\",";
                } else {
                    exclusionString += "\""+search_parameters[i]+"\"\n";
                }
            }

            String query = "{\n" +
                    "           \"_source\": {\n" +
                    "               \"excludes\": [\n" +
                    exclusionString +
                    "         ]\n" +
                    "                        },\n"+
                    "               \"query\": {\n" +
                    "                   \"match\" : {\n" +
                    "                       \"userName\" : \n" +
                    "                       \""+search_parameters[0]+"\"\n" +
                    "                               }\n" +
                    "                           }\n" +
                    "       }";

            Search search = new Search.Builder(query)
                    .addIndex(ESSettings.INDEX_NAME)
                    .addType(ESSettings.USER_TYPE_NAME)
                    .build();

            try {
                SearchResult result = ESSettings.client.execute(search);
                List<SearchResult.Hit<Map,Void>> hits = result.getHits(Map.class);
                SearchResult.Hit hit = hits.get(0);
                Map source = (Map)hit.source;
                String id = (String)source.get(JestResult.ES_METADATA_ID);
                if (result.isSucceeded()) {
                    rider = result.getSourceAsObject(User.class);
                    rider.setUserID(id);
                }
                else {
                    Log.i("Error", "The search query failed to find the rider that matched.");
                }
            }
            catch (Exception e) {
                Log.i("Error", "Something went wrong when we tried to communicate with the elasticsearch server!");
            }
            return rider;
        }
    }

    /**
     * This class is used to run an AsyncTask in the background to get driver info of a user
     * from the elastic search server.
     */
    public static class GetDriverInfo extends AsyncTask<String, Void, DriverInfo> {
        @Override
        protected DriverInfo doInBackground(String... search_parameters) {

            ESSettings.verifySettings();

            DriverInfo driverInfo = null;

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
                    .addType(ESSettings.DRIVER_TYPE_NAME)
                    .build();

            try {
                SearchResult result = ESSettings.client.execute(search);
                if (result.isSucceeded()) {
                    driverInfo = result.getSourceAsObject(DriverInfo.class);
                }
                else {
                    Log.i("Error", "The search query failed to find the driver info that matched.");
                }
            }
            catch (Exception e) {
                Log.i("Error", "Something went wrong when we tried to communicate with the elasticsearch server!");
            }

            return driverInfo;
        }
    }
}
