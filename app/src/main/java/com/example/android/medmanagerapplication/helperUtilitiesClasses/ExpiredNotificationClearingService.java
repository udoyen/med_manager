package com.example.android.medmanagerapplication.helperUtilitiesClasses;

import android.app.IntentService;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.example.android.medmanagerapplication.drugs.DrugContract;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class ExpiredNotificationClearingService extends IntentService {

    private static final String TAG = ExpiredNotificationClearingService.class.getSimpleName();

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     *
     */
    public ExpiredNotificationClearingService() {
        super("expired_notification_clearing_service");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        Date currentDate = new Date();
        Date yesterday = new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis((1)));


        Log.v(TAG, "ExpiredNotificationService onHandle method called");

        Cursor cursor = getContentResolver().query(DrugContract.DrugEntry.CONTENT_URI,
                null,
                null,
                null,
                null);
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

        assert cursor != null;
        cursor.close();

    }
}
