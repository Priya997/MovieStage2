package com.priyankaj.moviestage_2.Activities;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.priyankaj.moviestage_2.Adapters.ReviewsAdapter;
import com.priyankaj.moviestage_2.Adapters.TrailersAdapter;
import com.priyankaj.moviestage_2.Models.Reviews_Model;
import com.priyankaj.moviestage_2.Models.Trailers_Model;
import com.priyankaj.moviestage_2.R;
import com.priyankaj.moviestage_2.Utils.Connection;
import com.priyankaj.moviestage_2.Utils.Constant;
import com.priyankaj.moviestage_2.data.MoviesContract;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity {
    @BindView(R.id.Movie_rating)
    TextView T_rating;
    @BindView(R.id.button)
    Button add_Fav;
@BindView(R.id.Movie_title)   TextView T_title;
    @BindView(R.id.Movie_Overview)   TextView T_overview;
    @BindView(R.id.Movie_releasedate)   TextView T_releasedate;
    private String title, overview, poster, rating, release_date,id;
    @BindView(R.id.Movie_poster)    ImageView T_poster;
    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.trailers_recyclerview)
    RecyclerView t_recyclerView;
    @BindView(R.id.reviews_recyclerView)
    RecyclerView r_recyclerView;
    @BindView(R.id.Details_constraint_layout)
    ConstraintLayout constraintLayout;
    ArrayList<Trailers_Model> list=new ArrayList<>() ;
    ArrayList<Reviews_Model> review_list=new ArrayList<>() ;
    TrailersAdapter trailersAdapter;
    ReviewsAdapter reviewsAdapter;
    private static final String TrailersArrayKey = "taK";
    private static final String ReviewArrayKey = "raK";
     int f=0, count=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        AndroidNetworking.initialize(getApplicationContext());
        ActionBar actionBar=getSupportActionBar();
        Connection connection = new Connection(getApplicationContext());


        if (savedInstanceState != null) {
            id = savedInstanceState.getString(Constant.MOVIE_ID);
            title = savedInstanceState.getString(Constant.MOVIE_TITLE);
            overview = savedInstanceState.getString(Constant.MOVIE_OVERVIEW);
            poster = savedInstanceState.getString(Constant.MOVIE_POSTER);
            rating = savedInstanceState.getString(Constant.MOVIE_RATING);
            release_date = savedInstanceState.getString(Constant.MOVIE_RELEASE_DATE);
            list = savedInstanceState.getParcelableArrayList(TrailersArrayKey);
            review_list = savedInstanceState.getParcelableArrayList(ReviewArrayKey);
        } else {
            Intent i = getIntent();
            if (i != null) {
                id = i.getStringExtra(Constant.MOVIE_ID);
                title = i.getStringExtra(Constant.MOVIE_TITLE);
                overview = i.getStringExtra(Constant.MOVIE_OVERVIEW);
                poster = i.getStringExtra(Constant.MOVIE_POSTER);
                rating = i.getStringExtra(Constant.MOVIE_RATING);
                release_date = i.getStringExtra(Constant.MOVIE_RELEASE_DATE);
            } else {
                Toast.makeText(getApplicationContext(), R.string.no_data_error, Toast.LENGTH_LONG).show();
                finish();
            }
            if (!connection.isInternet()) {
                Toast.makeText(getApplicationContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                finish();
            } else {
                       fetchtrailers(Constant.URL+""+id+"/"+Constant.TRAILERS_URL);
                fetchreviews(Constant.URL + "" + id + "/" + Constant.REVIEWS_URL);
            }


        }


        Picasso.get().load(Constant.BIG_IMAGE_URL + poster).into(T_poster, new Callback() {
            @Override
            public void onSuccess() {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError(Exception e) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), R.string.image_error, Toast.LENGTH_SHORT).show();
            }

        });
        Cursor cursor = getContentResolver().query(MoviesContract.MoviesEntry.CONTENT_URI, null, id, null, null);
        count = cursor.getCount();
        if (count != 0) {
            cursor.moveToFirst();
            Log.v("movie name", "" + cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_MOVIE_NAME)));

            Log.v("count", String.valueOf(count));
            add_Fav.setText("Remove from Favourites");
            f = 1;
        }
        cursor.close();
        actionBar.setTitle(title);
        T_title.setText(title);
        T_overview.setText(overview);
        T_rating.setText(rating);
        T_releasedate.setText(release_date);
        Log.v("id", id);

        t_recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        trailersAdapter = new TrailersAdapter(getApplicationContext(),list);

        t_recyclerView.setAdapter(trailersAdapter);

        r_recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));

        reviewsAdapter = new ReviewsAdapter(getApplicationContext(),review_list);

        r_recyclerView.setAdapter(reviewsAdapter);

        add_Fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(f==0){
                    ContentValues values = new ContentValues();

                    values.put(   MoviesContract.MoviesEntry.COLUMN_MOVIE_ID, id);
                    values.put(   MoviesContract.MoviesEntry.COLUMN_MOVIE_NAME, title);
                    values.put(   MoviesContract.MoviesEntry.COLUMN_POSTER_URL, poster);
                    values.put(   MoviesContract.MoviesEntry.COLUMN_DATE, release_date);
                    values.put(   MoviesContract.MoviesEntry.COLUMN_OVERVIEW, overview);
                    values.put(   MoviesContract.MoviesEntry.COLUMN_RATING, rating);
                    try{
                        Uri uri = getContentResolver().insert(MoviesContract.MoviesEntry.CONTENT_URI,values);
                        add_Fav.setText("REMOVE FROM FAVOURITES");
                        f=1;
                        if (uri!=null){
                            Snackbar.make(constraintLayout,"Added to favourites",Snackbar.LENGTH_LONG)
                                    .show();
                        }
                    }catch (Exception e){
                        Snackbar.make(constraintLayout,"Already Added to favourites",Snackbar.LENGTH_LONG)
                                .show();
                    }

            }else{
                    Uri uri = MoviesContract.MoviesEntry.CONTENT_URI;
                    uri = uri.buildUpon().appendPath(id).build();
                    try{
                        getContentResolver().delete(uri,null,null);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    if (uri!=null){
                        add_Fav.setText("ADD TO FAVOURITES");
                        f=0;
                        Snackbar.make(constraintLayout,"Removed Successfuly",Snackbar.LENGTH_SHORT).show();
                    }
                 }
            }

        });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(Constant.MOVIE_ID, id);
        outState.putString(Constant.MOVIE_TITLE, title);
        outState.putString(Constant.MOVIE_OVERVIEW, overview);
        outState.putString(Constant.MOVIE_POSTER, poster);
        outState.putString(Constant.MOVIE_RATING, rating);
        outState.putString(Constant.MOVIE_RELEASE_DATE, release_date);
        outState.putParcelableArrayList(TrailersArrayKey, list);
        outState.putParcelableArrayList(ReviewArrayKey, review_list);
        Log.v("inside savedstate2", "   ");
        super.onSaveInstanceState(outState);
    }

    private void fetchreviews(String s) {
        AndroidNetworking.get(s)
                .addQueryParameter("api_key",Constant.API_KEY)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        parseJsonReviews(response);
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }

    private void parseJsonReviews(JSONObject response) {

        JSONArray results = response.optJSONArray("results");
        for(int i=0;i<results.length();i++) {
            Reviews_Model reviews_model=new Reviews_Model();
            JSONObject result=results.optJSONObject(i);
            String author=result.optString("author");
            String content=result.optString("content");
            Log.d("reviews",""+author+content);
            reviews_model.setAuthor(author);
            reviews_model.setContent(content);
            review_list.add(reviews_model);
        }
        reviewsAdapter.notifyDataSetChanged();
    }

    private void fetchtrailers(String s) {
        AndroidNetworking.get(s)
                .addQueryParameter("api_key", Constant.API_KEY)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        parseJsonTrailers(response);
                    }
                    @Override
                    public void onError(ANError anError) {
                        Log.d("error",""+anError);
                    }
                });
    }

    private void parseJsonTrailers(JSONObject response) {

        JSONArray results = response.optJSONArray("results");
        for(int i=0;i<results.length();i++) {
            Trailers_Model model = new Trailers_Model();
            JSONObject result=results.optJSONObject(i);
            String name=result.optString("name");
            String key=result.optString("key");
            model.setName(name);
            model.setKey(key);
            list.add(model);

        }
        trailersAdapter.notifyDataSetChanged();

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
