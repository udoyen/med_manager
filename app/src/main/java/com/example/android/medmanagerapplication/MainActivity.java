package com.example.android.medmanagerapplication;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.android.medmanagerapplication.drugs.ui.AddDrugActivity;
import com.example.android.medmanagerapplication.drugs.ui.DrugListAdapter;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int DRUG_LIST_ITEMS = 100;
    private DrugListAdapter drugListAdapter;
    private RecyclerView mDrugsList;

    public static final String TAG = MainActivity.class.getName();

    private static final int DRUG_LOADER_ID = 300;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "Creating MainActivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrugsList = (RecyclerView) findViewById(R.id.drug_list_recycler_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mDrugsList.setLayoutManager(layoutManager);

        mDrugsList.setHasFixedSize(true);

        drugListAdapter = new DrugListAdapter(DRUG_LIST_ITEMS);

        mDrugsList.setAdapter(drugListAdapter);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
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

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    /**
     * Open the add drug Activity
     * @param view to launch
     */
    public void onAddFabClick(View view) {

        Intent intent = new Intent(this, AddDrugActivity.class);
        startActivity(intent);

    }
}
