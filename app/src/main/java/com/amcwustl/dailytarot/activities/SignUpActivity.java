package com.amcwustl.dailytarot.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.amcwustl.dailytarot.R;
import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;

public class SignUpActivity extends AppCompatActivity {
  public static final String TAG = "SignupActivity";

  EditText emailEditText;
  EditText passwordEditText;
  Button submitButton;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sign_up);

    emailEditText = findViewById(R.id.SignUpActivityEmailEditTextTextEmailAddress);
    passwordEditText = findViewById(R.id.SignUpActivityPasswordEditTextTextPassword);
    submitButton = findViewById(R.id.SignUpActivitySignUpButton);

    setupSubmitButton();

  }

  void setupSubmitButton() {
    submitButton.setOnClickListener(v -> {
      Amplify.Auth.signUp(
              emailEditText.getText().toString(),
              passwordEditText.getText().toString(),
              AuthSignUpOptions.builder()
                      .userAttribute(AuthUserAttributeKey.email(), emailEditText.getText().toString())
                      .build(),
              successResponse -> {
                Log.i(TAG, "Signup succeeded: " + successResponse.toString());

                Intent goToVerifyActivity = new Intent(SignUpActivity.this, VerifyActivity.class);
                goToVerifyActivity.putExtra("email", emailEditText.getText().toString());

                startActivity(goToVerifyActivity);
              },
              failureResponse -> {
                Log.i(TAG, "Signup failed: " + failureResponse.toString());
              }
      );
    });
  }
}