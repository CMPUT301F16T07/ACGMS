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
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.Toast;

import com.ualberta.cs.alfred.ConnectivityChecker;
import com.ualberta.cs.alfred.LocalDataManager;
import com.ualberta.cs.alfred.R;
import com.ualberta.cs.alfred.Request;
import com.ualberta.cs.alfred.RequestDetailsActivity;
import com.ualberta.cs.alfred.RequestESGetController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by carlcastello on 09/11/16.
 */

public class RequestedFragment extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private ArrayAdapter<Request> requestAdapter;
    private ListView requestedListView;
    private SharedPreferences preferences;
    private RequestFragmentsListController rFLC;
    private List<Pair<String, String>> listNeeded;
    private String userID;


    private Button button1;
    private TableLayout tableLayout;
    private RelativeLayout.LayoutParams params;

    private EditText editText;

    // Custom Check View
    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;
    private RadioButton rb4;

    private int searchType = R.id.radioButtonKeyword;

    public RequestedFragment() {
        this.rFLC = new RequestFragmentsListController();
        this.listNeeded = null;
        this.requestAdapter = null;
        this.requestedListView = null;
        this.preferences = null;
        this.userID = null;
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
        View view = getView();

        userID = preferences.getString("USERID", null);

        ArrayList<Request> requestedList;
        if (preferences.getString("MODE", null).contentEquals("Driver Mode")) {
            this.listNeeded = Arrays.asList(new Pair<String, String>("requestStatus", "Pending"),
                    new Pair<String, String>("requestStatus", "Requested"));
            requestedList = rFLC.getRequestList(Arrays.asList(listNeeded.get(0))).removeDriver(userID);
            requestedList.addAll(rFLC.getRequestList(Arrays.asList(listNeeded.get(1))).returnArrayList());
        } else {
            this.listNeeded = Arrays.asList(new Pair<String, String>("riderID", userID));
            requestedList = (ArrayList<Request>) rFLC.getRequestList(listNeeded).getSpecificRequestList("Requested");
        }

        //determine if there is connectivity. If there is, save the data for future use
        //if not, load from a previoiusly saved image
        if (ConnectivityChecker.isConnected(getContext())){

            LocalDataManager.saveRRequestList(requestedList,preferences.getString("MODE", null),getContext());

            requestAdapter = new ArrayAdapter<>(view.getContext(), R.layout.custom_row, requestedList);
            requestedListView.setAdapter(requestAdapter);
        }
        else{
            requestedList = LocalDataManager.loadRRequestList(preferences.getString("MODE", null), getContext());
            requestAdapter = new ArrayAdapter<>(view.getContext(), R.layout.custom_row, requestedList);
            requestedListView.setAdapter(requestAdapter);
        }

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Requested", Integer.toString(requestedList.size()));
        editor.commit();
        ListFragment.update(getContext());

        requestedListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Request r = (Request) requestedListView.getItemAtPosition(position);
                Intent intent = new Intent(getActivity(), RequestDetailsActivity.class);
                intent.putExtra("passedRequest",r);
                intent.putExtra("FROM", "Requested");
                startActivity(intent);
                updateRequestList();
            }
        });
    }

    public void updateRequestList() {
        requestAdapter.clear();
        List returned;
        if (preferences.getString("MODE", null).contentEquals("Driver Mode")) {
            returned = rFLC.getRequestList(Arrays.asList(listNeeded.get(0))).removeDriver(userID);
            returned.addAll(rFLC.getRequestList(Arrays.asList(listNeeded.get(1))).returnArrayList());
            requestAdapter.addAll(returned);
        } else {
            returned = rFLC.getRequestList(listNeeded).getSpecificRequestList("Requested");
            requestAdapter.addAll(returned);
        }
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Requested", Integer.toString(returned.size()));
        editor.commit();

        requestAdapter.notifyDataSetChanged();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_requested,container,false);

        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        requestedListView = (ListView) view.findViewById(R.id.requestedListView);

        tableLayout = (TableLayout) view.findViewById(R.id.filter_table);
        tableLayout.setVisibility(View.GONE);

        button1 = (Button) view.findViewById(R.id.show_filter);
        if (preferences.getString("MODE", null).contentEquals("Driver Mode")) {
            button1.setOnClickListener(this);
        } else {
            button1.setVisibility(View.GONE);
        }

        Button button2 = (Button) view.findViewById(R.id.request_cancel_button);
        button2.setOnClickListener(this);

        Button button3 = (Button) view.findViewById(R.id.request_done_button);
        button3.setOnClickListener(this);

        editText = (EditText) view.findViewById(R.id.filter_input);

        rb1 = (RadioButton) view.findViewById(R.id.radioButtonKeyword);
        rb2 = (RadioButton) view.findViewById(R.id.radioButtonAddress);
        rb3 = (RadioButton) view.findViewById(R.id.radioButtonCoordinates);
        rb4 = (RadioButton) view.findViewById(R.id.radioButtonPrice);

        rb1.setOnCheckedChangeListener(this);
        rb2.setOnCheckedChangeListener(this);
        rb3.setOnCheckedChangeListener(this);
        rb4.setOnCheckedChangeListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.show_filter:
                button1.setVisibility(View.GONE);
                tableLayout.setVisibility(View.VISIBLE);
                params = (RelativeLayout.LayoutParams) requestedListView.getLayoutParams();
                params.addRule(RelativeLayout.ABOVE,R.id.filter_table);
                break;
            case R.id.request_cancel_button:
                button1.setVisibility(View.VISIBLE);
                tableLayout.setVisibility(View.GONE);
                params = (RelativeLayout.LayoutParams) requestedListView.getLayoutParams();
                params.addRule(RelativeLayout.ABOVE,R.id.show_filter);
                break;
            case R.id.request_done_button:
                button1.setVisibility(View.VISIBLE);
                tableLayout.setVisibility(View.GONE);
                params = (RelativeLayout.LayoutParams) requestedListView.getLayoutParams();
                params.addRule(RelativeLayout.ABOVE,R.id.show_filter);

                ArrayList<Request> requests = new ArrayList<>();

                //Todo Search elastic Search by the given filter
                RequestESGetController.GetRequestByMultiplePreferencesTask retrievedRequest =
                        new RequestESGetController.GetRequestByMultiplePreferencesTask();

                String filter = editText.getText().toString();

                Toast.makeText(getContext(),filter,Toast.LENGTH_SHORT).show();

                switch (searchType){
                    case R.id.radioButtonKeyword:
                        // Todo do some querry with Keywords
                        retrievedRequest.execute(
                                "requestStatus", "string", "Pending",
                                "_all", "string", filter
                        );
                        break;
                    case R.id.radioButtonAddress:
                        // Todo do some querry with Keyword - Address
                        retrievedRequest.execute(
                                "requestStatus", "string", "Pending",
                                "_all", "string", filter
                        );
                        break;
                    case R.id.radioButtonCoordinates:
                        // Todo do some querry with Keywords - Coordinates
                        retrievedRequest.execute(
                                "requestStatus", "string", "Pending",
                                "_all", "string", filter
                        );
                        break;
                    case R.id.radioButtonPrice:
                        // Todo do some querry with Price
                        break;
                }
                try {
                    requests = retrievedRequest.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                //determine if there is connectivity. If there is, save the data for future use
                //if not, load from a previoiusly saved image
                if (ConnectivityChecker.isConnected(getContext())){
                    LocalDataManager.saveRRequestList(requests,preferences.getString("MODE",null),getContext());

                    requestAdapter = new ArrayAdapter<>(v.getContext(), R.layout.custom_row, requests);
                    requestedListView.setAdapter(requestAdapter);
                }
                else{
                    requests = LocalDataManager.loadRRequestList(preferences.getString("MODE", null), getContext());
                    requestAdapter = new ArrayAdapter<>(v.getContext(), R.layout.custom_row, requests);
                    requestedListView.setAdapter(requestAdapter);

                }
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("Requested", Integer.toString(requests.size()));
                editor.commit();
                requestAdapter.notifyDataSetChanged();
                break;
        }

    }

    // Custom Radio button
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        EditText editText = (EditText) getView().findViewById(R.id.filter_input);
        if (isChecked) {
            switch (buttonView.getId()) {
                case R.id.radioButtonKeyword:
                    editText.setHint(R.string.search_keyword_text);
                    rb2.setChecked(false);
                    rb3.setChecked(false);
                    rb4.setChecked(false);
                    searchType = R.id.radioButtonKeyword;
                    break;
                case R.id.radioButtonAddress:
                    editText.setHint(R.string.search_address_text);
                    rb1.setChecked(false);
                    rb3.setChecked(false);
                    rb4.setChecked(false);
                    searchType = R.id.radioButtonAddress;
                    break;
                case R.id.radioButtonCoordinates:
                    editText.setHint(R.string.search_coordinate_text);
                    rb1.setChecked(false);
                    rb2.setChecked(false);
                    rb4.setChecked(false);
                    searchType = R.id.radioButtonCoordinates;
                    break;
                case R.id.radioButtonPrice:
                    editText.setHint(R.string.search_price);
                    rb1.setChecked(false);
                    rb2.setChecked(false);
                    rb3.setChecked(false);
                    searchType = R.id.radioButtonPrice;
                    break;
            }
        }
    }
}
