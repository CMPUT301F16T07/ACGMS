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


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by carlcastello on 09/11/16.
 */

public class AcceptedFragment extends Fragment {
    private ArrayAdapter<Request> requestAdapter;
    private ListView acceptedListView;
    private SharedPreferences preferences;
    private RequestFragmentsListController rFLC;
    private List<Pair<String, String>> listNeeded;
    private String userID;

    public AcceptedFragment() {
        this.rFLC = new RequestFragmentsListController();
        this.listNeeded = null;
        this.requestAdapter = null;
        this.acceptedListView = null;
        this.preferences = null;
        this.userID = null;
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
        View view = getView();

        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        userID = preferences.getString("USERID", null);
        acceptedListView = (ListView) view.findViewById(R.id.acceptedListView);
        ArrayList<Request> acceptedRequestList;

        if (preferences.getString("MODE", null).contentEquals("Driver Mode")) {
            this.listNeeded = Arrays.asList(new Pair<String, String>("requestStatus", "Accepted"));
            acceptedRequestList = rFLC.getRequestList(listNeeded).getWithDriver(userID);
        } else {
            this.listNeeded = Arrays.asList(new Pair<String, String>("riderID", userID));
            acceptedRequestList = (ArrayList<Request>) rFLC.getRequestList(listNeeded).getSpecificRequestList("Accepted");
        }
        requestAdapter = new ArrayAdapter<>(view.getContext(), R.layout.custom_row, acceptedRequestList);
        acceptedListView.setAdapter(requestAdapter);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Accepted", Integer.toString(acceptedRequestList.size()));
        editor.commit();
        ListFragment.update(getContext());

        acceptedListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Request r = (Request) acceptedListView.getItemAtPosition(position);
                Intent intent = new Intent(getActivity(), RequestDetailsActivity.class);
                intent.putExtra("passedRequest",r);
                intent.putExtra("FROM", "Accepted");
                startActivity(intent);
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_accepted,container,false);
        return view;
    }
}
