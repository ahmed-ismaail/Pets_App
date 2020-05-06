package com.example.android.pets.data;

import android.provider.BaseColumns;

public final class FoodContract {
    public static final class FoodEntry implements BaseColumns {
        public static final String TABLE_NAME = "FoodTable";
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_FOOD_NAME = "food";
        public static final String COLUMN_FOOD_AMOUNT = "amount";
        public static final String COLUMN_FOOD_TIMES_PER_DAY = "timesPerDay";
        public static final String COLUMN_PET_ID = "petID";
    }
}
