package com.example.android.medmanagerapplication.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.medmanagerapplication.drugs.DrugContract;
import com.example.android.medmanagerapplication.user.UserContract;

public class AppDbHelper extends SQLiteOpenHelper {
    // Logcat tag


    // Version
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "medManager";


    // Columns Names
    // login Table
//    private static final String KEY_ID = "userId";


    //Drug Table
//    private static final String KEY_DRUG = "drugId";


    // Table create statements
    private static final String CREATE_TABLE_LOGINS = "CREATE TABLE "
            + UserContract.UserEntry.TABLE_NAME + "(" + UserContract.UserEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + UserContract.UserEntry.EMAIL
            + " TEXT," + UserContract.UserEntry.PASSWORD + " TEXT" + ")";

    private static final String CREATE_TABLE_DRUGS = "CREATE TABLE "
            + DrugContract.DrugEntry.TABLE_NAME + "(" + DrugContract.DrugEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + DrugContract.DrugEntry.NAME
            + " TEXT," + DrugContract.DrugEntry.DESCRIPTION + " TEXT," + DrugContract.DrugEntry.INTERVAL + " INTEGER," + DrugContract.DrugEntry.START_DATE + " INTEGER," + DrugContract.DrugEntry.END_DATE
            + " INTEGER" + ")";

    public AppDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create the tables
        db.execSQL(CREATE_TABLE_LOGINS);
        db.execSQL(CREATE_TABLE_DRUGS);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE  IF EXISTS " + UserContract.UserEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DrugContract.DrugEntry.TABLE_NAME);

        // Create new tables
        onCreate(db);

    }
}
