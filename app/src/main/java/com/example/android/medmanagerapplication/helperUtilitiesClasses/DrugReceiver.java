package com.example.android.medmanagerapplication.helperUtilitiesClasses;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.example.android.medmanagerapplication.MainActivity;
import com.example.android.medmanagerapplication.R;

public class DrugReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub

        long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(
                context).setSmallIcon(R.drawable.arraow_back_white)
                .setContentTitle("Alarm Fired")
                .setContentText("Events to be Performed").setSound(alarmSound)
                .setAutoCancel(true).setWhen(when)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent);
        notificationManager.notify(2, mNotifyBuilder.build());

    }
}
