package com.example.android.medmanagerapplication;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;

public class ChartActivity extends AppCompatActivity {

    List<BarEntry> entries = new ArrayList<>();
    Drugs[] drugs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        BarChart barChart = findViewById(R.id.chart);


    }

    class ChartMaker extends AsyncTask<Void, Void, Boolean> {


        @Override
        protected Boolean doInBackground(Void... voids) {

            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
        }
    }



}
