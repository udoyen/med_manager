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

import com.example.android.medmanagerapplication.MainActivity;
import com.example.android.medmanagerapplication.R;

public class DrugReceiver extends BroadcastReceiver {

    public static final String TAG = DrugReceiver.class.getSimpleName();

    public static final String DRUG_NOTIFICATION_CHANNEL = "drug_channel";
    public String drugname;
    public int drugId;





    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub

        SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = mPreferences.edit();
        drugId = mPreferences.getInt("drugId", 1);
        drugname = mPreferences.getString("drugName", "");
        editor.apply();


        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context.getApplicationContext(), DRUG_NOTIFICATION_CHANNEL);
        Intent ii = new Intent(context.getApplicationContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context.getApplicationContext(), 0, ii, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
//        bigText.bigText("Heelo there");
//        bigText.setBigContentTitle("Today's Bible Verse");
//        bigText.setSummaryText("Text in detail");

        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setSmallIcon(R.drawable.arraow_back_white);
        mBuilder.setContentTitle(context.getString(R.string.notificatio_title));
        mBuilder.setContentText(drugname);
        mBuilder.setPriority(Notification.PRIORITY_MAX);
//        mBuilder.setStyle(bigText);

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
