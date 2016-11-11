package com.ualberta.cs.alfred;

import android.test.ActivityInstrumentationTestCase2;

import java.util.concurrent.ExecutionException;

/**
 * Created by grandmanitou on 2016-11-10.
 */

public class RequestElasticSearchControllerTest extends ActivityInstrumentationTestCase2 {

    public RequestElasticSearchControllerTest() {

        super(MainActivity.class);
    }

    /*
    NormalTweet newTweet = new NormalTweet(text);
    tweetList.add(newTweet);
    adapter.notifyDataSetChanged();
    // saveInFile(); // TODO replace this with elastic search
    ElasticsearchTweetController.AddTweetsTask addTweetsTask = new ElasticsearchTweetController.AddTweetsTask();
    addTweetsTask.execute(newTweet);
    */

    public void testAddRequestTask() {

        RequestList aList = new RequestList();

        // Create request #1
        String req1Status = "Pending";
        Address req1SrcAddr = new Address("U of A", 65.56777, 79.34555);
        Address req1DestAddr = new Address("Downtown", 50.56500, 89.56888);
        double req1Cost = 12.30;
        double req1Distance = 4.5;
        String req1RiderID = "rider001";

        Request req1 = new Request(req1Status, req1SrcAddr, req1DestAddr, req1Distance, req1Cost,
                req1RiderID);

        //aList.addRequest(req1);

        //RequestElasticSearchController.AddRequestTask addRequestsTask = new RequestElasticSearchController.AddRequestTask();
        //addRequestsTask.execute(req1);

        // Create request #2
        String req2Status = "Accepted";
        Address req2SrcAddr = new Address("West Ed", 51.56777, 30.34555);
        Address req2DestAddr = new Address("South Campus", 20.56500, 12.56888);;
        double req2Cost = 30.30;
        double req2Distance = 21.5;
        String req2RiderID = "rider001";

        Request req2 = new Request(req2Status, req2SrcAddr, req2DestAddr, req2Distance, req2Cost,
                req2RiderID);

        RequestElasticSearchController.AddRequestTask addRequestsTask = new RequestElasticSearchController.AddRequestTask();
        addRequestsTask.execute(req2);

    }

    public void testGetRequestTask() {

        RequestElasticSearchController.GetRequestTask retrievedRequest = new RequestElasticSearchController.GetRequestTask();
        retrievedRequest.execute("1");

        try {
            Request request = retrievedRequest.get();
            System.out.println("====================");
            System.out.println("Request cost is: " + request.getCost());
            System.out.println("====================");
            assert(true);
            //Rider jimbo = new Rider();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void testGetRequestMaxIdTask() {
        RequestElasticSearchController.GetRequestMaxIdTask retrievedRequest = new RequestElasticSearchController.GetRequestMaxIdTask();
        retrievedRequest.execute("requestID");

        try {
            Long maxID = retrievedRequest.get();
            System.out.println("====================");
            System.out.println("Request ID is: " + maxID);
            System.out.println("====================");
            assert(true);
            //Rider jimbo = new Rider();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

}
