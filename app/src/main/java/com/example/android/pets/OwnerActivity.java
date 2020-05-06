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
import com.example.android.pets.data.OwnerContract;
import com.example.android.pets.data.PetDbHelper;

public class OwnerActivity extends AppCompatActivity {

    EditText nameEditText, emailEditText, phoneEditText;
    PetDbHelper mDbHelper;
    Uri mCurrentPetUri;
    Cursor ownerCursor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner);

        setTitle("owner");

        mDbHelper = new PetDbHelper(this);

        nameEditText = findViewById(R.id.OwnerName_editText);
        emailEditText = findViewById(R.id.OwnerEmail_editText);
        phoneEditText = findViewById(R.id.OwnerPhone_editText);

        Intent intent = getIntent();
        mCurrentPetUri = intent.getData();

        if (mCurrentPetUri != null) {
            ownerCursor = getOwner();

            if(ownerCursor.getCount() > 0){
                setOwnerFields(ownerCursor);
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
                addOrUpdateOwner();
                finish();
                return true;
            case R.id.action_delete:
                deleteOwner();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void addOrUpdateOwner() {
        ContentValues values = new ContentValues();
        if (ownerCursor.getCount() == 0) {//add
            long PetID = ContentUris.parseId(mCurrentPetUri);

            values.put(OwnerContract.OwnerEntry.COLUMN_OWNER_NAME, nameEditText.getText().toString());
            values.put(OwnerContract.OwnerEntry.COLUMN_OWNER_EMAIL, emailEditText.getText().toString());
            values.put(OwnerContract.OwnerEntry.COLUMN_OWNER_PHONE, phoneEditText.getText().toString());
            values.put(OwnerContract.OwnerEntry.COLUMN_PET_ID, PetID);

            SQLiteDatabase database = mDbHelper.getWritableDatabase();
            database.insert(OwnerContract.OwnerEntry.TABLE_NAME, null, values);
        } else {//update
            int ownerID = ownerCursor.getInt(0);

            values.put(OwnerContract.OwnerEntry.COLUMN_OWNER_NAME, nameEditText.getText().toString());
            values.put(OwnerContract.OwnerEntry.COLUMN_OWNER_EMAIL, emailEditText.getText().toString());
            values.put(OwnerContract.OwnerEntry.COLUMN_OWNER_PHONE, phoneEditText.getText().toString());

            SQLiteDatabase database = mDbHelper.getWritableDatabase();
            database.update(OwnerContract.OwnerEntry.TABLE_NAME, values, OwnerContract.OwnerEntry._ID + "=" + ownerID, null);
        }
    }

    public void deleteOwner() {
        int ownerID = ownerCursor.getInt(0);

        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        database.delete(OwnerContract.OwnerEntry.TABLE_NAME, OwnerContract.OwnerEntry._ID + "=" + ownerID, null);
        clearOwnerFields();
    }

    public Cursor getOwner() {
        long PetID = ContentUris.parseId(mCurrentPetUri);

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        Cursor cursor = database.rawQuery
                ("Select * " + " from " + OwnerContract.OwnerEntry.TABLE_NAME +
                        " where " + OwnerContract.OwnerEntry.COLUMN_PET_ID +
                        " = " + PetID, null);

        cursor.moveToFirst();
        database.close();

        return cursor;
    }

    public void setOwnerFields(Cursor cursor) {
        nameEditText.setText(cursor.getString(1));
        emailEditText.setText(cursor.getString(2));
        phoneEditText.setText(cursor.getString(3));
    }

    public void clearOwnerFields() {
        nameEditText.setText("");
        emailEditText.setText("");
        phoneEditText.setText("");
    }
}
