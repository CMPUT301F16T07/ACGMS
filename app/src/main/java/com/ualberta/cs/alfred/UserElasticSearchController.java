package com.ualberta.cs.alfred;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import java.util.List;
import java.util.Map;

import io.searchbox.client.JestResult;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

/**
 * This controller holds the functionality to add and get users from
 * the elastic search server.
 *
 * @author mmcote
 * @version 1.0
 * @see Rider
 * @see Driver
 */
public class UserElasticSearchController {

    public static class AddUser<T> extends AsyncTask<T, Void, Void> {
        @Override
        // one or more objects (riders or drivers) given, can be an array of Riders without specifying an array
        protected Void doInBackground(T... objects) {
            String esIndex = "alfred";
            String esType;

            JestDroidClient client = new BuildClient().getClient();

            for (T object : objects) {
                if (object instanceof Rider) {
                    esType = "rider";
                } else {
                    esType = "driverinfo";
                }
                Index index = new Index.Builder(object).index(esIndex).type(esType).build();

                try {
                    DocumentResult result = client.execute(index);
                    if (!result.isSucceeded()) {
                        Log.i("Error", "Elastic search was not able to add the "+esType+".");
                    }
                } catch (Exception e) {
                    Log.i("Uh-Oh", "We failed to add a "+esType+ "to elastic search!");
                    e.printStackTrace();
                }
            }
            return null;
        }
    }

    /**
     * This class is used to run an AsyncTask in the background to get a rider from the
     * elastic search server.
     */
    public static class GetRider extends AsyncTask<String, Void, Rider> {
        @Override
        protected Rider doInBackground(String... search_parameters) {
            JestDroidClient client = new BuildClient().getClient();

            Rider rider = new Rider();

            String query = "{\n" +
                    "    \"query\": {\n" +
                    "        \"match\" : {\n" +
                    "            \"userName\" : \n" +
                    "                \""+search_parameters[0]+"\"\n" +
                    "            }\n" +
                    "    }\n" +
                    "}";

            Search search = new Search.Builder(query)
                    .addIndex("alfred")
                    .addType("rider")
                    .build();

            try {
                SearchResult result = client.execute(search);
                List<SearchResult.Hit<Map,Void>> hits = result.getHits(Map.class);
                SearchResult.Hit hit = hits.get(0);
                Map source = (Map)hit.source;
                String id = (String)source.get(JestResult.ES_METADATA_ID);
                if (result.isSucceeded()) {
                    rider = result.getSourceAsObject(Rider.class);
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
            JestDroidClient client = new BuildClient().getClient();

            DriverInfo driverInfo = new DriverInfo();

            String query = "{\n" +
                    "    \"query\": {\n" +
                    "        \"match\" : {\n" +
                    "            \"userName\" : \n" +
                    "                \""+search_parameters[0]+"\"\n" +
                    "            }\n" +
                    "    }\n" +
                    "}";

            Search search = new Search.Builder(query)
                    .addIndex("alfred")
                    .addType("driverinfo")
                    .build();

            try {
                SearchResult result = client.execute(search);
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
