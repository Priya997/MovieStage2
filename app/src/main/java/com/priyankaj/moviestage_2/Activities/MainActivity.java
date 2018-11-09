package com.priyankaj.moviestage_2.Activities;

import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.priyankaj.moviestage_2.Adapters.RecyclerViewAdapter;
import com.priyankaj.moviestage_2.Models.Movie_model;
import com.priyankaj.moviestage_2.R;
import com.priyankaj.moviestage_2.Utils.Connection;
import com.priyankaj.moviestage_2.Utils.Constant;
import com.priyankaj.moviestage_2.data.MoviesContract;
import com.priyankaj.moviestage_2.Adapters.RecyclerViewAdapter;
import com.priyankaj.moviestage_2.Models.Movie_model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.main_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
      Object item="Popular";
    RecyclerViewAdapter adapter;
    private static final String SpinnerKey = "sK";
    private static final String ArraylistKey = "alK";
    private ArrayList<Movie_model> list;
    Cursor cursor;
    boolean reinit = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AndroidNetworking.initialize(getApplicationContext());
        list = new ArrayList<>();
        ButterKnife.bind(this);
        Connection connection = new Connection(getApplicationContext());
        if(savedInstanceState!=null){
            reinit = false;
            CharSequence savedText = savedInstanceState.getCharSequence(SpinnerKey);
            item=savedText;
            list = savedInstanceState.getParcelableArrayList(ArraylistKey);
            Log.v("inside saved state", savedText + "   " + item.toString());
        } else if (!connection.isInternet())
            Toast.makeText(getApplicationContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();

        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));

        setSupportActionBar(toolbar);

        RecyclerView.LayoutManager linearLayoutManager = new GridLayoutManager(getApplicationContext(),2);
        recyclerView.setLayoutManager(linearLayoutManager);

       adapter = new RecyclerViewAdapter(MainActivity.this,list);
        recyclerView.setAdapter(adapter);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(SpinnerKey, item.toString());
        outState.putParcelableArrayList(ArraylistKey, list);

        Log.v("inside savedstate2","   "+item.toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.drawer_menu, menu);
        MenuItem spinnerMenuItem = menu.findItem(R.id.miSpinner);
        final Spinner spinner = (Spinner) MenuItemCompat.getActionView(spinnerMenuItem);
        spinner.getBackground().setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);
        final ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.choice, android.R.layout.simple_spinner_dropdown_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(spinnerAdapter);
        spinner.setSelection(spinnerAdapter.getPosition(item.toString()));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                item = adapterView.getItemAtPosition(position);
                ((TextView) adapterView.getChildAt(0)).setTextColor(Color.WHITE);
                if (reinit) {
                    if (item.toString().equals("Popular")) {
                        list.clear();
                        adapter.refresh(list);
                        fetchdata(Constant.URL + "" + Constant.POPULAR_URL);

                    }
                    if (item.toString().equals("Top Rated")) {
                        list.clear();
                        adapter.refresh(list);
                        fetchdata(Constant.URL + "" + Constant.TOP_RATED_URL);
                    }
                    if (item.toString().equals("Favourites")) {
                        list.clear();
                        cursor = getContentResolver().query(MoviesContract.MoviesEntry.CONTENT_URI,
                                null,
                                null,
                                null,
                                null);


                        if (cursor != null) {
                            cursor.moveToFirst();

                            while (!cursor.isAfterLast()) {

                                Movie_model movie_model = new Movie_model();
                                movie_model.setMovie_id(cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_MOVIE_ID)));
                                movie_model.setMovie_poster_url(cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_POSTER_URL)));
                                movie_model.setMovie_rating(cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_RATING)));
                                movie_model.setMovie_release_date(cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_DATE)));
                                movie_model.setMovie_synopsis(cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_OVERVIEW)));
                                movie_model.setMovie_title(cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_MOVIE_NAME)));
                                list.add(movie_model);
                                cursor.moveToNext();
                            }
                            cursor.close();

                        }
                        adapter.refresh(list);

                    }
                } else {
                    reinit = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        return true;

    }

    private void fetchdata(String url) {

        AndroidNetworking.get(url)
                .addQueryParameter("api_key", Constant.API_KEY)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {

                        JSONArray results = response.optJSONArray("results");
                        for(int i=0;i<results.length();i++) {
                            JSONObject result= results.optJSONObject(i);
                            Movie_model movie_model = new Movie_model();
                            movie_model.setMovie_id(result.optString(Constant.MOVIE_ID));
                            movie_model.setMovie_rating(result.optString(Constant.MOVIE_RATING));
                            movie_model.setMovie_release_date(result.optString(Constant.MOVIE_RELEASE_DATE));
                            movie_model.setMovie_synopsis(result.optString(Constant.MOVIE_OVERVIEW));
                            movie_model.setMovie_title(result.optString(Constant.MOVIE_TITLE));
                            movie_model.setMovie_poster_url(result.optString(Constant.MOVIE_POSTER));
                        list.add(i,movie_model);
                        }
adapter.refresh(list);
                    }

                    @Override
                    public void onError(ANError error) {
                        Log.v("response",""+error);
                    }
                });

    }

}

