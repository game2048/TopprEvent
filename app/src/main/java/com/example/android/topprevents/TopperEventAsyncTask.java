package com.example.android.topprevents;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Movie;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.android.topprevents.data.TopprEventContract;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.MappingJsonFactory;



/**
 * Created by vaibhav.seth on 9/25/16.
 */
    public class TopperEventAsyncTask extends AsyncTask<Void, Void, Void> {

    private final String LOG_TAG = TopperEventAsyncTask.class.getSimpleName();

    private final Context mContext;

    public TopperEventAsyncTask(Context context) {
        mContext = context;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        List<String> id = null;
        List<String> name = null;
        List<String> category = null;
        List<String> image = null;
        List<String> description = null;
        List<String> experience = null;
        String forecastJsonStr = null;

        try {
            final String FORECAST_BASE_URL =
                    "https://hackerearth.0x10.info/api/toppr_events?type=json&query=list_events";
            Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                    .build();

            URL url = new URL(builtUri.toString());
            Log.v("Forecast Json String", builtUri.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {

                forecastJsonStr = null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {

                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {

                forecastJsonStr = null;
            }
            forecastJsonStr = buffer.toString();

            id = getValuesListFromJsonForGivenField(forecastJsonStr, "id");
            name = getValuesListFromJsonForGivenField(forecastJsonStr, "name");
            image = getValuesListFromJsonForGivenField(forecastJsonStr, "image");
            category = getValuesListFromJsonForGivenField(forecastJsonStr, "category");
            description = getValuesListFromJsonForGivenField(forecastJsonStr, "description");
            experience = getValuesListFromJsonForGivenField(forecastJsonStr, "experience");
            int i = 0;


            String[] s1 = new String[id.size()];

            Vector<ContentValues> cVVector = new Vector<ContentValues>(id.size());
            for (String imageurl : id) {



                ContentValues movieValues = new ContentValues();

                movieValues.put(TopprEventContract.TopprEntry.COLUMN_TOPPR_ID, id.get(i));
                movieValues.put(TopprEventContract.TopprEntry.COLUMN_TOPPR_NAME, name.get(i));
                movieValues.put(TopprEventContract.TopprEntry.COLUMN_TOPPR_CATEGORY, category.get(i));
                movieValues.put(TopprEventContract.TopprEntry.COLUMN_TOPPR_DESCRIPTION, description.get(i));
                movieValues.put(TopprEventContract.TopprEntry.COLUMN_TOPPR_IMAGE, image.get(i));
                movieValues.put(TopprEventContract.TopprEntry.COLUMN_TOPPR_EXPERIENCE, experience.get(i));
                movieValues.put(TopprEventContract.TopprEntry.COLUMN_TOPPR_FAV, "false");

                cVVector.add(movieValues);

                System.out.println(s1[i]);
                i++;

            }
            int inserted = 0;
            // add to database
            if (cVVector.size() > 0) {
                ContentValues[] cvArray = new ContentValues[cVVector.size()];
                cVVector.toArray(cvArray);
                inserted = mContext.getContentResolver().bulkInsert(TopprEventContract.TopprEntry.CONTENT_URI, cvArray);
            }

            Log.d(LOG_TAG, "FetchWeatherTask Complete. " + inserted + " Inserted");

            Log.v("Forecast Json String", forecastJsonStr);
        } catch (IOException e) {
            Log.e("PlaceholderFragment", "Error ", e);

            forecastJsonStr = null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("PlaceholderFragment", "Error closing stream", e);
                }
            }
        }
        return null;
    }


    private List<String> getValuesListFromJsonForGivenField(
            String pubContentValue, String attributeName)
            throws JsonParseException, IOException {
        JsonFactory factory = new MappingJsonFactory();
        JsonParser jp = factory.createJsonParser(pubContentValue);
        org.codehaus.jackson.JsonToken current = jp.nextToken();
        List<String> attributeValues = new ArrayList<String>();
        while (jp.hasCurrentToken()) {
            if (null != jp.getCurrentName()
                    && jp.getCurrentName().equals(attributeName)) {
                org.codehaus.jackson.JsonToken currentValue = jp.nextValue();
                if (org.codehaus.jackson.JsonToken.VALUE_STRING == currentValue)
                    attributeValues.add(jp.getText());
                if (org.codehaus.jackson.JsonToken.VALUE_NUMBER_INT == currentValue)
                    attributeValues.add(jp.getText());
                if (org.codehaus.jackson.JsonToken.VALUE_NUMBER_FLOAT == currentValue)
                    attributeValues.add(jp.getText());
                if (org.codehaus.jackson.JsonToken.VALUE_TRUE == currentValue)
                    attributeValues.add(jp.getText());
                if (org.codehaus.jackson.JsonToken.START_ARRAY == currentValue) {
                    StringBuffer str = new StringBuffer();
                    while (org.codehaus.jackson.JsonToken.FIELD_NAME != jp.getCurrentToken()) {
                        str.append(jp.getText());
                        if (org.codehaus.jackson.JsonToken.END_ARRAY == jp.getCurrentToken())
                            break;
                        org.codehaus.jackson.JsonToken nextValue = jp.nextValue();
                        if ((!str.toString().equals("["))
                                && (org.codehaus.jackson.JsonToken.END_ARRAY != nextValue))
                            str.append(",");
                    }
                    attributeValues.add(str.toString());
                }
            }
            current = jp.nextToken();
        }
        return attributeValues;
    }
}

