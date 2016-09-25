package com.example.android.topprevents;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.topprevents.data.TopprEventContract;

/**
 * A placeholder fragment containing a simple view.
 */
public class TopprListEventFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    public TopprListEventFragment() {
    }

    ArrayListAdaptor adapter = null;
    public static final int TOPPR_LOADER = 0;
    SharedPreferences prefs;
    String sumExpense = "0";
    public static final String MyPREFERENCES = "MyPrefs";
    private static final String[] TOPPR_COLUMNS = {
            TopprEventContract.TopprEntry.TABLE_NAME + "." + TopprEventContract.TopprEntry._ID,
            TopprEventContract.TopprEntry.COLUMN_TOPPR_NAME,
            TopprEventContract.TopprEntry.COLUMN_TOPPR_CATEGORY,
            TopprEventContract.TopprEntry.COLUMN_TOPPR_IMAGE,
            TopprEventContract.TopprEntry.COLUMN_TOPPR_FAV,
            TopprEventContract.TopprEntry.COLUMN_TOPPR_DESCRIPTION,
            TopprEventContract.TopprEntry.COLUMN_TOPPR_EXPERIENCE
    };

    private static String sort = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_toppr_list_event, container, false);

        adapter = new ArrayListAdaptor(getActivity(),
                null, 0);
        ListView listView = (ListView) rootView.findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setEmptyView((TextView) rootView.findViewById(R.id.empty));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                if (cursor != null) {
                    Intent intent = new Intent(getActivity(), detail_toppr_event.class);
//                            .setData(ExpenseContract.ExpenseEntry.buildExpenseUri(cursor.getString(1))
//                            );
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

        final Button favbtn = (Button) rootView.findViewById(
                R.id.favSort
        );

        final Button catBtn = (Button) rootView.findViewById(
                R.id.categorySort
        );
//            ContentValues values = new ContentValues();
//            values.put(MovieContract.MovieEntry.COLUMN_MOVIE_FAV,"True");
//            int insertedUri = mContext.getContentResolver().update(MovieContract.MovieEntry.buildMovieUri(),values,MovieContract.MovieEntry.COLUMN_MOVIE_ID + "?",new String[] {String.valueOf(movieId)});

        favbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "fav", Toast.LENGTH_SHORT).show();
                sortOrder("Fav");
//                Intent intent = new Intent(getActivity(), fav.class);
//                startActivity(intent);
            }
        });

        catBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Category", Toast.LENGTH_SHORT).show();
                sortOrder("Category");
//                Intent intent = new Intent(getActivity(), fav.class);
//                startActivity(intent);
            }
        });

        final Button favourite = (Button) rootView.findViewById(
                R.id.favourites
        );
//            ContentValues values = new ContentValues();
//            values.put(MovieContract.MovieEntry.COLUMN_MOVIE_FAV,"True");
//            int insertedUri = mContext.getContentResolver().update(MovieContract.MovieEntry.buildMovieUri(),values,MovieContract.MovieEntry.COLUMN_MOVIE_ID + "?",new String[] {String.valueOf(movieId)});

        favourite.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "fav", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), fav.class);
                startActivity(intent);
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
        Uri expenseUri = null;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        expenseUri = TopprEventContract.TopprEntry.buildTopprUri();

        String[] count = {
                "Count(*)"
        };
//        int i =0;
//        String[] sel = new String[]{"strftime('%m',date('now'))"};
//        // Finally, insert location data into the database.
        Cursor selectedURI = getActivity().getContentResolver().query(TopprEventContract.TopprEntry.CONTENT_URI,
                count,
                null, null, null
        );
        while(selectedURI.moveToNext())
        {
            sumExpense=selectedURI.getString(0);
        }
//        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
//        prefs.getString(getString(R.string.pref_metric_key),
//                getString(R.string.pref_default));
//        SharedPreferences.Editor editor = prefs.edit();
//
//        editor.putString(getString(R.string.pref_metric_key), sumExpense);
//        editor.commit();
//
        TextView t= (TextView) getActivity().findViewById(R.id.totalItems);

//

        CursorLoader topprList = new CursorLoader(getActivity(),
                expenseUri,
                TOPPR_COLUMNS,
                null,
                null,
                sort);
        t.setText(getString(R.string.textAll) + sumExpense );

        return topprList;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

    private void sortOrder(String order)
    {
        if(order.equals("Category"))
        {
            sort = TopprEventContract.TopprEntry.COLUMN_TOPPR_CATEGORY ;
        }
        else if(order.equals("Fav"))
        {
            sort = TopprEventContract.TopprEntry.COLUMN_TOPPR_FAV + " DESC";
        }
        else
        {
            sort = null;
        }
        updateWeather();
        getLoaderManager().restartLoader(TOPPR_LOADER, null, this);


    }
    private void updateWeather() {
        TopperEventAsyncTask weatherTask = new TopperEventAsyncTask(getActivity());
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
//        String location = "";//prefs.getString(getString(R.string.pref_location_key),
////                getString(R.string.pref_location_default));
//        String units = prefs.getString(getString(R.string.pref_metric_key),
//                getString(R.string.pref_location_default));
//        if(units.equalsIgnoreCase("most popular")) {
//            units = "popularity.desc";
//            sort = sortOrder[0];
//            weatherTask.execute(location, units);
//        }
//        else if(units.equalsIgnoreCase("highest-rated")) {
//            units = "vote_average.desc";
//            sort = sortOrder[1];
        weatherTask.execute();//location, units);
//        }
    }
}

