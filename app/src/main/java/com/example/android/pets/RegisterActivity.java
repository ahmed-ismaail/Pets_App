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
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    EditText nameEditText, emailEditText;
    TextInputLayout passwordTextInput, confirmPasswordTextInput;
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
        passwordTextInput = findViewById(R.id.password_editText);
        confirmPasswordTextInput = findViewById(R.id.confirmPassword_editText);
        registerButton = findViewById(R.id.register_button);


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String password = Objects.requireNonNull(passwordTextInput.getEditText()).getText().toString();
                String confirmPassword = Objects.requireNonNull(confirmPasswordTextInput.getEditText()).getText().toString();

                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) ||
                    TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)){
                    Toast.makeText(RegisterActivity.this, "you have to fill all fields", Toast.LENGTH_SHORT).show();
                } else if (!TextUtils.isEmpty(password) && !TextUtils.isEmpty(confirmPassword) &&
                        !password.equals(confirmPassword)) {
                    Toast.makeText(RegisterActivity.this, "password doesn't match", Toast.LENGTH_SHORT).show();
                } else if (!TextUtils.isEmpty(email) && !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailEditText.setError("Invalid email");
                } else if (isAlreadyExist(email)) {
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
        values.put(ClerkContract.ClerkEntry.COLUMN_CLERK_PASSWORD, Objects.requireNonNull(passwordTextInput.getEditText()).getText().toString());

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
