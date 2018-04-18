package com.example.android.medmanagerapplication;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.android.medmanagerapplication.helperUtilitiesClasses.loginhelper.CheckUserStatus;
import com.example.android.medmanagerapplication.user.User;
import com.example.android.medmanagerapplication.user.UserContract;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor>, View.OnClickListener {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 19001;
    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;
    private boolean profileSet;
    private GoogleSignInClient mGoogleSignInClient;
    private ProgressDialog mProgressDialog;
    private GoogleSignInAccount account;
    private User user;
    boolean isSignedIn;
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;
    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private EditText mFirstName;
    private EditText mLastName;


    public LoginActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        findViewById(R.id.sign_in_button).setOnClickListener(this);

        Intent uIntent = new Intent(this, CheckUserStatus.class);
        startService(uIntent);

        SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = mPreferences.edit();
        profileSet = mPreferences.getBoolean("profileComplete", false);
        editor.apply();

        SharedPreferences sharedPreferences = android.support.v7.preference.PreferenceManager.getDefaultSharedPreferences(this);
        boolean dLogin = sharedPreferences.getBoolean("login_mode", true);


        mFirstName = findViewById(R.id.firstname);
        mLastName = findViewById(R.id.lastname);

        SharedPreferences listShared = PreferenceManager.getDefaultSharedPreferences(this);
        String listPref = listShared.getString("sigin_pref", "manual");


        // Check for Google signin
        if (listPref.equals("google") && account != null) {

            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            LoginActivity.this.startActivity(intent);
            finish();
        } else {
            // If user profile is set disable
            // the lastname and firstname input fields
            if (profileSet) {
                mFirstName.setEnabled(false);
                mFirstName.setVisibility(View.GONE);
                mLastName.setEnabled(false);
                mLastName.setVisibility(View.GONE);
                if (dLogin) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    LoginActivity.this.startActivity(intent);
                    finish();
                }

            } else {
                mFirstName.setEnabled(true);
                mFirstName.setVisibility(View.VISIBLE);
                mLastName.setEnabled(true);
                mLastName.setVisibility(View.VISIBLE);
            }

            // Set up the login form.
            mEmailView = findViewById(R.id.email);
            populateAutoComplete();


            mPasswordView = findViewById(R.id.password);
            mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                    if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                        attemptLogin();
                        return true;

                    }
                    return false;
                }
            });

            Button mEmailSignInButton = findViewById(R.id.emailSignIn_btn);
            mEmailSignInButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    attemptLogin();

                }
            });

            mLoginFormView = findViewById(R.id.login_form);
            mProgressView = findViewById(R.id.login_progress);
        }


    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        String firstname = mFirstName.getText().toString();
        String lastname = mLastName.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        if (!profileSet) {

            if (TextUtils.isEmpty(firstname)) {
                mFirstName.setError("This field is required");
                focusView = mFirstName;
                cancel = true;
            }


            if (TextUtils.isEmpty(lastname)) {
                mFirstName.setError("This field is required");
                focusView = mLastName;
                cancel = true;
            }

        }


        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            if (profileSet) {

                mAuthTask = new UserLoginTask(email, password, this);

            } else {

                mAuthTask = new UserLoginTask(email, password, this, firstname, lastname);

            }
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        // Check for valid email address
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isPasswordValid(String password) {

        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                if (isNetworkAvailable()) {
                    signIn();

                } else {
                    Snackbar.make(findViewById(R.id.login_container), "No network on your device!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                break;

        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        assert manager != null;
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {

            isAvailable = true;


        }

        return isAvailable;
    }


    @Override
    public void onStart() {
        super.onStart();

        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        SharedPreferences.Editor editor = getSharedPreferences(getString(R.string.userDetails), MODE_PRIVATE).edit();
        editor.putBoolean(getString(R.string.isSignedIn), true);
        editor.apply();
        if (!prefs.getBoolean(getString(R.string.isSignedIn), true)) {
            signOut();
        }

    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {

            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            hideProgressDialog();
            // Signed in successfully, show authenticated UI.
            goToMainPage(account);

        } catch (ApiException e) {
            hideProgressDialog();
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            Snackbar.make(findViewById(R.id.login_container), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();

        }
    }

    private void signIn() {
        showProgressDialog();
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Snackbar.make(findViewById(R.id.login_container), "You have revoked your google signin.", Snackbar.LENGTH_SHORT).show();
                    }
                });
    }

    private void goToMainPage(@Nullable GoogleSignInAccount account) {
        if (account != null) {
            Intent goToMain = new Intent(this, MainActivity.class);
            startActivity(goToMain);
            finish();
        } else {
            Intent goToMain = new Intent(this, LoginActivity.class);
            startActivity(goToMain);
            finish();
        }
    }

    private interface ProfileQuery {

        int ADDRESS = 0;
        int IS_PRIMARY = 1;

        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };


    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    @SuppressLint("StaticFieldLeak")
    class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;
        private final Context mContext;
        private String mFirstName;
        private String mLastName;


        UserLoginTask(String email, String password, Context context, String firstname, String lastname) {
            mEmail = email;
            mPassword = password;
            mContext = context;
            mLastName = lastname;
            mFirstName = firstname;
            user = new User(1, mEmail, password, firstname, lastname);
        }


        UserLoginTask(String email, String password, Context context) {
            mEmail = email;
            mPassword = password;
            mContext = context;
            user = new User(1, mEmail, password);
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            String passwd = "";
            // Authentication against a database

            String[] projection = {
                    UserContract.UserEntry._ID,
                    UserContract.UserEntry.EMAIL,
                    UserContract.UserEntry.PASSWORD
            };
            Cursor cursor = getContentResolver().query(UserContract.UserEntry.CONTENT_URI, projection,
                    "EMAIL = ?",
                    new String[]{user.email},
                    null);
            assert cursor != null;
            int passwordIndex = cursor.getColumnIndex(UserContract.UserEntry.PASSWORD);

            if (cursor.moveToFirst()) {
                passwd = cursor.getString(passwordIndex);
            }
            try {
                if (cursor.getCount() > 0) {
                    if (passwd.equals(user.password)) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    //noinspection UnusedAssignment
                    passwd = mPassword;
                    return true;
                }
            } catch (Exception e) {
                Log.i(TAG, "Try block Error: " + e.getMessage());
            } finally {
                cursor.close();
            }


            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {

            Log.v(TAG, "onPostExecute() called");

            mAuthTask = null;
            showProgress(false);

            final ContentValues values = new ContentValues();

            String[] projection = {
                    UserContract.UserEntry._ID,
                    UserContract.UserEntry.EMAIL
            };
            Cursor cursor = getContentResolver().query(UserContract.UserEntry.CONTENT_URI, projection,
                    "PASSWORD = ?",
                    new String[]{user.password},
                    null);
            assert cursor != null;
            if (success) {
                Log.v(TAG, "Checking if user exists!");
                if (cursor.getCount() > 0) {
                    finish();
                    Log.v(TAG, "Close the mCursor object");
                    cursor.close();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    LoginActivity.this.startActivity(intent);
                    finish();
                } else {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    Log.v(TAG, "Trying to create user");
                                    try {
                                        Log.v(TAG, "In try block");
                                        finish();

                                        values.put(UserContract.UserEntry.EMAIL, user.email);
                                        values.put(UserContract.UserEntry.PASSWORD, user.password);
                                        values.put(UserContract.UserEntry.FIRSTNAME, user.firstname);
                                        values.put(UserContract.UserEntry.LASTNAME, user.lastname);

                                        // Update database
                                        getContentResolver().insert(UserContract.UserEntry.CONTENT_URI, values);


                                        Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);
                                        LoginActivity.this.startActivity(myIntent);
                                    } catch (Exception e) {
                                        Log.i(TAG, "Error from user creation: " + e.getMessage());
                                    }
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    mPasswordView.setError(getString(R.string.error_incorrect_password));
                                    mPasswordView.requestFocus();
                                    break;
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(this.mContext);
                    builder.setMessage(R.string.confirm_registry).setPositiveButton(R.string.yes, dialogClickListener)
                            .setNegativeButton(R.string.no, dialogClickListener).show();
                }
            } else {
                Log.v(TAG, "Incorrect password");
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }


}
