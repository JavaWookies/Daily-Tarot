package com.amcwustl.dailytarot.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;

import com.amcwustl.dailytarot.R;
import com.amcwustl.dailytarot.data.CardDbHelper;
import com.amcwustl.dailytarot.models.Card;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;


public class ReadingActivity extends BaseActivity {
  private static final String TAG = "Reading Activity";
  private static final String HAS_READING_FOR_TODAY = "HAS_READING_FOR_TODAY";
  private Button drawCardsButton;
  private Button rewardAdButton;
  private ImageView deck;
  private ImageView cardOne;
  private ImageView cardTwo;
  private ImageView cardThree;
  private CardDbHelper dbHelper;
  private RewardedAd rewardedAd;
  private int userCoinCount = 0;
  private AlertDialog interpretationDialog;
  private Button btnGetInterpretation;
  private String currentInterpretation;
  private AdView mAdView;
  private int retryDelaySeconds = 5;
  private static final int MAX_RETRY_DELAY_SECONDS = 120;
  SharedPreferences preferences;
  private boolean isAdLoadFailureRepeated = false;
  private int adLoadFailureCount = 0;
  private static final int AD_LOAD_FAILURE_THRESHOLD = 8;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    setContentView(R.layout.activity_reading);
    super.onCreate(savedInstanceState);

    preferences = PreferenceManager.getDefaultSharedPreferences(this);

    initializeMobileAds();

    cardOne = findViewById(R.id.ReadingActivityDrawnCardOne);
    cardTwo = findViewById(R.id.ReadingActivityDrawnCardTwo);
    cardThree = findViewById(R.id.ReadingActivityDrawnCardThree);
    deck = findViewById(R.id.ReadingActivityDeckImage);

    btnGetInterpretation = findViewById(R.id.DailyCardActivityViewCardDetailsButton);
    btnGetInterpretation.setVisibility(View.GONE);
    btnGetInterpretation.setOnClickListener(view -> showInterpretationModal());
    drawCardsButton = findViewById(R.id.ReadingActivityDrawCardsButton);
    rewardAdButton = findViewById(R.id.ReadingActivityRewardAdButton);
    dbHelper = new CardDbHelper(this);
    setupCardTypes();
    setupDrawCardsButton();
    setupRewardAd();
    setupRewardAdButton();
    checkReadingForToday();

    mAdView = findViewById(R.id.adView);
    AdRequest adRequest = new AdRequest.Builder().build();
    mAdView.loadAd(adRequest);

  }

  @Override
  protected void onResume() {
    super.onResume();
    setupRewardAd();
    if (mAdView != null) {
      mAdView.resume();
    }
  }

  @Override
  public void onPause() {
    if (mAdView != null) {
      mAdView.pause();
    }
    super.onPause();
  }

  @Override
  public void onDestroy() {
    if (mAdView != null) {
      mAdView.destroy();
    }
    super.onDestroy();
  }

  private void setupCardTypes() {
    String cardType = preferences.getString(UserSettingsActivity.CARD_TYPE_TAG, "");
    String resourceName = "cover" + cardType;

    @SuppressLint("DiscouragedApi") int resourceId = getResources().getIdentifier(resourceName, "drawable", getPackageName());
    if (resourceId != 0) {
      deck.setImageResource(resourceId);
      cardOne.setImageResource(resourceId);
      cardTwo.setImageResource(resourceId);
      cardThree.setImageResource(resourceId);
    } else {
      Log.e(TAG, "Resource not found for card type: " + cardType);
    }

  }

  private void checkReadingForToday() {
    SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);

    @SuppressLint("SimpleDateFormat") String todayKey = new SimpleDateFormat("yyyy-MM-dd").format(new Date());



    if(!sharedPreferences.getBoolean(todayKey + HAS_READING_FOR_TODAY, false)) {
      userCoinCount = 10;
    }

    runOnUiThread(this::updateUIBasedOnCoinCount);

  }

  private void updateUIBasedOnCoinCount() {
    if (userCoinCount > 0) {

      drawCardsButton.setVisibility(View.VISIBLE);
      rewardAdButton.setVisibility(View.GONE);
    } else {

      drawCardsButton.setVisibility(View.GONE);
      rewardAdButton.setVisibility(View.VISIBLE);
    }
  }

  private void initializeMobileAds() {
    MobileAds.initialize(this, initializationStatus -> {
    });
  }

  private void setupDrawCardsButton() {
    drawCardsButton.setOnClickListener(view -> drawThreeRandomCards());
  }

  private void drawThreeRandomCards() {

    List<Card> drawnCards = new ArrayList<>();
    cardOne.setRotation(0);
    cardTwo.setRotation(0);
    cardThree.setRotation(0);

    Random random = new Random();
    int maxCardId = 78;
    HashSet<Integer> seenCards = new HashSet<>();
    while (drawnCards.size() < 3) {
      Integer randomCardId = random.nextInt(maxCardId) + 1;
      Card card = dbHelper.getCardById(Long.valueOf(randomCardId));
      if (card != null && !seenCards.contains(randomCardId)) {
        seenCards.add(randomCardId);
        drawnCards.add(card);
      }
    }

    for (Card card : drawnCards) {

      int randomOrientation = random.nextInt(2);
      card.setOrientation(randomOrientation);
    }

    setupCardImages(drawnCards);
    btnGetInterpretation.setVisibility(View.VISIBLE);


    StringBuilder interpretation = new StringBuilder();
    for (int i = 0; i < 3; i++){
      if(i == 0){
        interpretation.append(drawnCards.get(0).getIntPast()).append(" ");
      } else if (i == 1) {
        interpretation.append(drawnCards.get(1).getIntPresent()).append(" ");
      } else {
        interpretation.append(drawnCards.get(2).getIntFuture());
      }
    }

    currentInterpretation = interpretation.toString();
    userCoinCount -= 10;
    setupRewardAd();
    markReadingForToday();
    runOnUiThread(this::updateUIBasedOnCoinCount);
  }

  private void markReadingForToday() {
    SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
    SharedPreferences.Editor editor = sharedPreferences.edit();

    @SuppressLint("SimpleDateFormat") String todayKey = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

    editor.putBoolean(todayKey + HAS_READING_FOR_TODAY, true);
    editor.apply();
  }

  private void setupCardImages(List<Card> drawnCards) {
    List<ImageView> imageViewList = new ArrayList<>();
    imageViewList.add(cardOne);
    imageViewList.add(cardTwo);
    imageViewList.add(cardThree);

    for (int i = 0; i < 3; i++) {
      Card card = drawnCards.get(i);
      String cardName = card.getNameShort();
      String cardType = preferences.getString(UserSettingsActivity.CARD_TYPE_TAG, "");
      String resourceName = cardName + cardType;
      @SuppressLint("DiscouragedApi") int resourceId = getResources().getIdentifier(resourceName, "drawable", getPackageName());

      if (resourceId != 0) {
        ImageView imageView = imageViewList.get(i);
        imageView.setImageResource(resourceId);
        if (card.getOrientation() == 1) {
          imageView.setRotation(180f);
        }

        final int cardIndex = i;
        imageView.setOnClickListener(v -> navigateToCardDetail(drawnCards.get(cardIndex).getId()));
      } else {
        Log.e(TAG, "Resource not found for card: " + cardName);
      }
    }
  }

  private void showInterpretationModal() {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    View view = getLayoutInflater().inflate(R.layout.interpretation_modal, null);

    TextView tvInterpretationContent = view.findViewById(R.id.tv_interpretation_content);
    if (currentInterpretation != null) {
      tvInterpretationContent.setText(currentInterpretation);
    }
    Button btnCloseModal = view.findViewById(R.id.btn_close_modal);
    btnCloseModal.setOnClickListener(v -> interpretationDialog.dismiss());

    builder.setView(view);
    interpretationDialog = builder.create();
    interpretationDialog.show();
  }

  private void navigateToCardDetail(Long cardId) {
    Intent intent = new Intent(ReadingActivity.this, CardDetailActivity.class);
    intent.putExtra("card_id", cardId);
    startActivity(intent);
  }


  private void setupRewardAd() {
    AdRequest adRequest = new AdRequest.Builder().build();
    RewardedAd.load(this, "ca-app-pub-3940256099942544/5224354917",
            adRequest, new RewardedAdLoadCallback() {
              @Override
              public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                // Handle the error.
                Log.d(TAG, loadAdError.toString());
                rewardedAd = null;
                adLoadFailureCount++;

                if (adLoadFailureCount >= AD_LOAD_FAILURE_THRESHOLD) {
                  isAdLoadFailureRepeated = true;
                  handlePotentialAdBlocker();
                } else {
                  new Handler().postDelayed(() -> {
                    setupRewardAd();
                    // Double the delay for the next possible retry
                    retryDelaySeconds = Math.min(retryDelaySeconds * 2, MAX_RETRY_DELAY_SECONDS);
                  }, retryDelaySeconds * 1000L);
                }
              }

              @Override
              public void onAdLoaded(@NonNull RewardedAd ad) {
                retryDelaySeconds = 5;
                rewardedAd = ad;
                Log.d(TAG, "Ad was loaded.");
                adLoadFailureCount = 0;
                isAdLoadFailureRepeated = false;
              }
            });
  }

  private void handlePotentialAdBlocker() {
    runOnUiThread(() -> {
      AlertDialog.Builder builder = new AlertDialog.Builder(ReadingActivity.this);
      builder.setTitle("Ad Loading Issue");
      builder.setMessage("We're unable to load the reward ad required to get a new reading, which may be due to a network issue or an ad blocker. If you have global ad blocking enabled, please consider disabling in order to support this application remaining free. The next reading is enabled but features may not work as intended. ");
      builder.setPositiveButton("OK", (dialog, id) -> {
      });
      AlertDialog dialog = builder.create();
      dialog.show();
      userCoinCount += 10;
      updateUIBasedOnCoinCount();
      adLoadFailureCount = 0;
      isAdLoadFailureRepeated = false;
    });
  }

  public void setupRewardAdButton() {
    rewardAdButton.setOnClickListener(view -> {
      if (rewardedAd != null) {
        Activity activityContext = ReadingActivity.this;
        rewardedAd.show(activityContext, rewardItem -> {
          // Handle the reward.
          Log.d(TAG, "The user earned the reward.");
          int rewardAmount = rewardItem.getAmount();
          String rewardType = rewardItem.getType();
          Log.d(TAG, "Earned " + rewardAmount + " " + rewardType);
          userCoinCount += rewardAmount;
          runOnUiThread(this::updateUIBasedOnCoinCount);

          setupRewardAd();
        });

        rewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
          @Override
          public void onAdClicked() {
            // Called when a click is recorded for an ad.
            Log.d(TAG, "Ad was clicked.");

          }

          @Override
          public void onAdDismissedFullScreenContent() {
            // Called when ad is dismissed.
            // Set the ad reference to null so you don't show the ad a second time.
            Log.d(TAG, "Ad dismissed fullscreen content.");
            rewardedAd = null;

          }

          @Override
          public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
            // Called when ad fails to show.
            Log.e(TAG, "Ad failed to show fullscreen content.");
            rewardedAd = null;

          }

          @Override
          public void onAdImpression() {
            // Called when an impression is recorded for an ad.
            Log.d(TAG, "Ad recorded an impression.");
          }

          @Override
          public void onAdShowedFullScreenContent() {
            // Called when ad is shown.
            Log.d(TAG, "Ad showed fullscreen content.");
          }
        });
      } else {
        Log.d(TAG, "The rewarded ad wasn't ready yet.");
        setupRewardAd();
      }
    });
  }

}