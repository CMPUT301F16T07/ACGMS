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
 * Accepted Fragment is a fragment class where all accepted listed is found.
 *
 * @author carlcastello
 * @author averytan
 * @author mmcote
 * @author shltien
 */
public class ListFragment extends Fragment implements View.OnClickListener {

    private FragmentTransaction transaction;
    private static Button requestedButton;
    private static Button pendingButton;
    private static Button acceptedButton;


    /**
     * Instantiates a new List fragment.
     */
    public ListFragment() {
    }

    /**
     * New instance list fragment.
     *
     * @param position the position
     * @return the list fragment
     */
    public static ListFragment newInstance(int position) {
        Bundle args = new Bundle();
        args.putInt("index",position);
        ListFragment listFragment = new ListFragment();
        listFragment.setArguments(args);
        return listFragment;
    }

    /**
     * OnRsume to handld update request count
     */
    @Override
    public void onResume() {
        super.onResume();
        // call update function
        update(getContext());
    }


    /**
     * onCreate function where buttons are defined and created.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    //code from http://stackoverflow.com/questions/32700818/how-to-open-a-fragment-on-button-click-from-a-fragment-in-android
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
            switch (position) {
                case 0:
                    fragment = RequestedFragment.newInstance();
                    replaceFragmentwithoutStack(fragment);
                        requestedButton.setBackground(border);
                    break;
                case 1:
                    fragment = PendingFragment.newInstance();
                    replaceFragmentwithoutStack(fragment);
                        pendingButton.setBackground(border);
                    break;
                case 2:
                    fragment = AcceptedFragment.newInstance();
                    replaceFragmentwithoutStack(fragment);
                        acceptedButton.setBackground(border);
                    break;
            }
        }


        pendingButton.setOnClickListener(this);
        requestedButton.setOnClickListener(this);
        acceptedButton.setOnClickListener(this);

        return view;
    }

    /**
     * onClick where buttons click are read
     * @param v
     */
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

    /**
     * Fragment replacing function without back stack
     * @param fragment
     */
    private void replaceFragmentwithoutStack(Fragment fragment) {
        transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.list_container, fragment);
        transaction.commit();
    }

    /**
     * Updates function that replace the button counts
     *
     * @param context the context
     */
    public static void update(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        RequestFragmentsListController rFLC = new RequestFragmentsListController();
        rFLC.updateCounts(preferences.getString("MODE", null), context);
        if (requestedButton != null && pendingButton != null && acceptedButton != null) {
            requestedButton.setText("Requested\n"+preferences.getString("Requested", "Error"));
            pendingButton.setText("Pending\n"+preferences.getString("Requested", "Error"));
            acceptedButton.setText("Accepted\n"+preferences.getString("Requested", "Error"));
        }
    }
}
