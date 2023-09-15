package com.amcwustl.dailytarot.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.amcwustl.dailytarot.MainActivity;
import com.amcwustl.dailytarot.R;
import com.amplifyframework.core.Amplify;

public class LoginActivity extends AppCompatActivity {
  private static final String TAG = "LoginActivity";

  EditText emailEditText;
  EditText passwordEditText;
  Button submitButton;
  Button signUpButton;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);

    emailEditText = findViewById(R.id.LoginActivityLoginEditTextTextEmailAddress);
    passwordEditText = findViewById(R.id.LoginActivityPasswordEditTextTextPassword);
    submitButton = findViewById(R.id.LoginActivityLoginButton);
    signUpButton = findViewById(R.id.LoginActivitySignUpButton);

    String email = getIntent().getStringExtra("email");
    if (email != null && !email.isEmpty()) {
      emailEditText.setText(email);
    } else {
      Log.i(TAG, "Email was not passed or is empty");
    }

    setupSubmitButton();
    setupSignUpButton();

  }

void setupSubmitButton() {
  submitButton.setOnClickListener(v -> {
    Amplify.Auth.signIn(
            emailEditText.getText().toString(),
            passwordEditText.getText().toString(),
            success -> {
              Log.i(TAG, "Login succeeded: " + success.toString());

              Intent goToMainActivityIntent = new Intent(LoginActivity.this, MainActivity.class);
              startActivity(goToMainActivityIntent);
            },
            failure -> {
              Log.i(TAG, "Login failed: " + failure.toString());
            }
    );
  });
}

void setupSignUpButton(){
    signUpButton.setOnClickListener(view -> {
      Intent goToSignUpIntent = new Intent(LoginActivity.this, SignUpActivity.class);
      startActivity(goToSignUpIntent);
    });
}

}