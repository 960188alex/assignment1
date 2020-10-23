package com.codepath.flixster.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Parcel;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.flixster.DetailActivity;
import com.codepath.flixster.R;
import com.codepath.flixster.models.Movie;

import org.parceler.Parcels;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    Context context;
    List<Movie> movies;

    public MovieAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create view from layout view with item_movie model
        View movieView = LayoutInflater.from(context).inflate(R.layout.item_movie,parent,false);
        // create view holder with that view
        return new ViewHolder(movieView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // populate movie data into the view holder
        Movie movie = movies.get(position);
        // bind it to the holder
        holder.bind(movie);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout container;
        TextView tvTitle;
        TextView tvOverview;
        ImageView ivPoster;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            // from layout
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvOverview = itemView.findViewById(R.id.tvOverview);
            ivPoster = itemView.findViewById(R.id.ivPoster);
            container = itemView.findViewById(R.id.container);

        }

        public void bind(final Movie movie) {
            this.tvTitle.setText(movie.getTitle());
            this.tvOverview.setText(movie.getOverview());
            // use getting in the poster_path
            String imageUrlPath;
            if(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                imageUrlPath = movie.getBackdropPath();
            }else{
                imageUrlPath = movie.getPosterPath();
            }
            Glide.with(context)
                    .load(imageUrlPath)
                    .into(ivPoster);

            // set an event listener
            container.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    // movie is the layout (UI)
                    Toast.makeText(context, movie.getTitle(), Toast.LENGTH_LONG).show();

                    // use intent to open a new activity
                    Intent i = new Intent(context, DetailActivity.class);
                    i.putExtra("title",movie.getTitle());

                    // now instead of putting text only we want to putExtra the whole video
                    i.putExtra("movie", Parcels.wrap(movie)); // then we need to modify Movie constructor
                    context.startActivity(i);
                }
            });
        }
    }
}
