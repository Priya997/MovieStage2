package com.priyankaj.moviestage_2.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.priyankaj.moviestage_2.Models.Reviews_Model;
import com.priyankaj.moviestage_2.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.TrailersViewHolder> {
    ArrayList<Reviews_Model> list;
    Context context;

    public ReviewsAdapter(Context context, ArrayList<Reviews_Model> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public TrailersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.reviews_model, parent, false);
        return new TrailersViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailersViewHolder holder, int position) {
        Reviews_Model reviews_model = list.get(position);
        holder.author_name.setText(reviews_model.getAuthor());
        holder.content.setText(reviews_model.getContent());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class TrailersViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.review_author)
        TextView author_name;
        @BindView(R.id.review_content)
        TextView content;

        public TrailersViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);


        }

    }
}
