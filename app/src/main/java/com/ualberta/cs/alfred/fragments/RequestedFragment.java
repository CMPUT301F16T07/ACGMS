package com.ualberta.cs.alfred.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.ualberta.cs.alfred.R;
import com.ualberta.cs.alfred.Request;
import com.ualberta.cs.alfred.RequestDetailsActivity;
import com.ualberta.cs.alfred.RequestESGetController;
import com.ualberta.cs.alfred.RequestList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by carlcastello on 09/11/16.
 */

public class RequestedFragment extends Fragment {
    private ArrayAdapter<Request> requestAdapter;
    private ListView requestedListView;
    private SharedPreferences preferences;
    private RequestFragmentsListController rFLC;
    private List<Pair<String, String>> listNeeded;
    private String userName;

    public RequestedFragment() {
        this.rFLC = new RequestFragmentsListController();
        this.listNeeded = null;
        this.requestAdapter = null;
        this.requestedListView = null;
        this.preferences = null;
        this.userName = null;
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
        updateRequestList();
    }

    public void updateRequestList() {
        requestAdapter.clear();
        List returned;
        if (preferences.getString("MODE", null).contentEquals("Driver Mode")) {
            returned = rFLC.getRequestList(listNeeded, userName).removeDriver(userName);
            requestAdapter.addAll(returned);
        } else {
            returned = rFLC.getRequestList(listNeeded, userName).getSpecificRequestList("Requested");
            requestAdapter.addAll(returned);
        }
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Requested", Integer.toString(returned.size()));
        editor.commit();

        ListFragment.update(getContext());
        requestAdapter.notifyDataSetChanged();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_requested,container,false);
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        userName = preferences.getString("USERNAME", null);

        ArrayList<Request> requestedList;
        if (preferences.getString("MODE", null).contentEquals("Driver Mode")) {
            this.listNeeded = Arrays.asList(new Pair<String, String>("requestStatus", "Requested"),
                    new Pair<String, String>("requestStatus", "Pending"));
            requestedList = rFLC.getRequestList(listNeeded, userName).removeDriver(userName);
        } else {
            this.listNeeded = Arrays.asList(new Pair<String, String>("riderID", userName));
            requestedList = (ArrayList<Request>) rFLC.getRequestList(listNeeded, userName).getSpecificRequestList("Requested");
        }
        requestAdapter = new ArrayAdapter<>(view.getContext(), R.layout.custom_row, requestedList);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Requested", Integer.toString(requestedList.size()));
        editor.commit();

        requestedListView = (ListView) view.findViewById(R.id.requestedListView);
        requestedListView.setAdapter(requestAdapter);

        requestedListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Request r = (Request) requestedListView.getItemAtPosition(position);
                Intent intent = new Intent(getActivity(), RequestDetailsActivity.class);
                intent.putExtra("passedRequest",r);
                startActivity(intent);
            }
        });

        return view;
    }
}
