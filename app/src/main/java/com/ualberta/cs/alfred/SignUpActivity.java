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

    private Button submitButton;

    private String Mode;
    private String isRider;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        this.userName = preferences.getString("USERNAME", null);
        this.Mode = preferences.getString("MODE", null);

        Bundle extras = getIntent().getExtras();
        this.isRider = extras.getString("ISRIDER");
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

        if (this.isRider.contentEquals("true")) {
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
            creditCardNumberEditText.setVisibility(View.GONE);
            TextView creditCardNumberTextView = (TextView) findViewById(R.id.creditCardTextView);
            creditCardNumberTextView.setVisibility(View.GONE);
        } else {
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
        licenseNumberEditText = (EditText) findViewById(R.id.licenseNumberEditText);
        plateNumberEditText = (EditText) findViewById(R.id.plateNmberEditText);

        if(this.Mode.contentEquals("Rider Mode")) {
            licenseNumberEditText.setVisibility(View.GONE);
            TextView licenseNumberTextView = (TextView) findViewById(R.id.licenseNumberTextView);
            licenseNumberTextView.setVisibility(View.GONE);
            plateNumberEditText.setVisibility(View.GONE);
            TextView plateNumberTextView = (TextView) findViewById(R.id.plateNumberTextView);
            plateNumberTextView.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Mode.contentEquals("Rider Mode") || (Mode.contentEquals("Driver Mode") && isRider.contentEquals("false"))) {
                    Rider rider = new Rider(firstNameEditText.getText().toString(), lastNameEditText.getText().toString(),
                            userNameEditText.getText().toString(), inputDate, phoneNumberEditText.getText().toString(),
                            emailEditText.getText().toString(), creditCardNumberEditText.getText().toString());
                }
                if (Mode.contentEquals("Driver Mode")) {
                    DriverInfo driver = new DriverInfo(userNameEditText.getText().toString(),
                            licenseNumberEditText.getText().toString(), plateNumberEditText.getText().toString());
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
}
