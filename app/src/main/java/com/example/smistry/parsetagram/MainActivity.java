package com.example.smistry.parsetagram;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class MainActivity extends AppCompatActivity {

    private EditText usernameInput;
    private EditText passwordInput;
    private Button loginButton;
    private Button signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//
//        ParseUser currentUser = ParseUser.getCurrentUser();
//
//        if(currentUser != null){
//            Intent i = new Intent(MainActivity.this, HomeActivity.class);
//            startActivity(i);
//            Toast.makeText(MainActivity.this, "Automatically logged in", Toast.LENGTH_SHORT).show();
//        }

        usernameInput = findViewById(R.id.username);
        passwordInput = findViewById(R.id.password);
        loginButton = findViewById(R.id.login_button);
        signUpButton = findViewById(R.id.signUp_button);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = usernameInput.getText().toString();
                final String password = passwordInput.getText().toString();
                login(username,password);

            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 final String username = usernameInput.getText().toString();
                 final String password = passwordInput.getText().toString();
                signUp(username,password);
            }
        });

    }

    private void login (String username, String password){
        //TODO- add username and password
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if(e == null){
                    Log.d("Login Activity", "Login successful");
                    Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_LONG).show();
                    final Intent intent = new Intent(MainActivity.this, bottomNav.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Log.d("Login Activity", "Login Failure");
                    e.printStackTrace();
                }
            }
        });
    }

    private void signUp(String username, String password){
        // Create the ParseUser
        ParseUser user = new ParseUser();

        user.setUsername(username);
        user.setPassword(password);

        // Invoke signUpInBackground
        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("Sign Up Activity", "Sign Up Successful");
                    Toast.makeText(MainActivity.this, "Sign Up Successful", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();

                } else {
                    // Sign up didn't succeed. Look at the ParseException
                    // to figure out what went wrong
                    Log.d("Sign Up Activity", "Sign Up Failure");
                    Toast.makeText(MainActivity.this, "This username is already taken! Please choose another one.", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });


    }
}
