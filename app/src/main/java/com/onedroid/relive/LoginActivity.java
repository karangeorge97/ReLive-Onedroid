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
public class LoginActivity extends AppCompatActivity {
    EditText usernameInput, passwordInput;
    Button loginButton;
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
        setContentView(R.layout.activity_login);
        authHandler = new AuthHandler(this);


        try {
            authHandler.createUser("Test", "Test");
            authHandler.createUser("test", "test");
        } catch (Exception e) {
            e.printStackTrace();
        }

        authHandler = new AuthHandler(this);

        usernameInput = (EditText) findViewById(R.id.username);
        passwordInput = (EditText) findViewById(R.id.password);
        loginButton = (Button) findViewById(R.id.btnLogin);


        /*
         * User login , validates the fields that are entered
         * Will move to the main activity if authenticated
         * Displays a message if the validation fails
         */
        loginButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Override onClick.
             * Authenticate user based on username and password and start activity
             * @param view
             */
            @Override
            public void onClick(View view) {
                String username = usernameInput.getText().toString();
                String password = passwordInput.getText().toString();

                try {
                    authHandler.authenticateUser(username, password);

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("username", username);
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(LoginActivity.this, "Incorrect Details", Toast.LENGTH_LONG).show();
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