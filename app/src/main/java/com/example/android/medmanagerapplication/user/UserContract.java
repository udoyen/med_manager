package com.example.android.medmanagerapplication.user;

import android.net.Uri;
import android.provider.BaseColumns;

public class UserContract {
    // The authority, which is how the right content provider is known
    // to access
    public static final String AUTHORITY = "com.example.android.medmanager.user";

    // The base content URI = "content://" + <authority>
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    // Define possible paths for accessing data in this contract
    public static final String PATH_USER = "user";

    public static final long INVALID_USER_ID = -1;

    public static final class UserEntry implements BaseColumns {
        // TaskEntry content URI = base content URI + path
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_USER).build();

        public final static String _ID = BaseColumns._ID;
        public static  final  String TABLE_NAME = "logins";
        public static final  String EMAIL = "email";
        public static final String PASSWORD = "password";
    }
}