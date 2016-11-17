package com.ualberta.cs.alfred.fragments;

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
import com.ualberta.cs.alfred.RequestESGetController;
import com.ualberta.cs.alfred.RequestDetailsActivity;

import com.ualberta.cs.alfred.RequestList;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by carlcastello on 09/11/16.
 */

public class PendingFragment extends Fragment {
    private ArrayAdapter<Request> requestAdapter;
    private ListView pendingListView;
    private SharedPreferences preferences;

    public PendingFragment() {
    }

    public static PendingFragment newInstance() {
        Bundle args = new Bundle();
        PendingFragment pendingFragment = new PendingFragment();
        pendingFragment.setArguments(args);
        return pendingFragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        requestAdapter.clear();
        if (preferences.getString("MODE", null).contentEquals("Driver Mode")) {
            requestAdapter.addAll(getDriverPendingList());
        } else {
            requestAdapter.addAll(getRiderPendingList());
        }
        requestAdapter.notifyDataSetChanged();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pending,container,false);
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());


        pendingListView = (ListView) view.findViewById(R.id.pendingListView);

        RequestESGetController.GetRequestTask getRequestTask = new RequestESGetController.GetRequestTask();
        ArrayList<Request> pendingList = null;

        if (preferences.getString("MODE", null).contentEquals("Driver Mode")) {
            pendingList = getDriverPendingList();
            requestAdapter = new ArrayAdapter<>(view.getContext(), R.layout.custom_row, pendingList);
        } else {
            pendingList = getRiderPendingList();
            requestAdapter = new ArrayAdapter<>(view.getContext(), R.layout.custom_row, pendingList);
        }

        //update pending request count
        HomeFragment.pendingCount=pendingList.size();

        pendingListView.setAdapter(requestAdapter);

        pendingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Request r = (Request) pendingListView.getItemAtPosition(position);
                Intent intent = new Intent(getActivity(), RequestDetailsActivity.class);
                intent.putExtra("passedRequest",r);
                startActivity(intent);
            }
        });


        return view;
    }

    private ArrayList<Request> getRiderPendingList() {
        RequestESGetController.GetRequestTask getRequestTask = new RequestESGetController.GetRequestTask();
        ArrayList<Request> pendingList = null;

        try {
            getRequestTask.execute("riderID", preferences.getString("USERNAME", null));
//            getRequestTask.execute("riderID", "rider011");

            pendingList = (ArrayList<Request>) new RequestList(getRequestTask.get()).getSpecificRequestList("Pending");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return pendingList;
    }

    private ArrayList<Request> getDriverPendingList() {
        /* The request that should be retrieved are all requests that are currently with a requested status and those that
        are pending that do not include the driver on the bidlist of the request.
         */
        RequestESGetController.GetRequestTask getRequestTask = new RequestESGetController.GetRequestTask();
        RequestList pendingList = null;

        try {
            getRequestTask.execute("requestStatus", "Pending");
            pendingList = new RequestList(getRequestTask.get()).getWithDriver(preferences.getString("USERNAME", null));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return pendingList.returnArrayList();
    }
}
