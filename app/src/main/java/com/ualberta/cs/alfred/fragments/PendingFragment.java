package com.ualberta.cs.alfred.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

public class PendingFragment extends Fragment {
    public PendingFragment() {
    }

    public static PendingFragment newInstance() {
        Bundle args = new Bundle();
        PendingFragment pendingFragment = new PendingFragment();
        pendingFragment.setArguments(args);
        return pendingFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pending,container,false);
        Bundle bundle = this.getArguments();

        final ListView pendingListView = (ListView) view.findViewById(R.id.pendingListView);

        RequestElasticSearchController.GetRequestTask getRequestTask = new RequestElasticSearchController.GetRequestTask();
        ArrayList<Request> openPendingList = null;

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);

        try {
            getRequestTask.execute("riderID", "rider002");
            openPendingList = (ArrayList<Request>) new RequestList(getRequestTask.get()).getSpecificRequestList("Pending");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    /*    ArrayList<String> openRequests = new ArrayList<String>();
        String temp;
        for (Request request : openRequestList) {
            openRequests.add(request.getRequestID());
        }*/

        ArrayAdapter<Request> requestAdapter = new ArrayAdapter<Request>(view.getContext(), R.layout.custom_row, openPendingList);
        pendingListView.setAdapter(requestAdapter);

        pendingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Request r = (Request) pendingListView.getItemAtPosition(position);
                Intent intent = new Intent(getActivity(), RequestDetails.class);
                intent.putExtra("passedRequest",r);
                startActivity(intent);
            }
        });


        return view;
    }

}
