package com.ualberta.cs.alfred.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
    private static Button requestedButton;
    private static Button pendingButton;
    private static Button acceptedButton;


    public ListFragment() {
    }

    public static ListFragment newInstance(int position) {
        Bundle args = new Bundle();
        args.putInt("index",position);
        ListFragment listFragment = new ListFragment();
        listFragment.setArguments(args);
        return listFragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        update(getContext());
    }

    @Nullable
    @Override
    //http://stackoverflow.com/questions/32700818/how-to-open-a-fragment-on-button-click-from-a-fragment-in-android
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list,container,false);

        Fragment fragment;
        Bundle bundle = this.getArguments();

        requestedButton = (Button) view.findViewById(R.id.button_requested);
        pendingButton = (Button) view.findViewById(R.id.button_pending);
        acceptedButton = (Button) view.findViewById(R.id.button_accepted);
        Drawable border = getResources().getDrawable(R.drawable.button_border);

        if (bundle != null) {
            int position = bundle.getInt("index",0);
            //Toast.makeText(getActivity(), String.valueOf(position),Toast.LENGTH_SHORT).show();
            switch (position) {
                case 0:
                    fragment = RequestedFragment.newInstance();
                    replaceFragmentwithoutStack(fragment);
//                    acceptedButton.setBackground(noBorder);
                        requestedButton.setBackground(border);
//                    pendingButton.setBackground(noBorder);
                    break;
                case 1:
                    fragment = PendingFragment.newInstance();
                    replaceFragmentwithoutStack(fragment);
//                    requestedButton.setBackground(noBorder);
                        pendingButton.setBackground(border);
//                    acceptedButton.setBackground(noBorder);
                    break;
                case 2:
                    fragment = AcceptedFragment.newInstance();
                    replaceFragmentwithoutStack(fragment);
//                    requestedButton.setBackground(border);
                        acceptedButton.setBackground(border);
//                    pendingButton.setBackground(border);
                    break;
            }
        }


        pendingButton.setOnClickListener(this);
        requestedButton.setOnClickListener(this);
        acceptedButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        Bundle bundle = this.getArguments();
        Drawable border = getResources().getDrawable(R.drawable.button_border);
        Drawable noBorder = getResources().getDrawable(R.drawable.button_solid);
        Fragment fragment = null;
        switch (v.getId()) {
            case R.id.button_pending:
                fragment = PendingFragment.newInstance();
                replaceFragmentwithoutStack(fragment);
                requestedButton.setBackground(noBorder);
                pendingButton.setBackground(border);
                acceptedButton.setBackground(noBorder);
                break;

            case R.id.button_requested:
                fragment = RequestedFragment.newInstance();
                replaceFragmentwithoutStack(fragment);
                acceptedButton.setBackground(noBorder);
                requestedButton.setBackground(border);
                pendingButton.setBackground(noBorder);
                break;

            case R.id.button_accepted:
                fragment = AcceptedFragment.newInstance();
                replaceFragmentwithoutStack(fragment);
                requestedButton.setBackground(noBorder);
                acceptedButton.setBackground(border);
                pendingButton.setBackground(noBorder);
                break;
        }
    }

    private void replaceFragmentwithoutStack(Fragment fragment) {
        transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.list_container, fragment);
        transaction.commit();
    }

    public static void update(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        RequestFragmentsListController rFLC = new RequestFragmentsListController();
        rFLC.updateCounts(preferences.getString("MODE", null), context);
        if (requestedButton != null && pendingButton != null && acceptedButton != null) {
            requestedButton.setText("Requested\n"+preferences.getString("Requested", "Error"));
            pendingButton.setText("Pending\n"+preferences.getString("Pending", "Error"));
            acceptedButton.setText("Accepted\n"+preferences.getString("Accepted", "Error"));
        }
    }
}
