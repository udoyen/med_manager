package com.example.android.medmanagerapplication.helperUtilitiesClasses.loginhelper;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

public class CheckUserStatus extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     *
     */
    public CheckUserStatus() {
        super("check_user_status");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

//        ResultReceiver resultReceiver = intent.getParcelableExtra(PARAM.RESULT_RECEIVER.name());

    }
}
