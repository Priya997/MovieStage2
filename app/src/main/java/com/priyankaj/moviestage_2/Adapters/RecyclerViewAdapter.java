package com.priyankaj.moviestage_2.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.priyankaj.moviestage_2.Activities.DetailsActivity;
import com.priyankaj.moviestage_2.Models.Movie_model;
import com.priyankaj.moviestage_2.R;
import com.priyankaj.moviestage_2.Utils.Constant;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private final Context mContext;
    private ArrayList<Movie_model> list;

    public RecyclerViewAdapter(Context context, ArrayList<Movie_model> list) {
        this.list = list;
        this.mContext = context;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerviewlayout, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        final Movie_model temp_model = list.get(position);
        Picasso.get().load(Constant.SMALL_IMAGE_URL + temp_model.getMovie_poster_url()).into(holder.Movie_poster);
        holder.Movie_poster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mContext, DetailsActivity.class);
                i.putExtra(Constant.MOVIE_ID, temp_model.getMovie_id());
                i.putExtra(Constant.MOVIE_TITLE, temp_model.getMovie_title());
                i.putExtra(Constant.MOVIE_OVERVIEW, temp_model.getMovie_synopsis());
                i.putExtra(Constant.MOVIE_RATING, temp_model.getMovie_rating());
                i.putExtra(Constant.MOVIE_RELEASE_DATE, temp_model.getMovie_release_date());
                i.putExtra(Constant.MOVIE_POSTER, temp_model.getMovie_poster_url());

                mContext.startActivity(i);

            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void refresh(ArrayList<Movie_model> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.model_imageview)
        ImageView Movie_poster;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
