package com.example.android.medmanagerapplication;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.android.medmanagerapplication.drugs.DrugContract;
import com.example.android.medmanagerapplication.drugs.Drugs;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;

public class ChartActivity extends AppCompatActivity {

    private final ArrayList<Drugs> drugs = new ArrayList<>();
    private BarDataSet barDataSet;
    /**
     * Names for the stacked columns
     */
    private String[] mStackNames;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
//        BarChart barChart = findViewById(R.id.chart);

        HorizontalBarChart horizontalBarChart = findViewById(R.id.chart);

        BarData barData = new BarData(getDataSets());
//        barData.setBarWidth(0.4f);
//        horizontalBarChart.getAxisRight().setEnabled(false);
        horizontalBarChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(getXAxisValues()));
        horizontalBarChart.setFitBars(true);
        horizontalBarChart.setScaleYEnabled(true);
        horizontalBarChart.animateY(2000);
        horizontalBarChart.setData(barData);
        horizontalBarChart.invalidate();




    }

    private ArrayList<IBarDataSet> getDataSets() {

        ArrayList<IBarDataSet> mDataSets = new ArrayList<>();


        Cursor mCursor = getContentResolver().query(DrugContract.DrugEntry.CONTENT_URI,
                null,
                null,
                null,
                null);


        assert mCursor != null;
        int id = mCursor.getColumnIndex(DrugContract.DrugEntry._ID);
        int name = mCursor.getColumnIndex(DrugContract.DrugEntry.NAME);
        int desc = mCursor.getColumnIndex(DrugContract.DrugEntry.DESCRIPTION);
        int interval = mCursor.getColumnIndex(DrugContract.DrugEntry.INTERVAL);
        int startDate = mCursor.getColumnIndex(DrugContract.DrugEntry.START_DATE);
        int endDate = mCursor.getColumnIndex(DrugContract.DrugEntry.END_DATE);
        int duration = mCursor.getColumnIndex(DrugContract.DrugEntry.DURATION);

        while (mCursor.moveToNext()) {

            drugs.add(new Drugs(mCursor.getLong(id), mCursor.getString(name), mCursor.getString(desc),
                    mCursor.getInt(interval), mCursor.getLong(startDate), mCursor.getLong(endDate), mCursor.getLong(duration)));

        }

        mStackNames = new String[drugs.size()];
        int i = 0;
        mCursor.close();


        float[] n = getDurationFloat();

        // Categorize the drugs added
        ArrayList<BarEntry> d = new ArrayList<>();
        for (Drugs drug : drugs) {

            d.add(new BarEntry(getMyMonth(drug.getStartDate()), drug.getDuration()));

            barDataSet = new BarDataSet(d, "DRUGS");
            mStackNames[i] = drug.getName();
            i++;
        }

        barDataSet.setStackLabels(mStackNames);
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        barDataSet.setDrawValues(true);
        barDataSet.setBarBorderWidth(0.7f);
        mDataSets.add(barDataSet);

        return mDataSets;


    }



    private float[] getDurationFloat() {
        int t = 0;
        float[] floats = new float[drugs.size()];
        for (Drugs drug : drugs) {
            floats[t] = (float) drug.duration;
            t++;
        }
        return floats;
    }


    private int getMyMonth(long d) {
        GregorianCalendar mCalendar = new GregorianCalendar();
        mCalendar.setTimeInMillis(d);

        return mCalendar.get(Calendar.MONTH);
    }

    private ArrayList<String> getXAxisValues() {

        ArrayList<String> xAxis = new ArrayList<>();

        xAxis.add("JAN");
        xAxis.add("FEB");
        xAxis.add("MAR");
        xAxis.add("APR");
        xAxis.add("MAY");
        xAxis.add("JUN");
        xAxis.add("JUL");
        xAxis.add("AUG");
        xAxis.add("SEP");
        xAxis.add("OCT");
        xAxis.add("NOV");
        xAxis.add("DEC");
        return xAxis;
    }

    private float[] getColorCode() {
        Random random = new Random();

        float val1 = random.nextInt(256);
        float val2 = random.nextInt(256);
        float val3 = random.nextInt(256);

        return new float[]{val1, val2, val3};
    }





}
