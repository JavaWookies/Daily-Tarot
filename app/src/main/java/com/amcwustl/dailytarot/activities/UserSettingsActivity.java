package com.amcwustl.dailytarot.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.preference.PreferenceManager;

import com.amcwustl.dailytarot.R;
import com.amplifyframework.auth.AuthUser;

public class UserSettingsActivity extends AppCompatActivity {
    public static final String CARD_TYPE_TAG = "cardType";
    SharedPreferences preferences;
    AuthUser authUser;
    Spinner cardDisplaySpinner;
    Button saveSettingsButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        cardDisplaySpinner = findViewById(R.id.UserSettingsCardTypeSpinner);
        saveSettingsButton = findViewById(R.id.UserSettingsActivitySaveButton);

        setupCardDisplaySpinner();
        setupSaveSettingsButton();
    }

    void setupCardDisplaySpinner(){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.card_display_options,
                android.R.layout.simple_spinner_item
        );


        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        cardDisplaySpinner.setAdapter(adapter);
    }

    void setupSaveSettingsButton() {
        saveSettingsButton.setOnClickListener(v -> {
            String selectedValue = cardDisplaySpinner.getSelectedItem().toString();
            String tagValue;

            if(selectedValue.equals("Gold")){
                tagValue="";
            } else {
                tagValue="_light";
            }

            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(CARD_TYPE_TAG, tagValue);
            editor.apply();

            Toast.makeText(UserSettingsActivity.this, "Settings saved!", Toast.LENGTH_SHORT).show();
        });
    }
}

