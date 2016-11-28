package com.ualberta.cs.alfred;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Driver;
import java.util.Date;
import java.util.concurrent.ExecutionException;

/**
 * Created by Shelley on 2016-11-26.
 * Shows driver details when driver's username is clicked
 * Can contact the driver by phone or email
 */
public class DriverDetailsActivity extends AppCompatActivity{
    private String userID;
    private User user;
    private String userName;
    private String fullName;
    private String emailAddress;
    private String phoneNumber;
    private String rating;
    private String serialNumber;
    private String plateNumber;
    private String type;
    private String make;
    private String model;
    private String year;
    private String color;
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
        phoneNumber = user.getPhoneNumber();
        rating = new Double(user.getDriverInfo().getDriverRating().getRating()).toString();
        serialNumber = user.getDriverInfo().getVehicle().getSerialNumber();
        plateNumber = user.getDriverInfo().getVehicle().getPlateNumber();
        type = user.getDriverInfo().getVehicle().getType();
        make = user.getDriverInfo().getVehicle().getMake();
        model = user.getDriverInfo().getVehicle().getModel();
        year = String.valueOf(user.getDriverInfo().getVehicle().getYear());
        color = user.getDriverInfo().getVehicle().getColor();


        //setting the textviews to what we want to show
        TextView textView = (TextView) findViewById(R.id.driver_username);
        textView.setText(userName);

        textView = (TextView) findViewById(R.id.driver_full_name);
        textView.setText(fullName);

        textView = (TextView) findViewById(R.id.driver_email);
        textView.setText(emailAddress);

        textView = (TextView) findViewById(R.id.driver_phone);
        textView.setText(phoneNumber);

        textView = (TextView) findViewById(R.id.driver_rating);
        textView.setText(rating);

        textView = (TextView) findViewById(R.id.vehicle_serial_number);
        textView.setText(serialNumber);

        textView = (TextView) findViewById(R.id.vehicle_plate_number);
        textView.setText(plateNumber);

        textView = (TextView) findViewById(R.id.vehicle_type);
        textView.setText(type);

        textView = (TextView) findViewById(R.id.vehicle_make);
        textView.setText(make);

        textView = (TextView) findViewById(R.id.vehicle_model);
        textView.setText(model);

        textView = (TextView) findViewById(R.id.vehicle_year);
        textView.setText(year);

        textView = (TextView) findViewById(R.id.vehicle_color);
        textView.setText(color);

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
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + phoneNumber));
                startActivity(callIntent);


            }
        });


    }

    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.email_driver_button:
                //start email activity to send email
                Intent emailIntent = new Intent(DriverDetailsActivity.this, SendEmailActivity.class);
                emailIntent.putExtra("to",emailAddress);
                startActivity(emailIntent);
                break;
            case R.id.call_driver_button:
                //TODO: start call
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                String telString = "tel:"+phoneNumber;
                callIntent.setData(Uri.parse(telString));
                startActivity(callIntent);
                break;

        }

    }
}
