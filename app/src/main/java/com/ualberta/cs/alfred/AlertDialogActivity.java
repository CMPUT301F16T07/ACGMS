package com.ualberta.cs.alfred;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.view.ContextThemeWrapper;


// ALERT DIALOG
// Sources : http://techblogon.com/alert-dialog-with-edittext-in-android-example-with-source-code/

public class AlertDialogActivity extends Activity
{
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(AlertDialogActivity.this);
        Bundle extras = getIntent().getExtras();
        final String requestID = extras.getString("REQUESTID", null);
        final String requestCost = extras.getString("REQUESTCOST", null);
        preferences = PreferenceManager.getDefaultSharedPreferences(AlertDialogActivity.this);
        editor = preferences.edit();
        if (requestID != null && requestCost != null) {
            builder.setTitle("A rider has confirmed arrival and is awaiting payment!");
            builder.setMessage("The driver has sent the amount of " + requestCost + ", " +
                    "would you like to accept the payment now?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    RequestESSetController.SetPropertyValueTask setSelectedDriver =
                            new RequestESSetController.SetPropertyValueTask();
                    setSelectedDriver.execute(requestID, "driverID", "String", preferences.getString("USERID", null));
                    RequestESSetController.SetPropertyValueTask setRequestedStatus =
                            new RequestESSetController.SetPropertyValueTask();
                    setRequestedStatus.execute(requestID, "requestStatus", "string", "Completed");
                    editor.putBoolean("awaitingPaymentOpen", false);
                    editor.commit();
                    finish();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    RequestESSetController.SetPropertyValueTask setRequestedStatus =
                            new RequestESSetController.SetPropertyValueTask();
                    setRequestedStatus.execute(requestID, "requestStatus", "string", "Accepted");
                    dialog.cancel();
                    editor.putBoolean("awaitingPaymentOpen", false);
                    editor.commit();
                    finish();
                }
            });
            builder.setCancelable(Boolean.FALSE);
            android.support.v7.app.AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
}