package com.example.android.medmanagerapplication.drugs.ui;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.medmanagerapplication.R;
import com.example.android.medmanagerapplication.drugs.DrugContract;
import com.example.android.medmanagerapplication.helperUtilitiesClasses.CalculateDays;
import com.example.android.medmanagerapplication.helperUtilitiesClasses.CloseSoftKeyboardHelperClass;
import com.example.android.medmanagerapplication.helperUtilitiesClasses.DrugReceiver;
import com.travijuu.numberpicker.library.NumberPicker;

import java.util.Calendar;

public class AddDrugActivity extends AppCompatActivity {

    public static final String TAG = AddDrugActivity.class.getName();

    EditText nameEditTextView;
    EditText descriptionEditTextView;
    DatePickerDialog pickerDialog;
    EditText startDateEditText;
    EditText endDateEditText;
    NumberPicker numberPicker;
    Context context;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_drug);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        nameEditTextView = findViewById(R.id.nameOfDrug);
        descriptionEditTextView = findViewById(R.id.drug_description);
        startDateEditText = findViewById(R.id.startDate_editText);
        endDateEditText = findViewById(R.id.endDate_editText);

        numberPicker = findViewById(R.id.detail_Number_picker);


        startDateEditText.setInputType(InputType.TYPE_NULL);
        endDateEditText.setInputType(InputType.TYPE_NULL);

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

                if (!checkUserInputs()) {
                    onCreateLoader(view);

                } else {
                    Snackbar.make(view, "All fields are required!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
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


    public void onCreateLoader(View view) {
        Cursor cursor = getContentResolver().query(DrugContract.DrugEntry.CONTENT_URI, null, null, null, null);
        assert cursor != null;
        int bfCount = cursor.getCount();

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

        cursor = getContentResolver().query(DrugContract.DrugEntry.CONTENT_URI, null, null, null, null);
        assert cursor != null;
        int afCount = cursor.getCount();

        if (afCount > bfCount) {
            Log.v(TAG, "Notification created");
            addNotification();
            Snackbar.make(view, "New drug was added!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        } else {

            Snackbar.make(view, "New drug was not added!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
        Toast.makeText(AddDrugActivity.this, "Count: " + afCount, Toast.LENGTH_LONG).show();
        Log.v(TAG, "Created cursor closing");
        cursor.close();

        // Clear the input fields
        nameEditTextView.setText("");
        descriptionEditTextView.setText("");
        startDateEditText.setText(R.string.enter_start_date);
        endDateEditText.setText(R.string.enter_end_date_hint);
        numberPicker.setValue(1);
    }

    public Cursor addNotification(Cursor cursor) {




        return cursor;
    }

    private void addNotification() {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 3);
        calendar.set(Calendar.MINUTE, 30);
        calendar.set(Calendar.SECOND, 0);
        Intent intent1 = new Intent(this, DrugReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0,intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) this.getSystemService(ALARM_SERVICE);
        assert am != null;
        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntent);

    }



    public boolean checkUserInputs() {

        String name = nameEditTextView.getText().toString();
        String desc = descriptionEditTextView.getText().toString();
        String sDate = startDateEditText.getText().toString();
        String eDate = endDateEditText.getText().toString();
        int pValue = numberPicker.getValue();
        if (TextUtils.isEmpty(String.valueOf(pValue)) || TextUtils.isEmpty(name) || TextUtils.isEmpty(desc) || TextUtils.isEmpty(sDate) || TextUtils.isEmpty(eDate) ) {
            return true;
        }


        return false;

    }
}
