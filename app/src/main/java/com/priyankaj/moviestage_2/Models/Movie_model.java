package com.priyankaj.moviestage_2.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie_model implements Parcelable {

    private String Movie_title;
    private String Movie_synopsis;

    public static final Creator<Movie_model> CREATOR = new Creator<Movie_model>() {
        public Movie_model createFromParcel(Parcel source) {
            return new Movie_model(source);
        }

        @Override
        public Movie_model[] newArray(int size) {
            return new Movie_model[size];
        }
    };

    private String Movie_rating;
    private String Movie_release_date;
    private String Movie_poster_url;
    private String Movie_id;



    public String getMovie_title() {
        return Movie_title;
    }

    public void setMovie_title(String movie_title) {
        Movie_title = movie_title;
    }

    public String getMovie_synopsis() {
        return Movie_synopsis;
    }

    public void setMovie_synopsis(String movie_synopsis) {
        Movie_synopsis = movie_synopsis;
    }

    public String getMovie_rating() {
        return Movie_rating;
    }

    public void setMovie_rating(String movie_rating) {
        Movie_rating = movie_rating;
    }

    public String getMovie_release_date() {
        return Movie_release_date;
    }

    public String getMovie_id() {
        return Movie_id;
    }

    public void setMovie_id(String movie_id) {
        Movie_id = movie_id;
    }
    public void setMovie_release_date(String movie_release_date) {
        Movie_release_date = movie_release_date;
    }

    public String getMovie_poster_url() {
        return Movie_poster_url;
    }

    public void setMovie_poster_url(String movie_poster_url) {
        Movie_poster_url = movie_poster_url;
    }

    public Movie_model() {

    }


    public Movie_model(Parcel in) {
        Movie_id = in.readString();
        Movie_title = in.readString();
        Movie_poster_url = in.readString();
        Movie_synopsis = in.readString();
        Movie_rating = in.readString();
        Movie_release_date = in.readString();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Movie_id);
        dest.writeString(Movie_title);
        dest.writeString(Movie_poster_url);
        dest.writeString(Movie_synopsis);
        dest.writeString(Movie_rating);
        dest.writeString(Movie_release_date);


    }
}
