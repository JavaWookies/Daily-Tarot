package com.amcwustl.dailytarot.adapters;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amcwustl.dailytarot.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;


import java.util.List;

public class ReadingAdapter extends RecyclerView.Adapter<ReadingAdapter.ReadingViewHolder> {

    List<String[]> readings;

    Context callingActivity;



    public ReadingAdapter(List<String[]> readings, Context callingActivity){
        this.readings = readings;
        this.callingActivity = callingActivity;
    }

    @NonNull
    @Override
    public ReadingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View readingFragment = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_past_reading, parent, false);
        return new ReadingViewHolder(readingFragment);
    }

    @Override
    public void onBindViewHolder(@NonNull ReadingViewHolder holder, int position) {
        String[] reading = readings.get(position);
        TextView readingDateTextView = (TextView) holder.itemView.findViewById(R.id.PastReadingFragmentDate);
        ImageView cardImageOne = (ImageView) holder.itemView.findViewById(R.id.imageView1);
        ImageView cardImageTwo = (ImageView) holder.itemView.findViewById(R.id.imageView2);
        ImageView cardImageThree = (ImageView) holder.itemView.findViewById(R.id.imageView3);

        cardImageOne.setRotation(0f);
        cardImageTwo.setRotation(0f);
        cardImageThree.setRotation(0f);

        readingDateTextView.setText(reading[0]);

        int imageOneResId = holder.itemView.getContext().getResources().getIdentifier(
               reading[1],
                "drawable",
                holder.itemView.getContext().getPackageName()
        );

        int imageTwoResId = holder.itemView.getContext().getResources().getIdentifier(
                reading[3],
                "drawable",
                holder.itemView.getContext().getPackageName()
        );

        int imageThreeResId = holder.itemView.getContext().getResources().getIdentifier(
                reading[5],
                "drawable",
                holder.itemView.getContext().getPackageName()
        );

        loadCardImage(cardImageOne, reading[1], reading[2]);
        loadCardImage(cardImageTwo, reading[3], reading[4]);
        loadCardImage(cardImageThree, reading[5], reading[6]);


    }

    private void loadCardImage(ImageView imageView, String imageName, String orientation) {
        int imageResId = imageView.getContext().getResources().getIdentifier(
                imageName,
                "drawable",
                imageView.getContext().getPackageName()
        );


        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.cover)
                .error(R.drawable.cover);


        Glide.with(imageView.getContext())
                .load(imageResId)
                .apply(requestOptions)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView);

        if(orientation.equals("1")){
            imageView.setRotation(180f);
        }
    }

    @Override
    public int getItemCount() {
        return readings.size();
    }

    public static class ReadingViewHolder extends RecyclerView.ViewHolder {
        public ReadingViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}

