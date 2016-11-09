package com.ualberta.cs.alfred.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ualberta.cs.alfred.R;

/**
 * Created by carlcastello on 09/11/16.
 */

public class AcceptedFragment extends Fragment {
    public AcceptedFragment() {
    }

    public static AcceptedFragment newInstance() {
        Bundle args = new Bundle();
        AcceptedFragment acceptedFragment = new AcceptedFragment();
        acceptedFragment.setArguments(args);
        return acceptedFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_accepted,container,false);
        return view;
    }

}
