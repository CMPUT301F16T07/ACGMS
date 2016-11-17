package com.ualberta.cs.alfred;

import android.os.AsyncTask;
import android.util.Log;

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

    private static final String SERVER_URI = "http://ela1.ookoo.co:9200";
    private static final String INDEX_NAME = "alfred";
    private static final String RIDER_TYPE_NAME = "rider";
    private static final String DRIVER_TYPE_NAME = "driverinfo";

    private static JestDroidClient client;


    /**
     * adds users to elasticsearch server
     *
     *
     */
    public static class AddUser<T> extends AsyncTask<T, Void, Void> {
        @Override
        // one or more objects (riders or drivers) given, can be an array of Riders without specifying an array
        protected Void doInBackground(T... objects) {
            verifySettings();

            String esType;

            for (T object : objects) {
                if (object instanceof Rider) {
                    esType = RIDER_TYPE_NAME;
                } else {
                    esType = DRIVER_TYPE_NAME;
                }
                Index index = new Index.Builder(object).index(INDEX_NAME).type(esType).build();

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

            verifySettings();

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
                    .addIndex(INDEX_NAME)
                    .addType(RIDER_TYPE_NAME)
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

            verifySettings();

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

    /**
     * Check client settings
     */
    private static void verifySettings() {
        // if the client hasn't been initialized then we should make it!
        if (client == null) {
            DroidClientConfig.Builder builder = new DroidClientConfig.Builder(SERVER_URI);
            DroidClientConfig config = builder.build();

            JestClientFactory factory = new JestClientFactory();
            factory.setDroidClientConfig(config);
            client = (JestDroidClient) factory.getObject();
        }
    }
}
