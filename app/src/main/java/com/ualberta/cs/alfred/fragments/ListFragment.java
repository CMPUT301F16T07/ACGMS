package com.ualberta.cs.alfred.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ualberta.cs.alfred.R;

/**
 * Created by carlcastello on 08/11/16.
 */

public class ListFragment extends Fragment {

    public ListFragment() {
    }

    public static ListFragment newInstance() {
        Bundle args = new Bundle();
        ListFragment listFragment = new ListFragment();
        listFragment.setArguments(args);
        return listFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list,container,false);
        return view;
    }
}
