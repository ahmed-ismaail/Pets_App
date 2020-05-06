package com.example.android.pets;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.android.pets.data.FoodContract;
import com.example.android.pets.data.PetDbHelper;


public class FoodActivity extends AppCompatActivity {

    EditText foodEditText, foodAmountEditText, timesPerDayEditText;
    PetDbHelper mDbHelper;
    Uri mCurrentPetUri;
    Cursor foodCursor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pets_food);

        setTitle("Food");

        mDbHelper = new PetDbHelper(this);

        foodEditText = findViewById(R.id.Food_editText);
        foodAmountEditText = findViewById(R.id.Amount_editText);
        timesPerDayEditText = findViewById(R.id.times_editText);

        Intent intent = getIntent();
        mCurrentPetUri = intent.getData();

        if(mCurrentPetUri != null) {
            foodCursor = getFood();

            if (foodCursor.getCount() > 0) {
                setFoodFields(foodCursor);
            }
        }
    }

    //here I'm using the same options menu in the editor activity
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                addOrUpdateFood();
                finish();
                return true;
            case R.id.action_delete:
                deleteFood();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void addOrUpdateFood() {
        if (foodCursor.getCount() == 0) {//add
            long PetID = ContentUris.parseId(mCurrentPetUri);

            ContentValues values = new ContentValues();
            values.put(FoodContract.FoodEntry.COLUMN_FOOD_NAME, foodEditText.getText().toString());
            values.put(FoodContract.FoodEntry.COLUMN_FOOD_AMOUNT, foodAmountEditText.getText().toString());
            values.put(FoodContract.FoodEntry.COLUMN_FOOD_TIMES_PER_DAY, timesPerDayEditText.getText().toString());
            values.put(FoodContract.FoodEntry.COLUMN_PET_ID, PetID);

            SQLiteDatabase database = mDbHelper.getWritableDatabase();
            database.insert(FoodContract.FoodEntry.TABLE_NAME, null, values);
        } else {//update
            int foodID = foodCursor.getInt(0);

            ContentValues values = new ContentValues();
            values.put(FoodContract.FoodEntry.COLUMN_FOOD_NAME, foodEditText.getText().toString());
            values.put(FoodContract.FoodEntry.COLUMN_FOOD_AMOUNT, foodAmountEditText.getText().toString());
            values.put(FoodContract.FoodEntry.COLUMN_FOOD_TIMES_PER_DAY, timesPerDayEditText.getText().toString());

            SQLiteDatabase database = mDbHelper.getWritableDatabase();
            database.update(FoodContract.FoodEntry.TABLE_NAME, values, FoodContract.FoodEntry._ID + "=" + foodID, null);
        }
    }

    public void deleteFood() {
        int foodID = foodCursor.getInt(0);

        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        database.delete(FoodContract.FoodEntry.TABLE_NAME, FoodContract.FoodEntry._ID + "=" + foodID, null);
        clearFoodFields();
    }

    public Cursor getFood() {
        long PetID = ContentUris.parseId(mCurrentPetUri);

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        Cursor cursor = database.rawQuery
                ("Select * " + " from " + FoodContract.FoodEntry.TABLE_NAME +
                        " where " + FoodContract.FoodEntry.COLUMN_PET_ID +
                        " = " + PetID, null);

        cursor.moveToFirst();
        database.close();

        return cursor;
    }

    public void setFoodFields(Cursor cursor){
        foodEditText.setText(cursor.getString(1));
        foodAmountEditText.setText(cursor.getString(2));
        timesPerDayEditText.setText(cursor.getString(3));
    }

    public void clearFoodFields(){
        foodEditText.setText("");
        foodAmountEditText.setText("");
        timesPerDayEditText.setText("");
    }
}
