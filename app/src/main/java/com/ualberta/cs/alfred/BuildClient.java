package com.ualberta.cs.alfred;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

/**
 * Created by mmcote on 2016-11-09.
 */

public class BuildClient {
    private static JestDroidClient client;
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

    public JestDroidClient getClient() {
        return client;
    }
}
