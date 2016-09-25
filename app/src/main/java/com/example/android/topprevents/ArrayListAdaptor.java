package com.example.android.topprevents;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


public class ArrayListAdaptor extends CursorAdapter {


        public ArrayListAdaptor(Context context, Cursor c, int flags) {
            super(context, c, flags);
        }


        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            View view = (View) LayoutInflater.from(context).inflate(R.layout.toppr_event_list, parent, false);
            return view;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {

            TextView category = (TextView) view.findViewById(R.id.textView4);
            TextView name = (TextView) view.findViewById(R.id.textView2);
            ImageView image = (ImageView) view.findViewById(R.id.imageView);
            Picasso
                    .with(context)
                    .load(cursor.getString(3))
                    .into(image);
            name.setText(cursor.getString(1));
            category.setText(cursor.getString(2));;
        }

    }
