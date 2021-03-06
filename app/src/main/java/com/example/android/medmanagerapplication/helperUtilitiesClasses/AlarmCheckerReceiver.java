package com.example.android.medmanagerapplication.helperUtilitiesClasses;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.widget.Toast;

import java.util.Objects;

public class AlarmCheckerReceiver extends BroadcastReceiver {

    public AlarmCheckerReceiver() {

    }

    private static final String CHECK_FOR_PAST_ALARMS = "com.example.android.medmanagerapplication.helperUtilitiesClasses.jobservice.CUSTOM_INTENT";


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onReceive(Context context, Intent intent) {

        if (Objects.equals(intent.getAction(), CHECK_FOR_PAST_ALARMS)) {

            Toast.makeText(context, "Checking for past alarms!", Toast.LENGTH_LONG).show();
        }

    }
}
