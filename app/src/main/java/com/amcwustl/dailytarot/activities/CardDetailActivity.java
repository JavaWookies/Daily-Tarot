package com.amcwustl.dailytarot.activities;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.preference.PreferenceManager;

import com.amcwustl.dailytarot.R;
import com.amcwustl.dailytarot.data.CardDbHelper;
import com.amcwustl.dailytarot.models.Card;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

@SuppressWarnings("resource")
public class CardDetailActivity extends BaseActivity {

  private AdView mAdView;
  private int contentColor;
  private int titleColor;
  private ChipGroup chipGroup;
  SharedPreferences preferences;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    setContentView(R.layout.activity_card_detail);
    super.onCreate(savedInstanceState);

    MobileAds.initialize(this, initializationStatus -> {
    });

    mAdView = findViewById(R.id.adView);
    AdRequest adRequest = new AdRequest.Builder().build();
    mAdView.loadAd(adRequest);

    preferences = PreferenceManager.getDefaultSharedPreferences(this);

    TextView nameTextView = findViewById(R.id.CardDetailActivityCardNameTextView);
    TextView descTextView = findViewById(R.id.CardDetailActivityCardDescTextView);
    ImageView cardImageView = findViewById(R.id.CardDetailActivitySingleCardImageView);
    ChipGroup chipGroup = findViewById(R.id.CardDetailActivityChipGroup);

    Long cardId = getIntent().getLongExtra("card_id", -1);

    contentColor = ContextCompat.getColor(this, R.color.translucent_off_white);
    titleColor = ContextCompat.getColor(this, R.color.white);


    // Retrieve the card details using this ID
    CardDbHelper dbHelper = new CardDbHelper(this);
    Card card = dbHelper.getCardById(cardId);

    if (card != null) {
      nameTextView.setText(card.getName());

      SpannableStringBuilder cardInfoBuilder = new SpannableStringBuilder();
      cardInfoBuilder.append(getStyledTitles("Card Description: "));
      cardInfoBuilder.append("\n");
      cardInfoBuilder.append(getStyledCardInfo(card.getDesc()));
      cardInfoBuilder.append("\n\n");
      cardInfoBuilder.append(getStyledTitles("Card Meaning Face Up: "));
      cardInfoBuilder.append("\n");
      cardInfoBuilder.append(getStyledCardInfo(card.getMeaningUp()));
      cardInfoBuilder.append("\n\n");
      cardInfoBuilder.append(getStyledTitles("Card Meaning Reversed: "));
      cardInfoBuilder.append("\n");
      cardInfoBuilder.append(card.getMeaningRev());

      descTextView.setText(cardInfoBuilder);

      String cardName = card.getNameShort();
      String cardType = preferences.getString(UserSettingsActivity.CARD_TYPE_TAG, "");
      String resourceName = cardName + cardType;
      @SuppressLint("DiscouragedApi") int resourceId = getResources().getIdentifier(resourceName, "drawable", getPackageName());

      cardImageView.setImageResource(resourceId);
      displayAssociatedWords(card.getAssociatedWords());
    }
  }

  private void displayAssociatedWords(String associatedWords) {
    ChipGroup chipGroup = findViewById(R.id.CardDetailActivityChipGroup);

    String[] words = associatedWords.split(",");
    Typeface typeface = ResourcesCompat.getFont(this, R.font.libre_caslon_header);

    for (int i=0; i < words.length; i++){
      Chip chip = new Chip(this);
      chip.setText(words[i].trim());
      chip.setTextColor(ContextCompat.getColor(this, R.color.chip_text_color));
      chip.setTypeface(typeface);
      chip.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

      if(i == 0){
        chip.setChipBackgroundColorResource(R.color.chip_background_color_one);
      } else if (i == 1) {
        chip.setChipBackgroundColorResource(R.color.chip_background_color_two);
      } else if (i == 2) {
        chip.setChipBackgroundColorResource(R.color.chip_background_color_three);
      } else if (i == 3) {
        chip.setChipBackgroundColorResource(R.color.chip_background_color_four);
      } else {
        chip.setChipBackgroundColorResource(R.color.chip_background_color_one);
      }
      chipGroup.addView(chip);
    }

  }


  @Override
  protected void onResume() {
    super.onResume();
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

  private SpannableString getStyledTitles(String title) {
    SpannableString spannableTitle = new SpannableString(title);

    spannableTitle.setSpan(new ForegroundColorSpan(titleColor), 0, title.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
    return spannableTitle;
  }

  private SpannableString getStyledCardInfo(String content) {
    SpannableString spannableContent = new SpannableString(content);

    spannableContent.setSpan(new ForegroundColorSpan(contentColor), 0, content.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);

    return spannableContent;
  }

}

