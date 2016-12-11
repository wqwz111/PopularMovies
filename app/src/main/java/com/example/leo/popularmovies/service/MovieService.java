package com.example.leo.popularmovies.service;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.example.leo.popularmovies.Constants;
import com.example.leo.popularmovies.R;
import com.example.leo.popularmovies.data.MovieContract;
import com.example.leo.popularmovies.data.MovieContract.MovieEntry;
import com.example.leo.popularmovies.model.Movie;

public class MovieService extends IntentService {
    private static final String LOG_TAG = MovieService.class.getSimpleName();

    private static final String API_BASE_URL = "https://api.themoviedb.org/3";
    private static final String PATH_POPULAR_MOVIE = "movie/popular";
    private static final String PATH_TOP_RATED_MOVIE = "movie/top_rated";
    private static final String API_KEY = Constants.API_KEY;
    private static final String IMG_URL_PREFIX = "http://image.tmdb.org/t/p/w342";
    private static final String QUERY_PAGE = "page";
    private static final String QUERY_API_KEY = "api_key";


    public static final String ACTION_POPULAR = "action.POPULAR";
    public static final String ACTION_TOP_RATED = "action.TOP_RATED";

    private static final String JSON_ITEM_PAGE = "page";

    private static int sNextPage = 1;

    public MovieService() {
        super("MovieService");
    }

    public static void startAction(Context context, String action) {
        Intent intent = new Intent(context, MovieService.class);
        intent.setAction(action);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_POPULAR.equals(action)) {
                handleActionPopular();
            } else if (ACTION_TOP_RATED.equals(action)) {
                handleActionTopRated();
            }
        }
    }

    private void handleActionPopular() {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put(QUERY_API_KEY, API_KEY);
        parameters.put(QUERY_PAGE, String.valueOf(sNextPage));
        String jsonString = makeHttpRequest(PATH_POPULAR_MOVIE, parameters);
        ArrayList<ContentValues> movieList = parseMovieDataFromJson(jsonString);
        if (!movieList.isEmpty()) {
            ContentValues[] valuesArray = new ContentValues[movieList.size()];
            movieList.toArray(valuesArray);
            getContentResolver().bulkInsert(MovieContract.MovieEntry.CONTENT_URI,
                    valuesArray);
        }
    }

    private void handleActionTopRated() {
    }

    private String makeHttpRequest(String path, HashMap<String,String> parameters) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String jsonString = null;

        Uri.Builder builder = Uri.parse(API_BASE_URL).buildUpon().appendEncodedPath(path);
        Iterator it = parameters.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry parameter = (Map.Entry) it.next();
            builder.appendQueryParameter((String) parameter.getKey(),
                    (String) parameter.getValue());
            it.remove();
        }
        Uri uri = builder.build();
        try {
            URL url = new URL(uri.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream is = urlConnection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line;
            while((line = reader.readLine()) != null) {
                sb.append(line);
            }
            jsonString = sb.toString();
        } catch (IOException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(LOG_TAG, e.getMessage(), e);
                }
            }
        }
        return jsonString;
    }

    private ArrayList<ContentValues> parseMovieDataFromJson(String jsonString) {
        ArrayList<ContentValues> results = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            if (!jsonObject.has(Movie.Projection.RESULTS)) {
                return results;
            }
            if (jsonObject.has(JSON_ITEM_PAGE)) {
                sNextPage = jsonObject.getInt(JSON_ITEM_PAGE) + 1;
            }
            JSONArray movieArray = jsonObject.getJSONArray(Movie.Projection.RESULTS);
            for (int i = 0; i < movieArray.length(); i++) {
                JSONObject movieJSON = movieArray.getJSONObject(i);
                long id = movieJSON.getLong(Movie.Projection.ID);
                String title = movieJSON.getString(Movie.Projection.TITLE);
                String overview = movieJSON.getString(Movie.Projection.OVERVIEW);
                String releaseDate = movieJSON.getString(Movie.Projection.RELEASE_DATE);
                String imgPath = movieJSON.getString(Movie.Projection.IMAGE_PATH);
                long voteCount = movieJSON.getLong(Movie.Projection.VOTE_COUNT);
                String voteAverage = movieJSON.getString(Movie.Projection.VOTE_AVERAGE);

                ContentValues values = new ContentValues();
                values.put(MovieEntry.COLUMN_MOVIE_ID, id);
                values.put(MovieEntry.COLUMN_NAME, title);
                values.put(MovieEntry.COLUMN_IMG_URL, IMG_URL_PREFIX + imgPath);
                values.put(MovieEntry.COLUMN_OVERVIEW, overview);
                values.put(MovieEntry.COLUMN_RELEASE_DATE, releaseDate);
                values.put(MovieEntry.COLUMN_VOTE_AVERAGE, voteAverage);
                values.put(MovieEntry.COLUMN_VOTE_COUNT, voteCount);
                results.add(values);
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
        }

        return  results;
    }
}
