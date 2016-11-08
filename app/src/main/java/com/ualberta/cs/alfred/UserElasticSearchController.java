package com.ualberta.cs.alfred;

import android.os.AsyncTask;
import android.util.Log;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;

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
                        Log.i("Error", "Elastic search was not able to add the tweet.");
                    }
                } catch (Exception e) {
                    Log.i("Uhoh", "We failed to add a tweet to elastic search!");
                    e.printStackTrace();
                }
            }
            return null;
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
