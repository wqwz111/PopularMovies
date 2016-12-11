package com.example.leo.popularmovies.model;

/**
 * Created by leo on 16-12-11.
 */

public class Movie {
    private long id;
    private long movieId;
    private String title;
    private String overview;
    private String imagePath;
    private long voteCount;
    private String releaseDate;
    private String  voteAverage;

    public Movie(long id, long movieId, String title, String overview, String imagePath, String releaseDate,
                 String voteAverage, long voteCount) {
        this.id = id;
        this.movieId = movieId;
        this.title = title;
        this.overview = overview;
        this.imagePath = imagePath;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
        this. voteCount = voteCount;
    }

    public final class Projection {
        public static final String RESULTS = "results";

        public static final String ID = "id";
        public static final String TITLE = "title";
        public static final String IMAGE_PATH = "poster_path";
        public static final String VOTE_AVERAGE = "vote_average";
        public static final String RELEASE_DATE = "release_date";
        public static final String VOTE_COUNT = "vote_count";
        public static final String OVERVIEW = "overview";
    }
}
