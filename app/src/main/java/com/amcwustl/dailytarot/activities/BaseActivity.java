package com.amcwustl.dailytarot.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.amcwustl.dailytarot.MainActivity;
import com.amcwustl.dailytarot.R;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

public class BaseActivity extends AppCompatActivity {

    public DrawerLayout drawerLayout;
    public NavigationView myNavView;
    public ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myNavView = findViewById(R.id.MainActivityNavigationView);
        drawerLayout = findViewById(R.id.my_drawer_layout);

        setupNavigation();
        initializeMobileAds();
    }

    private void initializeMobileAds() {
        MobileAds.initialize(this, initializationStatus -> {
        });
    }

    protected void setupNavigation() {
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        setupNavClick();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    protected void setupNavClick(){
        myNavView.setNavigationItemSelectedListener(item -> {

            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                Intent mainActivityIntent = new Intent(this, MainActivity.class);
                startActivity(mainActivityIntent);
                return true;
            } else if (itemId == R.id.nav_settings) {
                Intent userSettingsIntent = new Intent(this, UserSettingsActivity.class);
                startActivity(userSettingsIntent);
                return true;
            } else if (itemId == R.id.nav_library) {
                Intent viewAllCardsIntent = new Intent(this, ViewAllCardsActivity.class);
                startActivity(viewAllCardsIntent);
                return true;
            } else if (itemId == R.id.nav_reading) {
                Intent readingIntent = new Intent(this, ReadingActivity.class);
                startActivity(readingIntent);
                return true;
            } else if (itemId == R.id.nav_card_of_the_day) {
                Intent cardOfTheDayIntent = new Intent(this, DailyCardActivity.class);
                startActivity(cardOfTheDayIntent);
                return true;
            } else if (itemId == R.id.nav_quiz){
                Intent quizIntent = new Intent(this, QuizActivity.class);
                startActivity(quizIntent);
                return true;
            } else if (itemId == R.id.nav_custom_spread){
                Intent customSpreadIntent = new Intent(this, CustomSpreadActivity.class);
                startActivity(customSpreadIntent);
                return true;
            } else if (itemId == R.id.nav_rate_app){
                openAppInPlayStore();
                return true;
            }
            return false;
        });
    }

    private void openAppInPlayStore() {
        String appPackageName = getPackageName(); // Get the current package name

        try {
            // Try to open in the Play Store app
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException e) {
            // If Play Store app is not installed, open in a web browser
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

}

