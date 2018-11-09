package com.priyankaj.moviestage_2.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;


public class MoviesContentProvider extends ContentProvider {

    public static final int MOVIES = 100;
    public static final int MOVIES_WITH_ID = 101;
    private DbHelper dbHelper;

    private static final UriMatcher uriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher() {

        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(MoviesContract.AUTHORITY, MoviesContract.PATH_MOVIES, MOVIES);
        uriMatcher.addURI(MoviesContract.AUTHORITY, MoviesContract.PATH_MOVIES + "/#", MOVIES_WITH_ID);
        return uriMatcher;
    }


    @Override
    public boolean onCreate() {
        Context context = getContext();
        dbHelper = new DbHelper(context);
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
        int match = uriMatcher.match(uri);

        Cursor cursor;
        switch (match) {
            case MOVIES:
                if (selection != null) {
                    Log.v("sda", selection);
                    cursor = sqLiteDatabase.rawQuery("select * from " + MoviesContract.MoviesEntry.TABLE_NAME +
                            " where " + MoviesContract.MoviesEntry.COLUMN_MOVIE_ID + " = " + selection, null);
                } else {
                    cursor = sqLiteDatabase.query(MoviesContract.MoviesEntry.TABLE_NAME,
                            projection,
                            selection,
                            selectionArgs,
                            null,
                            null,
                            sortOrder);
                    cursor.setNotificationUri(getContext().getContentResolver(), uri);
                }
                break;


            default:
                throw new UnsupportedOperationException("unknown Uri:" + uri);
        }

        return cursor;

    }


    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }


    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        int cases = uriMatcher.match(uri);
        Uri returnUri;
        switch (cases) {
            case MOVIES:
                String movieId = String.valueOf(values.get(MoviesContract.MoviesEntry.COLUMN_MOVIE_ID));
                Cursor cursor = sqLiteDatabase.rawQuery("select * from " + MoviesContract.MoviesEntry.TABLE_NAME +
                        " where " + MoviesContract.MoviesEntry.COLUMN_MOVIE_ID + " = " + movieId, null);
                int count = cursor.getCount();
                if (count == 0) {
                    long id = sqLiteDatabase.insert(MoviesContract.MoviesEntry.TABLE_NAME, null, values);
                    if (id > 0) {
                        returnUri = ContentUris.withAppendedId(MoviesContract.MoviesEntry.CONTENT_URI, id);
                    } else {
                        throw new android.database.SQLException("Failed to Add");
                    }
                    cursor.close();
                    break;
                } else {
                    throw new android.database.SQLException("Already added to favourites");
                }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }


    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        int match = uriMatcher.match(uri);

        int deleted;
        switch (match) {
            case MOVIES_WITH_ID:
                String id = uri.getPathSegments().get(1);
                deleted = sqLiteDatabase.delete(MoviesContract.MoviesEntry.TABLE_NAME, "id=?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("unknown uri: " + uri);
        }
        if (deleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return deleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }


}
