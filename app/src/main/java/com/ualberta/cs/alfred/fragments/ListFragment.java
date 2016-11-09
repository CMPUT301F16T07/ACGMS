package com.ualberta.cs.alfred.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ualberta.cs.alfred.R;

/**
 * Created by carlcastello on 08/11/16.
 */

public class ListFragment extends Fragment implements View.OnClickListener {

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
    //http://stackoverflow.com/questions/32700818/how-to-open-a-fragment-on-button-click-from-a-fragment-in-android
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list,container,false);

        Fragment fragment = new RequestedFragment();
        replaceFragment(fragment);

        Button pendingButton = (Button) view.findViewById(R.id.button_pending);
        Button requestedButton = (Button) view.findViewById(R.id.button_requested);
        Button acceptedButton = (Button) view.findViewById(R.id.button_accepted);

        pendingButton.setOnClickListener(this);
        requestedButton.setOnClickListener(this);
        acceptedButton.setOnClickListener(this);

        return view;
    }


    @Override
    public void onClick(View v) {
        Fragment fragment = null;
        switch (v.getId()) {
            case R.id.button_pending:
                fragment = new PendingFragment();
                replaceFragment(fragment);
                break;

            case R.id.button_requested:
                fragment = new RequestedFragment();
                replaceFragment(fragment);
                break;

            case R.id.button_accepted:
                fragment = new AcceptedFragment();
                replaceFragment(fragment);
                break;
        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.list_container, fragment);
        transaction.commit();
    }
}
