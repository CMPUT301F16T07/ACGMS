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
 * Resources: https://www.youtube.com/watch?v=V1tAL0kjjuU
 */
public class SendEmailActivity extends AppCompatActivity {

    private String to;

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
        to = intent.getExtras().getString("to", "None");

        Button sendButton = (Button) findViewById(R.id.sendEmailButton);
        Button cancelButton = (Button) findViewById(R.id.cancelEmailButton);
        final EditText editSubject = (EditText) findViewById(R.id.subject);
        EditText toEmail = (EditText) findViewById(R.id.toEmail);
        final EditText emailMessage = (EditText) findViewById(R.id.emailMessage);

        editSubject.setText("A message from "+ myUsername);
        toEmail.setText(to);
        emailMessage.setText(myUsername+" sent a message to you.");

        //TODO: set up onclicklisteners
        sendButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                String message = emailMessage.getText().toString();
                //code from https://www.youtube.com/watch?v=V1tAL0kjjuU
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                String[] toArray = new String[]{to};
                emailIntent.putExtra(Intent.EXTRA_EMAIL , toArray);
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, editSubject.getText().toString());
                emailIntent.putExtra(Intent.EXTRA_TEXT, emailMessage.getText().toString());
                emailIntent.setType("message/rfc822");
                startActivity(Intent.createChooser(emailIntent,"Email"));
                finish();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                finish();
            }
        });
    }



}
