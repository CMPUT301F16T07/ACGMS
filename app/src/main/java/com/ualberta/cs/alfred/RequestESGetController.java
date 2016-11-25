package com.ualberta.cs.alfred;

import android.os.AsyncTask;
import android.util.Log;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.searchbox.client.JestResult;
import io.searchbox.core.Get;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

/**
 * This handles the getting of items from Requests
 *
 * @author ookmm
 * @version 1.4
 */
public class RequestESGetController {

    /**
     * Get request task.
     *
     * @return a list of requests matching the search parameters.
     *
     * @Note: Use GetRequestByMultiplePreferencesTask function to allow for more search
     * preferences like finding Pending requests by certain rider.
     */
    public static class GetRequestTask extends AsyncTask<String, Void, ArrayList<Request>> {
        @Override
        protected ArrayList<Request> doInBackground(String... search_parameters) {
            ESSettings.verifySettings();

            ArrayList<Request> requests = new ArrayList<>();

            /**
             * Get the total number of results
             */
            String query = "{\n" +
                    "    \"query\": {\n" +
                    "        \"match\" : {\n" +
                    "            \"" + search_parameters[0] + "\" : \n" +
                    "                \"" + search_parameters[1] + "\"\n" +
                    "            }\n" +
                    "    }\n" +
                    "}";

            Search search = new Search.Builder(query)
                    .addIndex(ESSettings.INDEX_NAME)
                    .addType(ESSettings.REQUEST_TYPE_NAME)
                    .build();

                    JsonElement total = null;

                    try {
                        SearchResult result = ESSettings.client.execute(search);
                        if (result.isSucceeded()) {
                            // Number of elements that matched the search
                            total = result.getJsonObject().getAsJsonObject("hits").get("total");
                        } else {
                            Log.i("Error", "The search query failed to find the request that matched.");
                        }
                    } catch (Exception e) {
                        Log.i("Error", "Something went wrong when we tried to communicate with " +
                                "the elasticsearch server!");
                    }

                    /**
                     * Return results size based on the value of total
                     */
                    String query2 = String.format("{\n" +
                                    "    \"query\": {\n" +
                                    "        \"match\" : {\n" +
                                    "            \"" + search_parameters[0] + "\" : \n" +
                                    "                \"" + search_parameters[1] + "\"\n" +
                                    "            }\n" +
                                    "    },\n" +
                                    "\"size\" : %s" +
                                    "}",
                            total);

                    Search search2 = new Search.Builder(query2)
                            .addIndex(ESSettings.INDEX_NAME)
                            .addType(ESSettings.REQUEST_TYPE_NAME)
                            .build();

                    try {
                        SearchResult result2 = ESSettings.client.execute(search2);
                        if (result2.isSucceeded()) {
                            List<Request> foundRequests = result2.getSourceAsObjectList(Request.class);
                            requests.addAll(foundRequests);
                        } else {
                            Log.i("Error", "The search query failed to find the request that matched.");
                        }
                    } catch (Exception e) {
                        Log.i("Error", "Something went wrong when we tried to communicate with " +
                                "the elasticsearch server!");
                    }

                    return requests;
                }
            }

    /**
     * Gets request by id task.
     *
     * @return a request matching the id.
     */
    public static class GetRequestByIdTask extends AsyncTask<String, Void, Request> {
        @Override
        protected Request doInBackground(String... search_parameters) {

            ESSettings.verifySettings();

            Request request = null;


            Get get = new Get.Builder(ESSettings.INDEX_NAME, search_parameters[0])
                    .type(ESSettings.REQUEST_TYPE_NAME)
                    .build();

            try {
                JestResult result = ESSettings.client.execute(get);
                if (result.isSucceeded()) {
                    request = result.getSourceAsObject(Request.class);
                } else {
                    Log.i("Error", "The search query failed to find the request that matched.");
                }
            } catch (Exception e) {
                Log.i("Error", "Something went wrong when we tried to communicate with " +
                        "the elasticsearch server!");
            }

            return request;
        }
    }

    /**
     * Get Request using multiple preferences
     *
     * Example: get pending requests by a certain rider.
     * @Note: Can also be used to retrieve all requests, without any preferences. See test cases.
     */
    public static class GetRequestByMultiplePreferencesTask extends AsyncTask<String, Void, ArrayList<Request>> {
        @Override
        protected ArrayList<Request> doInBackground(String... preference_parameters) {

            ESSettings.verifySettings();

            ArrayList<Request> requests = new ArrayList<>();

            // The number of elements in "update_parameters"
            int pSize = preference_parameters.length;

            StringBuilder stringBuilder = new StringBuilder();

            int j = 0;
            int k = 0;
            for (int i = 0; i < pSize - 2; ++i) {

                if (i + j + k + 2 < pSize) {
                    String key = preference_parameters[i + j + k];
                    String valueType = preference_parameters[i + j + k + 1];
                    String value = preference_parameters[i + j + k + 2];

                    String keyValue = null;

                    switch (valueType.toLowerCase()) {

                        case "string":
                        case "date":
                            keyValue = String.format(
                                    "{ \n" +
                                            "\"match\" : { \n" +
                                            "\"%s\" : \"%s\"\n" +
                                            "}\n" +
                                            "}",
                                    key, value);
                            stringBuilder.append(keyValue);
                            break;

                        case "double":
                            double valueAsDouble = Double.parseDouble(value);
                            keyValue = String.format(
                                    "{ \n" +
                                            "\"match\" : { \n" +
                                            "\"%s\" : %s\n" +
                                            "}\n" +
                                            "}",
                                    key, valueAsDouble);
                            stringBuilder.append(keyValue);
                            break;

                        case "all":
                            keyValue = String.format(
                                    "{ \n" +
                                            "\"%s\" : %s\n" +
                                            "}",
                                    key, value);
                            stringBuilder.append(keyValue);
                            break;

                        default:
                            throw new IllegalArgumentException("Provided an unsupported property type: " +
                                    valueType);

                    }

                    /**
                     * Don't add new line and comma on last element
                     */
                    if (i + j + k + 2 < pSize - 1) {
                        String comma = ",\n";
                        stringBuilder.append(comma);
                    }

                    ++j;
                    ++k;

                }
            }

            /**
             * Get the total number of results
             */
            String query = String.format(
                    "{\n" +
                            "\"query\" : {\n" +
                            "\"bool\" : {\n" +
                            "\"must\" : [\n" +
                            "%s \n" +
                            "]\n" +
                            "}\n" +
                            "}\n" +
                            "}",
                    stringBuilder);

            Search search = new Search.Builder(query)
                    .addIndex(ESSettings.INDEX_NAME)
                    .addType(ESSettings.REQUEST_TYPE_NAME)
                    .build();

            JsonElement total = null;

            try {
                SearchResult result = ESSettings.client.execute(search);
                if (result.isSucceeded()) {
                    // Number of elements that matched the search
                    total = result.getJsonObject().getAsJsonObject("hits").get("total");
                } else {
                    Log.i("Error", "The search query failed to find the request that matched.");
                }
            } catch (Exception e) {
                Log.i("Error", "Something went wrong when we tried to communicate with " +
                        "the elasticsearch server!");
            }

            /**
             * Return results size based on the value of total
             */
            String query2 = String.format(
                    "{\n" +
                            "\"query\" : {\n" +
                            "\"bool\" : {\n" +
                            "\"must\" : [\n" +
                            "%s \n" +
                            "]\n" +
                            "}\n" +
                            "},\n" +
                            "\"size\" : %s\n" +
                            "}",
                    stringBuilder, total);

            Search search2 = new Search.Builder(query2)
                    .addIndex(ESSettings.INDEX_NAME)
                    .addType(ESSettings.REQUEST_TYPE_NAME)
                    .build();

            try {
                SearchResult result2 = ESSettings.client.execute(search2);


                if (result2.isSucceeded()) {

                    /*
                    JsonArray hits = result2.getJsonObject()
                            .getAsJsonObject("hits")
                            .getAsJsonArray("hits");

                    int numHits = hits.size();

                    System.out.println("==== Num hits =====");
                    System.out.println(numHits);
                    System.out.println("==== End of Num hits =====");
                    */

                    List<Request> foundRequests = result2.getSourceAsObjectList(Request.class);
                    requests.addAll(foundRequests);

                } else {
                    Log.i("Error", "The search query failed to find the request that matched.");
                }
            } catch (IOException e) {
                Log.i("Error", "Something went wrong when we tried to communicate with " +
                        "the elasticsearch server!");
            }

            return requests;
        }
    }

    /**
     * Get Request with multiple preferences sorted by price
     *
     * Example: get pending requests by a certain rider sorted by price in descending order.
     * @Note: Can also be used to get all requests sorted by cost.
     */
    public static class GetRequestSortedByPriceTask extends AsyncTask<String, Void, ArrayList<Request>> {
        @Override
        protected ArrayList<Request> doInBackground(String... preference_parameters) {

            ESSettings.verifySettings();

            ArrayList<Request> requests = new ArrayList<>();

            // The number of elements in "update_parameters"
            int pSize = preference_parameters.length;
            // Last element in preference_parameters is how to order the result (asc or desc)
            String orderBy = preference_parameters[pSize - 1];

            StringBuilder stringBuilder = new StringBuilder();

            int j = 0;
            int k = 0;
            for (int i = 0; i < pSize - 3; ++i) {

                if (i + j + k + 2 < pSize) {
                    String key = preference_parameters[i + j + k];
                    String valueType = preference_parameters[i + j + k + 1];
                    String value = preference_parameters[i + j + k + 2];

                    String keyValue = null;

                    switch (valueType.toLowerCase()) {

                        case "string":
                        case "date":
                            keyValue = String.format(
                                    "{ \n" +
                                            "\"match\" : { \n" +
                                            "\"%s\" : \"%s\"\n" +
                                            "}\n" +
                                            "}",
                                    key, value);
                            stringBuilder.append(keyValue);
                            break;

                        case "double":
                            double valueAsDouble = Double.parseDouble(value);
                            keyValue = String.format(
                                    "{ \n" +
                                            "\"match\" : { \n" +
                                            "\"%s\" : %s\n" +
                                            "}\n" +
                                            "}",
                                    key, valueAsDouble);
                            stringBuilder.append(keyValue);
                            break;

                        case "all":
                            keyValue = String.format(
                                    "{ \n" +
                                            "\"%s\" : %s\n" +
                                            "}",
                                    key, value);
                            stringBuilder.append(keyValue);
                            break;

                        default:
                            throw new IllegalArgumentException("Provided an unsupported property type: " +
                                    valueType);

                    }

                    /**
                     * Don't add new line and comma on last element
                     */
                    if (i + j + k + 2 < pSize - 2) {
                        String comma = ",\n";
                        stringBuilder.append(comma);
                    }

                    ++j;
                    ++k;

                }
            }

            /**
             * Get the total number of results
             */
            String query = String.format(
                    "{\n" +
                            "\"query\" : {\n" +
                            "\"bool\" : {\n" +
                            "\"must\" : [\n" +
                            "%s \n" +
                            "]\n" +
                            "}\n" +
                            "}\n" +
                            "}",
                    stringBuilder);

            Search search = new Search.Builder(query)
                    .addIndex(ESSettings.INDEX_NAME)
                    .addType(ESSettings.REQUEST_TYPE_NAME)
                    .build();

            JsonElement total = null;

            try {
                SearchResult result = ESSettings.client.execute(search);
                if (result.isSucceeded()) {
                    // Number of elements that matched the search
                    total = result.getJsonObject().getAsJsonObject("hits").get("total");
                } else {
                    Log.i("Error", "The search query failed to find the request that matched.");
                }
            } catch (Exception e) {
                Log.i("Error", "Something went wrong when we tried to communicate with " +
                        "the elasticsearch server!");
            }

            /**
             * String for sorting by cost
             */
            String sortByCost = String.format(
                    "\"sort\" : [\n" +
                            "{\n" +
                            "\"cost\" : {\n" +
                            "\"order\" : \"%s\"\n" +
                            "}\n" +
                            "}\n" +
                            "]\n",
            orderBy);

            /**
             * Return results size based on the value of total
             */
            String query2 = String.format(
                    "{\n" +
                            "\"query\" : {\n" +
                            "\"bool\" : {\n" +
                            "\"must\" : [\n" +
                            "%s \n" +
                            "]\n" +
                            "}\n" +
                            "},\n" +
                            "%s,\n" +
                            "\"size\" : %s\n" +
                            "}",
                    stringBuilder, sortByCost, total);

            Search search2 = new Search.Builder(query2)
                    .addIndex(ESSettings.INDEX_NAME)
                    .addType(ESSettings.REQUEST_TYPE_NAME)
                    .build();

            try {
                SearchResult result2 = ESSettings.client.execute(search2);


                if (result2.isSucceeded()) {

                    List<Request> foundRequests = result2.getSourceAsObjectList(Request.class);
                    requests.addAll(foundRequests);

                } else {
                    Log.i("Error", "The search query failed to find the request that matched.");
                }
            } catch (IOException e) {
                Log.i("Error", "Something went wrong when we tried to communicate with " +
                        "the elasticsearch server!");
            }

            return requests;
        }
    }

    /**
     * Get Request with multiple preferences sorted by price per kilometer
     *
     * Example: get pending requests by a certain rider sorted by price per kilometers in descending
     * order.
     * @Note: Can also be used to get all requests sorted by price per kilometer
     */
    public static class GetRequestSortedByPricePerKmTask extends AsyncTask<String, Void, ArrayList<Request>> {
        @Override
        protected ArrayList<Request> doInBackground(String... preference_parameters) {

            ESSettings.verifySettings();

            ArrayList<Request> requests = new ArrayList<>();

            // The number of elements in "update_parameters"
            int pSize = preference_parameters.length;
            // Last element in preference_parameters is how to order the result (asc or desc)
            String orderBy = preference_parameters[pSize - 1];

            StringBuilder stringBuilder = new StringBuilder();

            int j = 0;
            int k = 0;
            for (int i = 0; i < pSize - 3; ++i) {

                if (i + j + k + 2 < pSize) {
                    String key = preference_parameters[i + j + k];
                    String valueType = preference_parameters[i + j + k + 1];
                    String value = preference_parameters[i + j + k + 2];

                    String keyValue = null;

                    switch (valueType.toLowerCase()) {

                        case "string":
                        case "date":
                            keyValue = String.format(
                                    "{ \n" +
                                            "\"match\" : { \n" +
                                            "\"%s\" : \"%s\"\n" +
                                            "}\n" +
                                            "}",
                                    key, value);
                            stringBuilder.append(keyValue);
                            break;

                        case "double":
                            double valueAsDouble = Double.parseDouble(value);
                            keyValue = String.format(
                                    "{ \n" +
                                            "\"match\" : { \n" +
                                            "\"%s\" : %s\n" +
                                            "}\n" +
                                            "}",
                                    key, valueAsDouble);
                            stringBuilder.append(keyValue);
                            break;

                        case "all":
                            keyValue = String.format(
                                    "{ \n" +
                                            "\"%s\" : %s\n" +
                                            "}",
                                    key, value);
                            stringBuilder.append(keyValue);
                            break;

                        default:
                            throw new IllegalArgumentException("Provided an unsupported property type: " +
                                    valueType);

                    }

                    /**
                     * Don't add new line and comma on last element
                     */
                    if (i + j + k + 2 < pSize - 2) {
                        String comma = ",\n";
                        stringBuilder.append(comma);
                    }

                    ++j;
                    ++k;

                }
            }

            /**
             * Get the total number of results
             */
            String query = String.format(
                    "{\n" +
                            "\"query\" : {\n" +
                            "\"bool\" : {\n" +
                            "\"must\" : [\n" +
                            "%s \n" +
                            "]\n" +
                            "}\n" +
                            "}\n" +
                            "}",
                    stringBuilder);

            Search search = new Search.Builder(query)
                    .addIndex(ESSettings.INDEX_NAME)
                    .addType(ESSettings.REQUEST_TYPE_NAME)
                    .build();

            JsonElement total = null;

            try {
                SearchResult result = ESSettings.client.execute(search);
                if (result.isSucceeded()) {
                    // Number of elements that matched the search
                    total = result.getJsonObject().getAsJsonObject("hits").get("total");
                } else {
                    Log.i("Error", "The search query failed to find the request that matched.");
                }
            } catch (Exception e) {
                Log.i("Error", "Something went wrong when we tried to communicate with " +
                        "the elasticsearch server!");
            }

            /**
             * String for sorting cost per kilometer
             */
            String sortCostPerKm = String.format("\"sort\" : {\n" +
                    "\"_script\" : {\n" +
                    "\"type\" : \"number\",\n" +
                    "\"script\" : {\n" +
                    "\"lang\" : \"painless\",\n" +
                    "\"inline\" : \"doc['cost'].value / doc['distance'].value\"\n" +
                    "},\n" +
                    "\"order\" : \"%s\"\n" +
                    "}\n" +
                    "}",
                    orderBy);

            /**
             * Return results size based on the value of total
             */
            String query2 = String.format(
                    "{\n" +
                            "\"query\" : {\n" +
                            "\"bool\" : {\n" +
                            "\"must\" : [\n" +
                            "%s \n" +
                            "]\n" +
                            "}\n" +
                            "},\n" +
                            "%s,\n" +
                            "\"size\" : %s\n" +
                            "}",
                    stringBuilder, sortCostPerKm, total);

            Search search2 = new Search.Builder(query2)
                    .addIndex(ESSettings.INDEX_NAME)
                    .addType(ESSettings.REQUEST_TYPE_NAME)
                    .build();

            try {
                SearchResult result2 = ESSettings.client.execute(search2);


                if (result2.isSucceeded()) {

                    List<Request> foundRequests = result2.getSourceAsObjectList(Request.class);
                    requests.addAll(foundRequests);

                } else {
                    Log.i("Error", "The search query failed to find the request that matched.");
                }
            } catch (IOException e) {
                Log.i("Error", "Something went wrong when we tried to communicate with " +
                        "the elasticsearch server!");
            }

            return requests;
        }
    }

    /**
     * Get Request by geo-location
     *
     * Example: Find all pending request, by a certain user, that are within an area of 200km.
     * @Note: The distance parameter as to provided exactly like this: 200km, for example.
     * Look at test cases for implementation examples.
     */
    public static class GetRequestByLocationTask extends AsyncTask<String, Void, ArrayList<Request>> {
        @Override
        protected ArrayList<Request> doInBackground(String... preference_parameters) {

            ESSettings.verifySettings();

            ArrayList<Request> requests = new ArrayList<>();

            // The number of elements in "update_parameters"
            int pSize = preference_parameters.length;

            String distance = preference_parameters[pSize - 2];
            String coordinates = preference_parameters[pSize - 1];

            StringBuilder stringBuilder = new StringBuilder();

            int j = 0;
            int k = 0;
            for (int i = 0; i < pSize - 4; ++i) {

                if (i + j + k + 2 < pSize) {
                    String key = preference_parameters[i + j + k];
                    String valueType = preference_parameters[i + j + k + 1];
                    String value = preference_parameters[i + j + k + 2];

                    String keyValue = null;

                    switch (valueType.toLowerCase()) {

                        case "string":
                        case "date":
                            keyValue = String.format(
                                    "{ \n" +
                                            "\"match\" : { \n" +
                                            "\"%s\" : \"%s\"\n" +
                                            "}\n" +
                                            "}",
                                    key, value);
                            stringBuilder.append(keyValue);
                            break;

                        case "double":
                            double valueAsDouble = Double.parseDouble(value);
                            keyValue = String.format(
                                    "{ \n" +
                                            "\"match\" : { \n" +
                                            "\"%s\" : %s\n" +
                                            "}\n" +
                                            "}",
                                    key, valueAsDouble);
                            stringBuilder.append(keyValue);
                            break;

                        case "all":
                            keyValue = String.format(
                                    "{ \n" +
                                            "\"%s\" : %s\n" +
                                            "}",
                                    key, value);
                            stringBuilder.append(keyValue);
                            break;

                        default:
                            throw new IllegalArgumentException("Provided an unsupported property type: " +
                                    valueType);

                    }

                    /**
                     * Don't add new line and comma on last element
                     */
                    if (i + j + k + 2 < pSize - 3) {
                        String comma = ",\n";
                        stringBuilder.append(comma);
                    }

                    ++j;
                    ++k;

                }
            }

            /**
             * String for filtering by geo-distance
             */
            String geoDistance = String.format("\"filter\" : {\n" +
                            "\"geo_distance\" : {\n" +
                            "\"distance\" : \"%s\", \n" +
                            "\"sourceAddress.coordinates\" : %s \n" +
                            "}\n" +
                            "}",
                    distance, coordinates);

            /**
             * Get the total number of results
             */
            String query = String.format(
                    "{\n" +
                            "\"query\" : {\n" +
                            "\"bool\" : {\n" +
                            "\"must\" : [\n" +
                            "%s \n" +
                            "], \n" +
                            "%s\n" +
                            "}\n" +
                            "}\n" +
                            "}",
                    stringBuilder, geoDistance);

            Search search = new Search.Builder(query)
                    .addIndex(ESSettings.INDEX_NAME)
                    .addType(ESSettings.REQUEST_TYPE_NAME)
                    .build();

            JsonElement total = null;

            try {
                SearchResult result = ESSettings.client.execute(search);
                if (result.isSucceeded()) {
                    // Number of elements that matched the search
                    total = result.getJsonObject().getAsJsonObject("hits").get("total");
                } else {
                    Log.i("Error", "The search query failed to find the request that matched.");
                }
            } catch (Exception e) {
                Log.i("Error", "Something went wrong when we tried to communicate with " +
                        "the elasticsearch server!");
            }

            /**
             * Return results size based on the value of total
             */
            String query2 = String.format(
                    "{\n" +
                            "\"query\" : {\n" +
                            "\"bool\" : {\n" +
                            "\"must\" : [\n" +
                            "%s \n" +
                            "],\n" +
                            "%s \n" +
                            "}\n" +
                            "},\n" +
                            "\"size\" : %s\n" +
                            "}",
                    stringBuilder, geoDistance, total);

            Search search2 = new Search.Builder(query2)
                    .addIndex(ESSettings.INDEX_NAME)
                    .addType(ESSettings.REQUEST_TYPE_NAME)
                    .build();

            try {
                SearchResult result2 = ESSettings.client.execute(search2);


                if (result2.isSucceeded()) {

                    List<Request> foundRequests = result2.getSourceAsObjectList(Request.class);
                    requests.addAll(foundRequests);

                } else {
                    Log.i("Error", "The search query failed to find the request that matched.");
                }
            } catch (IOException e) {
                Log.i("Error", "Something went wrong when we tried to communicate with " +
                        "the elasticsearch server!");
            }

            return requests;
        }
    }
}