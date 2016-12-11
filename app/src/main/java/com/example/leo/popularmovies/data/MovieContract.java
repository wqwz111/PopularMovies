package com.example.leo.popularmovies.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by leo on 16-12-9.
 */

public class MovieContract {
    public static final String CONTENT_AUTHORITY = "com.example.leo.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_MOVIES = "movies";
    public static final String PATH_TRAILERS = "trailers";

    public static final String DB_NAME = "popular_movies.db";

    private static final String TYPE_INTEGER = " INTEGER";
    private static final String TYPE_TEXT = " TEXT";
    private static final String TYPE_REAL = " REAL";
    private static final String NOT_NULL = " NOT NULL";
    private static final String COMMA = ",";

    public static class MovieEntry implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_MOVIES);
        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
                + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
                + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;

        public static final String TABLE_NAME = "movie";

        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_IMG_URL = "img_url";
        public static final String COLUMN_VOTE_COUNT = "vote_count";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_VOTE_AVERAGE= "vote_average";
        public static final String COLUMN_OVERVIEW = "overview";

        public static final String CREATE_TABLE_SQL = "CREATE TABLE " + TABLE_NAME + "("
                + _ID + TYPE_INTEGER + " PRIMARY KEY AUTOINCREMENT" + COMMA
                + COLUMN_MOVIE_ID + TYPE_INTEGER + NOT_NULL + COMMA
                + COLUMN_NAME + TYPE_TEXT + NOT_NULL + COMMA
                + COLUMN_IMG_URL + TYPE_TEXT + COMMA
                + COLUMN_VOTE_COUNT + TYPE_INTEGER + NOT_NULL + COMMA
                + COLUMN_RELEASE_DATE + TYPE_TEXT + COMMA
                + COLUMN_OVERVIEW + TYPE_TEXT + COMMA
                + COLUMN_VOTE_AVERAGE + TYPE_REAL + ")";
        public static final String DELETE_TABLE_SQL = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static class TrailerEntry implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_TRAILERS);
        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
                + "/" + CONTENT_AUTHORITY + "/" + PATH_TRAILERS;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
                + "/" + CONTENT_AUTHORITY + "/" + PATH_TRAILERS;

        public static final String TABLE_NAME = "trailer";

        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_URL = "url";

        public static final String CREATE_TABLE_SQL = "CREATE TABLE " + TABLE_NAME + "("
                + _ID + TYPE_INTEGER + " PRIMARY KEY AUTOINCREMENT" + COMMA
                + COLUMN_MOVIE_ID + TYPE_TEXT + NOT_NULL + COMMA
                + COLUMN_TITLE + TYPE_TEXT + NOT_NULL + COMMA
                + COLUMN_URL + TYPE_TEXT + NOT_NULL  + ")";
        public static final String DELETE_TABLE_SQL = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}
