package com.example.android.medmanagerapplication.drugs.ui;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.android.medmanagerapplication.R;
import com.example.android.medmanagerapplication.SettingsActivity;
import com.example.android.medmanagerapplication.drugs.DrugContract;
import com.example.android.medmanagerapplication.helperUtilitiesClasses.CalculateDays;
import com.example.android.medmanagerapplication.helperUtilitiesClasses.CloseSoftKeyboardHelperClass;
import com.example.android.medmanagerapplication.helperUtilitiesClasses.DrugReceiver;
import com.example.android.medmanagerapplication.user.UserProfileActivity;
import com.travijuu.numberpicker.library.NumberPicker;

import java.text.ParseException;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class AddDrugActivity extends AppCompatActivity {

    public static final String TAG = AddDrugActivity.class.getName();

    EditText nameEditTextView;
    EditText descriptionEditTextView;
    DatePickerDialog pickerDialog;
    EditText startDateEditText;
    EditText endDateEditText;
    NumberPicker numberPicker;
    Context context;
    public static long addId;
    public static String addName;
    Long addDuration;
    int duration;
    String startDate;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_drug);
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        nameEditTextView = findViewById(R.id.nameOfDrug);
        descriptionEditTextView = findViewById(R.id.drug_description);
        startDateEditText = findViewById(R.id.startDate_editText);
        endDateEditText = findViewById(R.id.endDate_editText);

        numberPicker = findViewById(R.id.detail_Number_picker);


        startDateEditText.setInputType(InputType.TYPE_NULL);
        endDateEditText.setInputType(InputType.TYPE_NULL);

        // Prevent app closing f it's ActionBar is not set
        ActionBar actionBar = this.getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        startDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                // Date picker dialog
                pickerDialog = new DatePickerDialog(AddDrugActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Resources resource = getResources();
                        String text = String.format(resource.getString(R.string.date), dayOfMonth, (month + 1), year);
                        startDateEditText.setText(text);
                    }
                }, year, month, day);
                pickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                pickerDialog.show();

            }
        });

        endDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                // Date picker dialog
                pickerDialog = new DatePickerDialog(AddDrugActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Resources resource = getResources();
                        String text = String.format(resource.getString(R.string.date), dayOfMonth, (month + 1), year);
                        endDateEditText.setText(text);
                    }
                }, year, month, day);
                pickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                pickerDialog.show();

            }
        });

        startDateEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                CloseSoftKeyboardHelperClass.hideKeyboard(AddDrugActivity.this);
            }
        });

        endDateEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                CloseSoftKeyboardHelperClass.hideKeyboard(AddDrugActivity.this);
            }
        });


        FloatingActionButton fab = findViewById(R.id.update_drug_detail);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Remove
                Log.v(TAG, "AddDrugActivity add drug button called");

                String start = startDateEditText.getText().toString();
                String end = endDateEditText.getText().toString();

                try {

                    if (!checkUserInputs()) {
                        Log.v(TAG, "first if block");
                        if (!CalculateDays.compareDate(start, end)) {
                            Log.v(TAG, "Second if block");

                            onCreateLoader(view);
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

        FloatingActionButton fab_backBtn = findViewById(R.id.fab_backBtn);
        fab_backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               finish();
            }
        });


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
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Create new drug item
     * @param view
     */
    public void onCreateLoader(View view) throws ParseException {
        Cursor cursor = getContentResolver().query(DrugContract.DrugEntry.CONTENT_URI, null, null, null, null);
        assert cursor != null;
        int bfCount = cursor.getCount();
        Log.v(TAG, "Count before: " + bfCount);


        // TODO: Remove
        Log.v(TAG, "AddDrugActivity onCreateLoader called");
        // Calculate the duration in days
        long dur = CalculateDays.getDaysBetweenDates(startDateEditText.getText().toString(), endDateEditText.getText().toString());

        ContentValues contentValues = new ContentValues();
        contentValues.put(DrugContract.DrugEntry.NAME, nameEditTextView.getText().toString());
        contentValues.put(DrugContract.DrugEntry.DESCRIPTION, descriptionEditTextView.getText().toString());
        contentValues.put(DrugContract.DrugEntry.START_DATE, startDateEditText.getText().toString());
        contentValues.put(DrugContract.DrugEntry.END_DATE, endDateEditText.getText().toString());
        contentValues.put(DrugContract.DrugEntry.INTERVAL, numberPicker.getValue());
        contentValues.put(DrugContract.DrugEntry.DURATION, dur);

        getContentResolver().insert(DrugContract.DrugEntry.CONTENT_URI, contentValues);
        // TODO: Add DrugWidget Action here
        // Pass item information to DrugReceiver class
        String[] projection = {
                DrugContract.DrugEntry._ID,
                DrugContract.DrugEntry.NAME,
                DrugContract.DrugEntry.DESCRIPTION,
                DrugContract.DrugEntry.INTERVAL,
                DrugContract.DrugEntry.START_DATE,
                DrugContract.DrugEntry.END_DATE,
                DrugContract.DrugEntry.DURATION
        };

        cursor = getContentResolver().query(DrugContract.DrugEntry.CONTENT_URI, projection, null, null, null);
        // Use the variable to set the right date for the
        // alarm
        assert cursor != null;
        startDate = cursor.getString(cursor.getColumnIndex(DrugContract.DrugEntry.START_DATE));

        int afCount = cursor.getCount();

        Log.v(TAG, "Before Notification created");
        if (afCount > bfCount) {
            Log.v(TAG, "Notification created");
            setDrugItemInfo();
            addNotification();
            Snackbar.make(view, "New drug was added!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        } else {

            Snackbar.make(view, "New drug was not added!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
        Log.v(TAG, "Count after: " + afCount);
        Log.v(TAG, "Created cursor closing");
        cursor.close();

        // Clear the input fields
        nameEditTextView.setText("");
        descriptionEditTextView.setText("");
        startDateEditText.setText(R.string.enter_start_date);
        endDateEditText.setText(R.string.enter_end_date_hint);
        numberPicker.setValue(1);
    }

    public void setDrugItemInfo() {
        Log.v(TAG, "setDrumInfo called");

        String[] projection = {
                DrugContract.DrugEntry._ID,
                DrugContract.DrugEntry.NAME,
                DrugContract.DrugEntry.DESCRIPTION,
                DrugContract.DrugEntry.INTERVAL,
                DrugContract.DrugEntry.START_DATE,
                DrugContract.DrugEntry.END_DATE,
                DrugContract.DrugEntry.DURATION
        };

        Cursor cursor = getContentResolver().query(DrugContract.DrugEntry.CONTENT_URI, projection, null, null, null);

        Log.v(TAG, "About to get information");
        assert cursor != null;
        cursor.moveToPosition(cursor.getCount() - 1);
        addName = cursor.getString(cursor.getColumnIndex(DrugContract.DrugEntry.NAME));
        addId = cursor.getLong(cursor.getColumnIndex(DrugContract.DrugEntry._ID));
        Log.v(TAG, "Name of item: " + addName + "" + "Id of item: " + addId);


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("drugId", (int) addId);
        editor.putString("drugName", addName);
        editor.apply();

        cursor.close();
    }


    /**
     * Add drug medication notifications
     */
    private void addNotification() throws ParseException {
        Log.v(TAG, "addNotifiction call");

        // TODO: Check the Calendar for relevance
        Calendar calendar = Calendar.getInstance();
        Intent intent1 = new Intent(this, DrugReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, (int) addId,intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) this.getSystemService(ALARM_SERVICE);
        assert am != null;
        am.setRepeating(AlarmManager.RTC_WAKEUP, CalculateDays.dateInMillisconds(startDate), dailyInterval(numberPicker.getValue()), pendingIntent);

    }


    /**
     * Check that user input are not empty
     * @return boolean
     */
    public boolean checkUserInputs() throws ParseException {

        String name = nameEditTextView.getText().toString();
        String desc = descriptionEditTextView.getText().toString();
        String sDate = startDateEditText.getText().toString();
        String eDate = endDateEditText.getText().toString();
        int pValue = numberPicker.getValue();
        if (TextUtils.isEmpty(String.valueOf(pValue)) || TextUtils.isEmpty(name) || TextUtils.isEmpty(desc) || TextUtils.isEmpty(sDate) || TextUtils.isEmpty(eDate) ) {
            if (!CalculateDays.compareDate(sDate, eDate)) {

                return true;

            }
        }


        return false;

    }

    public long dailyInterval(long drugInterval) {

        int result = (int) Math.ceil(24 / drugInterval);

        return TimeUnit.HOURS.toMillis(result);
    }
}
