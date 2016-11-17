package com.ualberta.cs.alfred;

import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.searchbox.client.JestResult;
import io.searchbox.core.Get;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;


/**
 * This handles the getting of items from Requests
 *
 * @author ookmm
 * @version 1.0
 */
public class RequestESGetController {

    /**
     * Get request task.
     *
     * @return a list of requests matching the search parameters.
     */
    public static class GetRequestTask extends AsyncTask<String, Void, ArrayList<Request>> {
        @Override
        protected ArrayList<Request> doInBackground(String... search_parameters) {
            ESSettings.verifySettings();

            ArrayList<Request> requests = new ArrayList<>();

            String query = "{\n" +
                    "    \"query\": {\n" +
                    "        \"match\" : {\n" +
                    "            \""+search_parameters[0]+"\" : \n" +
                    "                \""+search_parameters[1]+"\"\n" +
                    "            }\n" +
                    "    }\n" +
                    "}";

            Search search = new Search.Builder(query)
                    .addIndex(ESSettings.INDEX_NAME)
                    .addType(ESSettings.REQUEST_TYPE_NAME)
                    .build();

            try {
                SearchResult result = ESSettings.client.execute(search);
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

            ESSettings.verifySettings();

            Request request = new Request();

            Get get = new Get.Builder(ESSettings.INDEX_NAME, search_parameters[0])
                    .type(ESSettings.REQUEST_TYPE_NAME)
                    .build();

            try {
                JestResult result = ESSettings.client.execute(get);
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
}
