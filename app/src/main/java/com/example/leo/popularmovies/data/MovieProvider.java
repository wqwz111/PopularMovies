package com.example.leo.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import com.example.leo.popularmovies.data.MovieContract.MovieEntry;
import com.example.leo.popularmovies.data.MovieContract.TrailerEntry;

public class MovieProvider extends ContentProvider {
    private static final int MOVIES = 100;
    private static final int MOVIE_ID = 101;
    private static final int TRAILERS = 200;
    private static final int TRAILER_ID = 201;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    private MovieDbHelper mMovieDbHelper;

    static {
        sUriMatcher.addURI(MovieContract.CONTENT_AUTHORITY,
                MovieContract.PATH_MOVIES, MOVIES);
        sUriMatcher.addURI(MovieContract.CONTENT_AUTHORITY,
                MovieContract.PATH_MOVIES + "/#", MOVIE_ID);
        sUriMatcher.addURI(MovieContract.CONTENT_AUTHORITY,
                MovieContract.PATH_TRAILERS, TRAILERS);
        sUriMatcher.addURI(MovieContract.CONTENT_AUTHORITY,
                MovieContract.PATH_TRAILERS + "/#", TRAILER_ID);
    }

    public MovieProvider() {
    }

    @Override
    public boolean onCreate() {
        mMovieDbHelper = new MovieDbHelper(getContext());
        return true;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int rowsDeleted;
        String tableName;
        int match = sUriMatcher.match(uri);
        switch (match) {
            case MOVIES:
                tableName = MovieEntry.TABLE_NAME;
                break;
            case MOVIE_ID:
                tableName = MovieEntry.TABLE_NAME;
                break;
            case TRAILERS:
                tableName = TrailerEntry.TABLE_NAME;
                break;
            case TRAILER_ID:
                tableName = TrailerEntry.TABLE_NAME;
                break;
            default:
                throw new IllegalArgumentException("Unknown Uri");
        }

        rowsDeleted = mMovieDbHelper.getWritableDatabase()
                .delete(tableName, selection, selectionArgs);
        return rowsDeleted;
    }

    @Override
    public String getType(Uri uri) {
        String type;
        int match = sUriMatcher.match(uri);
        switch (match) {
            case MOVIES:
                type = MovieEntry.CONTENT_LIST_TYPE;
                break;
            case MOVIE_ID:
                type = MovieEntry.CONTENT_ITEM_TYPE;
                break;
            case TRAILERS:
                type = TrailerEntry.CONTENT_LIST_TYPE;
                break;
            case TRAILER_ID:
                type = TrailerEntry.CONTENT_ITEM_TYPE;
                break;
            default:
                throw new IllegalArgumentException("Unknown Uri");
        }

        return type;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long id;
        String tableName;
        int match = sUriMatcher.match(uri);
        switch (match) {
            case MOVIES:
                tableName = MovieEntry.TABLE_NAME;
                break;
            case TRAILERS:
                tableName = TrailerEntry.TABLE_NAME;
                break;
            default:
                throw new IllegalArgumentException("Unknown Uri");
        }

        id = mMovieDbHelper.getWritableDatabase()
                .insert(tableName, null, values);
        uri = ContentUris.withAppendedId(uri, id);
        getContext().getContentResolver().notifyChange(uri, null);
        return uri;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor c;
        String tableName;
        int match = sUriMatcher.match(uri);
        switch (match) {
            case MOVIES:
                tableName = MovieEntry.TABLE_NAME;
                break;
            case MOVIE_ID:
                tableName = MovieEntry.TABLE_NAME;
                selection = "_id = ?";
                selectionArgs = new String[] {uri.getLastPathSegment()};
                break;
            case TRAILERS:
                tableName = TrailerEntry.TABLE_NAME;
                break;
            case TRAILER_ID:
                tableName = TrailerEntry.TABLE_NAME;
                selection = "_id = ?";
                selectionArgs = new String[] {uri.getLastPathSegment()};
                break;
            default:
                throw new IllegalArgumentException("Unknown Uri");
        }

        c = mMovieDbHelper.getReadableDatabase()
                .query(tableName, projection, selection, selectionArgs, null, null, sortOrder);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int rowsUpdated;
        String tableName;
        int match = sUriMatcher.match(uri);
        switch (match) {
            case MOVIE_ID:
                tableName = MovieEntry.TABLE_NAME;
                break;
            case TRAILER_ID:
                tableName = TrailerEntry.TABLE_NAME;
                break;
            default:
                throw new IllegalArgumentException("Unknown Uri");
        }

        rowsUpdated = mMovieDbHelper.getWritableDatabase()
                .update(tableName, values, selection, selectionArgs);

        if(rowsUpdated > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
}
