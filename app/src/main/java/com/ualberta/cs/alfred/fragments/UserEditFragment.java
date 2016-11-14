package com.ualberta.cs.alfred.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ualberta.cs.alfred.R;

/**
 * Created by carlcastello on 13/11/16.
 */

public class UserEditFragment extends Fragment implements View.OnClickListener {

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
