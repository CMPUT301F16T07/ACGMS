package com.ualberta.cs.alfred;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.BoolRes;
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
import java.util.concurrent.ExecutionException;

/**
 * Activity responsible for allowing users to sign up
 * @author mmcote
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
                    UserESGetController.GetUserTask retrievedUser = new UserESGetController.GetUserTask();
                    User user = null;
                    try {
                        user = retrievedUser.execute(userName).get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    String userID = user.getUserID();
                    UserESSetController.SetNestedObjectPropertyValueTask setNestedObjectPropertyValueTask =
                            new UserESSetController.SetNestedObjectPropertyValueTask();
                    if (oppositeExists.contentEquals("Driver Mode")) {
                        String surroundingObject = "riderInfo";
                        String creditCardNumberProperty = "creditCardNumber";
                        String creditCardNumberType = "string";
                        String creditCardNumberValue = creditCardNumberEditText.getText().toString();

                        setNestedObjectPropertyValueTask.execute(userID, surroundingObject,
                                creditCardNumberProperty, creditCardNumberType, creditCardNumberValue);
                    } else if (oppositeExists.contentEquals("Rider Mode")) {
                        String surroundingObject = "driverInfo";
                        DriverInfo driverInfo = collectDriverInfo();
                        String licenceNumberProperty = "licenceNumber";
                        String licenceNumberType = "string";
                        String licenceNumberInput = driverInfo.getLicenceNumber();

                        setNestedObjectPropertyValueTask.execute(userID, surroundingObject,
                                licenceNumberProperty, licenceNumberType, licenceNumberInput);

                        Vehicle vehicle = driverInfo.getVehicle();

                        String userPropertyFirstLevel = "driverInfo";
                        String userPropertySecondLevel = "vehicle";

                        String nestedObject0Property = "serialNumber";
                        String nestedObject0ValueType = "string";
                        String nestedObject0Value = vehicle.getSerialNumber();

                        String nestedObject1Property = "color";
                        String nestedObject1ValueType = "string";
                        String nestedObject1Value = vehicle.getColor();

                        String nestedObject2Property = "type";
                        String nestedObject2ValueType = "string";
                        String nestedObject2Value = vehicle.getType();

                        String nestedObject3Property = "plateNumber";
                        String nestedObject3ValueType = "string";
                        String nestedObject3Value = vehicle.getPlateNumber();

                        String nestedObject4Property = "make";
                        String nestedObject4ValueType = "string";
                        String nestedObject4Value = vehicle.getMake();

                        String nestedObject5Property = "model";
                        String nestedObject5ValueType = "string";
                        String nestedObject5Value = vehicle.getModel();

                        String nestedObject6Property = "year";
                        String nestedObject6ValueType = "int";
                        Integer nestedObject6Value = vehicle.getYear();


                        UserESSetController.SetDoublyNestedObjectPropertyValueTask setDoubleNestedObject =
                                new UserESSetController.SetDoublyNestedObjectPropertyValueTask();

                        setDoubleNestedObject.execute(userID, userPropertyFirstLevel, userPropertySecondLevel,
                                nestedObject0Property, nestedObject0ValueType, nestedObject0Value,
                                nestedObject1Property, nestedObject1ValueType, nestedObject1Value,
                                nestedObject2Property, nestedObject2ValueType, nestedObject2Value,
                                nestedObject3Property, nestedObject3ValueType, nestedObject3Value,
                                nestedObject4Property, nestedObject4ValueType, nestedObject4Value,
                                nestedObject5Property, nestedObject5ValueType, nestedObject5Value,
                                nestedObject6Property, nestedObject6ValueType, nestedObject6Value.toString()
                        );


                        String userProperty = "isDriver";
                        String userPropertyType = "boolean";
                        String userNewValue = "true";

                        UserESSetController.SetPropertyValueTask setPropertyValueTask =
                                new UserESSetController.SetPropertyValueTask();

                        setPropertyValueTask.execute(userID, userProperty, userPropertyType, userNewValue);
                    }
                } else {
                    createUser();
                }
                Intent intent = new Intent(SignUpActivity.this, MenuActivity.class);
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
                licenseNumberEditText.getText().toString(),
                vehicle);
        return driverInfo;
    }

    private void createUser() {
        String firstName = firstNameEditText.getText().toString();
        String lastName = lastNameEditText.getText().toString();
        String finalUserName = userNameEditText.getText().toString();
        String phoneNumber = phoneNumberEditText.getText().toString();
        String email = emailEditText.getText().toString();

        User user = null;
        if (Mode.contentEquals("Driver Mode")) {
            user = new User(firstName, lastName, finalUserName,
                    inputDate, phoneNumber, email, collectDriverInfo());
        } else {
            user = new User(firstName, lastName, finalUserName,
                    inputDate, phoneNumber, email,
                    new RiderInfo(creditCardNumberEditText.getText().toString()));
        }
        UserESGetController.GetUserTask getUserTask = new UserESGetController.GetUserTask();
        getUserTask.execute(userName);

        try {
            user = getUserTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        if (user != null) {
            SharedPreferences preferences =
                    PreferenceManager.getDefaultSharedPreferences(SignUpActivity.this);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("USERNAME", finalUserName);
            editor.putString("USERID", user.getUserID());
            editor.commit();
        }
    }
}