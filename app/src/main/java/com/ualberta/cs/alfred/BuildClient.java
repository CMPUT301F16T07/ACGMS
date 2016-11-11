package com.ualberta.cs.alfred;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

/**
 * This class is used to create and initialize a client to connect to the elastic search
 * server used for Alfred.
 *
 * @author mmcote
 * @version 1.0
 */

public class BuildClient {
    private static JestDroidClient client;

    /**
     * Instantiates a new client with the default settings.
     */
    public BuildClient() {
        setClient();
    }

    private void setClient() {
        // if the client hasn't been initialized then we should make it!
        DroidClientConfig.Builder builder = new DroidClientConfig.Builder("http://ela1.ookoo.co:9200");
        DroidClientConfig config = builder.build();

        JestClientFactory factory = new JestClientFactory();
        factory.setDroidClientConfig(config);
        this.client = (JestDroidClient) factory.getObject();
    }

    /**
     * Returns the client that has been initiated with the proper elastic search server
     * settings.
     *
     * @return the client
     */
    public JestDroidClient getClient() {
        return client;
    }
}
