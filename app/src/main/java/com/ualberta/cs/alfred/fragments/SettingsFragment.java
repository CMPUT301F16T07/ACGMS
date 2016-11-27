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

import com.ualberta.cs.alfred.ConnectivityChecker;
import com.ualberta.cs.alfred.LocalDataManager;
import com.ualberta.cs.alfred.R;
import com.ualberta.cs.alfred.Request;
import com.ualberta.cs.alfred.RequestDetailsActivity;
import com.ualberta.cs.alfred.RequestList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static com.ualberta.cs.alfred.R.id.acceptedListView;

/**
 * Created by carlcastello on 08/11/16.
 */

public class SettingsFragment extends Fragment {
    private ArrayAdapter<Request> completedRequestAdapter;
    private ListView completedRequestListView;
    private SharedPreferences preferences;
    private RequestFragmentsListController rFLC;
    private List<Pair<String, String>> listNeeded;
    private String userID;

    public SettingsFragment() {
        this.rFLC = new RequestFragmentsListController();
        this.listNeeded = null;
        this.completedRequestAdapter = null;
        this.completedRequestListView = null;
        this.preferences = null;
        this.userID = null;
    }

    public static SettingsFragment newInstance() {
        SettingsFragment settingsFragment = new SettingsFragment();
        return settingsFragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        View view = getView();

        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        userID = preferences.getString("USERID", null);
        completedRequestListView = (ListView) view.findViewById(R.id.completedRequestListView);
        ArrayList<Request> completedRequestList;

        if (preferences.getString("MODE", null).contentEquals("Driver Mode")) {
            this.listNeeded = Arrays.asList(new Pair<String, String>("requestStatus", "Awaiting Payment"),
                    new Pair<String, String>("requestStatus", "Completed"));
            completedRequestList = rFLC.getRequestList(listNeeded).getWithDriverAsSelected(userID);
        } else {
            this.listNeeded = Arrays.asList(new Pair<String, String>("riderID", userID));
            RequestList requests = rFLC.getRequestList(listNeeded);
            completedRequestList = requests.getSpecificRequestList("Awaiting Payment");
            completedRequestList.addAll(requests.getSpecificRequestList("Completed"));
        }
        //determine if there is connectivity. If there is, save the data for future use
        //if not, load from a previoiusly saved image
//        if (ConnectivityChecker.isConnected(getContext())){
//            LocalDataManager.saveRAcceptedList(acceptedRequestList,preferences.getString("MODE",null),getContext());

            completedRequestAdapter = new ArrayAdapter<Request>(view.getContext(), R.layout.custom_row, completedRequestList);
            completedRequestListView.setAdapter(completedRequestAdapter);
//        }
//        else{
//            acceptedRequestList = LocalDataManager.loadRAcceptedList(preferences.getString("MODE", null), getContext());
//            requestAdapter = new ArrayAdapter<>(view.getContext(), R.layout.custom_row, acceptedRequestList);
//            acceptedListView.setAdapter(requestAdapter);
//
//        }

        ListFragment.update(getContext());

        completedRequestListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Request r = (Request) completedRequestListView.getItemAtPosition(position);
                Intent intent = new Intent(getActivity(), RequestDetailsActivity.class);
                intent.putExtra("passedRequest",r);
                intent.putExtra("FROM", "Completed");
                startActivity(intent);
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings,container,false);
        return view;
    }
}
