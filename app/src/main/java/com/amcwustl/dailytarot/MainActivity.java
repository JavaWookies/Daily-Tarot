package com.amcwustl.dailytarot;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.preference.PreferenceManager;

import com.amcwustl.dailytarot.activities.BaseActivity;
import com.amcwustl.dailytarot.activities.CustomSpreadActivity;
import com.amcwustl.dailytarot.activities.DailyCardActivity;
import com.amcwustl.dailytarot.activities.QuizActivity;
import com.amcwustl.dailytarot.activities.ReadingActivity;
import com.amcwustl.dailytarot.activities.UserSettingsActivity;
import com.amcwustl.dailytarot.activities.ViewAllCardsActivity;
import com.amcwustl.dailytarot.data.CardDbHelper;
import com.amcwustl.dailytarot.utilities.NotificationHelper;
import com.google.android.gms.tasks.Task;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;


@SuppressWarnings("resource")
public class MainActivity extends BaseActivity {
  private static final String TAG = "Home Activity";

  private FrameLayout cardDailyReading;
  private FrameLayout cardOfTheDay;
  private FrameLayout viewAllCards;
  private FrameLayout tarotQuiz;
  private FrameLayout customSpread;
  private ImageView cardOne;
  private ImageView cardTwo;
  private ImageView cardThree;
  private ImageView cardFour;
  private ImageView cardFive;

  SharedPreferences preferences;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    setContentView(R.layout.activity_main);
    super.onCreate(savedInstanceState);

    preferences = PreferenceManager.getDefaultSharedPreferences(this);
    myNavView = findViewById(R.id.MainActivityNavigationView);
    cardDailyReading = findViewById(R.id.card_daily_reading);
    cardOfTheDay = findViewById(R.id.card_of_the_day);
    viewAllCards = findViewById(R.id.view_all_cards);
    tarotQuiz = findViewById(R.id.tarot_quiz);
    customSpread = findViewById(R.id.custom_spread);


    cardOne = findViewById(R.id.imageView_card_1);
    cardTwo = findViewById(R.id.imageView_card_2);
    cardThree = findViewById(R.id.imageView_card_3);
    cardFour = findViewById(R.id.imageView_card_4);
    cardFive = findViewById(R.id.imageView_card_5);


    setupCardNavigation();
    setupCardTypes();
    createNotificationChannel();

    CardDbHelper dbHelper = new CardDbHelper(this);

    if (dbHelper.isDatabaseEmpty()) {
      dbHelper.populateDatabaseWithJsonData(this);
    }

    int launchCount = preferences.getInt("launch_count", 0);

    launchCount++;

    SharedPreferences.Editor editor = preferences.edit();
    editor.putInt("launch_count", launchCount);
    editor.apply();

    Log.d(TAG, "Current launch count: " + launchCount);

    if (launchCount == 3) {
      promptForRating();
    }

    if (!preferences.contains("notifications_enabled")) {
      // Set default value for 'notifications_enabled' on first app launch
      editor.putBoolean("notifications_enabled", true);
      editor.apply();
    }
  }

  @Override
  protected void onResume() {
    super.onResume();
    scheduleNotificationsIfNeeded();
  }

  private void setupCardNavigation() {
    cardDailyReading.setOnClickListener(v -> {
      Intent readingIntent = new Intent(MainActivity.this, ReadingActivity.class);
      startActivity(readingIntent);
    });

    cardOfTheDay.setOnClickListener(v -> {
      Intent cardOfDayIntent = new Intent(MainActivity.this, DailyCardActivity.class);
      startActivity(cardOfDayIntent);
    });

    viewAllCards.setOnClickListener(v -> {
      Intent viewAllCardsIntent = new Intent(MainActivity.this, ViewAllCardsActivity.class);
      startActivity(viewAllCardsIntent);
    });

    tarotQuiz.setOnClickListener(v -> {
      Intent tarotQuizIntent = new Intent(MainActivity.this, QuizActivity.class);
      startActivity(tarotQuizIntent);
    });

    customSpread.setOnClickListener(v -> {
      Intent customSpreadIntent = new Intent(MainActivity.this, CustomSpreadActivity.class);
      startActivity(customSpreadIntent);
    });


  }

  private void setupCardTypes() {
    String cardType = preferences.getString(UserSettingsActivity.CARD_TYPE_TAG, "");
    String resourceName = "cover" + cardType;

    @SuppressLint("DiscouragedApi") int resourceId = getResources().getIdentifier(resourceName, "drawable", getPackageName());
    if (resourceId != 0) {
      cardOne.setImageResource(resourceId);
      cardTwo.setImageResource(resourceId);
      cardThree.setImageResource(resourceId);
      cardFour.setImageResource(resourceId);
      cardFive.setImageResource(resourceId);
    } else {
      Log.e(TAG, "Resource not found for card type: " + cardType);
    }

  }


  private void scheduleNotificationsIfNeeded() {

    boolean areNotificationsEnabled = preferences.getBoolean("notifications_enabled", true);

    NotificationHelper.cancelScheduledNotification(this, 1);
    NotificationHelper.cancelScheduledNotification(this, 2);

    if (areNotificationsEnabled) {
      NotificationHelper.scheduleNotification(this, 3 * 24 * 60 * 60 * 1000L, 1, "tarot_channel");
      NotificationHelper.scheduleNotification(this, 7 * 24 * 60 * 60 * 1000L, 2, "tarot_channel");
    }
  }


  private void createNotificationChannel() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      CharSequence name = "TarotChannel"; // Your channel name
      String description = "Tarot Notifications"; // Your channel description
      int importance = NotificationManager.IMPORTANCE_DEFAULT; // Set the importance level

      NotificationChannel channel = new NotificationChannel("tarot_channel", name, importance);
      channel.setDescription(description);

      // Register the channel with the system; you can customize additional settings here
      NotificationManager notificationManager = getSystemService(NotificationManager.class);
      notificationManager.createNotificationChannel(channel);
    }
  }

  private void promptForRating() {
    ReviewManager manager = ReviewManagerFactory.create(this);
    Task<ReviewInfo> request = manager.requestReviewFlow();
    request.addOnCompleteListener(task -> {
      if (task.isSuccessful()) {
        ReviewInfo reviewInfo = task.getResult();
        Task<Void> flow = manager.launchReviewFlow(this, reviewInfo);
        flow.addOnCompleteListener(task2 -> {
          if (task2.isSuccessful()) {
          } else {
            Log.e(TAG, "In-app review flow failed: " + task2.getException());
          }
        });
      } else {
        Log.e(TAG, "Requesting review flow failed: " + task.getException());
      }
    });

}

}





