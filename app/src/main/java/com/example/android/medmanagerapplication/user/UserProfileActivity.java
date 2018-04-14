package com.example.android.medmanagerapplication.user;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.android.medmanagerapplication.R;

public class UserProfileActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    public static final String TAG = UserProfileActivity.class.getSimpleName();
    private static final int USER_PROFILE_LOADER = 6;

    EditText profileFName;
    EditText profileLName;
    EditText profileEmail;
    EditText profilePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        profileFName = findViewById(R.id.userProfileFirstName_EditText);
        profileLName = findViewById(R.id.userProfileLastName_EditText);
        profileEmail = findViewById(R.id.userProfileEmail_EditTextView);
        profilePassword = findViewById(R.id.userProfilePassword_EditText);


//        profilePassword.setTransformationMethod(null);

        FloatingActionButton fab = findViewById(R.id.profile_fab_right);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!checUserInputs()) {
                    if (upDateUserProfile(view)) {
                        Snackbar.make(view, "Sucess: The user profile was updated!", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    } else {
                        Snackbar.make(view, "Error: The user profile was not updated!", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                } else {
                    Snackbar.make(view, "Error: All fields are required!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }


            }
        });

        FloatingActionButton fab2 = findViewById(R.id.profile_fab_left);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        getSupportLoaderManager().initLoader(USER_PROFILE_LOADER, null, this);
    }

    private boolean upDateUserProfile(View view) {

        // TODO: Remove
        Log.v(TAG, "UserProfile upDateUserProfile called");

        ContentValues contentValues = new ContentValues();

        contentValues.put(UserContract.UserEntry.FIRSTNAME, profileFName.getText().toString());
        contentValues.put(UserContract.UserEntry.LASTNAME, profileLName.getText().toString());
        contentValues.put(UserContract.UserEntry.EMAIL, profileEmail.getText().toString());
        contentValues.put(UserContract.UserEntry.PASSWORD, profilePassword.getText().toString());

        int result = getContentResolver().update(UserContract.UserEntry.CONTENT_URI,
                contentValues,
                null,
                null);
        if (result == 1) {

            return true;
        } else {
            Snackbar.make(view, "Error: The user profile was not updated!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }

        return false;

    }

    private boolean checUserInputs() {

        String fname = profileFName.getText().toString();
        String lname = profileLName.getText().toString();
        String email = profileEmail.getText().toString();
        String password = profilePassword.getText().toString();

        if (TextUtils.isEmpty(fname) || TextUtils.isEmpty(lname) ||
                TextUtils.isEmpty(email) ||
                TextUtils.isEmpty(password)) {
            return true;
        }

        return false;
    }
    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {

        Log.v(TAG, "UserProfile onCreateLoader called");

        return new CursorLoader(getApplicationContext(),
                UserContract.UserEntry.CONTENT_URI,
                null,
                null,
                null,
                null);

    }







    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {

        Log.v(TAG, "UserProfile cursor count: " + data.getCount());

        try {
            int fName = data.getColumnIndex(UserContract.UserEntry.FIRSTNAME);
            int lName = data.getColumnIndex(UserContract.UserEntry.LASTNAME);
            int email = data.getColumnIndex(UserContract.UserEntry.EMAIL);
            int passwd = data.getColumnIndex(UserContract.UserEntry.PASSWORD);

            if (data.moveToFirst()) {
                profileFName.setText(data.getString(fName));
                profileLName.setText(data.getString(lName));
                profileEmail.setText(data.getString(email));
                profilePassword.setText(data.getString(passwd));
            }
        } catch (Exception e) {
            Log.v(TAG, "UserProfile database error: " + e);

        } finally {
            data.close();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        loader.forceLoad();
    }
}
