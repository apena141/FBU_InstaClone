package com.example.fbu_instaclone;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class LoginActivity extends AppCompatActivity {

    public static final String TAG = "LoginActivity";
    Button btLogin;
    Button btSignup;
    EditText etUsername;
    EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btLogin = findViewById(R.id.btLogin);
        btSignup= findViewById(R.id.btSignup);
        etPassword = findViewById(R.id.etPassword);
        etUsername = findViewById(R.id.etUsername);

        btSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(etPassword.getText()) == true || TextUtils.isEmpty(etUsername.getText()) == true){
                    if(TextUtils.isEmpty(etPassword.getText()) == true){
                        etPassword.setError("Password cannot be empty");
                    }
                    if(TextUtils.isEmpty(etUsername.getText()) == true){
                        etUsername.setError("Username cannot be empty");
                    }
                    return;
                } else {
                    ParseUser user = new ParseUser();
                    user.setUsername(etUsername.getText().toString());
                    user.setPassword(etPassword.getText().toString());
                    user.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e == null){
                                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                LoginActivity.this.startActivity(i);
                            }else{
                                Log.e(TAG, "Exception: " + e.getMessage());
                                return;
                            }
                        }
                    });
                }
            }
        });
    }

}