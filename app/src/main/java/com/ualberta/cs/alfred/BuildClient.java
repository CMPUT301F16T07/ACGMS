package com.ualberta.cs.alfred;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

/**
 * Created by mmcote on 2016-11-09.
 */

public class BuildClient {
    JestDroidClient client;
    public BuildClient() {
        this.client = verifySettings(new JestDroidClient());
    }

    private static JestDroidClient verifySettings(JestDroidClient client) {
        // if the client hasn't been initialized then we should make it!
        if (client == null) {
            DroidClientConfig.Builder builder = new DroidClientConfig.Builder("http://ela1.ookoo.co:9200");
            DroidClientConfig config = builder.build();

            JestClientFactory factory = new JestClientFactory();
            factory.setDroidClientConfig(config);
            client = (JestDroidClient) factory.getObject();
        }
        return client;
    }

    public JestDroidClient getClient() {
        return client;
    }
}
