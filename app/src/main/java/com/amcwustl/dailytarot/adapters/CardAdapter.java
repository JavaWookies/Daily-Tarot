package com.amcwustl.dailytarot.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amcwustl.dailytarot.R;
import com.amcwustl.dailytarot.models.Card;
import com.amcwustl.dailytarot.activities.CardDetailActivity;
import com.bumptech.glide.Glide;

import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

  private final List<Card> cards;
  private final Context context;
  private final OnItemClickListener listener;

  public CardAdapter(List<Card> cards, Context context, OnItemClickListener listener) {
    this.cards = cards;
    this.context = context;
    this.listener = listener;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);
    return new ViewHolder(view, listener, cards);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    Card card = cards.get(position);

    int imageResId = holder.itemView.getContext().getResources().getIdentifier(
            card.getNameShort(),
            "drawable",
            holder.itemView.getContext().getPackageName()
    );

    Glide.with(holder.itemView.getContext())
            .load(imageResId)
            .placeholder(R.drawable.cover)
            .error(R.drawable.cover)
            .into(holder.cardImage);
  }

  @Override
  public int getItemCount() {
    return cards.size();
  }

  public interface OnItemClickListener {
    void onItemClick(Card card);
  }


  public static class ViewHolder extends RecyclerView.ViewHolder {
    ImageView cardImage;
    List<Card> cards;

    public ViewHolder(@NonNull View itemView, final OnItemClickListener listener, List<Card> cardList) {
      super(itemView);
      cardImage = itemView.findViewById(R.id.CardDetailActivitySingleCardImageView);
      this.cards = cardList;

      itemView.setOnClickListener(v -> {
        int position = getAdapterPosition();
        if (position != RecyclerView.NO_POSITION && listener != null) {
          listener.onItemClick(cards.get(position));
        }
      });
    }
  }
}

