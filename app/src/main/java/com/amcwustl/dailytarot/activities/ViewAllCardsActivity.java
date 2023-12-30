package com.amcwustl.dailytarot.activities;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amcwustl.dailytarot.R;
import com.amcwustl.dailytarot.adapters.CardAdapter;
import com.amcwustl.dailytarot.data.CardDbHelper;
import com.amcwustl.dailytarot.models.Card;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ViewAllCardsActivity extends BaseActivity {
  private static final String TAG = "ViewAllCardsActivity";

  RecyclerView recyclerView;
  CardAdapter cardAdapter;
  List<Card> tarotCardsList = new ArrayList<>();

  @SuppressWarnings("resource")
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    setContentView(R.layout.activity_view_all_cards);
    super.onCreate(savedInstanceState);

    Log.d(TAG, "onCreate: Started.");

    tarotCardsList.clear();
    Log.d(TAG, "onCreate: Cleared tarotCardsList.");

    CardDbHelper dbHelper = new CardDbHelper(this);
    tarotCardsList.addAll(dbHelper.getAllCards());
    Log.d(TAG, "onCreate: Added cards to tarotCardsList. Size: " + tarotCardsList.size());

    setupRecyclerView();
  }

  @Override
  public void onTrimMemory(int level) {
    super.onTrimMemory(level);
    Glide.get(this);
    Glide.get(this).trimMemory(level);
  }

  @Override
  public void onLowMemory() {
    super.onLowMemory();
    Glide.get(this);
    Glide.get(this).clearMemory();
  }

  void setupRecyclerView(){
    recyclerView = findViewById(R.id.ViewAllCardsActivityRecyclerView);
    GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
    recyclerView.setLayoutManager(layoutManager);
    Log.d(TAG, "setupRecyclerView: Set GridLayoutManager.");

    int spaceInPixels = getResources().getDimensionPixelSize(R.dimen.grid_spacing);
    recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
      @Override
      public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        int position = parent.getChildAdapterPosition(view);
        Log.d(TAG, "getItemOffsets: Setting item offsets for position " + position);

        int halfSpace = spaceInPixels / 2;

        outRect.top = halfSpace;
        outRect.bottom = spaceInPixels;
        outRect.left = halfSpace;
        outRect.right = halfSpace;

        int totalItemCount = Objects.requireNonNull(parent.getAdapter()).getItemCount();
        if (position >= totalItemCount - (totalItemCount % 3)) {
          outRect.bottom = 0;
        }
      }
    });
    Log.d(TAG, "setupRecyclerView: Added ItemDecoration.");

    cardAdapter = new CardAdapter(tarotCardsList, this, card -> {
      Intent intent = new Intent(ViewAllCardsActivity.this, CardDetailActivity.class);
      intent.putExtra("card_id", card.getId());
      startActivity(intent);
    });
    recyclerView.setAdapter(cardAdapter);
  }
}
