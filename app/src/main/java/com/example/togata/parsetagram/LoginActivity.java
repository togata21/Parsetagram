package com.example.togata.parsetagram;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameInput;
    private EditText passwordInput;
    private Button loginBtn;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        usernameInput = (EditText) findViewById(R.id.etUsername);
        passwordInput = (EditText) findViewById(R.id.etPassword);
        loginBtn = (Button) findViewById(R.id.bLogin);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = usernameInput.getText().toString();
                final String password = passwordInput.getText().toString();
                login(username, password);
            }
        });

        preferences = getSharedPreferences("com.example.togete.parsetagram", Context.MODE_PRIVATE);
        if (!preferences.getString("username", "").equals("")){
            login(preferences.getString("username", ""), preferences.getString("password", ""));
        }
    }

    private void login(final String username, final String password){
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e == null){
                    Log.d("LoginActivity", "Login successful!");
                    preferences.edit().putString("username", username).commit();
                    preferences.edit().putString("password", password).commit();
                    Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivityForResult(i, 1);
                    finish();
                }
                else{
                    Log.e("LoginActivity", "Login failure!");
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == 2){
                ParseUser.logOut();
                SharedPreferences preferences = getSharedPreferences("com.example.togete.parsetagram", Context.MODE_PRIVATE);
                preferences.edit().remove("username").commit();
                preferences.edit().remove("password").commit();
                ParseUser currentUser = ParseUser.getCurrentUser();
            }
        }
    }


}
