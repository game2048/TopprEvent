package com.example.android.topprevents;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.topprevents.data.TopprEventContract;

/**
 * A placeholder fragment containing a simple view.
 */
public class favFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    public favFragment() {
    }
    ArrayListAdaptor adapter = null;
    public static final int TOPPR_LOADER = 0;
    SharedPreferences prefs ;
    private static final String[] TOPPR_COLUMNS = {
            TopprEventContract.TopprEntry.TABLE_NAME + "." + TopprEventContract.TopprEntry._ID,
            TopprEventContract.TopprEntry.COLUMN_TOPPR_NAME,
            TopprEventContract.TopprEntry.COLUMN_TOPPR_CATEGORY,
            TopprEventContract.TopprEntry.COLUMN_TOPPR_IMAGE,
            TopprEventContract.TopprEntry.COLUMN_TOPPR_FAV
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_fav, container, false);

        adapter = new ArrayListAdaptor(getActivity(),
                null,0);
        ListView listView = (ListView) rootView.findViewById(R.id.listViewfav);
        listView.setAdapter(adapter);
        listView.setEmptyView((TextView)rootView.findViewById(R.id.emptyFav));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                if (cursor != null) {
                    Intent intent = new Intent(getActivity(), detail_toppr_event.class);

                    intent.putExtra("id", cursor.getString(0));
                    intent.putExtra("name", cursor.getString(1));
                    intent.putExtra("category", cursor.getString(2));
                    intent.putExtra("image", cursor.getString(3));
                    intent.putExtra("fav", cursor.getString(4));
                    intent.putExtra("desc", cursor.getString(5));
                    intent.putExtra("exp", cursor.getString(6));
                    startActivity(intent);
                }
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(TOPPR_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        updateWeather();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri expenseUri =  null;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        expenseUri = TopprEventContract.TopprEntry.buildTopprUri();

        String[] fav = {
                "true"
        };
        CursorLoader expenseList = new CursorLoader(getActivity(),
                expenseUri,
                TOPPR_COLUMNS,
                TopprEventContract.TopprEntry.COLUMN_TOPPR_FAV+"=?",
                fav,
                null);
        return expenseList;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

    private void updateWeather() {
        TopperEventAsyncTask weatherTask = new TopperEventAsyncTask(getActivity());

        weatherTask.execute();

    }
}
