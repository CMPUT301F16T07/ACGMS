package com.ualberta.cs.alfred.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ualberta.cs.alfred.R;

/**
 * Created by carlcastello on 13/11/16.
 */

public class UserFragment extends Fragment {

    FragmentTransaction transaction;

    public UserFragment() {
    }

    public static UserFragment newInstance(int position) {
        Bundle args = new Bundle();
        args.putInt("index",position);
        UserFragment userFragment = new UserFragment();
        userFragment.setArguments(args);
        return userFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        int position = 0;
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            position = bundle.getInt("index", 0);
        }
        Fragment fragment = UserViewFragment.newInstance(position);
        replaceFragmentwithoutStack(fragment);

        return view;
    }

    private void replaceFragmentwithoutStack(Fragment fragment) {
        transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.user_fragment_container, fragment);
        transaction.commit();
    }
}
