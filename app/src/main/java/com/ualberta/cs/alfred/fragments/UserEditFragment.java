package com.ualberta.cs.alfred.fragments;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ualberta.cs.alfred.R;
import com.ualberta.cs.alfred.RequestDetailsActivity;
import com.ualberta.cs.alfred.User;
import com.ualberta.cs.alfred.UserESGetController;
import com.ualberta.cs.alfred.UserESSetController;

import java.util.concurrent.ExecutionException;

/**
 * Created by carlcastello on 13/11/16.
 */

public class UserEditFragment extends Fragment implements View.OnClickListener {

    private User user;

    private String userName;
    private String userID;
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
        userID = preferences.getString("USERID",null);

        //retrieving rider's informatino from elasticsearch
        UserESGetController.GetUserTask getRider = new UserESGetController.GetUserTask();
        try {
            user = getRider.execute(userName).get();
            //user = getRider.execute(userID).get();
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
                String userProperty1 = null;
                String userPropertyType1 = null;
                String userNewValue1 = null;
                String userProperty2 = null;
                String userPropertyType2 = null;
                String userNewValue2 = null;
                String userProperty3 = null;
                String userPropertyType3 = null;
                String userNewValue3 = null;
                String userProperty4 = null;
                String userPropertyType4 = null;
                String userNewValue4 = null;
                String userProperty5 = null;
                String userPropertyType5 = null;
                String userNewValue5 = null;


                // Get new information from the input box.
                if (userNameEdit.getText() != null) {
                    userProperty1 = "userName";
                    userPropertyType1 = "string";
                    userNewValue1 = userNameEdit.getText().toString();
                    //TODO: check if username changed
                    if (userNewValue1.contentEquals(userNewValue1) == false){//if username was changed
                        //TODO: check if username exists
                        UserESGetController.GetUserTask retrievedUser = new UserESGetController.GetUserTask();
                        User user = null;
                        try {
                            user = retrievedUser.execute(userNewValue1).get();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }

                        if (user == null) {//if username is not taken
                            Toast.makeText(getContext(),"Username is NOT taken. it's your lucky day!",Toast.LENGTH_LONG);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("USERNAME", userNewValue1);
                            editor.commit();
                            //update elasticsearch
                            UserESSetController.SetPropertyValueTask setUserNameValueTask =
                                    new UserESSetController.SetPropertyValueTask();
                            setUserNameValueTask.execute(userID, userProperty1, userPropertyType1, userNewValue1);
                        }else{ //if username is taken
                            Toast.makeText(getContext(),"Username is taken. Try a different one.",Toast.LENGTH_LONG);
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setCancelable(Boolean.TRUE);
                            builder.setTitle("Username is taken. Try a different one.");
                            builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                        }
                    }

                }
                if (firstNameEdit.getText() != null) {
                    userProperty2 = "firstName";
                    userPropertyType2 = "string";
                    userNewValue2 = firstNameEdit.getText().toString();
                    //update elasticsearch
                    UserESSetController.SetPropertyValueTask setFirstNameValueTask =
                            new UserESSetController.SetPropertyValueTask();
                    setFirstNameValueTask.execute(userID, userProperty2, userPropertyType2, userNewValue2);
                }
                if (lastNameEdit.getText() != null) {
                    userProperty3 = "lastName";
                    userPropertyType3 = "string";
                    userNewValue3 = lastNameEdit.getText().toString();
                    //update elasticsearch
                    UserESSetController.SetPropertyValueTask setLastNameValueTask =
                            new UserESSetController.SetPropertyValueTask();
                    setLastNameValueTask.execute(userID, userProperty3, userPropertyType3, userNewValue3);
                }
                if (phoneEdit.getText() != null) {
                    userProperty4 = "phoneNumber";
                    userPropertyType4 = "string";
                    userNewValue4 = phoneEdit.getText().toString();
                    //update elasticsearch
                    UserESSetController.SetPropertyValueTask setPhoneNumberValueTask =
                            new UserESSetController.SetPropertyValueTask();
                    setPhoneNumberValueTask.execute(userID, userProperty4, userPropertyType4, userNewValue4);
                }
                if (emailEdit.getText() != null) {
                    userProperty5 = "email";
                    userPropertyType5 = "string";
                    userNewValue5 = emailEdit.getText().toString();
                    //update elasticsearch
                    UserESSetController.SetPropertyValueTask setEmailValueTask =
                            new UserESSetController.SetPropertyValueTask();
                    setEmailValueTask.execute(userID, userProperty5, userPropertyType5, userNewValue5);
                }
                FragmentManager fm = getActivity().getSupportFragmentManager();
                for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                    fm.popBackStack();
                }
                //Fragment fragment = this.newInstance();//changed from UserEditFragment.newInstance()
                Fragment fragment = UserViewFragment.newInstance(0);
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
