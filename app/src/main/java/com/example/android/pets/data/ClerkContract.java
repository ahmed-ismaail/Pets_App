package com.example.android.pets.data;

import android.provider.BaseColumns;

public final class ClerkContract {

    public static final class ClerkEntry implements BaseColumns {

        public static final String TABLE_NAME = "CLERK";
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_CLERK_NAME = "name";
        public static final String COLUMN_CLERK_EMAIL = "email";
        public static final String COLUMN_CLERK_PASSWORD = "password";
        public static final String COLUMN_CLERK_PHONE = "phone";
    }
}
