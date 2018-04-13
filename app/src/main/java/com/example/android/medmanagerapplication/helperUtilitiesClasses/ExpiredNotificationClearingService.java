package com.example.android.medmanagerapplication.helperUtilitiesClasses;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.android.medmanagerapplication.drugs.DrugContract;

import java.util.Date;

public class ExpiredNotificationClearingService extends IntentService {

    public static final String TAG = ExpiredNotificationClearingService.class.getSimpleName();

    Cursor cursor;
    Context context;
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     *
     */
    public ExpiredNotificationClearingService() {
        super("expired_notification_clearing_service");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Date currentDate = new Date();

        Log.v(TAG, "ExpiredNotificationService onHandle method called");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            cursor = getContentResolver().query(DrugContract.DrugEntry.CONTENT_URI,
                    null,
                    null,
                    null);
        }
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    int duration = cursor.getColumnIndex(DrugContract.DrugEntry.DURATION);
                    Date futureDate = new Date(System.currentTimeMillis() + duration);
                    int id = (int) cursor.getLong(cursor.getColumnIndex(DrugContract.DrugEntry._ID));

                    if (currentDate.after(futureDate)) {
                        Log.v(TAG, "Result of date comparison: " + currentDate.after(futureDate));
                        Log.v(TAG, "do loop in receiver called: " + id);
                        Intent nRIntent = new Intent(ExpiredNotificationClearingService.this, AlarmDeleter.class);
                        nRIntent.putExtra("drugId", id);
                        startService(intent);
                    }

                } while (cursor.moveToNext());
            }
        }

    }
}
