package com.amcwustl.dailytarot.models;

import android.provider.BaseColumns;

public class CardContract {
    private CardContract(){}

    public static class CardEntry implements BaseColumns {
        public static final String TABLE_NAME = "tarot_cards";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_NAME_SHORT = "name_short";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_MEANING_UP = "meaning_up";
        public static final String COLUMN_MEANING_REV = "meaning_rev";
        public static final String COLUMN_DESC = "desc";
        public static final String COLUMN_INT_PAST = "int_past";
        public static final String COLUMN_INT_PRESENT = "int_present";
        public static final String COLUMN_INT_FUTURE = "int_future";
        public static final String COLUMN_INT_PAST_REV = "int_past_rev";
        public static final String COLUMN_INT_PRESENT_REV = "int_present_rev";
        public static final String COLUMN_INT_FUTURE_REV = "int_future_rev";
        public static final String COLUMN_ASSOCIATED_WORDS = "associated_words";
    }

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + CardEntry.TABLE_NAME + " (" +
                    CardEntry._ID + " INTEGER PRIMARY KEY," +
                    CardEntry.COLUMN_TYPE + " TEXT," +
                    CardEntry.COLUMN_NAME_SHORT + " TEXT," +
                    CardEntry.COLUMN_NAME + " TEXT," +
                    CardEntry.COLUMN_MEANING_UP + " TEXT," +
                    CardEntry.COLUMN_MEANING_REV + " TEXT," +
                    CardEntry.COLUMN_DESC + " TEXT," +
                    CardEntry.COLUMN_INT_PAST + " TEXT," +
                    CardEntry.COLUMN_INT_PRESENT + " TEXT," +
                    CardEntry.COLUMN_INT_FUTURE + " TEXT," +
                    CardEntry.COLUMN_INT_PAST_REV + " TEXT," +
                    CardEntry.COLUMN_INT_PRESENT_REV + " TEXT," +
                    CardEntry.COLUMN_INT_FUTURE_REV + " TEXT," +
                    CardEntry.COLUMN_ASSOCIATED_WORDS + " TEXT)";


}
