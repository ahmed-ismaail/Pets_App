package com.example.android.pets;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.android.pets.data.ClerkContract;
import com.example.android.pets.data.PetDbHelper;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {

    EditText nameEditText, emailEditText, phoneEditText;
    TextInputLayout passwordEditText;
    Button saveButton;
    ImageView editIcon;

    private PetDbHelper Dbhelper;
    Cursor cursor;
    GoogleSignInClient mGoogleSignInClient;
    int clerkId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        setTitle("Edit Account");

        Dbhelper = new PetDbHelper(this);

        emailEditText = findViewById(R.id.AccountEmail_editText);
        emailEditText.setEnabled(false);//this to make it not editable
        nameEditText = findViewById(R.id.AccountName_editText);
        nameEditText.setEnabled(false);
        phoneEditText = findViewById(R.id.AccountPhone_editText);
        phoneEditText.setEnabled(false);
        passwordEditText = findViewById(R.id.AccountPassword_editText);
        passwordEditText.setEnabled(false);
        saveButton = findViewById(R.id.save_button);
        saveButton.setEnabled(false);
        editIcon = findViewById(R.id.editIcon_imageView);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        final GoogleSignInAccount googleAccount = GoogleSignIn.getLastSignedInAccount(ProfileActivity.this);
        if(googleAccount != null) {
            String name = googleAccount.getGivenName();
            String email = googleAccount.getEmail();

            nameEditText.setText(name);
            emailEditText.setText(email);
            phoneEditText.setHint("");
        } else {
            //this email is coming from the logIn page
            cursor = getClerk(LoginActivity.EMAIL);

            clerkId = cursor.getInt(0);
            nameEditText.setText(cursor.getString(1));
            emailEditText.setText(cursor.getString(2));
            Objects.requireNonNull(passwordEditText.getEditText()).setText(cursor.getString(3));
            phoneEditText.setText(cursor.getString(4));
        }

        editIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(googleAccount != null){
                    Toast.makeText(ProfileActivity.this,"It's A Google Account You Can't Edit",Toast.LENGTH_SHORT).show();
                }
                else {
                    nameEditText.setEnabled(true);
                    phoneEditText.setEnabled(true);
                    passwordEditText.setEnabled(true);
                    saveButton.setEnabled(true);
                }
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateClerkInfo(clerkId);
                nameEditText.setEnabled(false);
                phoneEditText.setEnabled(false);
                passwordEditText.setEnabled(false);
                saveButton.setEnabled(false);
            }
        });

    }

    public Cursor getClerk(String email) {
        SQLiteDatabase db = Dbhelper.getReadableDatabase();

        Cursor cursor = db.rawQuery
                ("Select * " + " from " + ClerkContract.ClerkEntry.TABLE_NAME +
                        " where " + ClerkContract.ClerkEntry.COLUMN_CLERK_EMAIL +
                        " = '" + email + "'", null);

        cursor.moveToFirst();
        db.close();

        return cursor;
    }

    public void updateClerkInfo(int clerkID) {
        ContentValues values = new ContentValues();
        values.put(ClerkContract.ClerkEntry.COLUMN_CLERK_NAME, nameEditText.getText().toString());
        values.put(ClerkContract.ClerkEntry.COLUMN_CLERK_PASSWORD, passwordEditText.getEditText().getText().toString());
        values.put(ClerkContract.ClerkEntry.COLUMN_CLERK_PHONE, phoneEditText.getText().toString());

        SQLiteDatabase database = Dbhelper.getWritableDatabase();
        database.update(ClerkContract.ClerkEntry.TABLE_NAME, values, ClerkContract.ClerkEntry._ID + "=" +  clerkID ,null);
    }
}
