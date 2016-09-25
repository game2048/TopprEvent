package com.example.android.topprevents.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class TopprEventProvider extends ContentProvider {

    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private TopprEventDbHelper mOpenHelper;

    static final int TOPPR = 100;
    static final int TOPPR_WITH_ID = 101;
    static final int TOPPR_FAV = 102;

    private static final SQLiteQueryBuilder sTopprByIdQueryBuilder;

    static{
        sTopprByIdQueryBuilder = new SQLiteQueryBuilder();

        sTopprByIdQueryBuilder.setTables(
                TopprEventContract.TopprEntry.TABLE_NAME );
    }

    private static final String sTopprIdSelection =
            TopprEventContract.TopprEntry.TABLE_NAME+
                    "." + TopprEventContract.TopprEntry._ID + " = ? ";


    private Cursor getWeatherByTopprId(Uri uri, String[] projection, String sortOrder) {
        String movieId = TopprEventContract.TopprEntry.getTopprIdFromUri(uri);

        String[] selectionArgs;
        String selection;

        selectionArgs = new String[]{movieId};
        selection = sTopprIdSelection;

        return sTopprByIdQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = TopprEventContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority, TopprEventContract.PATH_TOPPR, TOPPR);
        matcher.addURI(authority, TopprEventContract.PATH_TOPPR + "/FAV", TOPPR_FAV);
        matcher.addURI(authority, TopprEventContract.PATH_TOPPR + "/*", TOPPR_WITH_ID);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new TopprEventDbHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {

        final int match = sUriMatcher.match(uri);

        switch (match) {
            case TOPPR:
                return TopprEventContract.TopprEntry.CONTENT_ITEM_TYPE;
            case TOPPR_WITH_ID:
                return TopprEventContract.TopprEntry.CONTENT_TYPE;
            case TOPPR_FAV:
                return TopprEventContract.TopprEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            case TOPPR_WITH_ID:
            {
                retCursor = getWeatherByTopprId(uri, projection, sortOrder);
                break;
            }

            case TOPPR: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        TopprEventContract.TopprEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case TOPPR: {
                long _id = db.insert(TopprEventContract.TopprEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = TopprEventContract.TopprEntry.buildTopprUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        if ( null == selection ) selection = "1";
        switch (match) {
            case TOPPR:
                rowsDeleted = db.delete(
                        TopprEventContract.TopprEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(
            Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case TOPPR:
                rowsUpdated = db.update(TopprEventContract.TopprEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case TOPPR:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(TopprEventContract.TopprEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }
}