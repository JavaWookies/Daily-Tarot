package com.amcwustl.dailytarot.utilities;

import android.content.SharedPreferences;

import com.amcwustl.dailytarot.data.CardDbHelper;
import com.amcwustl.dailytarot.models.Card;

import java.util.ArrayList;
import java.util.List;

public class CardStateUtil {

    public static void saveReadingState(SharedPreferences preferences, List<Card> cards, String prefKey) {
        SharedPreferences.Editor editor = preferences.edit();
        for (int i = 0; i < cards.size(); i++) {
            editor.putLong(prefKey + "CardId" + i, cards.get(i).getId());
            editor.putInt(prefKey + "CardOrientation" + i, cards.get(i).getOrientation());
        }
        editor.apply();
    }

    public static List<Card> restoreReadingState(SharedPreferences preferences, CardDbHelper dbHelper, String prefKey) {
        List<Card> restoredCards = new ArrayList<>();
        int i = 0;
        while (preferences.contains(prefKey + "CardId" + i)) {
            long cardId = preferences.getLong(prefKey + "CardId" + i, -1);
            int orientation = preferences.getInt(prefKey + "CardOrientation" + i, 0);
            Card card = dbHelper.getCardById(cardId);
            if (card != null) {
                card.setOrientation(orientation);
                restoredCards.add(card);
            }
            i++;
        }
        return restoredCards;
    }
}
