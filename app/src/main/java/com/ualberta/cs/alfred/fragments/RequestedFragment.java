package com.ualberta.cs.alfred.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
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
        requestAdapter.addAll(getRequestList());
        requestAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPause() {
        super.onPause();
        System.out.println("In onPause");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_requested,container,false);

        Bundle bundle = this.getArguments();

        requestedListView = (ListView) view.findViewById(R.id.requestedListView);
        ArrayList<Request> openRequestList = getRequestList();

        requestAdapter = new ArrayAdapter<Request>(view.getContext(), R.layout.custom_row, openRequestList);
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


    private ArrayList<Request> getRequestList() {
        RequestElasticSearchController.GetRequestTask getRequestTask = new RequestElasticSearchController.GetRequestTask();
        ArrayList<Request> openRequestList = null;

//        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);

        try {
            getRequestTask.execute("riderID", "rider013");
            openRequestList = (ArrayList<Request>) new RequestList(getRequestTask.get()).getSpecificRequestList("Accepted");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return openRequestList;
    }

}
