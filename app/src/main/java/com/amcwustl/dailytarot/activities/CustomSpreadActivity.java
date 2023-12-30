package com.amcwustl.dailytarot.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.preference.PreferenceManager;

import com.amcwustl.dailytarot.R;
import com.amcwustl.dailytarot.data.CardDbHelper;
import com.amcwustl.dailytarot.models.Card;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class CustomSpreadActivity extends BaseActivity {
    private static final String TAG = "Custom Spread Activity";
    private CardDbHelper dbHelper;
    private ImageView deckImageView;
    private FrameLayout droppableArea;
    private AlertDialog instructionsDialog;
    SharedPreferences preferences;
    private String cardType;
    private HashMap<Integer, Long> drawnCards;
    private int drawnCardCount = 0;
    private List<View> droppedImages;
    private LinearLayout lockLayout;
    private LinearLayout instructionsLayout;
    private LinearLayout resetLayout;
    private InterstitialAd mInterstitialAd;
    private static final String RESET_COUNT_KEY = "reset_count";
    private static final int RESET_COUNT_THRESHOLD = 3;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_custom_spread);
        super.onCreate(savedInstanceState);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        cardType = preferences.getString(UserSettingsActivity.CARD_TYPE_TAG, "");

        lockLayout = findViewById(R.id.lockLayout);
        instructionsLayout = findViewById(R.id.instructionsLayout);
        resetLayout = findViewById(R.id.clearLayout);

        deckImageView = findViewById(R.id.deckImageView);
        droppableArea = findViewById(R.id.droppableArea);


        dbHelper = new CardDbHelper(this);

        drawnCards = new HashMap<>();
        droppedImages = new ArrayList<>();

        setupCardType();
        setupDeckTouchListener();
        setupDroppableArea();
        setupInstructionsLayoutButton();
        setupInterstitial();

    }

    private void setupCardType() {
        String resourceName = "cover" + cardType;
        @SuppressLint("DiscouragedApi") int resourceId = getResources().getIdentifier(resourceName, "drawable", getPackageName());
        if (resourceId != 0) {
            deckImageView.setImageResource(resourceId);
        } else {
            Log.e(TAG, "Resource not found for card type: " + cardType);
        }
    }

    private void setupDeckTouchListener() {
        deckImageView.setOnLongClickListener(v -> {
            ImageView newCard = createNewCardImageView();

            // Initially, set the card at the deck's position or make it invisible
            newCard.setX(deckImageView.getX());
            newCard.setY(deckImageView.getY());
            droppableArea.addView(newCard);

            ClipData.Item item = new ClipData.Item((CharSequence) v.getTag());
            ClipData dragData = new ClipData(
                    (CharSequence) v.getTag(),
                    new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN},
                    item);
            View.DragShadowBuilder myShadow = new View.DragShadowBuilder(deckImageView);

            newCard.startDragAndDrop(dragData, myShadow, newCard, 0);

            return true;
        });
    }


    private ImageView createNewCardImageView() {
        ImageView imageView = new ImageView(this);
        int widthInPx = convertDpToPx(90);
        int heightInPx = convertDpToPx(155);

        imageView.setLayoutParams(new FrameLayout.LayoutParams(widthInPx, heightInPx));
        if (Objects.equals(cardType, "")) {
            imageView.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.cover, null));
        } else {
            imageView.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.cover_light, null));
        }
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setTag("newCard_" + drawnCardCount);

        return imageView;
    }

    private int convertDpToPx(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
    }

    private void setupDroppableArea() {
        droppableArea.setOnDragListener((view, dragEvent) -> {
            switch (dragEvent.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    // Retrieve the dragged view and set it visible
                    View draggedView = (View) dragEvent.getLocalState();
                    draggedView.setVisibility(View.VISIBLE);
                    return true;
                case DragEvent.ACTION_DROP:
                    draggedView = (View) dragEvent.getLocalState();

                    int cardWidth = draggedView.getWidth();
                    int cardHeight = draggedView.getHeight();

                    float dropX = dragEvent.getX() - (cardWidth / 2f);
                    float dropY = dragEvent.getY() - (cardHeight / 2f);

                    int leftBound = droppableArea.getLeft();
                    int topBound = droppableArea.getTop();
                    int rightBound = droppableArea.getRight();
                    int bottomBound = droppableArea.getBottom();

                    if (dropX >= leftBound && dropY >= topBound &&
                            dropX + cardWidth <= rightBound && dropY + cardHeight <= bottomBound) {
                        draggedView.setX(dropX);
                        draggedView.setY(dropY);
                        handleCardDropLogic(draggedView);
                        setupCardTouchListener(draggedView);
                    } else {
                        ((ViewGroup) draggedView.getParent()).removeView(draggedView);
                        return false;
                    }
                    return true;
            }
            return false;
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupCardTouchListener(View cardView) {
        cardView.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                v.startDragAndDrop(data, shadowBuilder, v, 0);
                return true;
            }
            return false;
        });
    }

    private void handleCardDropLogic(View draggedView) {
        Object tag = draggedView.getTag();
        if (tag != null && tag.toString().startsWith("newCard_")) {
            long cardId = drawRandomCard();
            drawnCards.put(drawnCardCount, cardId);
            drawnCardCount++;
            if (drawnCardCount == 1) {
                setupLockLayoutButton();
                setupResetButton();
            }
            droppedImages.add((ImageView) draggedView);
            draggedView.setTag(null);

            if (drawnCardCount == 9) {
                deckImageView.setOnLongClickListener(null);
            }
        }
    }

    private long drawRandomCard() {
        Random random = new Random();
        long maxCardId = 78;
        long randomValue;
        do {
            randomValue = (Math.abs(random.nextLong()) % maxCardId) + 1;
        } while (drawnCards.containsValue(randomValue));

        return randomValue;

    }

    private void setupLockLayoutButton() {
        lockLayout.setOnClickListener(view -> {
            ImageView lockIcon = findViewById(R.id.lockIcon);
            TextView lockText = findViewById(R.id.lockText);

            int goldColor = Color.parseColor("#FFD700");

            lockIcon.setImageResource(R.drawable.baseline_lock_24);

            lockText.setTextColor(goldColor);

            lockLayout();

        });
    }

    @SuppressWarnings("ConstantConditions")
    @SuppressLint("ClickableViewAccessibility")
    private void lockLayout() {
        deckImageView.setOnLongClickListener(null);


        for (View cardView : droppedImages) {

            ImageView card = (ImageView) cardView;

            card.setOnTouchListener(null);

            card.setOnClickListener(view -> flipCard(card, drawnCards.get(droppedImages.indexOf(cardView))));
        }
    }

    private void flipCard(ImageView card, long cardId) {

        Random random = new Random();
        int randomOrientation = random.nextInt(2);
        Card drawnCard = dbHelper.getCardById(cardId);
        drawnCard.setOrientation(randomOrientation);
        String cardName = drawnCard.getNameShort();
        String cardType = preferences.getString(UserSettingsActivity.CARD_TYPE_TAG, "");
        String resourceName = cardName + cardType;

        @SuppressLint("DiscouragedApi") int resourceId = getResources().getIdentifier(resourceName, "drawable", getPackageName());

        if (resourceId != 0) {
            float cameraDistance = 20 * getResources().getDisplayMetrics().density * card.getWidth();
            card.setCameraDistance(cameraDistance);

            ObjectAnimator firstHalfFlip = ObjectAnimator.ofFloat(card, "rotationY", 0f, 90f);
            firstHalfFlip.setDuration(250);

            ObjectAnimator secondHalfFlip = ObjectAnimator.ofFloat(card, "rotationY", -90f, 0f);
            secondHalfFlip.setDuration(250);

            firstHalfFlip.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    card.setImageResource(resourceId);
                    if (drawnCard.getOrientation() == 1) {
                        card.setRotation(180f);
                    }
                    secondHalfFlip.start();
                }
            });

            firstHalfFlip.start();
            card.setOnClickListener(null);
            card.setOnLongClickListener(view -> {
                navigateToCardDetail(drawnCard.getId());
                return true;
            });
        } else {
            Log.e(TAG, "Resource not found for card: " + cardName);
        }
    }

    private void navigateToCardDetail(Long cardId) {
        Intent intent = new Intent(CustomSpreadActivity.this, CardDetailActivity.class);
        intent.putExtra("card_id", cardId);
        startActivity(intent);
    }

    private void setupInstructionsLayoutButton() {
        instructionsLayout.setOnClickListener(view -> showInstructionsModal());
    }

    private void showInstructionsModal() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.interpretation_modal, null);
        TextView tvInterpretationContent = view.findViewById(R.id.tv_interpretation_content);
        tvInterpretationContent.setText(getString(R.string.custom_instructions_text));
        Button btnCloseModal = view.findViewById(R.id.btn_close_modal);
        btnCloseModal.setOnClickListener(v -> instructionsDialog.dismiss());

        builder.setView(view);
        instructionsDialog = builder.create();
        instructionsDialog.show();

    }

    private void setupResetButton() {
        resetLayout.setOnClickListener(view -> resetLayout());
    }


    private void resetLayout() {
        int resetCount = preferences.getInt(RESET_COUNT_KEY, 0);

        resetCount++;

        if (resetCount >= RESET_COUNT_THRESHOLD) {
            showInterstitialAd();
            resetCount = 0;
        }

        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(RESET_COUNT_KEY, resetCount);
        editor.apply();

        performReset();
    }

    private void performReset(){
        drawnCards.clear();
        drawnCardCount = 0;
        for (View cardView : droppedImages) {
            droppableArea.removeView(cardView);
        }
        droppedImages.clear();

        ImageView lockIcon = findViewById(R.id.lockIcon);
        TextView lockText = findViewById(R.id.lockText);

        int whiteColor = Color.parseColor("#FFFFFF");

        lockIcon.setImageResource(R.drawable.baseline_lock_open_24);

        lockText.setTextColor(whiteColor);

        setupDeckTouchListener();
    }

    private void setupInterstitial(){
        MobileAds.initialize(this, initializationStatus -> {});
        loadInterstitialAd();
    }

    private void loadInterstitialAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(this,"ca-app-pub-9366728814901706/5839980039", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        mInterstitialAd = interstitialAd;
                        Log.i(TAG, "onAdLoaded");
                        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                // Called when ad is dismissed.
                                Log.d(TAG, "Ad dismissed fullscreen content.");
                                mInterstitialAd = null;
                                loadInterstitialAd(); // Load a new ad
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                // Called when ad fails to show.
                                Log.e(TAG, "Ad failed to show fullscreen content.");
                                mInterstitialAd = null;
                            }

                            @Override
                            public void onAdClicked() {
                                // Called when a click is recorded for an ad.
                                Log.d(TAG, "Ad was clicked.");
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
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        Log.d(TAG, loadAdError.toString());
                        mInterstitialAd = null;
                    }
                });
    }

    private void showInterstitialAd(){
        if (mInterstitialAd != null) {
            mInterstitialAd.show(CustomSpreadActivity.this);
        } else {
            Log.d("TAG", "The interstitial ad wasn't ready yet.");
        }
    }

}