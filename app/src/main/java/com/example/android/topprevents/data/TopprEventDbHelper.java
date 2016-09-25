package com.example.android.topprevents.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.topprevents.data.TopprEventContract.TopprEntry;

/**
 * Manages a local database for Toppr Event data.
 */
public class TopprEventDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 2;

    static final String DATABASE_NAME = "toppr.db";

    public TopprEventDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + TopprEntry.TABLE_NAME + " (" +
                TopprEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                TopprEntry.COLUMN_TOPPR_ID + " REAL UNIQUE NOT NULL, " +
                TopprEntry.COLUMN_TOPPR_NAME + " TEXT NOT NULL, " +
                TopprEntry.COLUMN_TOPPR_IMAGE + " TEXT NOT NULL, " +
                TopprEntry.COLUMN_TOPPR_CATEGORY + " TEXT NOT NULL, " +
                TopprEntry.COLUMN_TOPPR_DESCRIPTION + " TEXT NOT NULL, " +
                TopprEntry.COLUMN_TOPPR_EXPERIENCE + " TEXT NOT NULL, " +
                TopprEntry.COLUMN_TOPPR_FAV + " TEXT NOT NULL " +
                " );";

        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TopprEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
