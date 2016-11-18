package com.ualberta.cs.alfred.fragments;

import android.util.Pair;

import com.ualberta.cs.alfred.Request;
import com.ualberta.cs.alfred.RequestESGetController;
import com.ualberta.cs.alfred.RequestList;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by mmcote on 2016-11-17.
 */

public class RequestFragmentsListController {
    public RequestList getRequestList(List<Pair<String, String>> queries, String userID) {
        /* The request that should be retrieved are all requests that are currently with a requested status and those that
        are pending that do not include the driver on the bidlist of the request.
         */
        RequestList requestedList = new RequestList();
        try {
            for (Pair<String, String> query : queries) {
                RequestESGetController.GetRequestTask getRequested = new RequestESGetController.GetRequestTask();
                getRequested.execute(queries.get(0).first, queries.get(0).second);
                List<Request> requested = getRequested.get();
                requestedList.addMultipleRequest(requested);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return requestedList;
    }
}
