package com.ualberta.cs.alfred;

import android.os.AsyncTask;
import android.util.Log;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.searchbox.client.JestResult;
import io.searchbox.core.Delete;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Get;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

/**
 * Request Controller for Elasticsearch
 * @author ookmm
 * @version 1.3
 */
public class RequestElasticSearchController {

    //private static final String SERVER_URI = "http://ela1.ookoo.co:9200";
    private static final String SERVER_URI = "http://192.168.1.213:9200";
    private static final String INDEX_NAME = "alfred";
    private static final String TYPE_NAME = "request";

    private static JestDroidClient client;

    /**
     * The type Add request task.
     */
    public static class AddRequestTask extends AsyncTask<Request, Void, Void> {
        @Override
        // one or more Request given, can be an array of Requests without specifying an array
        protected Void doInBackground(Request... requests) {
            verifySettings();

            for (Request request : requests) {
                Index index = new Index.Builder(request).index(INDEX_NAME).type(TYPE_NAME).build();

                try {
                    DocumentResult result = client.execute(index);
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
     * Get request task.
     *
     * @return a list of requests matching the search parameters.
     */
    public static class GetRequestTask extends AsyncTask<String, Void, ArrayList<Request>> {
        @Override
        protected ArrayList<Request> doInBackground(String... search_parameters) {
            verifySettings();

            ArrayList<Request> requests = new ArrayList<Request>();

            String query = "{\n" +
                    "    \"query\": {\n" +
                    "        \"match\" : {\n" +
                    "            \""+search_parameters[0]+"\" : \n" +
                    "                \""+search_parameters[1]+"\"\n" +
                    "            }\n" +
                    "    }\n" +
                    "}";

            Search search = new Search.Builder(query)
                    .addIndex(INDEX_NAME)
                    .addType(TYPE_NAME)
                    .build();

            try {
                SearchResult result = client.execute(search);
                if (result.isSucceeded()) {
                    List<Request> foundRequests = result.getSourceAsObjectList(Request.class);
                    requests.addAll(foundRequests);
                }
                else {
                    Log.i("Error", "The search query failed to find the request that matched.");
                }
            }
            catch (Exception e) {
                Log.i("Error", "Something went wrong when we tried to communicate with " +
                        "the elasticsearch server!");
            }

            return requests;
        }
    }

    /**
     * Gets requests by id task.
     *
     * @return a list of requests
     */
    public static class GetRequestByIdTask extends AsyncTask<String, Void, Request> {
        @Override
        protected Request doInBackground(String... search_parameters) {

            verifySettings();

            Request request = new Request();

            Get get = new Get.Builder(INDEX_NAME, search_parameters[0])
                    .type(TYPE_NAME)
                    .build();

            try {
                JestResult result = client.execute(get);
                if (result.isSucceeded()) {
                    request = result.getSourceAsObject(Request.class);
                }
                else {
                    Log.i("Error", "The search query failed to find the request that matched.");
                }
            }
            catch (Exception e) {
                Log.i("Error", "Something went wrong when we tried to communicate with " +
                        "the elasticsearch server!");
            }

            return request;
        }
    }

    /**
     * Delete request by id task.
     */
    public static class DeleteRequestTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... search_parameters) {

            verifySettings();

            try {
                client.execute(new Delete.Builder(search_parameters[0])
                .index(INDEX_NAME)
                .type(TYPE_NAME)
                .build());
            } catch (IOException e) {
                Log.i("Error", "Something went wrong when we tried to communicate with " +
                        "the elasticsearch server!");
            }

            return null;
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
