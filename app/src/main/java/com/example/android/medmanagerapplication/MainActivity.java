package com.example.android.medmanagerapplication;

//import android.app.LoaderManager;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.android.medmanagerapplication.drugs.DrugContract;
import com.example.android.medmanagerapplication.drugs.ui.AddDrugActivity;
import com.example.android.medmanagerapplication.drugs.ui.DrugDetailActivity;
import com.example.android.medmanagerapplication.drugs.ui.DrugListAdapter;
import com.example.android.medmanagerapplication.helperUtilitiesClasses.AlarmDeleter;
import com.example.android.medmanagerapplication.helperUtilitiesClasses.JobSchedulerService;

import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

//import android.content.Loader;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, DrugListAdapter.OnItemClicked {

    private static final int DRUG_LIST_ITEMS = 100;
    private DrugListAdapter drugListAdapter;
    private RecyclerView mDrugsListRecylcerView;

    public static final String TAG = MainActivity.class.getName();

    private static final int DRUG_LOADER_ID = 3;

    private static final String CHECK_FOR_PAST_ALARMS = "com.example.android.medmanagerapplication.helperUtilitiesClasses.jobservice.CUSTOM_INTENT";


    public boolean bDetail;
    Context context;

    JobScheduler jobScheduler;
    private static final int JOB_ID = 1;

    public BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        Cursor cursor;


        @Override
        public void onReceive(Context context, Intent intent) {

            Date currentDate = new Date();
            //TODO: Tidy
            Date yesterday = new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis((1)));
            AlarmDeleter alarmDeleter = new AlarmDeleter();


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                // Use this to remove expired notifications
                if (Objects.equals(intent.getAction(), CHECK_FOR_PAST_ALARMS)) {

                    Log.v(TAG, "Broadcast receiver fired from HomeActivity");
                    Toast.makeText(MainActivity.this, "Database to checked", Toast.LENGTH_LONG).show();

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        cursor = getContentResolver().query(DrugContract.DrugEntry.CONTENT_URI,
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
                                        Log.v(TAG, "do loop in receiver called: " + id);
                                        Intent nRIntent = new Intent(MainActivity.this, AlarmDeleter.class);
                                        nRIntent.putExtra("drugId", id);
                                        startService(intent);
//                                        alarmDeleter.cancelNotification(id);
                                    }

                                } while (cursor.moveToNext());
                            }
                        }

                    }

                }
            }
        }


    };


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "Creating MainActivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(CHECK_FOR_PAST_ALARMS);
        registerReceiver(this.broadcastReceiver, intentFilter);

        // Initialize JobScheduler
        jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(new JobInfo.Builder(JOB_ID,
                new ComponentName(this, JobSchedulerService.class))
//                .setBackoffCriteria(15000, JobInfo.BACKOFF_POLICY_EXPONENTIAL)
//                .setPeriodic(15000)
                .setMinimumLatency(15000) //TODO: Remove
                .setOverrideDeadline(10000)
                .setRequiresCharging(false)
                .setPersisted(true)
                .build());

        mDrugsListRecylcerView = findViewById(R.id.rv_drugs);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mDrugsListRecylcerView.setLayoutManager(layoutManager);

        mDrugsListRecylcerView.setHasFixedSize(true);

        drugListAdapter = new DrugListAdapter(this, null);

        drugListAdapter.setOnClick(this);

        mDrugsListRecylcerView.setAdapter(drugListAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                long id = (long) viewHolder.itemView.getTag();
                if (deleteDrug((int) id)) {
//                    mDrugsListRecylcerView.setAdapter(drugListAdapter);
//                getSupportLoaderManager().initLoader(DRUG_LOADER_ID, null, MainActivity.this);
//                drugListAdapter.notifyDataSetChanged();
                    Intent intent = new Intent(MainActivity.this, AlarmDeleter.class);
                    intent.putExtra("drugId", id);
                    startService(intent);
                }

            }
        }).attachToRecyclerView(mDrugsListRecylcerView);


        getSupportLoaderManager().initLoader(DRUG_LOADER_ID, null, this);


        FloatingActionButton fab = findViewById(R.id.update_drug_detail);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                onAddFabClick(view);
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.v(TAG, "onResumeCalled");
        drugListAdapter = new DrugListAdapter(this, null);

        drugListAdapter.setOnClick(this);
        mDrugsListRecylcerView.setAdapter(drugListAdapter);
        getSupportLoaderManager().initLoader(DRUG_LOADER_ID, null, this);
        drugListAdapter.notifyDataSetChanged();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * Open the add drug Activity
     *
     * @param view to launch
     */
    public void onAddFabClick(View view) {

        Intent intent = new Intent(this, AddDrugActivity.class);
        startActivity(intent);

    }


    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        Log.v(TAG, "onCreateLoader called");
        String[] projection = {
                DrugContract.DrugEntry._ID,
                DrugContract.DrugEntry.NAME,
                DrugContract.DrugEntry.DESCRIPTION,
                DrugContract.DrugEntry.INTERVAL,
                DrugContract.DrugEntry.START_DATE,
                DrugContract.DrugEntry.END_DATE,
                DrugContract.DrugEntry.DURATION
        };
        return new CursorLoader(this, DrugContract.DrugEntry.CONTENT_URI, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        Log.v(TAG, "onLoaderFinished called");
        data.moveToFirst();
        int c = drugListAdapter.getItemCount();
        int d = data.getCount();
        Log.v(TAG, "Item Count is: " + c + " " + "And cursor count: " + d);
        drugListAdapter.swapCursor(data);


    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        Log.v(TAG, "onLoaderReset called");
        drugListAdapter.swapCursor(null);

    }

    @Override
    public void onItemClick(int position) {
        Log.v(TAG, "onItemClick called from MainActivty");
        String[] projection = {
                DrugContract.DrugEntry._ID
        };
        Cursor cursor = getContentResolver().query(DrugContract.DrugEntry.CONTENT_URI,
                projection, "_ID = ?",
                new String[]{String.valueOf(position)},
                null);
        try {

            assert cursor != null;
            int drugId = cursor.getColumnIndex(DrugContract.DrugEntry._ID);
            cursor.moveToFirst();
            int id = cursor.getInt(drugId);
            //TODO: Tidy
            Log.v(TAG, "Clicked position: " + position + " " + "Drug id: " + id);
            cursor.close();
            Intent intent = new Intent(this, DrugDetailActivity.class);
            // Pass information to the Activity here
            intent.putExtra("DrugID", id);
            startActivity(intent);
        } catch (Exception e) {
            Log.v(TAG, "Item detail presentation error!: " + e);
        } finally {
            assert cursor != null;
            cursor.close();
        }


    }

    public boolean deleteDrug(int drugId) {

        Uri SINGLE_DRUG_DELETE = ContentUris.withAppendedId(
                DrugContract.DrugEntry.CONTENT_URI, drugId
        );

        int itemToRemove = getContentResolver().delete(SINGLE_DRUG_DELETE, null, null);

        return itemToRemove == 1;
    }


    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver, new IntentFilter(CHECK_FOR_PAST_ALARMS));


    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);

    }


}
