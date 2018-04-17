package com.example.android.medmanagerapplication.user;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.android.medmanagerapplication.database.AppDbHelper;

import java.util.Objects;

public class UserContentProvider extends ContentProvider {
    // Define final integer constants for the directory of plants and a single item.
    // It's convention to use 100, 200, 300, etc for directories,
    // and related ints (101, 102, ..) for items in that directory.
    private static final int USER = 100;
    private static final int USER_ID = 101;

    // Decaler a static variable for the Uri matcher that you construct
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private static final String TAG = UserContentProvider.class.getName();

    // Define a static buildUriMatcher that associates URI's with their int match
    private static UriMatcher buildUriMatcher() {
        //Initialize a UriMatcher
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        //Add URI matches
        uriMatcher.addURI(UserContract.AUTHORITY, UserContract.PATH_USER, USER);
        uriMatcher.addURI(UserContract.AUTHORITY, UserContract.PATH_USER + "/#", USER_ID);

        return uriMatcher;
    }

    // Database helper that will provide access to the database
    private AppDbHelper mUserDbHelper;


    @Override
    public boolean onCreate() {
        Context context = getContext();
        mUserDbHelper = new AppDbHelper(context);

        return true;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        // Get access to underlying database (read-only for query)
        final SQLiteDatabase db = mUserDbHelper.getReadableDatabase();

        // Write URI match code and set a variable to return a Cursor
        int match = sUriMatcher.match(uri);
        Cursor retCursor;

        switch (match) {
            // Query for the plants directory
            case USER:
                retCursor = db.query(UserContract.UserEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case USER_ID:
                String id = uri.getPathSegments().get(1);
                retCursor = db.query(UserContract.UserEntry.TABLE_NAME,
                        projection,
                        "_id=?",
                        new String[]{id},
                        null,
                        null,
                        sortOrder);
                break;
            // Default exception
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Set a notification URI on the Cursor and return that Cursor
        retCursor.setNotificationUri(Objects.requireNonNull(getContext()).getContentResolver(), uri);

        // Return the desired Cursor
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = mUserDbHelper.getWritableDatabase();

        // Write URI matching code to identify the match for the user directory
        int match = sUriMatcher.match(uri);
        Uri returnUri; // URI to be returned
        switch (match) {
            case USER:
                // Insert new values into the database
                long id = db.insert(UserContract.UserEntry.TABLE_NAME, null, values);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(UserContract.UserEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            // Default case
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }


        // Notify the resolver if the uri has been changed, and return the newly inserted URI
        Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri, null);

        // Return constructed uri (this points to the newly inserted row of data)
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mUserDbHelper.getWritableDatabase();

        // Write URI matching code to identify the match for the user directory
        int match = sUriMatcher.match(uri);
        // Track the updated user
        int userUpdated;

        switch (match) {
            case USER:
                userUpdated = db.update(UserContract.UserEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case USER_ID:
                selection = UserContract.UserEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                userUpdated = db.update(UserContract.UserEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }

        // Notify the resolver of a change and return the number of items updated
        if (userUpdated != 0) {
            // A place (or more) was updated, set notification
            Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri, null);
        }
        // Return the number of places deleted
        return userUpdated;
    }
}