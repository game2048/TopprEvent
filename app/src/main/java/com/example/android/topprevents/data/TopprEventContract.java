package com.example.android.topprevents.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;


/**
 * Defines table and column names for the Toppr Event database.
 */
public class TopprEventContract {


    public static final String CONTENT_AUTHORITY = "com.example.android.topprevents";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_TOPPR = "toppr";

    public static final class TopprEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TOPPR).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TOPPR;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TOPPR;

        // Table name
        public static final String TABLE_NAME = "toppr";

        public static final String COLUMN_TOPPR_ID = "id";

        public static final String COLUMN_TOPPR_NAME = "name";
        public static final String COLUMN_TOPPR_IMAGE = "image";
        public static final String COLUMN_TOPPR_CATEGORY = "category";
        public static final String COLUMN_TOPPR_DESCRIPTION = "description";
        public static final String COLUMN_TOPPR_EXPERIENCE = "experience";
        public static final String COLUMN_TOPPR_FAV = "favourite";


        public static Uri buildTopprUri(Long id) {
            return CONTENT_URI.buildUpon().appendPath(String.valueOf(id)).build();
        }

        public static Uri buildTopprUri(String id) {
            return CONTENT_URI.buildUpon().appendPath(String.valueOf(id)).build();
        }
        
        public static Uri buildTopprUri() {
            return CONTENT_URI;
        }

        public static String getTopprIdFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }
}
