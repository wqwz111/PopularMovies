package com.example.leo.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.leo.popularmovies.data.MovieContract.MovieEntry;
import com.example.leo.popularmovies.data.MovieContract.TrailerEntry;

/**
 * Created by leo on 16-12-10.
 */

public class MovieDbHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;

    public MovieDbHelper(Context context) {
        super(context, MovieContract.DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(MovieEntry.CREATE_TABLE_SQL);
        db.execSQL(TrailerEntry.CREATE_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(MovieEntry.DELETE_TABLE_SQL);
        db.execSQL(TrailerEntry.DELETE_TABLE_SQL);
        onCreate(db);
    }
}
