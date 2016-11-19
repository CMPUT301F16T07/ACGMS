package com.ualberta.cs.alfred;

import android.os.AsyncTask;
import android.util.Log;

import java.util.List;
import java.util.Map;

import io.searchbox.client.JestResult;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.params.Parameters;

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
    /**
     * adds users to elasticsearch server
     *
     *
     */
    public static class AddUser<T> extends AsyncTask<T, Void, Void> {
        @Override
        // one or more objects (riders or drivers) given, can be an array of Riders without specifying an array
        protected Void doInBackground(T... objects) {
            ESSettings.verifySettings();


            String esType;

            for (T object : objects) {
                if (object instanceof Rider) {
                    esType = ESSettings.RIDER_TYPE_NAME;
                } else {
                    esType = ESSettings.DRIVER_TYPE_NAME;
                }
                Index index = new Index.Builder(object)
                        .index(ESSettings.INDEX_NAME)
                        .type(esType)
                        .setParameter(Parameters.REFRESH, true)
                        .build();

                try {
                    DocumentResult result = ESSettings.client.execute(index);
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

            ESSettings.verifySettings();

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
                    .addIndex(ESSettings.INDEX_NAME)
                    .addType(ESSettings.RIDER_TYPE_NAME)
                    .build();

            try {
                SearchResult result = ESSettings.client.execute(search);
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

            ESSettings.verifySettings();

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
