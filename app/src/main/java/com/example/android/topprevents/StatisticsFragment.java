package com.example.android.topprevents;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.topprevents.data.TopprEventContract;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class StatisticsFragment extends Fragment {
    BarChart lineChart;

    public StatisticsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_statistics, container, false);

        lineChart = (BarChart) rootView.findViewById(R.id.linechart);

        DbTask d = new DbTask(getActivity());
        d.execute();
        return rootView;
    }

    class DbTask extends AsyncTask<String, Void, String> {
        private final Context mContext;
        ArrayList<BarDataSet> entries = null;
        ArrayList<String> labels = new ArrayList<String>();

        public DbTask(Context context) {
            mContext = context;
        }

        @Override
        protected String doInBackground(String... params) {

            String[] FORECAST_COLUMNS = {
                    // In this case the id needs to be fully qualified with a table name, since
                    // the content provider joins the location & weather tables in the background
                    // (both have an _id column)
                    // On the one hand, that's annoying.  On the other, you can search the weather table
                    // using the location set by the user, which is only in the Location table.
                    // So the convenience is worth it.

                    TopprEventContract.TopprEntry.COLUMN_TOPPR_CATEGORY,
                    "COUNT(*)"
            };
            int i =0;
            // Finally, insert location data into the database.
            Cursor selectedURI = mContext.getContentResolver().query(TopprEventContract.TopprEntry.CONTENT_URI,
                    FORECAST_COLUMNS,
                    "group by "+ TopprEventContract.TopprEntry.COLUMN_TOPPR_CATEGORY , null,null
            );
            ArrayList<BarEntry> valueSet1 = new ArrayList<>();
            ArrayList<BarEntry> valueSet2 = new ArrayList<>();
            ArrayList<BarEntry> valueSet3 = new ArrayList<>();
            ArrayList<BarEntry> valueSet4 = new ArrayList<>();
            ArrayList<BarEntry> valueSet5 = new ArrayList<>();


            while (selectedURI.moveToNext()) {
                if(selectedURI.getString(0).equals("BOT")) {
                    BarEntry v1e1 = new BarEntry(Float.parseFloat(selectedURI.getString(1)), i);
                    valueSet1.add(v1e1);
                }
                else if(selectedURI.getString(0).equals("HACKATHON")) {
                    BarEntry v2e1 = new BarEntry(Float.parseFloat(selectedURI.getString(1)), i);
                    valueSet2.add(v2e1);
                }
                else if(selectedURI.getString(0).equals("HIRING")) {
                    BarEntry v2e1 = new BarEntry(Float.parseFloat(selectedURI.getString(1)), i);
                    valueSet3.add(v2e1);
                }
                else if(selectedURI.getString(0).equals("Competitive")) {
                    BarEntry v2e1 = new BarEntry(Float.parseFloat(selectedURI.getString(1)), i);
                    valueSet4.add(v2e1);
                }
                else  {
                    BarEntry v2e1 = new BarEntry(Float.parseFloat(selectedURI.getString(1)), i);
                    valueSet5.add(v2e1);
                }
            }
            labels.add("Data");
            BarDataSet barDataSet1 = new BarDataSet(valueSet1, getString(R.string.bot));
            barDataSet1.setColor(Color.rgb(0, 155, 0));
            BarDataSet barDataSet2 = new BarDataSet(valueSet2, getString(R.string.hackathon));
            barDataSet2.setColor(Color.rgb(193, 37, 82));
            BarDataSet barDataSet3 = new BarDataSet(valueSet3, getString(R.string.hiring));
            barDataSet3.setColor(Color.rgb(255, 102, 0));
            BarDataSet barDataSet4 = new BarDataSet(valueSet4, getString(R.string.competitive));
            barDataSet4.setColor(Color.rgb(245, 199, 155));
            BarDataSet barDataSet5 = new BarDataSet(valueSet5, getString(R.string.others));
            barDataSet5.setColor(Color.rgb(179, 155, 53));

            entries = new ArrayList<>();
            entries.add(barDataSet1);
            entries.add(barDataSet2);
            entries.add(barDataSet3);
            entries.add(barDataSet4);
            entries.add(barDataSet5);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            int animateSeconds = 2000;
            super.onPostExecute(s);

            BarData data = new BarData(labels,  entries);
            lineChart.setDescription(getString(R.string.graph));
            lineChart.setData(data);
            lineChart.animateXY(animateSeconds, animateSeconds);
            lineChart.invalidate();
        }
    }
}
