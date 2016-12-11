package com.example.leo.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;

import com.example.leo.popularmovies.data.MovieContract;
import com.squareup.picasso.Picasso;

/**
 * Created by leo on 16-12-8.
 */
public class MovieAdapter extends CursorAdapter {

    public MovieAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ImageView ivThumbnail = (ImageView) view.findViewById(R.id.iv_thumbnail);
        String url = cursor.getString(cursor.getColumnIndexOrThrow(
                MovieContract.MovieEntry.COLUMN_IMG_URL));
        Picasso.with(context).load(url).into(ivThumbnail);
    }
}
