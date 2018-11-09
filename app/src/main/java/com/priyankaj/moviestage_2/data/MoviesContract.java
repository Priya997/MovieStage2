package com.priyankaj.moviestage_2.data;

import android.net.Uri;
import android.provider.BaseColumns;


public class MoviesContract {

    public static final String AUTHORITY="com.example.prudhvi.popularmovies";

    public static final Uri BASE_CONTENT_URI=Uri.parse("content://"+AUTHORITY);

    public static final String PATH_MOVIES= "movies";

    public static final class MoviesEntry implements BaseColumns {

        public static final Uri CONTENT_URI= BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        public static final String TABLE_NAME="favouritemovies";

        public static final String COLUMN_MOVIE_ID="id";

        public static final String COLUMN_MOVIE_NAME="name";

        public static final String COLUMN_POSTER_URL="posterurl";

        public static final String COLUMN_RATING="rating";

        public static final String COLUMN_DATE="date";

        public static final String COLUMN_OVERVIEW="overview";
    }
}
