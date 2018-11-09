package com.priyankaj.moviestage_2.Adapters;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.priyankaj.moviestage_2.Models.Trailers_Model;
import com.priyankaj.moviestage_2.R;

import java.util.ArrayList;

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.TrailersViewHolder> {
    ArrayList<Trailers_Model> list;
    Context context;
    public TrailersAdapter(Context context , ArrayList<Trailers_Model> list)
    {
        this.context=context;
        this.list=list;
    }
    @NonNull
    @Override
    public TrailersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailers_model,parent,false);
        return new TrailersViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailersViewHolder holder, int position) {
       Trailers_Model trailers_model = list.get(position);
       holder.trailer_name.setText(trailers_model.getName());
       holder.trailer_key=trailers_model.getKey();

    }

    @Override
    public int getItemCount() {
         return list.size();
    }

    public class TrailersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView trailer_name;
        String trailer_key;

        public TrailersViewHolder(View itemView) {
            super(itemView);
            trailer_name=itemView.findViewById(R.id.trailer_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
String id = trailer_key;
            Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
            Intent webIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://www.youtube.com/watch?v=" + id));
            try {
                v.getContext().startActivity(appIntent);
            } catch (ActivityNotFoundException ex) {
                v.getContext().startActivity(webIntent);
            }

        }
    }
}
