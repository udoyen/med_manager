package com.example.android.medmanagerapplication.drugs.ui;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
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
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.medmanagerapplication.R;
import com.example.android.medmanagerapplication.drugs.DrugContract;
import com.example.android.medmanagerapplication.helperUtilitiesClasses.CalculateDays;
import com.example.android.medmanagerapplication.helperUtilitiesClasses.CloseSoftKeyboardHelperClass;
import com.travijuu.numberpicker.library.Enums.ActionEnum;
import com.travijuu.numberpicker.library.Interface.ValueChangedListener;
import com.travijuu.numberpicker.library.NumberPicker;

import java.util.Calendar;

public class DrugDetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = DrugDetailActivity.class.getSimpleName();

    int drugID;
    Cursor cursor;
    Context context;
    private static final String DRUG_ID = "DrugID";

    EditText name;
    EditText description;
    EditText startDate;
    EditText endDate;
    TextView duration;
    NumberPicker interval;
    DatePickerDialog pickerDialog;

    LoaderManager loaderManager;
    private static final int DETAILPAGE_LOADER_ID = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drug_detail_activty);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                // Date picker dialog
                pickerDialog = new DatePickerDialog(DrugDetailActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Resources resource = getResources();
                        String text = String.format(resource.getString(R.string.date), dayOfMonth, (month + 1), year);
                        startDate.setText(text);
                    }
                }, year, month, day);
                pickerDialog.show();

            }
        });

        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                // Date picker dialog
                pickerDialog = new DatePickerDialog(DrugDetailActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Resources resource = getResources();
                        String text = String.format(resource.getString(R.string.date), dayOfMonth, (month + 1), year);
                        endDate.setText(text);
                    }
                }, year, month, day);
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
                if (!checkUserInputs()) {
                    updateDrugItem(view);
                }
            }
        });

        FloatingActionButton fab2 = findViewById(R.id.delete_drug);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (deleteDrug(drugID)) {
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



        loaderManager = getSupportLoaderManager();

        loaderManager.initLoader(DETAILPAGE_LOADER_ID, null, this);

        interval.setValueChangedListener(new ValueChangedListener() {

            @Override
            public void valueChanged(int value, ActionEnum action) {

                Toast.makeText(DrugDetailActivity.this, "From number picker value change event", Toast.LENGTH_LONG).show();
            }
        });


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
                startDate.setText(cursor.getString(dStart));
                endDate.setText(cursor.getString(dEnd));
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
//        cursor.close();

    }

    public boolean deleteDrug(int drugId) {

        Uri SINGLE_DRUG_DELETE = ContentUris.withAppendedId(
                DrugContract.DrugEntry.CONTENT_URI, drugId
        );

        int itemToRemove = getContentResolver().delete(SINGLE_DRUG_DELETE, null, null);


        return itemToRemove == 1;

    }

    @SuppressLint("Assert")
    public void updateDrugItem(View view) {

        // TODO: Remove
        Log.v(TAG, "AddDrugActivity onCreateLoader called");
        // Calculate the duration in days
        long dur = CalculateDays.getDaysBetweenDates(startDate.getText().toString(), endDate.getText().toString());

        ContentValues contentValues = new ContentValues();
        contentValues.put(DrugContract.DrugEntry.NAME, name.getText().toString());
        contentValues.put(DrugContract.DrugEntry.DESCRIPTION, description.getText().toString());
        contentValues.put(DrugContract.DrugEntry.START_DATE, startDate.getText().toString());
        contentValues.put(DrugContract.DrugEntry.END_DATE, endDate.getText().toString());
        contentValues.put(DrugContract.DrugEntry.INTERVAL, interval.getValue());
        contentValues.put(DrugContract.DrugEntry.DURATION, dur);

        int result = getContentResolver().update(DrugContract.DrugEntry.CONTENT_URI, contentValues, "_ID = ?", new String[]{String.valueOf(drugID)});
        // TODO: Add DrugWidget Action here

        if (result == 1) {
            Snackbar.make(view, "Success: The drug item was upated!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        } else {
            Snackbar.make(view, "Error: The drug item was not upated!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }

    }

    public boolean checkUserInputs() {

        String nameText = name.getText().toString();
        String desc = description.getText().toString();
        String sDate = startDate.getText().toString();
        String eDate = endDate.getText().toString();
        int pValue = interval.getValue();
        if (TextUtils.isEmpty(String.valueOf(pValue)) || TextUtils.isEmpty(nameText) || TextUtils.isEmpty(desc) || TextUtils.isEmpty(sDate) || TextUtils.isEmpty(eDate) ) {
            return true;
        }


        return false;

    }


}
