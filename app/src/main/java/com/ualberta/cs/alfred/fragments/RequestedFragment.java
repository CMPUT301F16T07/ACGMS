package com.ualberta.cs.alfred.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ualberta.cs.alfred.R;
import com.ualberta.cs.alfred.Request;
import com.ualberta.cs.alfred.RequestDetails;
import com.ualberta.cs.alfred.RequestElasticSearchController;
import com.ualberta.cs.alfred.RequestList;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by carlcastello on 09/11/16.
 */

public class RequestedFragment extends Fragment {
    private ArrayAdapter<Request> requestAdapter;
    private ListView requestedListView;
    private SharedPreferences preferences;
    public RequestedFragment() {
    }

    public static RequestedFragment newInstance() {
        Bundle args = new Bundle();
        RequestedFragment requestedFragment = new RequestedFragment();
        requestedFragment.setArguments(args);
        return requestedFragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        requestAdapter.clear();
        if (preferences.getString("MODE", null).contentEquals("Driver Mode")) {
            requestAdapter.addAll(getDriverRequestedList());
        } else {
            requestAdapter.addAll(getRiderRequestedList());
        }
        requestAdapter.notifyDataSetChanged();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_requested,container,false);

        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        ArrayList<Request> requestedList;

        if (preferences.getString("MODE", null).contentEquals("Driver Mode")) {
            requestedList = getDriverRequestedList();
            requestAdapter = new ArrayAdapter<>(view.getContext(), R.layout.custom_row, requestedList);
        } else {
            requestedList = getRiderRequestedList();
            requestAdapter = new ArrayAdapter<>(view.getContext(), R.layout.custom_row, requestedList);
        }

        requestedListView = (ListView) view.findViewById(R.id.requestedListView);
        requestedListView.setAdapter(requestAdapter);


        requestedListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Request r = (Request) requestedListView.getItemAtPosition(position);
                Intent intent = new Intent(getActivity(), RequestDetails.class);
                intent.putExtra("passedRequest",r);
                startActivity(intent);
            }
        });

        return view;
    }

    private ArrayList<Request> getRiderRequestedList() {
        RequestElasticSearchController.GetRequestTask getRequestTask = new RequestElasticSearchController.GetRequestTask();
        ArrayList<Request> requestedList = null;

        try {
            getRequestTask.execute("riderID", preferences.getString("USERNAME", null));
            requestedList = (ArrayList<Request>) new RequestList(getRequestTask.get()).getSpecificRequestList("Requested");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return requestedList;
    }

    private ArrayList<Request> getDriverRequestedList() {
        /* The request that should be retrieved are all requests that are currently with a requested status and those that
        are pending that do not include the driver on the bidlist of the request.
         */
        RequestElasticSearchController.GetRequestTask getRequestTask = new RequestElasticSearchController.GetRequestTask();
        RequestList requestedList = null;

        try {
            getRequestTask.execute("requestStatus", "Pending");
            requestedList = new RequestList(getRequestTask.get()).removeDriver(preferences.getString("USERNAME", null));
            getRequestTask.execute("requestStatus", "Requested");
            requestedList.addMultipleRequest(getRequestTask.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return requestedList.returnArrayList();
    }

}
