package com.example.android.medmanagerapplication.helperUtilitiesClasses.loginhelper;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.android.medmanagerapplication.user.UserContract;

public class CheckUserStatus extends IntentService {
    public static final String TAG = CheckUserStatus.class.getSimpleName();

    Cursor cursor;
    Context context;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     *
     */
    public CheckUserStatus() {
        super("check_user_status");
    }

    @SuppressLint("NewApi")
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        Log.v(TAG, "CheckUserStatus onHandleIntent called");

        cursor = getContentResolver().query(UserContract.UserEntry.CONTENT_URI,
                null,
                null,
                null,
                null);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();


        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int firstname = cursor.getColumnIndex(UserContract.UserEntry.FIRSTNAME);
                int lastname = cursor.getColumnIndex(UserContract.UserEntry.LASTNAME);

                if (cursor.getString(firstname) != null && cursor.getString(lastname) != null) {
                    editor.putBoolean("profileComplete", true);
                    editor.apply();
                } else {
                    editor.putBoolean("profileComplete", false);
                    editor.apply();
                }
            }
        }
    }
}
