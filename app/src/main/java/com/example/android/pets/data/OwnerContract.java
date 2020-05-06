package com.example.android.pets.data;

import android.provider.BaseColumns;

public class OwnerContract {
    public static final class OwnerEntry implements BaseColumns {
        public static final String TABLE_NAME = "Owner";
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_OWNER_NAME = "name";
        public static final String COLUMN_OWNER_EMAIL = "email";
        public static final String COLUMN_OWNER_PHONE = "phone";
        public static final String COLUMN_PET_ID = "petID";
    }
}
