package com.amcwustl.dailytarot.activities;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.widget.SwitchCompat;
import androidx.preference.PreferenceManager;

import com.amcwustl.dailytarot.R;
import com.amcwustl.dailytarot.utilities.NotificationHelper;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class UserSettingsActivity extends BaseActivity {
    public static final String CARD_TYPE_TAG = "cardType";
    private static final String TAG = "UserSettingsActivity";
    SharedPreferences preferences;
    Spinner cardDisplaySpinner;
    Button saveSettingsButton;
    SwitchCompat notificationToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_user_settings);
        super.onCreate(savedInstanceState);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        cardDisplaySpinner = findViewById(R.id.UserSettingsCardTypeSpinner);
        saveSettingsButton = findViewById(R.id.UserSettingsActivitySaveButton);
        notificationToggle = findViewById(R.id.notification_toggle);

        setupCardDisplaySpinner();
        setupSaveSettingsButton();
        initializeNotificationToggle();
        setupNotificationToggleListener();

        AdView mAdViewBanner = findViewById(R.id.adView);
        AdRequest adRequestBanner = new AdRequest.Builder().build();
        mAdViewBanner.loadAd(adRequestBanner);

    }

    void setupCardDisplaySpinner(){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.card_display_options,
                R.layout.spinner_item
        );


        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        cardDisplaySpinner.setAdapter(adapter);
    }

    private void initializeNotificationToggle() {
        boolean areNotificationsEnabled = preferences.getBoolean("notifications_enabled", true);
        notificationToggle.setChecked(areNotificationsEnabled);
    }

    private void setupNotificationToggleListener() {
        notificationToggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("notifications_enabled", isChecked);
            editor.apply();

            if (!isChecked) {
                // Cancel notifications if the user has disabled them
                NotificationHelper.cancelScheduledNotification(this, 1);
                NotificationHelper.cancelScheduledNotification(this, 2);
            }
        });
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

