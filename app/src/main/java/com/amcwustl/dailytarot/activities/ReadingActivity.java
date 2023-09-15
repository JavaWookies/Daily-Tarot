package com.amcwustl.dailytarot.activities;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.amcwustl.dailytarot.R;
import com.amcwustl.dailytarot.data.CardDbHelper;
import com.amcwustl.dailytarot.models.Card;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;

import com.amplifyframework.core.model.query.Where;
import com.amplifyframework.datastore.generated.model.Reading;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;

import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;


public class ReadingActivity extends AppCompatActivity {
  private static final String TAG = "Reading Activity";
  private Button drawCardsButton;
  private Button rewardAdButton;
  private ImageView deck;
  private ImageView cardOne;
  private ImageView cardTwo;
  private ImageView cardThree;
  private CardDbHelper dbHelper;
  private String userId;
  private RewardedAd rewardedAd;
  private int userCoinCount = 0;
  SharedPreferences preferences;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_reading);

    preferences = PreferenceManager.getDefaultSharedPreferences(this);

    Amplify.Auth.getCurrentUser(
            authUser -> userId = authUser.getUserId(),
            error -> Log.e("MyAmplifyApp", "Error getting current user", error)
    );

    initializeMobileAds();

    cardOne = findViewById(R.id.ReadingActivityDrawnCardOne);
    cardTwo = findViewById(R.id.ReadingActivityDrawnCardTwo);
    cardThree = findViewById(R.id.ReadingActivityDrawnCardThree);
    deck = findViewById(R.id.ReadingActivityDeckImage);

    drawCardsButton = findViewById(R.id.ReadingActivityDrawCardsButton);
    rewardAdButton = findViewById(R.id.ReadingActivityRewardAdButton);
    dbHelper = new CardDbHelper(this);
    setupCardTypes();
    setupDrawCardsButton();
    setupRewardAd();
    setupRewardAdButton();
    checkReadingForToday();

  }

  @Override
  protected void onResume() {
    super.onResume();
    setupRewardAd();
  }

  private void setupCardTypes() {
    String cardType = preferences.getString(UserSettingsActivity.CARD_TYPE_TAG, "");
    String resourceName = "cover" + cardType;

    int resourceId = getResources().getIdentifier(resourceName, "drawable", getPackageName());
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
    Date currentDate = new Date();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    String formattedDate = dateFormat.format(currentDate);

    List<Reading> readings = new ArrayList<>();

    Amplify.API.query(
            ModelQuery.list(Reading.class),
            response -> {
              for (Reading reading : response.getData()) {
                if (reading.getUserId().equals(userId) && reading.getDateCreated().equals(formattedDate)) {
                  readings.add(reading);
                }
              }

              if (!(readings.size() > 0)) {
                userCoinCount = 10;
              }

              runOnUiThread(this::updateUIBasedOnCoinCount);
            },
            error -> {
              Log.e(TAG, "Error retrieving readings for today: " + error);

            }
    );
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
    drawCardsButton.setOnClickListener(view -> {
      drawThreeRandomCards();
    });
  }

  private void drawThreeRandomCards() {

    List<Card> drawnCards = new ArrayList<>();
    cardOne.setRotation(0);
    cardTwo.setRotation(0);
    cardThree.setRotation(0);



    TextView description = findViewById(R.id.ReadingActivityInterpretationPlaceHolder);

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

    StringBuilder interpretation = new StringBuilder();
    for (Card c : drawnCards) {
      interpretation.append(c.getName()).append(": ");
      if (c.getOrientation() == 0) {
        interpretation.append(c.getMeaningUp()).append("\n");
      } else {
        interpretation.append(c.getMeaningRev()).append("\n");
      }
    }

    String result = interpretation.toString();
    description.setText(result);
    userCoinCount -= 10;
    pushReadingToDynamo(drawnCards, result);
    setupRewardAd();
    runOnUiThread(this::updateUIBasedOnCoinCount);


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
      int resourceId = getResources().getIdentifier(resourceName, "drawable", getPackageName());

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

  private void navigateToCardDetail(Long cardId) {
    Intent intent = new Intent(ReadingActivity.this, CardDetailActivity.class);
    intent.putExtra("card_id", cardId);
    startActivity(intent);
  }


  private void pushReadingToDynamo(List<Card> drawnCards, String interpretation) {
    Date currentDate = new Date();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    String formattedDate = dateFormat.format(currentDate);


    Reading readingToSave = Reading.builder()
            .userId(userId)
            .dateCreated(formattedDate)
            .cardOneId(drawnCards.get(0).getId().intValue())
            .cardOneOrientation(drawnCards.get(0).getOrientation())
            .cardTwoId(drawnCards.get(1).getId().intValue())
            .cardTwoOrientation(drawnCards.get(1).getOrientation())
            .cardThreeId(drawnCards.get(2).getId().intValue())
            .cardThreeOrientation(drawnCards.get(2).getOrientation())
            .interpretation(interpretation)
            .build();

    Amplify.API.mutate(
            ModelMutation.create(readingToSave),
            successResponse -> {
              Log.i(TAG, "ReadingActivity.pushReadingToDynamo(): made Reading successfully");
            },
            failureResponse -> Log.i(TAG, "ReadingActivity.pushReadingToDynamo(): failed with this response" + failureResponse)
    );
  }


  public void setupRewardAd() {
    AdRequest adRequest = new AdRequest.Builder().build();
    RewardedAd.load(this, "ca-app-pub-3940256099942544/5224354917",
            adRequest, new RewardedAdLoadCallback() {
              @Override
              public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                // Handle the error.
                Log.d(TAG, loadAdError.toString());
                rewardedAd = null;
              }

              @Override
              public void onAdLoaded(@NonNull RewardedAd ad) {
                rewardedAd = ad;
                Log.d(TAG, "Ad was loaded.");
              }
            });
  }

  public void setupRewardAdButton() {
    rewardAdButton.setOnClickListener(view -> {
      if (rewardedAd != null) {
        Activity activityContext = ReadingActivity.this;
        rewardedAd.show(activityContext, new OnUserEarnedRewardListener() {
          @Override
          public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
            // Handle the reward.
            Log.d(TAG, "The user earned the reward.");
            int rewardAmount = rewardItem.getAmount();
            String rewardType = rewardItem.getType();
            Log.d(TAG, "Earned " + rewardAmount + " " + rewardType);
            userCoinCount += rewardAmount;
            runOnUiThread(() -> updateUIBasedOnCoinCount());

            setupRewardAd();
          }
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
          public void onAdFailedToShowFullScreenContent(AdError adError) {
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
      }
    });
  }

}