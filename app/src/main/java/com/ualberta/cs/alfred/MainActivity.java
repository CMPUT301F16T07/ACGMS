package com.ualberta.cs.alfred;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.ualberta.cs.alfred.fragments.RequestFragmentsListController;
import java.util.concurrent.ExecutionException;

/**
 * MainActivity is the first activity which the user sees
 * if there is no user currently logged in in the cache.
 * <p>
 * This activity handles the users requests to log in either
 * the rider or driver domain. This activity uses the LoginController
 * to check whether the user exists under the domain, and if they do
 * not what next they will have to enter in order to become part of
 * the requested domain.
 * <p>
 * Created by carlcastello on 08/11/16.
 * Completed by mmcote on 08/09/16.
 *
 * @author mmcote
 * @version 1.1
 */
public class MainActivity extends AppCompatActivity {
    private Button loginButton;
    private Button signUpButton;
    private EditText userName;
    private RadioGroup driverRider;
    private String mode;
    SharedPreferences.Editor editor;

    private void grabMode() {
        // grab the corresponding user information based on a query using the
        // username to check if the user even exists
        int selected = driverRider.getCheckedRadioButtonId();

        // if no choice was selected for either driver or rider mode then rider will
        // be selected by default, by UI design this is automatically picked already
        if (selected == -1) {
            selected = findViewById(R.id.mode2_button).getId();
        }

        final RadioButton radioButtonSelected = (RadioButton) findViewById(selected);
        mode = radioButtonSelected.getText().toString();

        // Access the default SharedPreferences
        editor.putString("USERNAME", userName.getText().toString());
        if (mode.contentEquals("Driver")) {
            mode = "Driver Mode";
            editor.putString("MODE", mode);
        } else {
            mode = "Rider Mode";
            editor.putString("MODE", mode);
        }
        editor.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        editor = preferences.edit();
        loginButton = (Button) findViewById(R.id.main_button);
        signUpButton = (Button) findViewById(R.id.sign_up_button);
        userName = (EditText) findViewById(R.id.username_input);
        driverRider = (RadioGroup) findViewById(R.id.radioGroup);
    }

    /**
     * Determines whether a driver or rider is trying to log in and shows the appropriate screens
     */
    @Override
    protected void onResume(){
        super.onResume();
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                grabMode();
                editor.putString("USERNAME", userName.getText().toString());
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });

        loginButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        grabMode();
                        // check if the username exists in the current elastic search server
                        LoginController loginController = new LoginController(userName.getText().toString(), mode);
                        Boolean userExist = null;
                        try {
                            userExist = loginController.checkUser();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        // if the user already exists as the desired type of user
                        if (userExist != null && userExist == Boolean.TRUE) {
                            // Launch MenuActivity where the bottom navbar is located.
                            UserESGetController.GetUserTask getUserTask = new UserESGetController.GetUserTask();
                            getUserTask.execute(userName.getText().toString());
                            User user = null;
                            try {
                                user = (User) getUserTask.get();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }
                            if (user != null) {
                                editor.putString("USERID", user.getUserID());
                                editor.commit();
                            }
                            Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                            startActivity(intent);
                            finish();
                            // if the user does not exist as the desired type of user or not at all
                        } else if (userExist == Boolean.FALSE) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            Boolean isOpposite = Boolean.FALSE;
                            String oppositeMode = "Driver Mode";
                            try {
                                if (loginController.checkOpposite()) {
                                    isOpposite = Boolean.TRUE;
                                    if (mode.contentEquals("Driver Mode")) {
                                        oppositeMode = "Rider Mode";
                                    }
                                    builder.setMessage(
                                            "No "+mode+" profile was found under the username of "
                                                    +userName.getText().toString()+ ". Although a " +oppositeMode+
                                                    " profile was found. Would you like to add additional " +mode+" info?");
                                } else {
                                    builder.setMessage("No profile was found under the username of "
                                            +userName.getText().toString()+ " Would you like to create a new profile?");
                                }
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            builder.setTitle(mode + " not found");
                            builder.setCancelable(Boolean.FALSE);
                            final Boolean finalIsOpposite = isOpposite;
                            final String finalOppositeMode = oppositeMode;
                            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                                    if (finalIsOpposite) {
                                        intent.putExtra("OPPOSITE", finalOppositeMode);
                                    }
                                    startActivity(intent);
                                    finish();
                                }
                            });
                            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    userName.setText("");
                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        } else {
                            // This scenerio covers the event where the device is connected to the internet but had an error occur with the ES server
                            Toast.makeText(MainActivity.this, "Sorry there was a connection error with the server, please try again.", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );
    }
}