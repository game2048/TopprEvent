package com.example.android.topprevents;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.ShareCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.android.topprevents.data.TopprEventContract;
import com.squareup.picasso.Picasso;

import java.util.Random;

/**
 * A placeholder fragment containing a simple view.
 */
public class detail_toppr_eventFragment extends Fragment {

    public detail_toppr_eventFragment() {
    }

    private String eventId = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail_toppr_event, container, false);
        final Intent in = getActivity().getIntent();
        final TextView name = (TextView) rootView.findViewById(
                R.id.name);
        final TextView desc = (TextView) rootView.findViewById(
                R.id.description);
        final TextView ctc = (TextView) rootView.findViewById(
                R.id.ctc);
        final TextView exp = (TextView) rootView.findViewById(
                R.id.experience);
        final ImageView image = (ImageView) rootView.findViewById(
                R.id.imageView);
        ((ToggleButton) rootView.findViewById(R.id.toggleButton)).setChecked(Boolean.parseBoolean(in.getStringExtra("fav").toString()));
        eventId = in.getStringExtra("id");


        name.setText(in.getStringExtra("name"));
        desc.setText(in.getStringExtra("desc"));
        Random rand = new Random();

        int randomNum = rand.nextInt((40 - 10) + 1) + 10;
        ctc.setText(randomNum+"L-"+(randomNum+5)+"L INR");
        exp.setText(getString(R.string.Experience) +in.getStringExtra("exp"));

        Picasso
                .with(getActivity())
                .load(in.getStringExtra("image"))
                .into(image);

        final ToggleButton quantityTextView = (ToggleButton) rootView.findViewById(
                R.id.toggleButton);
        quantityTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                UpdateDbTask eventUpdateTask = new UpdateDbTask(getActivity());
                System.out
                        .println("On click of the Toggle Button is called !!");
                if (quantityTextView.isChecked()) {
                    System.out.println("Checked");

                    eventUpdateTask.execute("true");
                } else {
                    eventUpdateTask.execute("false");
                    System.out.println("Not Checked ");
                }
            }
        });

        rootView.findViewById(R.id.share_fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(Intent.createChooser(ShareCompat.IntentBuilder.from(getActivity())
                        .setType("text/plain")
                        .setText(in.getStringExtra("desc"))
                        .getIntent(), getString(R.string.share)));
            }
        });

        return rootView;
    }

    public class UpdateDbTask extends AsyncTask<String, Void, Void> {
        private final Context mContext;

        public UpdateDbTask(Context context) {
            mContext = context;
        }

        @Override
        protected Void doInBackground(String... params) {
            ContentValues values = new ContentValues();
            values.put(TopprEventContract.TopprEntry.COLUMN_TOPPR_FAV,params[0]);
            System.out.println("Update");
            int insertedUri = mContext.getContentResolver().update(TopprEventContract.TopprEntry.buildTopprUri(),values,TopprEventContract.TopprEntry.TABLE_NAME + "." + TopprEventContract.TopprEntry._ID + "=?",new String[] {String.valueOf(eventId)});
            return null;
        }
    }
}
