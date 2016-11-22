package com.ualberta.cs.alfred;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

/**
 * Holds the configuration settings to access Elasticsearch
 *
 * @author ookmm
 * @version 1.0
 */
public class ESSettings {

    private static final String SERVER_URI = "http://ela1.ookoo.co:9200";
    static final String INDEX_NAME = "alfred";
    static final String REQUEST_TYPE_NAME = "request";
    static final String USER_TYPE_NAME = "user";
    static final String RIDER_TYPE_NAME = "rider";
    static final String VEHICLE_TYPE_NAME = "vehicle";
    static final String DRIVER_TYPE_NAME = "driverinfo";
    static JestDroidClient client;

    /**
     * Check client settings
     */
    static void verifySettings() {
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
