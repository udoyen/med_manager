package com.example.android.medmanagerapplication.helperUtilitiesClasses;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

public class AlarmDeleter extends IntentService {

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


        if (intent != null) {
            long id = intent.getLongExtra("drugId", 1);

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
