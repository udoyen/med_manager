package com.example.android.medmanagerapplication.helperUtilitiesClasses;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.util.Log;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class JobSchedulerService extends JobService{

    public static final String TAG = JobSchedulerService.class.getSimpleName();

    private static final String CHECK_FOR_PAST_ALARMS = "com.example.android.medmanagerapplication.helperUtilitiesClasses.jobservice.CUSTOM_INTENT";
    private Handler mJobHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            Log.v(TAG, "handleMessage called from Jobscheduler service");
            broadcastIntent();
            jobFinished((JobParameters) msg.obj, false);
            return true;
        }
    });

    @Override
    public boolean onStartJob(JobParameters params) {
        mJobHandler.sendMessage(Message.obtain(mJobHandler, 1, params));
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        mJobHandler.removeMessages(1);
        return false;
    }

    public void broadcastIntent () {
        Log.v(TAG, "broadcastIntent called from Jobscheduler service");

        Intent intent = new Intent(String.valueOf(this));
        intent.setAction(CHECK_FOR_PAST_ALARMS);
        getApplicationContext().sendBroadcast(intent);
    }
}
