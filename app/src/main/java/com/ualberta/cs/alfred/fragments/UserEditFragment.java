package com.ualberta.cs.alfred.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ualberta.cs.alfred.R;
import com.ualberta.cs.alfred.Rider;
import com.ualberta.cs.alfred.User;
import com.ualberta.cs.alfred.UserElasticSearchController;

import java.util.concurrent.ExecutionException;

/**
 * Created by carlcastello on 13/11/16.
 */

public class UserEditFragment extends Fragment implements View.OnClickListener {

    User user;

    private String userName;
    private String emailAddress;
    private String phoneNumber;
    private String firstName;
    private String lastName;

    private EditText userNameEdit;
    private EditText firstNameEdit;
    private EditText lastNameEdit;
    private EditText emailEdit;
    private EditText phoneEdit;

    private SharedPreferences preferences;
    private  FragmentTransaction transaction;

    public UserEditFragment() {
    }

    public static UserEditFragment newInstance() {
        Bundle args = new Bundle();
        UserEditFragment userEditFragment = new UserEditFragment();
        userEditFragment.setArguments(args);
        return userEditFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_edit, container, false);


        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        userName = preferences.getString("USERNAME", null);


        //retrieving rider's informatino from elasticsearch
        UserElasticSearchController.GetRider getRider = new UserElasticSearchController.GetRider();
        try {
            user = getRider.execute(userName).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        firstName = user.getFirstName();
        lastName = user.getLastName();
        emailAddress = user.getEmail();
        phoneNumber= user.getPhoneNumber();

        // Set input text to the user's information
        userNameEdit = (EditText) view.findViewById(R.id.edit_username_input);
        firstNameEdit = (EditText) view.findViewById(R.id.edit_firstname_input);
        lastNameEdit = (EditText) view.findViewById(R.id.edit_lastname_input);
        emailEdit = (EditText) view.findViewById(R.id.edit_email_input);
        phoneEdit = (EditText) view.findViewById(R.id.edit_phone_input);

        userNameEdit.setText(userName);
        firstNameEdit.setText(firstName);
        lastNameEdit.setText(lastName);
        emailEdit.setText(emailAddress);
        phoneEdit.setText(phoneNumber);



        Button editButton = (Button) view.findViewById(R.id.done_editUser_button);
        editButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.done_editUser_button:
                //FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                //fragmentManager.popBackStackImmediate();


                // Get new information from the input box.
                if (userNameEdit.getText() != null) {
                    userName = userNameEdit.getText().toString();
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("USERNAME",userName);
                    editor.commit();
                }
                if (firstNameEdit.getText() != null) {
                    firstName = firstNameEdit.getText().toString();
                }
                if (lastNameEdit.getText() != null) {
                    lastName = lastNameEdit.getText().toString();
                }
                if (phoneEdit.getText() != null) {
                    phoneNumber = phoneEdit.getText().toString();
                }
                if (emailEdit.getText() != null) {
                    emailAddress = emailEdit.getText().toString();
                }



                Fragment fragment = new UserViewFragment().newInstance();
                replaceFragmentwithoutStack(fragment);
                break;
        }
    }

    private void replaceFragmentwithoutStack(Fragment fragment) {
        transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.user_fragment_container, fragment);
        transaction.commit();
    }

}
