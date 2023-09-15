package com.amcwustl.dailytarot.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.amcwustl.dailytarot.R;
import com.amcwustl.dailytarot.data.CardDbHelper;
import com.amcwustl.dailytarot.models.Card;

public class CardDetailActivity extends AppCompatActivity {

  // Define your views
  private TextView nameTextView, meaningUpTextView, meaningRevTextView, descTextView;
  private ImageView cardImageView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_card_detail);

    // Initialize your views
    nameTextView = findViewById(R.id.CardDetailActivityCardNameTextView);
    meaningUpTextView = findViewById(R.id.CardDetailActivityCardMeaningUpTextView);
    meaningRevTextView = findViewById(R.id.CardDetailActivityCardMeaningRevTextView);
    descTextView = findViewById(R.id.CardDetailActivityCardDescTextView);
    cardImageView = findViewById(R.id.CardDetailActivitySingleCardImageView);

    Long cardId = getIntent().getLongExtra("card_id", -1);

    // Retrieve the card details using this ID
    CardDbHelper dbHelper = new CardDbHelper(this);
    Card card = dbHelper.getCardById(cardId);

    if (card != null) {
      nameTextView.setText(card.getName());
      meaningUpTextView.setText(card.getMeaningUp());
      meaningRevTextView.setText(card.getMeaningRev());
      descTextView.setText(card.getDesc());

      int imageResId = getResources().getIdentifier(
              card.getNameShort(),
              "drawable",
              getPackageName()
      );
      cardImageView.setImageResource(imageResId);
    }
  }
}

