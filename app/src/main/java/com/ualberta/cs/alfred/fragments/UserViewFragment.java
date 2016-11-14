package com.ualberta.cs.alfred.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ualberta.cs.alfred.R;
import com.ualberta.cs.alfred.Rider;
import com.ualberta.cs.alfred.UserElasticSearchController;

import java.util.concurrent.ExecutionException;

/**
 * Created by carlcastello on 08/11/16.
 */

public class UserViewFragment extends Fragment implements View.OnClickListener {

    Rider rider;
    private TextView textView;
    private String userName;
    private String fullName;
    private String emailAddress;
    private String phoneNumber;

    private FragmentTransaction transaction;

    public UserViewFragment() {
    }

    public static UserViewFragment newInstance() {
        Bundle args = new Bundle();
        UserViewFragment userViewFragment = new UserViewFragment();
        userViewFragment.setArguments(args);
        return userViewFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_view, container, false);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        userName = preferences.getString("USERNAME", null);


        //retrieving rider's informatino from elasticsearch
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


        //setting the textviews to what we want to show
        textView = (TextView) view.findViewById(R.id.textview2);
        textView.setText(userName);

        textView = (TextView) view.findViewById(R.id.textview4);
        textView.setText(fullName);

        textView = (TextView) view.findViewById(R.id.textview6);
        textView.setText(emailAddress);

        textView = (TextView) view.findViewById(R.id.textview8);
        textView.setText(phoneNumber);

        Button editButton = (Button) view.findViewById(R.id.edit_user_button);
        editButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_user_button:
                Fragment fragment = new UserEditFragment().newInstance();
                replaceFragmentwithStack(fragment);
                break;
        }
    }

    private void replaceFragmentwithStack(Fragment fragment) {
        transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.user_fragment_container, fragment);
        transaction.commit();
    }
}
