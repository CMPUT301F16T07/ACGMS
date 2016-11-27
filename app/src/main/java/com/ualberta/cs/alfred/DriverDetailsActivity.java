package com.ualberta.cs.alfred;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Date;
import java.util.concurrent.ExecutionException;

/**
 * Created by Shelley on 2016-11-26.
 */
public class DriverDetailsActivity extends AppCompatActivity{
    private String userID;
    private User user;
    private String userName;
    private String fullName;
    private String emailAddress;
    private String phoneNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_details);

        final Intent intent = getIntent();

        userID = intent.getExtras().getString("ID","");

        user = new User("","","",new Date(),"","");
        try {
            //retrieving rider's username from elasticsearch with userID
            UserESGetController.GetUserByIdTask getUserByID = new UserESGetController.GetUserByIdTask();
            user = getUserByID.execute(userID).get();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        userName = user.getUserName();
        fullName = user.getFirstName() + " " + user.getLastName();
        emailAddress = user.getEmail();
        phoneNumber= user.getPhoneNumber();

        //setting the textviews to what we want to show
        TextView textView = (TextView) findViewById(R.id.driver_username);
        textView.setText(userName);

        textView = (TextView) findViewById(R.id.driver_full_name);
        textView.setText(fullName);

        textView = (TextView) findViewById(R.id.driver_email);
        textView.setText(emailAddress);

        textView = (TextView) findViewById(R.id.driver_phone);
        textView.setText(phoneNumber);

        Button emailButton = (Button) findViewById(R.id.email_driver_button);;
        Button callButton = (Button) findViewById(R.id.call_driver_button);

        emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //start email activity to send email
                Intent intent = new Intent(DriverDetailsActivity.this, SendEmailActivity.class);
                intent.putExtra("to", emailAddress);
                startActivity(intent);
            }
        });
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //start call

            }
        });


    }

    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.email_driver_button:
                //start email activity to send email
                Intent intent = new Intent(DriverDetailsActivity.this, SendEmailActivity.class);
                intent.putExtra("to",emailAddress);
                startActivity(intent);
                break;
            case R.id.call_driver_button:
                //TODO: start call

                break;

        }

    }
}
