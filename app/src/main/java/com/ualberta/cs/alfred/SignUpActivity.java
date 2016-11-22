package com.ualberta.cs.alfred;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Activity responsible for allowing users to sign up
 *
 *
 */
public class SignUpActivity extends AppCompatActivity {
    private SimpleDateFormat formatter;
    private Date inputDate;

    private EditText userNameEditText;
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText dateOfBirthEditText;
    private EditText phoneNumberEditText;
    private EditText emailEditText;
    private EditText creditCardNumberEditText;
    private EditText licenseNumberEditText;
    private EditText plateNumberEditText;
    private EditText serialNumberEditText;
    private EditText typeEditText;
    private EditText makeEditText;
    private EditText modelEditText;
    private EditText yearEditText;
    private EditText colorEditText;

    private Button submitButton;

    private String Mode;
    private String oppositeExists;
    private String userName;

    //TODO: Check this!
    /* =====
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        this.userName = preferences.getString("USERNAME", null);
        this.Mode = preferences.getString("MODE", null);
        this.oppositeExists = null;

        Intent intent = getIntent();
        if (intent.hasExtra("OPPOSITE")) {
            this.oppositeExists = intent.getExtras().getString("OPPOSITE");
        }

        submitButton = (Button) findViewById(R.id.submitButton);
        userNameEditText = (EditText) findViewById(R.id.usernameEditText);
        userNameEditText.setText(this.userName);
        userNameEditText.setInputType(0);

        firstNameEditText = (EditText) findViewById(R.id.firstNameEditText);
        lastNameEditText = (EditText) findViewById(R.id.lastNameEditText);
        dateOfBirthEditText = (EditText) findViewById(R.id.dateOfBirthEditText);
        phoneNumberEditText = (EditText) findViewById(R.id.phoneNumberEditText);
        emailEditText = (EditText) findViewById(R.id.emailEditText);
        creditCardNumberEditText = (EditText) findViewById(R.id.creditCardEditText);
        licenseNumberEditText = (EditText) findViewById(R.id.licenseNumberEditText);
        plateNumberEditText = (EditText) findViewById(R.id.plateNmberEditText);
        serialNumberEditText = (EditText) findViewById(R.id.serialNumberEditText);
        typeEditText = (EditText) findViewById(R.id.typeEditText);
        makeEditText = (EditText) findViewById(R.id.makeEditText);
        modelEditText = (EditText) findViewById(R.id.modelEditText);
        yearEditText = (EditText) findViewById(R.id.yearEditText);
        colorEditText = (EditText) findViewById(R.id.colorEditText);

        // if one of the two modes does exist then the user at a prior point must of entered
        // all of their basic information
        if (this.oppositeExists != null) {
            firstNameEditText.setVisibility(View.GONE);
            TextView firstNameTextView = (TextView) findViewById(R.id.firstNameTextView);
            firstNameTextView.setVisibility(View.GONE);
            lastNameEditText.setVisibility(View.GONE);
            TextView lastNameTextView = (TextView) findViewById(R.id.lastNameTextView);
            lastNameTextView.setVisibility(View.GONE);
            dateOfBirthEditText.setVisibility(View.GONE);
            TextView dateOfBirthTextView = (TextView) findViewById(R.id.dateOfBirthTextView);
            dateOfBirthTextView.setVisibility(View.GONE);
            phoneNumberEditText.setVisibility(View.GONE);
            TextView phoneNumberTextView = (TextView) findViewById(R.id.phoneNumberTextView);
            phoneNumberTextView.setVisibility(View.GONE);
            emailEditText.setVisibility(View.GONE);
            TextView emailTextView = (TextView) findViewById(R.id.emailTextView);
            emailTextView.setVisibility(View.GONE);
        }
        if ((this.oppositeExists != null && this.oppositeExists.contentEquals("Rider Mode")) || Mode.contentEquals("Driver Mode")) {
            creditCardNumberEditText.setVisibility(View.GONE);
            TextView creditCardNumberTextView = (TextView) findViewById(R.id.creditCardTextView);
            creditCardNumberTextView.setVisibility(View.GONE);
        } else if ((this.oppositeExists != null && this.oppositeExists.contentEquals("Driver Mode")) || Mode.contentEquals("Rider Mode")){
            licenseNumberEditText.setVisibility(View.GONE);
            TextView licenseNumberTextView = (TextView) findViewById(R.id.licenseNumberTextView);
            licenseNumberTextView.setVisibility(View.GONE);
            plateNumberEditText.setVisibility(View.GONE);
            TextView plateNumberTextView = (TextView) findViewById(R.id.plateNumberTextView);
            plateNumberTextView.setVisibility(View.GONE);
            serialNumberEditText.setVisibility(View.GONE);
            TextView serialNumberTextView = (TextView) findViewById(R.id.serialNumberTextView);
            serialNumberTextView.setVisibility(View.GONE);
            typeEditText.setVisibility(View.GONE);
            TextView typeTextView = (TextView) findViewById(R.id.typeTextView);
            typeTextView.setVisibility(View.GONE);
            makeEditText.setVisibility(View.GONE);
            TextView makeTextView = (TextView) findViewById(R.id.makeTextView);
            makeTextView.setVisibility(View.GONE);
            modelEditText.setVisibility(View.GONE);
            TextView modelTextView = (TextView) findViewById(R.id.modelTextView);
            modelTextView.setVisibility(View.GONE);
            yearEditText.setVisibility(View.GONE);
            TextView yearTextView = (TextView) findViewById(R.id.yearTextView);
            yearTextView.setVisibility(View.GONE);
            colorEditText.setVisibility(View.GONE);
            TextView colorTextView = (TextView) findViewById(R.id.colorTextView);
            colorTextView.setVisibility(View.GONE);
        }

        dateOfBirthEditText.setFocusable(false);

        inputDate = new Date();

        formatter = new SimpleDateFormat("yyyy-MM-dd");
        String todayString = formatter.format(inputDate);

        dateOfBirthEditText.setText(todayString);
        dateOfBirthEditText.setOnClickListener( new View.OnClickListener() {
            Calendar currentHabitDate = Calendar.getInstance();
            @Override
            public void onClick(View v) {
                new DatePickerDialog(SignUpActivity.this, startDateDialog,
                        currentHabitDate.get(Calendar.YEAR), currentHabitDate.get(Calendar.MONTH),
                        currentHabitDate.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (oppositeExists != null) {
                    Toast.makeText(SignUpActivity.this, "Update user is to be done", Toast.LENGTH_LONG);
//                    if (oppositeExists.contentEquals("Driver Mode")) {
//                        UserElasticSearchController.UpdateUser<RiderInfo> updateUser = new UserElasticSearchController.UpdateUser<RiderInfo>(userName, "riderInfo");
//                        RiderInfo riderInfo = new RiderInfo(creditCardNumberEditText.getText().toString());
//                        updateUser.execute(riderInfo);
//                    } else if (oppositeExists.contentEquals("Rider Mode")) {
//                        UserElasticSearchController.UpdateUser<DriverInfo> updateUser = new UserElasticSearchController.UpdateUser<DriverInfo>(userName, "driverInfo");
//                        updateUser.execute(collectDriverInfo());
//                    }
                } else {
                    UserElasticSearchController.AddUser<User> addUser = new UserElasticSearchController.AddUser<User>();
                    RiderInfo riderInfo = new RiderInfo(creditCardNumberEditText.getText().toString());
                    addUser.execute(collectUser());
                }
                Intent intent = new Intent(SignUpActivity.this, MenuActivity.class);
                SharedPreferences preferences =
                        PreferenceManager.getDefaultSharedPreferences(SignUpActivity.this);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("USERNAME", userNameEditText.getText().toString());
                editor.commit();
                startActivity(intent);
                finish();
            }
        });
    }

    DatePickerDialog.OnDateSetListener startDateDialog = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            dateOfBirthEditText.setText(year+"-"+(month + 1)+"-"+dayOfMonth);
            try {
                inputDate = formatter.parse(year+"-"+(month+1)+"-"+dayOfMonth);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    };


    private DriverInfo collectDriverInfo() {
        Vehicle vehicle = new Vehicle(
                serialNumberEditText.getText().toString(),
                plateNumberEditText.getText().toString(),
                typeEditText.getText().toString(),
                makeEditText.getText().toString(),
                modelEditText.getText().toString(),
                Integer.valueOf(yearEditText.getText().toString()),
                colorEditText.getText().toString());
        DriverInfo driverInfo = new DriverInfo(
                userNameEditText.getText().toString(),
                licenseNumberEditText.getText().toString(),
                vehicle);
        return driverInfo;
    }

    private User collectUser() {
        String firstName = firstNameEditText.getText().toString();
        String lastName = lastNameEditText.getText().toString();
        String finalUserName = userNameEditText.getText().toString();
        String phoneNumber = phoneNumberEditText.getText().toString();
        String email = emailEditText.getText().toString();

        if (Mode.contentEquals("Driver Mode")) {
            return new User(firstName, lastName, finalUserName,
                    inputDate, phoneNumber, email, collectDriverInfo());
        } else {
            return new User(firstName, lastName, finalUserName,
                    inputDate, phoneNumber, email,
                    new RiderInfo(creditCardNumberEditText.getText().toString()));
        }
    }
    =====
    */
}
