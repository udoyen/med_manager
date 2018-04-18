package com.example.android.medmanagerapplication.user;

import android.content.ContentValues;
import android.content.Intent;
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
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.android.medmanagerapplication.ChartActivity;
import com.example.android.medmanagerapplication.R;
import com.example.android.medmanagerapplication.SettingsActivity;
import com.example.android.medmanagerapplication.helperUtilitiesClasses.GoogleAccountSignOutHelper;

public class UserProfileActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final String TAG = UserProfileActivity.class.getSimpleName();
    private static final int USER_PROFILE_LOADER = 6;

    private EditText profileFName;
    private EditText profileLName;
    private EditText profileEmail;
    private EditText profilePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

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

        return TextUtils.isEmpty(fname) || TextUtils.isEmpty(lname) ||
                TextUtils.isEmpty(email) ||
                TextUtils.isEmpty(password);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.general_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case R.id.action_user_profile:
                Intent iUser = new Intent(this, UserProfileActivity.class);
                iUser.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(iUser);
                return true;
            case R.id.action_user_settings:
                Intent iSettings = new Intent(this, SettingsActivity.class);
                startActivity(iSettings);
                return true;
            case R.id.revoke_google_account_signin:
                Intent i = new Intent(this, GoogleAccountSignOutHelper.class);
                startService(i);
                return true;
            case R.id.action_chart:
                Intent i2 = new Intent(this, ChartActivity.class);
                startService(i2);
                return true;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
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
