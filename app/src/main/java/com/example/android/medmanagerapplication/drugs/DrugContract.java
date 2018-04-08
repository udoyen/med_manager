package com.example.android.medmanagerapplication.drugs;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class DrugContract {

    // The authority, which is how the right content provider is known
    // to access
    public static final String AUTHORITY = "com.example.android.medmanagerapplication.drugs";

    // The base content URI = "content://" + <authority>
    public static final Uri DRUG_BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    // Define possible paths for accessing data in this contract
    public static final String PATH_DRUG = "drugs";

    public static final long INVALID_DRUG_ID = -1;
    private static final int ITEM_ID = 2;

    public static final class DrugEntry implements BaseColumns {
        // TaskEntry content URI = base content URI + path
        public static final Uri CONTENT_URI =
                DRUG_BASE_CONTENT_URI.buildUpon().appendPath(PATH_DRUG).build();

        public static final Uri DELETE_CONTENT_URI = ContentUris.withAppendedId(DRUG_BASE_CONTENT_URI.buildUpon().appendPath(PATH_DRUG).build(), ITEM_ID);


        public final static String _ID = BaseColumns._ID;

        public static final String TABLE_NAME = "drugs";
        public static final String NAME = "name";
        public static final String DESCRIPTION = "description";
        public static final String INTERVAL = "interval";
        public static final String START_DATE = "startDate";
        public static final String END_DATE = "endDate";
        public static final String DURATION = "duration";
    }
}