package com.ualberta.cs.alfred.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ualberta.cs.alfred.R;
import com.ualberta.cs.alfred.Rider;
import com.ualberta.cs.alfred.UserElasticSearchController;

import java.util.concurrent.ExecutionException;

/**
 * Created by carlcastello on 08/11/16.
 */

public class UserFragment extends Fragment {

    Rider rider;
    private TextView textView;
    private String userName;
    private String fullName;
    private String emailAddress;
    private String phoneNumber;

    public UserFragment() {
    }

    public static UserFragment newInstance() {
        Bundle args = new Bundle();
        UserFragment userFragment = new UserFragment();
        userFragment.setArguments(args);
        return userFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        userName = preferences.getString("USERNAME", null);

        UserElasticSearchController.GetRider getRider = new UserElasticSearchController.GetRider();

        try {
            rider = getRider.execute(userName).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        fullName = rider.getFirstName() + " " + rider.getLastName();
        emailAddress = rider.getEmail();
        phoneNumber= rider.getPhoneNumber();

        textView = (TextView) view.findViewById(R.id.textView2);
        textView.setText(userName);

        textView = (TextView) view.findViewById(R.id.textView4);
        textView.setText(fullName);

        textView = (TextView) view.findViewById(R.id.textView6);
        textView.setText(emailAddress);

        textView = (TextView) view.findViewById(R.id.textView8);
        textView.setText(phoneNumber);


        return view;
    }
}
