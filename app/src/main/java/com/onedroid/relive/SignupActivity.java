package com.onedroid.relive;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.onedroid.relive.handlers.AuthHandler;
import com.onedroid.relive.service.AccountService;

/**
 * Activity class for Login.
 */
public class SignupActivity extends AppCompatActivity {
    EditText usernameInput, passwordInput;
    Button signupButton;
    AuthHandler authHandler;

    /**
     * Overrides onCreate.
     * Adds Listeners and bind intent. Sets content view.
     *
     * @param savedInstanceState Bundle object.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        authHandler = new AuthHandler(this);



        authHandler = new AuthHandler(this);

        usernameInput = (EditText) findViewById(R.id.username);
        passwordInput = (EditText) findViewById(R.id.password);
        signupButton = (Button) findViewById(R.id.signup);


        /*
         * User login , validates the fields that are entered
         * Will move to the main activity if authenticated
         * Displays a message if the validation fails
         */
        signupButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Override onClick.
             * Authenticate user based on username and password and start activity
             * @param view The view to go to after the user enters the information
             */
            @Override
            public void onClick(View view) {
                String username = usernameInput.getText().toString();
                String password = passwordInput.getText().toString();
                if (username.isEmpty()) {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Username cannot be empty",
                            Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                if (password.isEmpty()) {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Password cannot be empty",
                            Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                try {
                    authHandler.createUser(username, password);
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Sign Up Successful",
                            Toast.LENGTH_SHORT);
                    toast.show();
                    finish();
                } catch (Exception e) {
                    Toast.makeText(SignupActivity.this, "Username taken please try with a different one", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Override onResume
     * Set username input and password input to be empty
     */
    @Override
    protected void onResume() {
        super.onResume();
        usernameInput.setText("");
        passwordInput.setText("");
    }
}