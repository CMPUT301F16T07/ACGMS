package com.ualberta.cs.alfred;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.concurrent.ExecutionException;

/**
 * Created by Shelley on 2016-11-24.
 */
public class SendEmailActivity extends AppCompatActivity {

    private String from;
    private String to;
    /*private Button sendButton;
    private Button cancelButton;
    private EditText fromEmail;
    private EditText toEmail;
    private EditText emailMessage;
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_email);

        //get App user's email
        String myEmail;
        User me = null;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(SendEmailActivity.this);
        String myUsername = preferences.getString("USERNAME", null);
        UserESGetController.GetUserTask getUser = new UserESGetController.GetUserTask();
        try {
            me = getUser.execute(myUsername).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        myEmail = me.getEmail();

        final Intent intent = getIntent();
        from = myEmail;
        to = intent.getExtras().getString("to", "None");

        Button sendButton = (Button) findViewById(R.id.sendEmailButton);
        Button cancelButton = (Button) findViewById(R.id.cancelEmailButton);
        EditText fromEmail = (EditText) findViewById(R.id.fromEmail);
        EditText toEmail = (EditText) findViewById(R.id.toEmail);
        EditText emailMessage = (EditText) findViewById(R.id.emailMessage);

        fromEmail.setText(from);
        toEmail.setText(to);
        emailMessage.setText(myUsername+" sent a message to you.");

        sendButton.setOnClickListener();
        //TODO: set up onclicklistener
    }

}
