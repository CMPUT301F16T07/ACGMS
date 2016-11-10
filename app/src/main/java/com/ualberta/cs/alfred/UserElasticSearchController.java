package com.ualberta.cs.alfred;

import android.os.AsyncTask;
import android.util.Log;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

/**
 * Created by mmcote on 2016-11-08.
 */

public class UserElasticSearchController {
    private static JestDroidClient client;


    public static class AddRider extends AsyncTask<Rider, Void, Void> {
        @Override
        // one or more Riders given, can be an array of Riders without specifying an array
        protected Void doInBackground(Rider... riders) {
            verifySettings();

            for (Rider rider : riders) {
                Index index = new Index.Builder(rider).index("riderlist").type("rider").build();

                try {
                    DocumentResult result = client.execute(index);
                    if (!result.isSucceeded()) {
                        Log.i("Error", "Elastic search was not able to add the rider.");
                    }
                } catch (Exception e) {
                    Log.i("Uh-Oh", "We failed to add a rider to elastic search!");
                    e.printStackTrace();
                }
            }
            return null;
        }
    }

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
                    .addIndex("riderlist")
                    .addType("rider")
                    .build();

            try {
                SearchResult result = client.execute(search);
                if (result.isSucceeded()) {
                    rider = result.getSourceAsObject(Rider.class);
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

    public static class AddDriverInfo extends AsyncTask<DriverInfo, Void, Void> {
        @Override
        // one or more Riders given, can be an array of Riders without specifying an array
        protected Void doInBackground(DriverInfo... driversInfo) {
            verifySettings();

            for (DriverInfo driverInfo : driversInfo) {
                Index index = new Index.Builder(driverInfo).index("driverinfolist").type("driverinfo").build();

                try {
                    DocumentResult result = client.execute(index);
                    if (!result.isSucceeded()) {
                        Log.i("Error", "Elastic search was not able to add the driver info.");
                    }
                } catch (Exception e) {
                    Log.i("Uh-Oh", "We failed to add a driver to elastic search!");
                    e.printStackTrace();
                }
            }
            return null;
        }
    }

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
                    .addIndex("driverinfolist")
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

    private static void verifySettings() {
        // if the client hasn't been initialized then we should make it!
        if (client == null) {
            DroidClientConfig.Builder builder = new DroidClientConfig.Builder("http://ela1.ookoo.co:9200");
            DroidClientConfig config = builder.build();

            JestClientFactory factory = new JestClientFactory();
            factory.setDroidClientConfig(config);
            client = (JestDroidClient) factory.getObject();
        }
    }
}
