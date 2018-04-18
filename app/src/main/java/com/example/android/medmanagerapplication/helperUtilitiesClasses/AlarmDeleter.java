package com.example.android.medmanagerapplication.helperUtilitiesClasses;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

public class AlarmDeleter extends IntentService {

    private static final String TAG = AlarmDeleter.class.getSimpleName();

    private Context mContext;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     *
     */
    public AlarmDeleter() {
        super("notificationDeleter");
    }



    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        Log.v(TAG, "IntentService called");

        if (intent != null) {
            long id = intent.getLongExtra("drugId", 1);
            Log.v(TAG, "do loop in receiver called from AlarmDeleter class:" + id);

            try {
                Intent notificationIntent = new Intent(getApplicationContext(), DrugReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), (int) id, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                assert alarmManager != null;
                alarmManager.cancel(pendingIntent);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

    }
}
