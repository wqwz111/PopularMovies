package com.example.leo.popularmovies;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.leo.popularmovies.data.MovieContract;
import com.example.leo.popularmovies.service.MovieService;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener,
        LoaderManager.LoaderCallbacks<Cursor> {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private static final int MOVIES_LOADER = 0;
    private static final String[] PROJECTION = new String[] {
            MovieContract.MovieEntry._ID,
            MovieContract.MovieEntry.COLUMN_NAME,
            MovieContract.MovieEntry.COLUMN_IMG_URL};

    private MovieAdapter mAdapter;
    private Loader<Cursor> mLoader;
    private GridView mGvMovies;
    private View mEmpteyView;
    private View mLoadingView;

    private boolean mShouldFetchDataFromNet = true;
    private boolean mIsLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGvMovies = (GridView) findViewById(R.id.gv_movies);
        mEmpteyView = findViewById(R.id.empty_view);
        mLoadingView = findViewById(R.id.loading_view);
        mGvMovies.setOnItemClickListener(this);
        mAdapter = new MovieAdapter(this, null);
        mGvMovies.setAdapter(mAdapter);

        mLoader = getLoaderManager().initLoader(MOVIES_LOADER, null, this);
        mLoadingView.setVisibility(View.VISIBLE);

        mGvMovies.setOnScrollListener(new AbsListView.OnScrollListener() {
            private  int lastItemIndex;
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                        && lastItemIndex == mAdapter.getCount() - 1) {
                    if (!mIsLoading) {
                        mIsLoading = true;
                        MovieService.startAction(MainActivity.this, MovieService.ACTION_POPULAR);
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                                 int totalItemCount) {
                lastItemIndex = firstVisibleItem + visibleItemCount - 1;
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        mLoader.forceLoad();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, DetailActivity.class);
        String uri = ContentUris.withAppendedId(MovieContract.MovieEntry.CONTENT_URI, id)
                .toString();
        intent.putExtra(Intent.EXTRA_TEXT, uri);
        startActivity(intent);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, MovieContract.MovieEntry.CONTENT_URI,
                PROJECTION, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mIsLoading = false;
        mLoadingView.setVisibility(View.GONE);
        if (data.getCount() == 0) {
            if (mShouldFetchDataFromNet) {
                MovieService.startAction(this, MovieService.ACTION_POPULAR);
                mLoadingView.setVisibility(View.VISIBLE);
                mShouldFetchDataFromNet = false;
            } else {
                mGvMovies.setEmptyView(mEmpteyView);
            }
        }
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}
