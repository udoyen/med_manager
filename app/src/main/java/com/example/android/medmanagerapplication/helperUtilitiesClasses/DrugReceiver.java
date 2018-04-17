package com.example.android.medmanagerapplication.helperUtilitiesClasses;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.android.medmanagerapplication.MainActivity;
import com.example.android.medmanagerapplication.R;

public class DrugReceiver extends BroadcastReceiver {

    private static final String TAG = DrugReceiver.class.getSimpleName();

    private static final String DRUG_NOTIFICATION_CHANNEL = "drug_channel";


    @Override
    public void onReceive(Context context, Intent intent) {

        SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = mPreferences.edit();
        int drugId = mPreferences.getInt("drugId", 1);
        String drugname = mPreferences.getString("drugName", "");
        editor.apply();

        Log.v(TAG, "From DrugReceiver Drug id: " + drugId + " " + "Drug Name: " + drugname);


        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context.getApplicationContext(), DRUG_NOTIFICATION_CHANNEL);
        Intent ii = new Intent(context.getApplicationContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context.getApplicationContext(), drugId, ii, PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setSmallIcon(R.drawable.ic_first_aid_white);
        mBuilder.setContentTitle(context.getString(R.string.notificatio_title));
        mBuilder.setDefaults(NotificationCompat.DEFAULT_SOUND);
        mBuilder.setContentText(drugname);
        mBuilder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });
        mBuilder.setPriority(Notification.PRIORITY_MAX);
        mBuilder.setChannelId(DRUG_NOTIFICATION_CHANNEL);

        NotificationManager mNotificationManager =
                (NotificationManager) context.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(DRUG_NOTIFICATION_CHANNEL,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            assert mNotificationManager != null;
            mNotificationManager.createNotificationChannel(channel);
        }

        assert mNotificationManager != null;
        mNotificationManager.notify(drugId, mBuilder.build());

    }


}
