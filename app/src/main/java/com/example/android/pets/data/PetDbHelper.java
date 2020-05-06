/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.pets.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.pets.data.PetContract.PetEntry;
import com.example.android.pets.data.ClerkContract.ClerkEntry;
import com.example.android.pets.data.FoodContract.FoodEntry;
import com.example.android.pets.data.OwnerContract.OwnerEntry;

/**
 * Database helper for Pets app. Manages database creation and version management.
 */
public class PetDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "shelter.db";

    //if you change the database schema you must change the database version
    private static final int DATABASE_VERSION = 10;

    public PetDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * This is called when the database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a String that contains the SQL statement to create the pets table
        String SQL_CREATE_PETS_TABLE = "CREATE TABLE " + PetEntry.TABLE_NAME + " ("
                + PetEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PetEntry.COLUMN_PET_NAME + " TEXT NOT NULL, "
                + PetEntry.COLUMN_PET_BREED + " TEXT, "
                + PetEntry.COLUMN_PET_GENDER + " INTEGER NOT NULL, "
                + PetEntry.COLUMN_PET_WEIGHT + " INTEGER NOT NULL DEFAULT 0);";

        // Create a String that contains the SQL statement to create the clerk table
        String SQL_CREATE_CLERK_TABLE = "CREATE TABLE " + ClerkEntry.TABLE_NAME + " ("
                + ClerkEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ClerkEntry.COLUMN_CLERK_NAME + " TEXT NOT NULL, "
                + ClerkEntry.COLUMN_CLERK_EMAIL + " TEXT NOT NULL, "
                + ClerkEntry.COLUMN_CLERK_PASSWORD + " TEXT NOT NULL, "
                + ClerkEntry.COLUMN_CLERK_PHONE + " TEXT);";

        // Create a String that contains the SQL statement to create the FOOD table
        String SQL_CREATE_FOOD_TABLE = "CREATE TABLE " + FoodEntry.TABLE_NAME + " ("
                + FoodEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + FoodEntry.COLUMN_FOOD_NAME + " TEXT NOT NULL, "
                + FoodEntry.COLUMN_FOOD_AMOUNT + " INTEGER NOT NULL, "
                + FoodEntry.COLUMN_FOOD_TIMES_PER_DAY + " INTEGER NOT NULL, "
                + FoodEntry.COLUMN_PET_ID + " INTEGER NOT NULL, "
                + " FOREIGN KEY( " + FoodEntry.COLUMN_PET_ID + " ) REFERENCES " + PetEntry.TABLE_NAME + " ( " + PetEntry._ID + " ));";

        // Create a String that contains the SQL statement to create the Owner table
        String SQL_CREATE_OWNER_TABLE = "CREATE TABLE " + OwnerEntry.TABLE_NAME + " ("
                + OwnerEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + OwnerEntry.COLUMN_OWNER_NAME + " TEXT NOT NULL, "
                + OwnerEntry.COLUMN_OWNER_EMAIL + " TEXT NOT NULL, "
                + OwnerEntry.COLUMN_OWNER_PHONE + " TEXT NOT NULL, "
                + OwnerEntry.COLUMN_PET_ID + " INTEGER NOT NULL, "
                + " FOREIGN KEY( " + OwnerEntry.COLUMN_PET_ID + " ) REFERENCES " + PetEntry.TABLE_NAME + " ( " + PetEntry._ID + " ));";

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_PETS_TABLE);
        db.execSQL(SQL_CREATE_CLERK_TABLE);
        db.execSQL(SQL_CREATE_FOOD_TABLE);
        db.execSQL(SQL_CREATE_OWNER_TABLE);
    }

    /**
     * This is called when the database needs to be upgraded.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + ClerkEntry.TABLE_NAME);
        db.execSQL("drop table if exists " + PetEntry.TABLE_NAME);
        db.execSQL("drop table if exists " + FoodEntry.TABLE_NAME);
        db.execSQL("drop table if exists " + OwnerEntry.TABLE_NAME);

        onCreate(db);
    }
}