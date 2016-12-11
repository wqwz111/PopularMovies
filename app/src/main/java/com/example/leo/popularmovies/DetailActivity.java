package com.example.leo.popularmovies;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.leo.popularmovies.data.MovieContract.MovieEntry;

public class DetailActivity extends AppCompatActivity {
    private Uri mUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mUri = Uri.parse(getIntent().getStringExtra(Intent.EXTRA_TEXT));
        Cursor c = getContentResolver().query(mUri, null, null, null, null);
        if (c.moveToFirst()) {
            long movieId = c.getLong(c.getColumnIndex(MovieEntry.COLUMN_MOVIE_ID));
            String title = c.getString(c.getColumnIndex(MovieEntry.COLUMN_NAME));
            String overview = c.getString(c.getColumnIndex(MovieEntry.COLUMN_OVERVIEW));
            String imagePath = c.getString(c.getColumnIndex(MovieEntry.COLUMN_IMG_URL));
            String releaseDate = c.getString(c.getColumnIndex(MovieEntry.COLUMN_RELEASE_DATE));
            String voteAverage = c.getString(c.getColumnIndex(MovieEntry.COLUMN_VOTE_AVERAGE));
            long voteCount = c.getLong(c.getColumnIndex(MovieEntry.COLUMN_VOTE_COUNT));
        }
    }
}
