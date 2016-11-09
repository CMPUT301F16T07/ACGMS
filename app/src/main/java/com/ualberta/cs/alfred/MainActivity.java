package com.ualberta.cs.alfred;

import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;


/**
 *
 * MainActivity Class
 * This is where the app starts
 * and it prompts user to log-in
 *
 * Created by carlcastello on 08/11/16.
 */
public class MainActivity extends AppCompatActivity {
    Button loginButton;
    EditText userName;
    EditText password;
    RadioGroup driverRider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginButton = (Button) findViewById(R.id.main_button);
        userName = (EditText) findViewById(R.id.username_input);
        password = (EditText) findViewById(R.id.password_input);
        driverRider = (RadioGroup) findViewById(R.id.radioGroup);

    }

    @Override
    protected void onResume(){
        super.onResume();

        loginButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {

                        // grab the corresponding user information based on a query using the
                        // username to check if the user even exists
                        int selected = driverRider.getCheckedRadioButtonId();
                        RadioButton radioButtonSelected = (RadioButton) findViewById(selected);



                        LoginController loginController = new LoginController(userName.getText().toString(), radioButtonSelected.getText().toString());
                        if (loginController.check()) {
                            // Launch MenuActivity where the buttom navbar is located.
                            Intent intent= new Intent(MainActivity.this, MenuActivity.class);
                            if (radioButtonSelected.getText().toString().contentEquals("Driver")) {
                                intent.putExtra("MODE", "DRIVER");
                            } else {
                                intent.putExtra("MODE", "RIDER");
                            }
                            startActivity(intent);
                            finish();
                        } else {
                            // TODO: HANDLE UNABLE TO LOG IN AND UNABLE TO CONNECT
                        }

                    }
                }
        );
    }
}
