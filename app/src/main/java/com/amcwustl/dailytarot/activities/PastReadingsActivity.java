package com.amcwustl.dailytarot.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.amcwustl.dailytarot.R;
import com.amcwustl.dailytarot.adapters.ReadingAdapter;
import com.amcwustl.dailytarot.data.CardDbHelper;
import com.amcwustl.dailytarot.models.Card;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Reading;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PastReadingsActivity extends AppCompatActivity {
    private final String TAG = "PastReadingsActivity";
    SharedPreferences preferences;
    List<String[]> readings = new ArrayList<>();
    String userId;
    CardDbHelper dbHelper;

    ReadingAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_readings);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        dbHelper = new CardDbHelper(this);

        Amplify.Auth.getCurrentUser(
                authUser -> userId = authUser.getUserId(),
                error -> Log.e("MyAmplifyApp", "Error getting current user", error)
        );

        updateReadingsListFromDatabase();
        setupRecyclerView();
    }


    void updateReadingsListFromDatabase() {
        String cardType = preferences.getString(UserSettingsActivity.CARD_TYPE_TAG, "");
        readings.clear();
        Amplify.API.query(
                ModelQuery.list(Reading.class),
                success -> {
                    Log.i(TAG, "Retrieved readings successfully.");
                    for (Reading reading : success.getData()) {
                        if (reading.getUserId().equals(userId)) {
                            String[] readingToAdd = new String[7];
                            readingToAdd[0] = reading.getDateCreated();
                            readingToAdd[1] = dbHelper.getCardById((long) reading.getCardOneId()).getNameShort() + cardType;
                            readingToAdd[2] = reading.getCardOneOrientation().toString();
                            readingToAdd[3] = dbHelper.getCardById((long) reading.getCardTwoId()).getNameShort() + cardType;
                            readingToAdd[4] = reading.getCardTwoOrientation().toString();
                            readingToAdd[5] = dbHelper.getCardById((long) reading.getCardThreeId()).getNameShort() + cardType;
                            readingToAdd[6] = reading.getCardThreeOrientation().toString();
                            readings.add(readingToAdd);
                        }
                    }

                    sortReadingsByFirstValue();


                },
                failure -> Log.i(TAG, "Did not read tasks successfully.")
        );
    }

    public void sortReadingsByFirstValue() {
        Collections.sort(readings, Comparator.comparing(reading -> reading[0]));
        runOnUiThread(() -> {
            adapter.notifyDataSetChanged();
        });

    }

    void setupRecyclerView() {
        RecyclerView pastReadingsRecyclerView = (RecyclerView) findViewById(R.id.PastReadingsActivityRecyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        pastReadingsRecyclerView.setLayoutManager(layoutManager);

        adapter = new ReadingAdapter(readings, this);
        pastReadingsRecyclerView.setAdapter(adapter);
    }
}

