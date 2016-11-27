package com.ualberta.cs.alfred.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
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
import android.widget.Button;
import android.widget.ListView;


import com.ualberta.cs.alfred.ConnectivityChecker;
import com.ualberta.cs.alfred.LocalDataManager;
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
        AcceptedFragment acceptedFragment = new AcceptedFragment();
        return acceptedFragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        View view = getView();
        // change button colors
//        Button requestedBtn = (Button) view.findViewById(R.id.button_requested);
//        Button pendingBtn = (Button) view.findViewById(R.id.button_pending);
//        Button acceptedBtn = (Button) view.findViewById(R.id.button_accepted);
//        requestedBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
//        Drawable border = getResources().getDrawable(R.drawable.button_border);
//        acceptedBtn.setBackground(border);
//        pendingBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

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
        //determine if there is connectivity. If there is, save the data for future use
        //if not, load from a previoiusly saved image
        if (ConnectivityChecker.isConnected(getContext())){
            LocalDataManager.saveRAcceptedList(acceptedRequestList,preferences.getString("MODE",null),getContext());

            requestAdapter = new ArrayAdapter<>(view.getContext(), R.layout.custom_row, acceptedRequestList);
            acceptedListView.setAdapter(requestAdapter);
        }
        else{
            acceptedRequestList = LocalDataManager.loadRAcceptedList(preferences.getString("MODE", null), getContext());
            requestAdapter = new ArrayAdapter<>(view.getContext(), R.layout.custom_row, acceptedRequestList);
            acceptedListView.setAdapter(requestAdapter);

        }

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
