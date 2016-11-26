package com.ualberta.cs.alfred.fragments;

import android.content.Intent;
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
import com.ualberta.cs.alfred.RequestDetailsActivity;
import com.ualberta.cs.alfred.SendEmailActivity;
import com.ualberta.cs.alfred.User;
import com.ualberta.cs.alfred.UserESGetController;

import java.util.Date;
import java.util.concurrent.ExecutionException;

/**
 * Created by carlcastello on 08/11/16.
 */

public class UserViewFragment extends Fragment implements View.OnClickListener {

    private FragmentTransaction transaction;
    private String emailAddress;
    private String phoneNumber;

    public UserViewFragment() {
    }

    public static UserViewFragment newInstance(int position, String userID) {
        Bundle args = new Bundle();
        args.putInt("index",position);
        args.putString("userID", userID);
        UserViewFragment userViewFragment = new UserViewFragment();
        userViewFragment.setArguments(args);
        return userViewFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_view, container, false);

        String userName = "";
        String userMode = "";

        int position = 0;
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            position = bundle.getInt("index", 0);
        }
        if (position == 0) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            userName = preferences.getString("USERNAME", null);
            userMode = preferences.getString("MODE", "None");
        } else {
            // Todo access the username of the person that click the driver profile.
            userName = bundle.getString("userID");
            userMode = "Driver";
        }

        User user = new User("","","",new Date(),"","");

        try {
            //retrieving rider's information from elasticsearch
            UserESGetController.GetUserTask getUser = new UserESGetController.GetUserTask();
            user = getUser.execute(userName).get();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        String fullName = user.getFirstName() + " " + user.getLastName();
        emailAddress = user.getEmail();
        phoneNumber= user.getPhoneNumber();


        //setting the textviews to what we want to show
        TextView textView = (TextView) view.findViewById(R.id.edit_username_input);
        if (position == 0) {
            textView.setText(userName + " (" + userMode + ")");
        } else {
            textView.setText(userName);
        }

        textView = (TextView) view.findViewById(R.id.edit_firstname_input);
        textView.setText(fullName);

        textView = (TextView) view.findViewById(R.id.edit_email_input);
        textView.setText(emailAddress);

        textView = (TextView) view.findViewById(R.id.textview8);
        textView.setText(phoneNumber);

        Button emailButton = (Button) view.findViewById(R.id.email_user_button);
        emailButton.setOnClickListener(this);
        Button callButton = (Button) view.findViewById(R.id.call_user_button);
        callButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.email_user_button:
                //start email activity to send email
                Intent intent = new Intent(getActivity(), SendEmailActivity.class);
                intent.putExtra("to",emailAddress);
                startActivity(intent);
                break;
            case R.id.call_user_button:
                //start call
                break;
        }

    }


    private void replaceFragmentwithStack(Fragment fragment) {
        transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.user_fragment_container, fragment);
        transaction.commit();
    }
}
