package com.ualberta.cs.alfred;

import android.os.AsyncTask;
import android.util.Log;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import org.json.JSONObject;

import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.core.search.aggregation.TermsAggregation;

/**
 * Created by grandmanitou on 2016-11-10.
 */

public class RequestElasticSearchController {

    private static final String SERVER_URI = "http://ela1.ookoo.co:9200";
    
    private static final String INDEX_NAME = "alfred";
    private static final String TYPE_NAME = "request";

    private static JestDroidClient client;

    public static class AddRequestTask extends AsyncTask<Request, Void, Void> {
        @Override
        // one or more Riders given, can be an array of Riders without specifying an array
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

    public static class GetRequestMaxIdTask extends AsyncTask<String, Void, Long> {
        @Override
        protected Long doInBackground(String... search_parameters) {
            verifySettings();

            //Request request = new Request();
            Long maxID = null;

            String query = "{                                                       \n" +
                    "           \"aggs\" : {                                        \n" +
                    "               \"max_requestID\" : {                           \n" +
                    "                   \"max\" : {                                 \n" +
                    "                   \"field\" : \""+search_parameters[0]+"\"    \n" +
                    "               }                                               \n" +
                    "           }                                                   \n" +
                    "       },                                                      \n" +
                    //"       \"size\" : 1                                            \n" +
                    "       }";

            /*query = "{" +
                    "\"query\" : {\n" +
                    "\"match_all\" : {}\n" +
                    "},\n" +
                    "\"sort\" : [\n" +
                    "{\n" +
                    "\"requestID\" \n" +
                    "\"order\" : \""+search_parameters[0]+"\" \n" +
                    "}\n" +
                    "}\n" +
                    "],\n" +
                    "\"size\" : 1 \n" +
                    "}"; */

            Search search = new Search.Builder(query)
                    .addIndex(INDEX_NAME)
                    .addType(TYPE_NAME)
                    .build();

            try {
                SearchResult result = client.execute(search);
                if (result.isSucceeded()) {
                    Request request = result.getSourceAsObject(Request.class);
                    maxID = request.getRequestID();

                }
                else {
                    Log.i("Error", "The search query failed to find the request that matched.");
                }
            }
            catch (Exception e) {
                Log.i("Error", "Something went wrong when we tried to communicate with the elasticsearch server!");
            }

            return maxID;
        }
    }

    public static class GetRequestTask extends AsyncTask<String, Void, Request> {
        @Override
        protected Request doInBackground(String... search_parameters) {
            verifySettings();

            Request request = new Request();

            String query = "{\n" +
                    "    \"query\": {\n" +
                    "        \"match\" : {\n" +
                    "            \"requestID\" : \n" +
                    "                \""+search_parameters[0]+"\"\n" +
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
                    request = result.getSourceAsObject(Request.class);
                }
                else {
                    Log.i("Error", "The search query failed to find the request that matched.");
                }
            }
            catch (Exception e) {
                Log.i("Error", "Something went wrong when we tried to communicate with the elasticsearch server!");
            }

            return request;
        }
    }

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
