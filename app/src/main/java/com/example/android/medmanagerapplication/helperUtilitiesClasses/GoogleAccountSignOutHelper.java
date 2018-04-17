package com.example.android.medmanagerapplication.helperUtilitiesClasses;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.preference.PreferenceManager;
import android.widget.Toast;

import com.example.android.medmanagerapplication.R;


public class GoogleAccountSignOutHelper extends IntentService {

    private Context context;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     *
     */
    public GoogleAccountSignOutHelper() {
        super("google account helper");
    }

    @SuppressWarnings("SameParameterValue")
    private void showToast(String message) {
        final String msg = message;
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(GoogleAccountSignOutHelper.this);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(getString(R.string.isSignedIn), true);
                editor.apply();
            }
        });
    }

    //TODO: Delete when done
    public void logOutUser () {

        Toast.makeText(context, "Called from Google Account Helper", Toast.LENGTH_LONG).show();
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        showToast("Google Account login revoke!");
    }


}
