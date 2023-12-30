package com.amcwustl.dailytarot.data;

import static com.amcwustl.dailytarot.models.CardContract.SQL_CREATE_ENTRIES;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.amcwustl.dailytarot.R;
import com.amcwustl.dailytarot.models.Card;
import com.amcwustl.dailytarot.models.CardContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CardDbHelper extends SQLiteOpenHelper {
  private static final int DATABASE_VERSION = 3;
  private static final String DATABASE_NAME = "TarotCardDatabase.db";


  public CardDbHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    db.execSQL(SQL_CREATE_ENTRIES);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    db.execSQL("DROP TABLE IF EXISTS " + CardContract.CardEntry.TABLE_NAME);
    onCreate(db);
  }



  public boolean isDatabaseEmpty() {
    SQLiteDatabase db = this.getReadableDatabase();
    String countQuery = "SELECT count(*) FROM " + CardContract.CardEntry.TABLE_NAME;
    Cursor cursor = db.rawQuery(countQuery, null);
    int count = 0;
    if (cursor != null) {
      cursor.moveToFirst();
      count = cursor.getInt(0);
      cursor.close();

    }
    db.close();
    return count == 0;
  }

  public void populateDatabaseWithJsonData(Context context) {
    if (!isDatabaseEmpty()) {
      Log.d("CardDbHelper", "Database already has cards. No need to populate again.");
      return;
    }

    String jsonData = loadJsonFromRawResource(context, R.raw.card_data);

    if (jsonData != null && !jsonData.isEmpty()) {
      try {
        JSONObject jsonRoot = new JSONObject(jsonData);
        JSONArray jsonCards = jsonRoot.getJSONArray("cards");

        for (int i = 0; i < jsonCards.length(); i++) {
          JSONObject jsonCard = jsonCards.getJSONObject(i);
          Card card = new Card();

          card.setType(jsonCard.getString("type"));
          card.setNameShort(jsonCard.getString("name_short"));
          card.setName(jsonCard.getString("name"));
          card.setMeaningUp(jsonCard.getString("meaning_up"));
          card.setMeaningRev(jsonCard.getString("meaning_rev"));
          card.setDesc(jsonCard.getString("desc"));
          card.setIntPast(jsonCard.getString("int_past"));
          card.setIntPresent(jsonCard.getString("int_present"));
          card.setIntFuture(jsonCard.getString("int_future"));
          card.setIntPastRev(jsonCard.getString("int_past_rev"));
          card.setIntPresentRev(jsonCard.getString("int_present_rev"));
          card.setIntFutureRev(jsonCard.getString("int_future_rev"));
          card.setAssociatedWords(jsonCard.getString("associated_words"));

          this.insertCard(card);
        }
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }
  }

  @SuppressLint("Range")
  public List<Card> getAllCards() {
    List<Card> cards = new ArrayList<>();
    SQLiteDatabase db = this.getReadableDatabase();
    String selectQuery = "SELECT * FROM " + CardContract.CardEntry.TABLE_NAME;
    Cursor cursor = db.rawQuery(selectQuery, null);
    if (cursor.moveToFirst()) {
      do {
        Card card = new Card();
        card.setId(cursor.getLong((cursor.getColumnIndex(CardContract.CardEntry._ID))));
        card.setType(cursor.getString(cursor.getColumnIndex(CardContract.CardEntry.COLUMN_TYPE)));
        card.setNameShort(cursor.getString(cursor.getColumnIndex(CardContract.CardEntry.COLUMN_NAME_SHORT)));
        card.setName(cursor.getString(cursor.getColumnIndex(CardContract.CardEntry.COLUMN_NAME)));
        card.setMeaningUp(cursor.getString(cursor.getColumnIndex(CardContract.CardEntry.COLUMN_MEANING_UP)));
        card.setMeaningRev(cursor.getString(cursor.getColumnIndex(CardContract.CardEntry.COLUMN_MEANING_REV)));
        card.setDesc(cursor.getString(cursor.getColumnIndex(CardContract.CardEntry.COLUMN_DESC)));
        card.setIntPast(cursor.getString(cursor.getColumnIndex(CardContract.CardEntry.COLUMN_INT_PAST)));
        card.setIntPresent(cursor.getString(cursor.getColumnIndex(CardContract.CardEntry.COLUMN_INT_PRESENT)));
        card.setIntFuture(cursor.getString(cursor.getColumnIndex(CardContract.CardEntry.COLUMN_INT_FUTURE)));
        card.setIntPastRev(cursor.getString(cursor.getColumnIndex(CardContract.CardEntry.COLUMN_INT_PAST_REV)));
        card.setIntPresentRev(cursor.getString(cursor.getColumnIndex(CardContract.CardEntry.COLUMN_INT_PRESENT_REV)));
        card.setIntFutureRev(cursor.getString(cursor.getColumnIndex(CardContract.CardEntry.COLUMN_INT_FUTURE_REV)));
        card.setAssociatedWords(cursor.getString(cursor.getColumnIndex(CardContract.CardEntry.COLUMN_ASSOCIATED_WORDS)));


        card.setOrientation(0);
        cards.add(card);
      } while (cursor.moveToNext());
    }
    cursor.close();
    return cards;
  }

  public void insertCard(Card card) {
    SQLiteDatabase db = getWritableDatabase();
    ContentValues values = new ContentValues();
    values.put(CardContract.CardEntry.COLUMN_TYPE, card.getType());
    values.put(CardContract.CardEntry.COLUMN_NAME_SHORT, card.getNameShort());
    values.put(CardContract.CardEntry.COLUMN_NAME, card.getName());
    values.put(CardContract.CardEntry.COLUMN_MEANING_UP, card.getMeaningUp());
    values.put(CardContract.CardEntry.COLUMN_MEANING_REV, card.getMeaningRev());
    values.put(CardContract.CardEntry.COLUMN_DESC, card.getDesc());
    values.put(CardContract.CardEntry.COLUMN_INT_PAST, card.getIntPast());
    values.put(CardContract.CardEntry.COLUMN_INT_PRESENT, card.getIntPresent());
    values.put(CardContract.CardEntry.COLUMN_INT_FUTURE, card.getIntFuture());
    values.put(CardContract.CardEntry.COLUMN_INT_PAST_REV, card.getIntPastRev());
    values.put(CardContract.CardEntry.COLUMN_INT_PRESENT_REV, card.getIntPresentRev());
    values.put(CardContract.CardEntry.COLUMN_INT_FUTURE_REV, card.getIntFutureRev());
    values.put(CardContract.CardEntry.COLUMN_ASSOCIATED_WORDS, card.getAssociatedWords());


    db.insert(CardContract.CardEntry.TABLE_NAME, null, values);
    db.close();
  }


  private String loadJsonFromRawResource(Context context, int resourceId) {
    try {
      InputStream inputStream = context.getResources().openRawResource(resourceId);
      InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
      BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
      StringBuilder jsonStringBuilder = new StringBuilder();
      String line;

      while ((line = bufferedReader.readLine()) != null) {
        jsonStringBuilder.append(line);
      }

      bufferedReader.close();
      return jsonStringBuilder.toString();
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  public Card getCardById(Long cardId) {
    SQLiteDatabase db = getReadableDatabase();
    Card card = null;

    String[] projection = {
            CardContract.CardEntry._ID,
            CardContract.CardEntry.COLUMN_TYPE,
            CardContract.CardEntry.COLUMN_NAME_SHORT,
            CardContract.CardEntry.COLUMN_NAME,
            CardContract.CardEntry.COLUMN_MEANING_UP,
            CardContract.CardEntry.COLUMN_MEANING_REV,
            CardContract.CardEntry.COLUMN_DESC,
            CardContract.CardEntry.COLUMN_INT_PAST,
            CardContract.CardEntry.COLUMN_INT_PRESENT,
            CardContract.CardEntry.COLUMN_INT_FUTURE,
            CardContract.CardEntry.COLUMN_INT_PAST_REV,
            CardContract.CardEntry.COLUMN_INT_PRESENT_REV,
            CardContract.CardEntry.COLUMN_INT_FUTURE_REV,
            CardContract.CardEntry.COLUMN_ASSOCIATED_WORDS
    };

    String selection = CardContract.CardEntry._ID + " = ?";
    String[] selectionArgs = {String.valueOf(cardId)};

    Cursor cursor = db.query(
            CardContract.CardEntry.TABLE_NAME,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            null
    );

    if (cursor != null) {
      if (cursor.moveToFirst()) {
        card = new Card();
        card.setId(cursor.getLong(cursor.getColumnIndexOrThrow(CardContract.CardEntry._ID)));
        card.setType(cursor.getString(cursor.getColumnIndexOrThrow(CardContract.CardEntry.COLUMN_TYPE)));
        card.setNameShort(cursor.getString(cursor.getColumnIndexOrThrow(CardContract.CardEntry.COLUMN_NAME_SHORT)));
        card.setName(cursor.getString(cursor.getColumnIndexOrThrow(CardContract.CardEntry.COLUMN_NAME)));
        card.setMeaningUp(cursor.getString(cursor.getColumnIndexOrThrow(CardContract.CardEntry.COLUMN_MEANING_UP)));
        card.setMeaningRev(cursor.getString(cursor.getColumnIndexOrThrow(CardContract.CardEntry.COLUMN_MEANING_REV)));
        card.setDesc(cursor.getString(cursor.getColumnIndexOrThrow(CardContract.CardEntry.COLUMN_DESC)));
        card.setIntPast(cursor.getString(cursor.getColumnIndexOrThrow(CardContract.CardEntry.COLUMN_INT_PAST)));
        card.setIntPresent(cursor.getString(cursor.getColumnIndexOrThrow(CardContract.CardEntry.COLUMN_INT_PRESENT)));
        card.setIntFuture(cursor.getString(cursor.getColumnIndexOrThrow(CardContract.CardEntry.COLUMN_INT_FUTURE)));
        card.setIntPastRev(cursor.getString(cursor.getColumnIndexOrThrow(CardContract.CardEntry.COLUMN_INT_PAST_REV)));
        card.setIntPresentRev(cursor.getString(cursor.getColumnIndexOrThrow(CardContract.CardEntry.COLUMN_INT_PRESENT_REV)));
        card.setIntFutureRev(cursor.getString(cursor.getColumnIndexOrThrow(CardContract.CardEntry.COLUMN_INT_FUTURE_REV)));
        card.setAssociatedWords(cursor.getString(cursor.getColumnIndexOrThrow(CardContract.CardEntry.COLUMN_ASSOCIATED_WORDS)));


      }
      cursor.close();
    }
    db.close();
    return card;
  }
}
