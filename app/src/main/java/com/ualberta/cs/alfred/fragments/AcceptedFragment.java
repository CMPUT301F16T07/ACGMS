package com.ualberta.cs.alfred.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.ualberta.cs.alfred.R;
import com.ualberta.cs.alfred.Request;
import com.ualberta.cs.alfred.RequestDetails;
import com.ualberta.cs.alfred.RequestElasticSearchController;
import com.ualberta.cs.alfred.RequestList;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by carlcastello on 09/11/16.
 */

public class AcceptedFragment extends Fragment {
    private ArrayAdapter<Request> requestAdapter;
    private ListView pendingListView;
    private SharedPreferences preferences;

    public AcceptedFragment() {
    }

    public static AcceptedFragment newInstance() {
        Bundle args = new Bundle();
        AcceptedFragment acceptedFragment = new AcceptedFragment();
        acceptedFragment.setArguments(args);
        return acceptedFragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        requestAdapter.clear();
        if (preferences.getString("MODE", null).contentEquals("Driver Mode")) {
            requestAdapter.addAll(getDriverAcceptedList());
        } else {
            requestAdapter.addAll(getRiderAcceptedList());
        }
        requestAdapter.notifyDataSetChanged();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_accepted,container,false);
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        final ListView acceptedListView = (ListView) view.findViewById(R.id.acceptedListView);


        RequestElasticSearchController.GetRequestTask getRequestTask = new RequestElasticSearchController.GetRequestTask();
        ArrayList<Request> acceptedList = null;

        if (preferences.getString("MODE", null).contentEquals("Driver Mode")) {
            acceptedList = getDriverAcceptedList();
            requestAdapter = new ArrayAdapter<>(view.getContext(), R.layout.custom_row, acceptedList);
        } else {
            acceptedList = getRiderAcceptedList();
            requestAdapter = new ArrayAdapter<>(view.getContext(), R.layout.custom_row, acceptedList);
        }

        //update accepted request count
        HomeFragment.acceptedCount=acceptedList.size();

        acceptedListView.setAdapter(requestAdapter);

        acceptedListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Request r = (Request) acceptedListView.getItemAtPosition(position);
                Intent intent = new Intent(getActivity(), RequestDetails.class);
                intent.putExtra("passedRequest",r);
                startActivity(intent);
            }
        });

        return view;
    }


    private ArrayList<Request> getRiderAcceptedList() {
        RequestElasticSearchController.GetRequestTask getRequestTask = new RequestElasticSearchController.GetRequestTask();
        ArrayList<Request> acceptedList = null;

        try {
            getRequestTask.execute("riderID", preferences.getString("USERNAME", null));
            acceptedList = (ArrayList<Request>) new RequestList(getRequestTask.get()).getSpecificRequestList("Accepted");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return acceptedList;
    }

    private ArrayList<Request> getDriverAcceptedList() {
        /* The request that should be selected is the requests that are accepted with only the driver left in the bidList
         */
        RequestElasticSearchController.GetRequestTask getRequestTask = new RequestElasticSearchController.GetRequestTask();
        RequestList acceptedList = null;

        try {
            getRequestTask.execute("requestStatus", "Accepted");
            acceptedList = new RequestList(getRequestTask.get()).getWithDriver(preferences.getString("USERNAME", null));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return acceptedList.returnArrayList();
    }
}
