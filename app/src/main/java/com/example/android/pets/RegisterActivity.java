package com.example.android.pets;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.android.pets.data.ClerkContract;
import com.example.android.pets.data.PetDbHelper;

public class RegisterActivity extends AppCompatActivity {

    EditText nameEditText, emailEditText, passwordEditText, confirmPasswordEditText;
    Button registerButton;
    private PetDbHelper mDbHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        setTitle("Register");

        mDbHelper = new PetDbHelper(this);

        nameEditText = findViewById(R.id.name_editText);
        emailEditText = findViewById(R.id.email_editText);
        passwordEditText = findViewById(R.id.password_editText);
        confirmPasswordEditText = findViewById(R.id.confirmPassword_editText);
        registerButton = findViewById(R.id.register_button);

        if (TextUtils.isEmpty(nameEditText.getText())) {
            nameEditText.setError("name is required");
        }
        if (TextUtils.isEmpty(emailEditText.getText())) {
            emailEditText.setError("email is required");
        }
        if (TextUtils.isEmpty(passwordEditText.getText())) {
            passwordEditText.setError("password is required");
        }
        if (TextUtils.isEmpty(confirmPasswordEditText.getText())) {
            confirmPasswordEditText.setError("confirm password is required");
        }

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(nameEditText.getText()) || TextUtils.isEmpty(emailEditText.getText()) ||
                        TextUtils.isEmpty(passwordEditText.getText()) || TextUtils.isEmpty(confirmPasswordEditText.getText())) {
                    Toast.makeText(RegisterActivity.this, "you have to fill all fields", Toast.LENGTH_SHORT).show();
                } else if (!TextUtils.isEmpty(passwordEditText.getText()) && !TextUtils.isEmpty(confirmPasswordEditText.getText()) &&
                        !passwordEditText.getText().toString().equals(confirmPasswordEditText.getText().toString())) {
                    confirmPasswordEditText.setError("password doesn't match");
                } else if (!TextUtils.isEmpty(emailEditText.getText()) &&
                        !Patterns.EMAIL_ADDRESS.matcher(emailEditText.getText().toString()).matches()) {
                    emailEditText.setError("Invalid email");
                } else if (isAlreadyExist(emailEditText.getText().toString())) {
                    emailEditText.setError("email already exist choose another one ");
                } else {
                    addClerk();
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    public void addClerk() {
        ContentValues values = new ContentValues();
        values.put(ClerkContract.ClerkEntry.COLUMN_CLERK_NAME, nameEditText.getText().toString());
        values.put(ClerkContract.ClerkEntry.COLUMN_CLERK_EMAIL, emailEditText.getText().toString());
        values.put(ClerkContract.ClerkEntry.COLUMN_CLERK_PASSWORD, passwordEditText.getText().toString());

        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        database.insert(ClerkContract.ClerkEntry.TABLE_NAME, null, values);
    }

    public boolean isAlreadyExist(String email) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery
                ("Select " + ClerkContract.ClerkEntry.COLUMN_CLERK_EMAIL +
                        " from " + ClerkContract.ClerkEntry.TABLE_NAME +
                        " where " + ClerkContract.ClerkEntry.COLUMN_CLERK_EMAIL +
                        " = '" + email + "'", null);

        cursor.moveToFirst();
        db.close();

        if (cursor.getCount() == 0) {
            return false;
        }else
            return cursor.getString(0).equals(email);
    }
}
