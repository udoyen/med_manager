package com.example.android.medmanagerapplication.helperUtilitiesClasses;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

public class AlarmDeleter extends IntentService {

    public static final String TAG = AlarmDeleter.class.getSimpleName();

    Context context;

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
            int id = intent.getIntExtra("drugId", 1);
            Log.v(TAG, "do loop in receiver called from AlarmDeleter class:" + id);

            try {
                Intent notificationIntent = new Intent(context, DrugReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(), id, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                assert alarmManager != null;
                alarmManager.cancel(pendingIntent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
