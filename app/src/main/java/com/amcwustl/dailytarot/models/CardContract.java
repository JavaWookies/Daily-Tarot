package com.amcwustl.dailytarot.models;

import android.provider.BaseColumns;

public class CardContract {
    private CardContract(){}

    public static class CardEntry implements BaseColumns {
        public static final String TABLE_NAME = "tarot_cards";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_NAME_SHORT = "name_short";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_VALUE = "value";
        public static final String COLUMN_VALUE_INT = "value_int";
        public static final String COLUMN_MEANING_UP = "meaning_up";
        public static final String COLUMN_MEANING_REV = "meaning_rev";
        public static final String COLUMN_DESC = "desc";
    }

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + CardEntry.TABLE_NAME + " (" +
                    CardEntry._ID + " INTEGER PRIMARY KEY," +
                    CardEntry.COLUMN_TYPE + " TEXT," +
                    CardEntry.COLUMN_NAME_SHORT + " TEXT," +
                    CardEntry.COLUMN_NAME + " TEXT," +
                    CardEntry.COLUMN_VALUE + " TEXT," +
                    CardEntry.COLUMN_VALUE_INT + " INTEGER," +
                    CardEntry.COLUMN_MEANING_UP + " TEXT," +
                    CardEntry.COLUMN_MEANING_REV + " TEXT," +
                    CardEntry.COLUMN_DESC + " TEXT" +
                    ")";
}
