package com.example.android.medmanagerapplication.drugs.ui;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.android.medmanagerapplication.ChartActivity;
import com.example.android.medmanagerapplication.R;
import com.example.android.medmanagerapplication.SettingsActivity;
import com.example.android.medmanagerapplication.drugs.DrugContract;
import com.example.android.medmanagerapplication.helperUtilitiesClasses.AlarmDeleter;
import com.example.android.medmanagerapplication.helperUtilitiesClasses.CalculateDays;
import com.example.android.medmanagerapplication.helperUtilitiesClasses.CloseSoftKeyboardHelperClass;
import com.example.android.medmanagerapplication.helperUtilitiesClasses.DrugReceiver;
import com.example.android.medmanagerapplication.helperUtilitiesClasses.GoogleAccountSignOutHelper;
import com.example.android.medmanagerapplication.user.UserProfileActivity;
import com.travijuu.numberpicker.library.NumberPicker;

import java.text.ParseException;
import java.util.Calendar;
import java.util.TimeZone;

public class DrugDetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = DrugDetailActivity.class.getSimpleName();
    private static final String DRUG_ID = "DrugID";
    private static final int DETAILPAGE_LOADER_ID = 4;
    private int drugID;
    private EditText name;
    private EditText description;
    private EditText startDate;
    private EditText endDate;
    private TextView duration;
    private NumberPicker interval;
    private DatePickerDialog pickerDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drug_detail_activty);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        name = findViewById(R.id.detail_NameOfDrug);
        description = findViewById(R.id.detail_description);
        startDate = findViewById(R.id.detail_StartDate_editText);
        endDate = findViewById(R.id.detail_EndDate_editText);
        interval = findViewById(R.id.detail_Number_picker);
        duration = findViewById(R.id.detailDuration_TextView);

        startDate.setInputType(InputType.TYPE_NULL);
        endDate.setInputType(InputType.TYPE_NULL);

        // Get the id from the intent object then query database
        // to get drug of interest
        Bundle extras = getIntent().getExtras();
        // Make sure the extra captures something
        if (extras == null) {

            return;
        }

        drugID = extras.getInt(DRUG_ID);

        Log.v(TAG, "Drug id: " + drugID);


        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                calendar.setFirstDayOfWeek(Calendar.SUNDAY);
                calendar.setTimeZone(TimeZone.getDefault());
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                // Date picker dialog
                pickerDialog = new DatePickerDialog(DrugDetailActivity.this, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Resources resource = getResources();
                        String text = String.format(resource.getString(R.string.date), dayOfMonth, (month + 1), year);
                        startDate.setText(text);
                    }
                }, year, month, day);
                pickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                pickerDialog.show();

            }
        });

        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar2 = Calendar.getInstance();
                calendar2.setFirstDayOfWeek(Calendar.SUNDAY);
                calendar2.setTimeZone(TimeZone.getDefault());
                int day = calendar2.get(Calendar.DAY_OF_MONTH);
                int month = calendar2.get(Calendar.MONTH);
                int year = calendar2.get(Calendar.YEAR);

                // Date picker dialog
                pickerDialog = new DatePickerDialog(DrugDetailActivity.this, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Resources resource = getResources();
                        String text = String.format(resource.getString(R.string.date), dayOfMonth, (month + 1), year);
                        endDate.setText(text);
                    }
                }, year, month, day);
                pickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                pickerDialog.show();

            }
        });

        // Get rid of the soft key when this has focus
        startDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                CloseSoftKeyboardHelperClass.hideKeyboard(DrugDetailActivity.this);
            }
        });

        endDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                CloseSoftKeyboardHelperClass.hideKeyboard(DrugDetailActivity.this);
            }
        });

        startDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(endDate.getText().toString())) {
                    duration.setText(String.valueOf(CalculateDays.getDaysBetweenDates(startDate.getText().toString(), endDate.getText().toString())));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        endDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(startDate.getText().toString())) {
                    duration.setText(String.valueOf(CalculateDays.getDaysBetweenDates(startDate.getText().toString(), endDate.getText().toString())));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        FloatingActionButton fab = findViewById(R.id.update_drug_detail);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String start = startDate.getText().toString();
                String end = endDate.getText().toString();

                try {

                    if (!checkUserInputs()) {
                        Log.v(TAG, "first if block");
                        if (!CalculateDays.compareDate(start, end)) {
                            Log.v(TAG, "Second if block");

                            updateDrugItem(view);
                        } else {
                            Snackbar.make(view, "Please make sure the end date is not behind the start date!", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }

                    } else {
                        Snackbar.make(view, "Please check the entered values, and make sure the end date is not behind of the start date!", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                } catch (ParseException e) {
                    Snackbar.make(view, "All fields are required!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    e.printStackTrace();
                }
            }
        });

        FloatingActionButton fab2 = findViewById(R.id.delete_drug);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (deleteDrug(drugID)) {
                        Intent intent = new Intent(DrugDetailActivity.this, AlarmDeleter.class);
                        intent.putExtra("drugId", drugID);
                        startService(intent);
                        //TODO: Remove
//                        deleter.cancelNotification(drugID);
                        finish();
                    } else {
                        Snackbar.make(view, "Item was not deleted!", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                } catch (Exception e) {
                    Log.v(TAG, "Error from item delete: " + e);
                    Snackbar.make(view, "Error: Item was not deleted!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }


            }
        });

        FloatingActionButton fab3 = findViewById(R.id.go_back);
        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        LoaderManager loaderManager = getSupportLoaderManager();

        loaderManager.initLoader(DETAILPAGE_LOADER_ID, null, this);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.general_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case R.id.action_user_profile:
                Intent iUser = new Intent(this, UserProfileActivity.class);
                iUser.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(iUser);
                return true;
            case R.id.action_user_settings:
                Intent iSettings = new Intent(this, SettingsActivity.class);
                startActivity(iSettings);
                return true;
            case R.id.revoke_google_account_signin:
                Intent google = new Intent(this, GoogleAccountSignOutHelper.class);
                startService(google);
                return true;
            case R.id.action_chart:
                Intent chart = new Intent(this, ChartActivity.class);
                startActivity(chart);
                return true;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {

        Log.v(TAG, "Detail onCreateLoader called with id: " + drugID);
        // Use Cursor object

        String[] projection = {
                DrugContract.DrugEntry._ID,
                DrugContract.DrugEntry.NAME,
                DrugContract.DrugEntry.DESCRIPTION,
                DrugContract.DrugEntry.INTERVAL,
                DrugContract.DrugEntry.START_DATE,
                DrugContract.DrugEntry.END_DATE,
                DrugContract.DrugEntry.DURATION
        };
        return new CursorLoader(getApplicationContext(),
                DrugContract.DrugEntry.CONTENT_URI,
                projection,
                "_ID = ?",
                new String[]{String.valueOf(drugID)},
                null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {

        Log.v(TAG, "Detail onLoadFinished called");
        try {
            int dName = cursor.getColumnIndex(DrugContract.DrugEntry.NAME);
            int dDesc = cursor.getColumnIndex(DrugContract.DrugEntry.DESCRIPTION);
            int dStart = cursor.getColumnIndex(DrugContract.DrugEntry.START_DATE);
            int dEnd = cursor.getColumnIndex(DrugContract.DrugEntry.END_DATE);
            int dDuration = cursor.getColumnIndex(DrugContract.DrugEntry.DURATION);
            int dInterval = cursor.getColumnIndex(DrugContract.DrugEntry.INTERVAL);

            Log.v(TAG, "cursor count: " + cursor.getCount());

            if (cursor.moveToFirst()) {
                Log.v(TAG, "cursor count after: " + cursor.getCount());
                name.setText(cursor.getString(dName));
                description.setText(cursor.getString(dDesc));
                startDate.setText(CalculateDays.timeInStringFormat(cursor.getLong(dStart)));
                endDate.setText(CalculateDays.timeInStringFormat(cursor.getLong(dEnd)));
                duration.setText(cursor.getString(dDuration));
                interval.setValue(cursor.getInt(dInterval));
            }
        } catch (Exception e) {
            Log.v(TAG, "Detailpage database error: " + e);
        } finally {

            cursor.close();
        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        loader.forceLoad();
    }

    private boolean deleteDrug(int drugId) {

        Uri SINGLE_DRUG_DELETE = ContentUris.withAppendedId(
                DrugContract.DrugEntry.CONTENT_URI, drugId
        );

        int itemToRemove = getContentResolver().delete(SINGLE_DRUG_DELETE, null, null);

        return itemToRemove == 1;
    }

    @SuppressLint("Assert")
    private void updateDrugItem(View view) throws ParseException {

        // TODO: Remove
        Log.v(TAG, "AddDrugActivity onCreateLoader called");
        // Calculate the duration in days
        long dur = CalculateDays.getDaysBetweenDates(startDate.getText().toString(), endDate.getText().toString());

        ContentValues contentValues = new ContentValues();
        contentValues.put(DrugContract.DrugEntry.NAME, name.getText().toString());
        contentValues.put(DrugContract.DrugEntry.DESCRIPTION, description.getText().toString());
        contentValues.put(DrugContract.DrugEntry.START_DATE, CalculateDays.dateInMillisconds(startDate.getText().toString()));
        contentValues.put(DrugContract.DrugEntry.END_DATE, CalculateDays.dateInMillisconds(endDate.getText().toString()));
        contentValues.put(DrugContract.DrugEntry.INTERVAL, interval.getValue());
        contentValues.put(DrugContract.DrugEntry.DURATION, dur);

        int result = getContentResolver().update(DrugContract.DrugEntry.CONTENT_URI, contentValues, "_ID = ?", new String[]{String.valueOf(drugID)});
        // TODO: Add DrugWidget Action here

        if (result == 1) {
            addNotification();
            Snackbar.make(view, "Success: The drug item was updated!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        } else {
            Snackbar.make(view, "Error: The drug item was not updated!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }

    }


    /**
     * update drug medication notifications
     */
    private void addNotification() throws ParseException {
        Log.v(TAG, "DrugDetailActivity addNotifiction call with id: " + drugID + " " + "Start date: " + startDate.getText().toString() + " " + "End date: " + endDate.getText().toString());

        Intent intent1 = new Intent(this, DrugReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, drugID, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) this.getSystemService(ALARM_SERVICE);
        assert am != null;
        am.setRepeating(AlarmManager.RTC_WAKEUP, CalculateDays.dateInMillisconds(startDate.getText().toString()), CalculateDays.dailyInterval(interval.getValue()), pendingIntent);

    }

    private boolean checkUserInputs() throws ParseException {

        String nameText = name.getText().toString();
        String desc = description.getText().toString();
        String sDate = startDate.getText().toString();
        String eDate = endDate.getText().toString();
        int pValue = interval.getValue();
        return (TextUtils.isEmpty(String.valueOf(pValue)) || TextUtils.isEmpty(nameText) || TextUtils.isEmpty(desc) || TextUtils.isEmpty(sDate) || TextUtils.isEmpty(eDate)) && !CalculateDays.compareDate(sDate, eDate);


    }


}
