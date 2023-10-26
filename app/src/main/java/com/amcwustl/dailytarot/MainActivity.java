package com.amcwustl.dailytarot;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.MenuItem;

import com.amcwustl.dailytarot.activities.BaseActivity;
import com.amcwustl.dailytarot.activities.ReadingActivity;

import com.amcwustl.dailytarot.activities.UserSettingsActivity;
import com.amcwustl.dailytarot.activities.ViewAllCardsActivity;
import com.amcwustl.dailytarot.data.CardDbHelper;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.ActionBarDrawerToggle;

import androidx.annotation.NonNull;



public class MainActivity extends BaseActivity {
  private final String TAG = "MainActivity";
//  public DrawerLayout drawerLayout;
//  public NavigationView myNavView;
//  public ActionBarDrawerToggle actionBarDrawerToggle;



  @Override
  protected void onCreate(Bundle savedInstanceState) {
    setContentView(R.layout.activity_main);
    super.onCreate(savedInstanceState);

    myNavView = findViewById(R.id.MainActivityNavigationView);



//    drawerLayout = findViewById(R.id.my_drawer_layout);
//    actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);


//    drawerLayout.addDrawerListener(actionBarDrawerToggle);
//    actionBarDrawerToggle.syncState();

//    getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    CardDbHelper dbHelper = new CardDbHelper(this);

    if (dbHelper.isDatabaseEmpty()) {
      dbHelper.populateDatabaseWithJsonData(this);
      Log.d("MainActivity", "Database populated with data.");
    }


  }

//  @Override
//  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//
//    if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
//      return true;
//    }
//    return super.onOptionsItemSelected(item);
//
//  }
//
//  private void setupNavClick(){
//    myNavView.setNavigationItemSelectedListener(item -> {
//
//      int itemId = item.getItemId();
//      Log.i("MainActivity", "the logged item is:" + itemId);
//      if (itemId == R.id.nav_home) {
//        Intent mainActivityIntent = new Intent(MainActivity.this, MainActivity.class);
//        startActivity(mainActivityIntent);
//        return true;
//      } else if (itemId == R.id.nav_settings) {
//        Intent userSettingsIntent = new Intent(MainActivity.this, UserSettingsActivity.class);
//        startActivity(userSettingsIntent);
//        Log.i("MainActivity", "User Settings Clicked");
//        return true;
//      } else if (itemId == R.id.nav_library) {
//        Intent viewAllCardsIntent = new Intent(MainActivity.this, ViewAllCardsActivity.class);
//        startActivity(viewAllCardsIntent);
//        return true;
//      } else if (itemId == R.id.nav_reading) {
//        Intent readingIntent = new Intent(MainActivity.this, ReadingActivity.class);
//        startActivity(readingIntent);
//        return true;
//      }
////      drawerLayout.closeDrawer();
//      return true;
//    });
//  }
}





