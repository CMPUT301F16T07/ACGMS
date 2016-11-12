package com.ualberta.cs.alfred.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ualberta.cs.alfred.R;
import com.ualberta.cs.alfred.User;

/**
 * Created by carlcastello on 08/11/16.
 */

public class UserFragment extends Fragment {
    /**
     *
     *
     */
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
        View view = inflater.inflate(R.layout.fragment_user,container,false);
        return view;
    }
}
