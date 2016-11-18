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

    private FragmentTransaction transaction;
    public static Button pendingButton;
    public static Button requestedButton;
    public static Button acceptedButton;


    public ListFragment() {
    }

    public static ListFragment newInstance(int position) {
        Bundle args = new Bundle();
        args.putInt("index",position);
        ListFragment listFragment = new ListFragment();
        listFragment.setArguments(args);
        return listFragment;
    }


    @Nullable
    @Override
    //http://stackoverflow.com/questions/32700818/how-to-open-a-fragment-on-button-click-from-a-fragment-in-android
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list,container,false);
        Fragment fragment;
        Bundle bundle = this.getArguments();

        fragment = new RequestedFragment().newInstance();
        replaceFragmentwithoutStack(fragment);

        if (bundle != null) {
            int position = bundle.getInt("index",0);
            //Toast.makeText(getActivity(), String.valueOf(position),Toast.LENGTH_SHORT).show();
            switch (position) {
                case 0:
                    fragment = new RequestedFragment().newInstance();
                    replaceFragmentwithoutStack(fragment);
                    break;
                case 1:
                    fragment = new PendingFragment().newInstance();
                    replaceFragmentwithoutStack(fragment);
                    break;
                case 2:
                    fragment = new AcceptedFragment().newInstance();
                    replaceFragmentwithoutStack(fragment);
                    break;
            }
        }

        pendingButton = (Button) view.findViewById(R.id.button_pending);
        pendingButton.setText("PENDING\n"+Integer.toString(HomeFragment.pendingCount));
        pendingButton.setOnClickListener(this);

        requestedButton = (Button) view.findViewById(R.id.button_requested);
        requestedButton.setText("REQUESTED\n"+Integer.toString(HomeFragment.requestedCount));
        requestedButton.setOnClickListener(this);

        acceptedButton = (Button) view.findViewById(R.id.button_accepted);
        acceptedButton.setText("ACCEPTED\n"+Integer.toString(HomeFragment.acceptedCount));
        acceptedButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        Bundle bundle = this.getArguments();

        Fragment fragment = null;
        switch (v.getId()) {
            case R.id.button_pending:
                fragment = new PendingFragment().newInstance();
                replaceFragmentwithoutStack(fragment);
                break;

            case R.id.button_requested:
                fragment = new RequestedFragment().newInstance();
                replaceFragmentwithoutStack(fragment);
                break;

            case R.id.button_accepted:
                fragment = new AcceptedFragment().newInstance();
                replaceFragmentwithoutStack(fragment);
                break;
        }
    }

    private void replaceFragmentwithoutStack(Fragment fragment) {
        transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.list_container, fragment);
        transaction.commit();
    }
}
